package manual.servlet;

import manual.dto.CalculationRequest;
import manual.dto.CalculationResponse;
import manual.dto.TabulatedFunctionRequest;
import operations.TabulatedFunctionOperationService;
import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import functions.factory.TabulatedFunctionFactory;
import exceptions.InconsistentFunctionsException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "FunctionOperationServlet", urlPatterns = {"/api/functions/operations"})
public class FunctionOperationServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(FunctionOperationServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("POST request to FunctionOperationServlet");

        try {
            CalculationRequest calcRequest = parseJsonRequest(request, CalculationRequest.class);
            String operation = calcRequest.getOperation();
            
            if (operation == null || operation.isEmpty()) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Operation is required");
                return;
            }

            TabulatedFunctionRequest funcA = calcRequest.getFunctionA();
            TabulatedFunctionRequest funcB = calcRequest.getFunctionB();
            
            if (funcA == null || funcB == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Both functions are required");
                return;
            }

            TabulatedFunctionFactory factory = createFactory(funcA.getType());
            TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(factory);
            
            TabulatedFunction functionA = convertToTabulatedFunction(funcA, factory);
            TabulatedFunction functionB = convertToTabulatedFunction(funcB, factory);
            
            long startTime = System.currentTimeMillis();
            TabulatedFunction result;
            
            switch (operation.toLowerCase()) {
                case "add":
                    logger.info("Performing addition operation");
                    result = service.add(functionA, functionB);
                    break;
                case "subtract":
                    logger.info("Performing subtraction operation");
                    result = service.subtract(functionA, functionB);
                    break;
                case "multiply":
                    logger.info("Performing multiplication operation");
                    result = service.multiply(functionA, functionB);
                    break;
                case "divide":
                    logger.info("Performing division operation");
                    result = service.divide(functionA, functionB);
                    break;
                default:
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, 
                        "Unknown operation: " + operation + ". Supported: add, subtract, multiply, divide");
                    return;
            }
            
            long endTime = System.currentTimeMillis();
            long computationTime = endTime - startTime;
            
            CalculationResponse calcResponse = new CalculationResponse();
            calcResponse.setOperation(operation);
            calcResponse.setFunctionType(result.getClass().getSimpleName());
            calcResponse.setComputationTimeMs(computationTime);
            calcResponse.setDetails("Operation completed successfully. Result has " + result.getCount() + " points");
            
            logger.info("Operation " + operation + " completed in " + computationTime + " ms");
            sendSuccess(request, response, calcResponse);
            
        } catch (InconsistentFunctionsException e) {
            logger.severe("Inconsistent functions error: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.severe("Error performing function operation: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Failed to perform operation: " + e.getMessage());
        }
    }

    private TabulatedFunctionFactory createFactory(String type) {
        if (type != null && type.equalsIgnoreCase("LINKED_LIST")) {
            return new LinkedListTabulatedFunctionFactory();
        }
        return new ArrayTabulatedFunctionFactory();
    }

    private TabulatedFunction convertToTabulatedFunction(TabulatedFunctionRequest request, 
                                                          TabulatedFunctionFactory factory) {
        if (request.getXValues() != null && request.getYValues() != null) {
            return factory.create(request.getXValues(), request.getYValues());
        }
        throw new IllegalArgumentException("Function must have xValues and yValues");
    }
}

