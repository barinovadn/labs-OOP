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

    /**
     * Поиск в ширину по графу композитных функций
     */
    public List<FunctionResponse> breadthFirstSearch(Long startFunctionId, SearchCriteria criteria) throws SQLException {
        logger.info("BFS from function ID: " + startFunctionId);
        long startTime = System.currentTimeMillis();

        List<FunctionResponse> result = new ArrayList<>();
        Set<Long> visited = new HashSet<>();
        Queue<Long> queue = new LinkedList<>();

        if (startFunctionId != null) {
            queue.offer(startFunctionId);
            visited.add(startFunctionId);
        }

        while (!queue.isEmpty()) {
            Long currentFunctionId = queue.poll();
            logger.fine("BFS processing: " + currentFunctionId);

            FunctionResponse function = functionRepository.findById(currentFunctionId);
            if (function != null && matchesCriteria(function, criteria)) {
                result.add(function);
            }

            List<CompositeFunctionResponse> compositesAsFirst = compositeRepository.findByUserId(function.getUserId())
                    .stream()
                    .filter(c -> c.getFirstFunctionId().equals(currentFunctionId))
                    .toList();

            List<CompositeFunctionResponse> compositesAsSecond = compositeRepository.findByUserId(function.getUserId())
                    .stream()
                    .filter(c -> c.getSecondFunctionId().equals(currentFunctionId))
                    .toList();

            for (CompositeFunctionResponse composite : compositesAsFirst) {
                if (!visited.contains(composite.getSecondFunctionId())) {
                    queue.offer(composite.getSecondFunctionId());
                    visited.add(composite.getSecondFunctionId());
                }
            }

            for (CompositeFunctionResponse composite : compositesAsSecond) {
                if (!visited.contains(composite.getFirstFunctionId())) {
                    queue.offer(composite.getFirstFunctionId());
                    visited.add(composite.getFirstFunctionId());
                }
            }

            if (result.size() >= criteria.getLimit()) {
                logger.info("BFS reached limit: " + criteria.getLimit());
                break;
            }
        }

        long endTime = System.currentTimeMillis();
        logger.info("BFS completed. Found " + result.size() + " items in " + (endTime - startTime) + "ms");
        return result;
    }

    /**
     * Поиск в глубину по графу композитных функций
     */
    public List<FunctionResponse> depthFirstSearch(Long startFunctionId, SearchCriteria criteria) throws SQLException {
        logger.info("DFS from function ID: " + startFunctionId);
        long startTime = System.currentTimeMillis();

        List<FunctionResponse> result = new ArrayList<>();
        Set<Long> visited = new HashSet<>();

        if (startFunctionId != null) {
            dfsRecursive(startFunctionId, criteria, result, visited, 0);
        }

        long endTime = System.currentTimeMillis();
        logger.info("DFS completed. Found " + result.size() + " items in " + (endTime - startTime) + "ms");
        return result;
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

    /**
     * Иерархический поиск - находит все функции в иерархии от корневой функции
     */
    public List<FunctionResponse> hierarchicalSearch(Long rootFunctionId, SearchCriteria criteria) throws SQLException {
        logger.info("Hierarchical search from root: " + rootFunctionId);
        long startTime = System.currentTimeMillis();

        List<FunctionResponse> result = new ArrayList<>();
        buildHierarchy(rootFunctionId, criteria, result, 0);

        long endTime = System.currentTimeMillis();
        logger.info("Hierarchical search completed. Found " + result.size() + " items");
        return result;
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