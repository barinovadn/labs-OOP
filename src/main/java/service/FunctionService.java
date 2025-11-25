package service;

import dto.FunctionRequest;
import dto.FunctionResponse;
import entity.FunctionEntity;
import entity.UserEntity;
import repository.FunctionRepository;
import repository.UserRepository;
import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.TabulatedFunctionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
@Transactional
public class FunctionService {
    private static final Logger logger = Logger.getLogger(FunctionService.class.getName());

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private UserRepository userRepository;

    private TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();

    public FunctionResponse createFunction(FunctionRequest request) {
        logger.info("Creating function: " + request.getFunctionName());
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        FunctionEntity function = new FunctionEntity(
                user,
                request.getFunctionName(),
                request.getFunctionType(),
                request.getFunctionExpression(),
                request.getXFrom(),
                request.getXTo()
        );

        function = functionRepository.save(function);
        logger.info("Function created with ID: " + function.getFunctionId());
        return toResponse(function);
    }

    public FunctionResponse getFunctionById(Long id) {
        logger.info("Getting function by ID: " + id);
        FunctionEntity function = functionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Function not found with id: " + id));
        return toResponse(function);
    }

    public List<FunctionResponse> getFunctionsByUserId(Long userId) {
        logger.info("Getting functions for user ID: " + userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return functionRepository.findByUser(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<FunctionResponse> getAllFunctions(String sortBy) {
        logger.info("Getting all functions, sort by: " + sortBy);
        List<FunctionEntity> functions = functionRepository.findAll();
        if (sortBy != null && !sortBy.isEmpty()) {
            functions.sort((a, b) -> {
                switch (sortBy.toLowerCase()) {
                    case "name":
                        return a.getFunctionName().compareTo(b.getFunctionName());
                    case "type":
                        return a.getFunctionType().compareTo(b.getFunctionType());
                    case "created":
                        return a.getCreatedAt().compareTo(b.getCreatedAt());
                    default:
                        return 0;
                }
            });
        }
        return functions.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public FunctionResponse updateFunction(Long id, FunctionRequest request) {
        logger.info("Updating function with ID: " + id);
        FunctionEntity function = functionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Function not found with id: " + id));

        function.setFunctionName(request.getFunctionName());
        function.setFunctionType(request.getFunctionType());
        function.setFunctionExpression(request.getFunctionExpression());
        function.setXFrom(request.getXFrom());
        function.setXTo(request.getXTo());

        function = functionRepository.save(function);
        logger.info("Function updated: " + id);
        return toResponse(function);
    }

    public void deleteFunction(Long id) {
        logger.info("Deleting function with ID: " + id);
        if (!functionRepository.existsById(id)) {
            throw new RuntimeException("Function not found with id: " + id);
        }
        functionRepository.deleteById(id);
        logger.info("Function deleted: " + id);
    }

    private FunctionResponse toResponse(FunctionEntity function) {
        FunctionResponse response = new FunctionResponse();
        response.setFunctionId(function.getFunctionId());
        response.setUserId(function.getUser().getUserId());
        response.setFunctionName(function.getFunctionName());
        response.setFunctionType(function.getFunctionType());
        response.setFunctionExpression(function.getFunctionExpression());
        response.setXFrom(function.getXFrom());
        response.setXTo(function.getXTo());
        response.setCreatedAt(function.getCreatedAt());
        return response;
    }
}

