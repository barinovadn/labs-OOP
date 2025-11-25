package service;

import dto.CompositeFunctionRequest;
import dto.CompositeFunctionResponse;
import entity.CompositeFunctionEntity;
import entity.FunctionEntity;
import entity.UserEntity;
import repository.CompositeFunctionRepository;
import repository.FunctionRepository;
import repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
@Transactional
public class CompositeFunctionService {
    private static final Logger logger = Logger.getLogger(CompositeFunctionService.class.getName());

    @Autowired
    private CompositeFunctionRepository compositeFunctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FunctionRepository functionRepository;

    public CompositeFunctionResponse createCompositeFunction(CompositeFunctionRequest request) {
        logger.info("Creating composite function: " + request.getCompositeName());
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        FunctionEntity firstFunction = functionRepository.findById(request.getFirstFunctionId())
                .orElseThrow(() -> new RuntimeException("First function not found with id: " + request.getFirstFunctionId()));

        FunctionEntity secondFunction = functionRepository.findById(request.getSecondFunctionId())
                .orElseThrow(() -> new RuntimeException("Second function not found with id: " + request.getSecondFunctionId()));

        CompositeFunctionEntity composite = new CompositeFunctionEntity(
                user,
                request.getCompositeName(),
                firstFunction,
                secondFunction
        );

        composite = compositeFunctionRepository.save(composite);
        logger.info("Composite function created with ID: " + composite.getCompositeId());
        return toResponse(composite);
    }

    public CompositeFunctionResponse getCompositeFunctionById(Long id) {
        logger.info("Getting composite function by ID: " + id);
        CompositeFunctionEntity composite = compositeFunctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Composite function not found with id: " + id));
        return toResponse(composite);
    }

    public List<CompositeFunctionResponse> getCompositeFunctionsByUserId(Long userId) {
        logger.info("Getting composite functions for user ID: " + userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return compositeFunctionRepository.findByUser(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CompositeFunctionResponse updateCompositeFunction(Long id, CompositeFunctionRequest request) {
        logger.info("Updating composite function with ID: " + id);
        CompositeFunctionEntity composite = compositeFunctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Composite function not found with id: " + id));

        FunctionEntity firstFunction = functionRepository.findById(request.getFirstFunctionId())
                .orElseThrow(() -> new RuntimeException("First function not found with id: " + request.getFirstFunctionId()));

        FunctionEntity secondFunction = functionRepository.findById(request.getSecondFunctionId())
                .orElseThrow(() -> new RuntimeException("Second function not found with id: " + request.getSecondFunctionId()));

        composite.setCompositeName(request.getCompositeName());
        composite.setFirstFunction(firstFunction);
        composite.setSecondFunction(secondFunction);

        composite = compositeFunctionRepository.save(composite);
        logger.info("Composite function updated: " + id);
        return toResponse(composite);
    }

    public void deleteCompositeFunction(Long id) {
        logger.info("Deleting composite function with ID: " + id);
        if (!compositeFunctionRepository.existsById(id)) {
            throw new RuntimeException("Composite function not found with id: " + id);
        }
        compositeFunctionRepository.deleteById(id);
        logger.info("Composite function deleted: " + id);
    }

    private CompositeFunctionResponse toResponse(CompositeFunctionEntity composite) {
        CompositeFunctionResponse response = new CompositeFunctionResponse();
        response.setCompositeId(composite.getCompositeId());
        response.setUserId(composite.getUser().getUserId());
        response.setCompositeName(composite.getCompositeName());
        response.setFirstFunctionId(composite.getFirstFunction().getFunctionId());
        response.setSecondFunctionId(composite.getSecondFunction().getFunctionId());
        response.setCreatedAt(composite.getCreatedAt());
        return response;
    }
}

