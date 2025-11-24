package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.FunctionResponse;
import manual.search.FunctionGraphSearch;
import manual.search.SearchCriteria;
import manual.search.SearchAlgorithm;
import manual.repository.FunctionRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;

@WebServlet(name = "FunctionSearchServlet", urlPatterns = {"/api/functions/search"})
public class FunctionSearchServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(FunctionSearchServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("POST request to FunctionSearchServlet");

        try {
            SearchCriteria criteria = parseJsonRequest(request, SearchCriteria.class);
            String algorithmParam = request.getParameter("algorithm");
            SearchAlgorithm algorithm = algorithmParam != null ? 
                SearchAlgorithm.fromString(algorithmParam) : SearchAlgorithm.QUICK;

            logger.info("Search request with algorithm: " + algorithm + ", criteria: " + criteria);

            try (Connection conn = DatabaseConnection.getConnection()) {
                FunctionRepository functionRepo = new FunctionRepository(conn);
                List<FunctionResponse> allFunctions = functionRepo.findAll();
                
                List<FunctionResponse> results = new ArrayList<>();
                Set<Long> visited = new HashSet<>();

                if (algorithm == SearchAlgorithm.DEEP) {
                    logger.info("Performing DEEP search");
                    for (FunctionResponse function : allFunctions) {
                        if (!visited.contains(function.getFunctionId())) {
                            deepSearch(function.getFunctionId(), functionRepo, criteria, results, visited, 0);
                        }
                    }
                } else {
                    logger.info("Performing QUICK search");
                    quickSearch(allFunctions, functionRepo, criteria, results);
                }

                logger.info("Search completed. Found " + results.size() + " functions");
                sendSuccess(request, response, results);

            } catch (SQLException e) {
                logger.severe("Database error in FunctionSearchServlet: " + e.getMessage());
                sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            }
        } catch (IOException e) {
            logger.severe("Error parsing search criteria: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request body");
        } catch (Exception e) {
            logger.severe("Error performing search: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Failed to perform search: " + e.getMessage());
        }
    }

    private void deepSearch(Long functionId, FunctionRepository functionRepo, SearchCriteria criteria,
                          List<FunctionResponse> results, Set<Long> visited, int depth) throws SQLException {
        if (visited.contains(functionId) || results.size() >= criteria.getLimit()) {
            return;
        }

        logger.fine("DEEP processing function ID: " + functionId + " at depth: " + depth);
        visited.add(functionId);

        FunctionResponse function = functionRepo.findById(functionId);
        if (function != null && matchesCriteria(function, criteria)) {
            results.add(function);
            logger.fine("Function " + function.getFunctionName() + " matches criteria");
        }

        List<FunctionResponse> allFunctions = functionRepo.findAll();
        for (FunctionResponse related : allFunctions) {
            if (!visited.contains(related.getFunctionId())) {
                deepSearch(related.getFunctionId(), functionRepo, criteria, results, visited, depth + 1);
            }
        }
    }

    private void quickSearch(List<FunctionResponse> allFunctions, FunctionRepository functionRepo,
                          SearchCriteria criteria, List<FunctionResponse> results) throws SQLException {
        List<FunctionResponse> queue = new ArrayList<>(allFunctions);
        Set<Long> visited = new HashSet<>();

        while (!queue.isEmpty() && results.size() < criteria.getLimit()) {
            FunctionResponse function = queue.remove(0);
            
            if (visited.contains(function.getFunctionId())) {
                continue;
            }
            visited.add(function.getFunctionId());

            logger.fine("BFS processing function ID: " + function.getFunctionId());
            
            if (matchesCriteria(function, criteria)) {
                results.add(function);
                logger.fine("Function " + function.getFunctionName() + " matches criteria");
            }
        }
    }

    private boolean matchesCriteria(FunctionResponse function, SearchCriteria criteria) {
        boolean matches = true;

        if (criteria.hasFunctionNameFilter()) {
            matches = matches && function.getFunctionName().toLowerCase()
                    .contains(criteria.getFunctionName().toLowerCase());
        }

        if (criteria.hasFunctionTypeFilter()) {
            matches = matches && function.getFunctionType().equalsIgnoreCase(criteria.getFunctionType());
        }

        if (criteria.hasXRangeFilter()) {
            if (criteria.getMinX() != null) {
                matches = matches && function.getXFrom() >= criteria.getMinX();
            }
            if (criteria.getMaxX() != null) {
                matches = matches && function.getXTo() <= criteria.getMaxX();
            }
        }

        logger.finest("Function " + function.getFunctionName() + " matches: " + matches);
        return matches;
    }
}

