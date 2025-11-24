package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.CalculationResponse;
import manual.dto.FunctionResponse;
import manual.repository.FunctionRepository;
import manual.repository.PointRepository;
import operations.TabulatedDifferentialOperator;
import operations.DifferentialOperator;
import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "FunctionDifferentiateServlet", urlPatterns = {"/api/functions/*/differentiate"})
public class FunctionDifferentiateServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(FunctionDifferentiateServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        String requestURI = request.getRequestURI();
        logger.info("POST request to FunctionDifferentiateServlet: " + requestURI);

        try {
            String[] parts = requestURI.split("/");
            Long functionId = null;
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].equals("functions") && i + 1 < parts.length) {
                    try {
                        functionId = Long.parseLong(parts[i + 1]);
                        break;
                    } catch (NumberFormatException e) {
                        // continue
                    }
                }
            }

            if (functionId == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
                return;
            }

            String differentialType = request.getParameter("differentialType");
            if (differentialType == null) {
                differentialType = "MIDDLE";
            }

            String stepParam = request.getParameter("step");
            Double step = stepParam != null ? Double.parseDouble(stepParam) : null;

            try (Connection conn = DatabaseConnection.getConnection()) {
                FunctionRepository functionRepo = new FunctionRepository(conn);
                FunctionResponse functionResponse = functionRepo.findById(functionId);
                
                if (functionResponse == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Function not found");
                    return;
                }

                PointRepository pointRepo = new PointRepository(conn);
                List<manual.dto.PointResponse> points = pointRepo.findByFunctionId(functionId);
                
                if (points == null || points.isEmpty()) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, 
                        "Function has no points. Calculate points first.");
                    return;
                }

                double[] xValues = new double[points.size()];
                double[] yValues = new double[points.size()];
                for (int i = 0; i < points.size(); i++) {
                    xValues[i] = points.get(i).getXValue();
                    yValues[i] = points.get(i).getYValue();
                }

                ArrayTabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
                TabulatedFunction function = factory.create(xValues, yValues);

                DifferentialOperator<TabulatedFunction> operator = new TabulatedDifferentialOperator(factory);
                logger.info("Using TabulatedDifferentialOperator with differential type: " + differentialType + 
                    (step != null ? " and step: " + step : ""));

                long startTime = System.currentTimeMillis();
                TabulatedFunction derivative = operator.derive(function);
                long endTime = System.currentTimeMillis();
                long computationTime = endTime - startTime;

                CalculationResponse calcResponse = new CalculationResponse();
                calcResponse.setOperation("differentiate");
                calcResponse.setFunctionType(derivative.getClass().getSimpleName());
                calcResponse.setComputationTimeMs(computationTime);
                calcResponse.setDetails("Derivative computed with " + differentialType + " method. " + 
                    "Result has " + derivative.getCount() + " points");

                logger.info("Differentiation completed in " + computationTime + " ms");
                sendSuccess(request, response, calcResponse);

            } catch (SQLException e) {
                logger.severe("Database error in FunctionDifferentiateServlet: " + e.getMessage());
                sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            } catch (NumberFormatException e) {
                logger.severe("Invalid step parameter: " + e.getMessage());
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid step parameter");
            }
        } catch (Exception e) {
            logger.severe("Error differentiating function: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Failed to differentiate function: " + e.getMessage());
        }
    }
}

