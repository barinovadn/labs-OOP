package manual.search;

import manual.DatabaseConnection;
import manual.dto.FunctionResponse;
import manual.dto.CompositeFunctionResponse;
import manual.repository.FunctionRepository;
import manual.repository.CompositeFunctionRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class FunctionGraphSearch {
    private static final Logger logger = Logger.getLogger(FunctionGraphSearch.class.getName());

    private final Connection connection;
    private final FunctionRepository functionRepository;
    private final CompositeFunctionRepository compositeRepository;

    public FunctionGraphSearch() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.functionRepository = new FunctionRepository(connection);
        this.compositeRepository = new CompositeFunctionRepository(connection);
        logger.info("FunctionGraphSearch initialized");
    }

    private void dfsRecursive(Long functionId, SearchCriteria criteria, List<FunctionResponse> result,
                              Set<Long> visited, int depth) throws SQLException {
        if (visited.contains(functionId) || result.size() >= criteria.getLimit()) {
            return;
        }

        logger.fine("DFS processing: " + functionId + " at depth: " + depth);
        visited.add(functionId);

        FunctionResponse function = functionRepository.findById(functionId);
        if (function != null && matchesCriteria(function, criteria)) {
            result.add(function);
        }

        List<CompositeFunctionResponse> compositesAsFirst = compositeRepository.findByUserId(function.getUserId())
                .stream()
                .filter(c -> c.getFirstFunctionId().equals(functionId))
                .toList();

        List<CompositeFunctionResponse> compositesAsSecond = compositeRepository.findByUserId(function.getUserId())
                .stream()
                .filter(c -> c.getSecondFunctionId().equals(functionId))
                .toList();

        for (CompositeFunctionResponse composite : compositesAsFirst) {
            dfsRecursive(composite.getSecondFunctionId(), criteria, result, visited, depth + 1);
        }

        for (CompositeFunctionResponse composite : compositesAsSecond) {
            dfsRecursive(composite.getFirstFunctionId(), criteria, result, visited, depth + 1);
        }
    }

    private void buildHierarchy(Long functionId, SearchCriteria criteria, List<FunctionResponse> result, int level) throws SQLException {
        if (result.size() >= criteria.getLimit()) {
            return;
        }

        logger.fine("Building hierarchy for: " + functionId + " at level: " + level);

        FunctionResponse function = functionRepository.findById(functionId);
        if (function != null && matchesCriteria(function, criteria)) {
            result.add(function);
        }

        List<CompositeFunctionResponse> childComposites = compositeRepository.findByUserId(function.getUserId())
                .stream()
                .filter(c -> c.getFirstFunctionId().equals(functionId))
                .toList();

        for (CompositeFunctionResponse composite : childComposites) {
            buildHierarchy(composite.getSecondFunctionId(), criteria, result, level + 1);
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

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.info("FunctionGraphSearch closed");
        }
    }
}