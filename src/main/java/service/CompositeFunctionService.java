package service;

import dto.CompositeFunctionRequest;
import dto.CompositeFunctionResponse;
import dto.CompositeFunctionPointResponse;
import entity.CompositeFunctionEntity;
import entity.FunctionEntity;
import entity.UserEntity;
import entity.PointEntity;
import repository.CompositeFunctionRepository;
import repository.FunctionRepository;
import repository.UserRepository;
import repository.PointRepository;
import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.Point;
import operations.TabulatedFunctionOperationService;
import operations.TabulatedDifferentialOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
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

    @Autowired
    private PointRepository pointRepository;

    private TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService();
    private TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator();

    public CompositeFunctionResponse createCompositeFunction(CompositeFunctionRequest request) {
        logger.info("Creating composite function: " + request.getCompositeName());
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        FunctionEntity firstFunction = functionRepository.findById(request.getFirstFunctionId())
                .orElseThrow(() -> new RuntimeException("First function not found with id: " + request.getFirstFunctionId()));

        FunctionEntity secondFunction = functionRepository.findById(request.getSecondFunctionId())
                .orElseThrow(() -> new RuntimeException("Second function not found with id: " + request.getSecondFunctionId()));

        // Check for division by zero function
        String operationMethod = request.getOperationMethod();
        String operationUpper = (operationMethod != null && !operationMethod.isEmpty()) 
            ? operationMethod.toUpperCase() 
            : "COMPOSITION";
        
        // For single-function operations (differentiation, integration), second function is not required
        boolean isSingleFunctionOp = operationUpper.equals("DIFFERENTIATE") || 
                                     operationUpper.equals("DIFFERENTIATION") || 
                                     operationUpper.equals("ДИФФЕРЕНЦИРОВАНИЕ") ||
                                     operationUpper.equals("INTEGRATE") || 
                                     operationUpper.equals("INTEGRATION") || 
                                     operationUpper.equals("ИНТЕГРИРОВАНИЕ");
        
        if (!isSingleFunctionOp) {
            if (operationUpper.equals("DIVIDE") || operationUpper.equals("DIVISION") || operationUpper.equals("ДЕЛЕНИЕ")) {
                if (isZeroFunction(secondFunction)) {
                    throw new RuntimeException("Cannot divide by zero function. The second function must not be identically zero.");
                }
            }
        }

        CompositeFunctionEntity composite = new CompositeFunctionEntity(
                user,
                request.getCompositeName(),
                firstFunction,
                secondFunction
        );
        composite.setXFrom(request.getXFrom());
        composite.setXTo(request.getXTo());
        composite.setOperationMethod(request.getOperationMethod());

        composite = compositeFunctionRepository.save(composite);
        logger.info("Composite function created with ID: " + composite.getCompositeId());
        return toResponse(composite);
    }

    private boolean isZeroFunction(FunctionEntity function) {
        List<PointEntity> points = pointRepository.findByFunctionIdOrderByXValue(function.getFunctionId());
        if (points.isEmpty()) {
            return false; // Empty function is not considered zero
        }
        
        double EPSILON = 1e-9;
        for (PointEntity point : points) {
            if (Math.abs(point.getYValue()) > EPSILON) {
                return false; // Found a non-zero value
            }
        }
        return true; // All values are zero (within epsilon)
    }

    public CompositeFunctionResponse getCompositeFunctionById(Long id) {
        logger.info("Getting composite function by ID: " + id);
        CompositeFunctionEntity composite = compositeFunctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Composite function not found with id: " + id));
        return toResponse(composite);
    }

    public CompositeFunctionPointResponse getCompositeFunctionPointInfo(Long id) {
        CompositeFunctionEntity composite = compositeFunctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Composite function not found with id: " + id));
        CompositeFunctionPointResponse resp = new CompositeFunctionPointResponse();
        resp.setCompositeId(composite.getCompositeId());
        resp.setUserId(composite.getUser().getUserId());
        resp.setFirstFunctionId(composite.getFirstFunction().getFunctionId());
        resp.setSecondFunctionId(composite.getSecondFunction().getFunctionId());
        resp.setOperationMethod(composite.getOperationMethod());
        return resp;
    }

    public List<PointEntity> calculateCompositeFunctionPoints(Long id) {
        logger.info("Calculating points for composite function ID: " + id);
        CompositeFunctionEntity composite = compositeFunctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Composite function not found with id: " + id));

        FunctionEntity firstFunction = composite.getFirstFunction();
        FunctionEntity secondFunction = composite.getSecondFunction();
        String operationMethod = composite.getOperationMethod();

        if (operationMethod == null || operationMethod.isEmpty()) {
            operationMethod = "COMPOSITION"; // Default to composition for backward compatibility
        }

        String operationUpper = (operationMethod != null && !operationMethod.isEmpty()) 
            ? operationMethod.toUpperCase() 
            : "COMPOSITION";

        List<PointEntity> firstPoints = pointRepository.findByFunctionIdOrderByXValue(firstFunction.getFunctionId());
        
        if (firstPoints.size() < 2) {
            throw new RuntimeException("First function must have at least 2 points");
        }
        
        // For single-function operations, second function is not used
        boolean isSingleFunctionOp = operationUpper.equals("DIFFERENTIATE") || 
                                     operationUpper.equals("DIFFERENTIATION") || 
                                     operationUpper.equals("ДИФФЕРЕНЦИРОВАНИЕ") ||
                                     operationUpper.equals("INTEGRATE") || 
                                     operationUpper.equals("INTEGRATION") || 
                                     operationUpper.equals("ИНТЕГРИРОВАНИЕ");
        
        List<PointEntity> secondPoints = null;
        if (!isSingleFunctionOp) {
            secondPoints = pointRepository.findByFunctionIdOrderByXValue(secondFunction.getFunctionId());
            if (secondPoints.size() < 2) {
                throw new RuntimeException("Second function must have at least 2 points");
            }
        }

        double[] firstXValues = new double[firstPoints.size()];
        double[] firstYValues = new double[firstPoints.size()];
        for (int i = 0; i < firstPoints.size(); i++) {
            firstXValues[i] = firstPoints.get(i).getXValue();
            firstYValues[i] = firstPoints.get(i).getYValue();
        }

        TabulatedFunction firstTabulated = new ArrayTabulatedFunction(firstXValues, firstYValues);
        TabulatedFunction secondTabulated = null;
        double[] secondXValues = null;
        double[] secondYValues = null;
        
        if (!isSingleFunctionOp) {
            secondXValues = new double[secondPoints.size()];
            secondYValues = new double[secondPoints.size()];
            for (int i = 0; i < secondPoints.size(); i++) {
                secondXValues[i] = secondPoints.get(i).getXValue();
                secondYValues[i] = secondPoints.get(i).getYValue();
            }
            secondTabulated = new ArrayTabulatedFunction(secondXValues, secondYValues);
        }

        TabulatedFunction resultFunction;

        switch (operationUpper) {
            case "ADD":
            case "ADDITION":
            case "СЛОЖЕНИЕ":
                resultFunction = operationService.add(firstTabulated, secondTabulated);
                break;
            case "SUBTRACT":
            case "SUBTRACTION":
            case "ВЫЧИТАНИЕ":
                resultFunction = operationService.subtract(firstTabulated, secondTabulated);
                break;
            case "MULTIPLY":
            case "MULTIPLICATION":
            case "УМНОЖЕНИЕ":
                resultFunction = operationService.multiply(firstTabulated, secondTabulated);
                break;
            case "DIVIDE":
            case "DIVISION":
            case "ДЕЛЕНИЕ":
                resultFunction = operationService.divide(firstTabulated, secondTabulated);
                break;
            case "DIFFERENTIATE":
            case "DIFFERENTIATION":
            case "ДИФФЕРЕНЦИРОВАНИЕ":
                resultFunction = differentialOperator.derive(firstTabulated);
                break;
            case "INTEGRATE":
            case "INTEGRATION":
            case "ИНТЕГРИРОВАНИЕ":
                resultFunction = operationService.integrate(firstTabulated);
                break;
            case "INTERPOLATE":
            case "INTERPOLATION":
            case "ИНТЕРПОЛЯЦИЯ":
                // Use second function's x values for interpolation
                if (secondXValues == null || secondXValues.length == 0) {
                    throw new RuntimeException("Interpolation requires a second function with x values");
                }
                double[] interpolatedX = new double[secondXValues.length];
                System.arraycopy(secondXValues, 0, interpolatedX, 0, secondXValues.length);
                resultFunction = operationService.interpolateLinearly(firstTabulated, interpolatedX);
                break;
            case "COMPOSITION":
            case "COMPOSITE":
            default:
                // Default composition: f(g(x))
                resultFunction = composeFunctions(firstTabulated, secondTabulated);
                break;
        }

        List<PointEntity> resultPoints = new ArrayList<>();
        for (int i = 0; i < resultFunction.getCount(); i++) {
            PointEntity point = new PointEntity();
            point.setXValue(resultFunction.getX(i));
            point.setYValue(resultFunction.getY(i));
            resultPoints.add(point);
        }

        logger.info("Calculated " + resultPoints.size() + " points for composite function ID: " + id);
        return resultPoints;
    }

    private TabulatedFunction composeFunctions(TabulatedFunction first, TabulatedFunction second) {
        // Create composition f(g(x))
        double minX = Math.max(first.leftBound(), second.leftBound());
        double maxX = Math.min(first.rightBound(), second.rightBound());
        int sampleCount = Math.max(100, Math.max(first.getCount(), second.getCount()) * 10);
        double step = (maxX - minX) / (sampleCount - 1);

        double[] xValues = new double[sampleCount];
        double[] yValues = new double[sampleCount];

        for (int i = 0; i < sampleCount; i++) {
            double x = minX + i * step;
            double gx = second.apply(x);
            double fgx = first.apply(gx);
            xValues[i] = x;
            yValues[i] = fgx;
        }

        return new ArrayTabulatedFunction(xValues, yValues);
    }

    public List<CompositeFunctionResponse> getAllCompositeFunctions() {
        logger.info("Getting all composite functions");
        return compositeFunctionRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
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
        composite.setXFrom(request.getXFrom());
        composite.setXTo(request.getXTo());
        composite.setOperationMethod(request.getOperationMethod());

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
        response.setXFrom(composite.getXFrom());
        response.setXTo(composite.getXTo());
        response.setOperationMethod(composite.getOperationMethod());
        return response;
    }
}

