package search;

import entity.*;
import repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private static final Logger logger = Logger.getLogger(SearchService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private CompositeFunctionRepository compositeFunctionRepository;

    @Autowired
    private PointRepository pointRepository;

    public List<Object> depthFirstSearch(Long userId, String searchTerm) {
        logger.info("Начало поиска в глубину для пользователя: " + userId + ", термин: " + searchTerm);
        List<Object> results = new ArrayList<>();
        Set<Long> visited = new HashSet<>();

        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            dfsUserHierarchy(user, searchTerm.toLowerCase(), visited, results);
        }

        logger.info("Поиск в глубину завершен. Найдено: " + results.size() + " результатов");
        return results;
    }

    private void dfsUserHierarchy(UserEntity user, String searchTerm, Set<Long> visited, List<Object> results) {
        if (visited.contains(user.getUserId())) return;
        visited.add(user.getUserId());

        if (containsSearchTerm(user, searchTerm)) {
            results.add(user);
        }

        for (FunctionEntity function : user.getFunctions()) {
            dfsFunctionHierarchy(function, searchTerm, visited, results);
        }

        for (CompositeFunctionEntity composite : user.getCompositeFunctions()) {
            dfsCompositeHierarchy(composite, searchTerm, visited, results);
        }
    }

    private void dfsFunctionHierarchy(FunctionEntity function, String searchTerm, Set<Long> visited, List<Object> results) {
        if (visited.contains(function.getFunctionId())) return;
        visited.add(function.getFunctionId());

        if (containsSearchTerm(function, searchTerm)) {
            results.add(function);
        }

        for (PointEntity point : function.getComputedPoints()) {
            if (containsSearchTerm(point, searchTerm)) {
                results.add(point);
            }
        }
    }

    private void dfsCompositeHierarchy(CompositeFunctionEntity composite, String searchTerm, Set<Long> visited, List<Object> results) {
        if (visited.contains(composite.getCompositeId())) return;
        visited.add(composite.getCompositeId());

        if (containsSearchTerm(composite, searchTerm)) {
            results.add(composite);
        }

        if (composite.getFirstFunction() != null) {
            dfsFunctionHierarchy(composite.getFirstFunction(), searchTerm, visited, results);
        }
        if (composite.getSecondFunction() != null) {
            dfsFunctionHierarchy(composite.getSecondFunction(), searchTerm, visited, results);
        }
    }

    public List<Object> breadthFirstSearch(Long userId, String searchTerm) {
        logger.info("Начало поиска в ширину для пользователя: " + userId + ", термин: " + searchTerm);
        List<Object> results = new ArrayList<>();
        Set<Long> visited = new HashSet<>();
        Queue<Object> queue = new LinkedList<>();

        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            queue.offer(user);
            visited.add(user.getUserId());
        }

        while (!queue.isEmpty()) {
            Object current = queue.poll();

            if (containsSearchTerm(current, searchTerm.toLowerCase())) {
                results.add(current);
            }

            addNeighborsToQueue(current, queue, visited);
        }

        logger.info("Поиск в ширину завершен. Найдено: " + results.size() + " результатов");
        return results;
    }

    private void addNeighborsToQueue(Object node, Queue<Object> queue, Set<Long> visited) {
        if (node instanceof UserEntity) {
            UserEntity user = (UserEntity) node;
            for (FunctionEntity function : user.getFunctions()) {
                if (!visited.contains(function.getFunctionId())) {
                    queue.offer(function);
                    visited.add(function.getFunctionId());
                }
            }
            for (CompositeFunctionEntity composite : user.getCompositeFunctions()) {
                if (!visited.contains(composite.getCompositeId())) {
                    queue.offer(composite);
                    visited.add(composite.getCompositeId());
                }
            }
        } else if (node instanceof FunctionEntity) {
            FunctionEntity function = (FunctionEntity) node;
            for (PointEntity point : function.getComputedPoints()) {
                if (!visited.contains(point.getPointId())) {
                    queue.offer(point);
                    visited.add(point.getPointId());
                }
            }
        } else if (node instanceof CompositeFunctionEntity) {
            CompositeFunctionEntity composite = (CompositeFunctionEntity) node;
            if (composite.getFirstFunction() != null && !visited.contains(composite.getFirstFunction().getFunctionId())) {
                queue.offer(composite.getFirstFunction());
                visited.add(composite.getFirstFunction().getFunctionId());
            }
            if (composite.getSecondFunction() != null && !visited.contains(composite.getSecondFunction().getFunctionId())) {
                queue.offer(composite.getSecondFunction());
                visited.add(composite.getSecondFunction().getFunctionId());
            }
        }
    }

    public List<Object> hierarchicalSearch(Class<?> entityClass, String searchTerm, String sortField, boolean ascending) {
        logger.info("Иерархический поиск для класса: " + entityClass.getSimpleName() + ", термин: " + searchTerm);

        List<Object> results = new ArrayList<>();
        String lowerSearchTerm = searchTerm.toLowerCase();

        if (entityClass.equals(UserEntity.class)) {
            results.addAll(searchUsers(lowerSearchTerm));
        } else if (entityClass.equals(FunctionEntity.class)) {
            results.addAll(searchFunctions(lowerSearchTerm));
        } else if (entityClass.equals(CompositeFunctionEntity.class)) {
            results.addAll(searchCompositeFunctions(lowerSearchTerm));
        } else if (entityClass.equals(PointEntity.class)) {
            results.addAll(searchPoints(lowerSearchTerm));
        }

        sortResults(results, sortField, ascending);

        logger.info("Иерархический поиск завершен. Найдено: " + results.size() + " результатов");
        return results;
    }

    public <T> Optional<T> singleSearch(Class<T> entityClass, String fieldName, Object value) {
        logger.info("Одиночный поиск: " + entityClass.getSimpleName() + "." + fieldName + " = " + value);

        Optional<T> result = Optional.empty();

        if (entityClass.equals(UserEntity.class)) {
            if ("username".equals(fieldName)) {
                result = (Optional<T>) userRepository.findByUsername((String) value);
            } else if ("email".equals(fieldName)) {
                result = (Optional<T>) userRepository.findByEmail((String) value);
            }
        } else if (entityClass.equals(FunctionEntity.class)) { }

        logger.info("Одиночный поиск завершен: " + (result.isPresent() ? "найдено" : "не найдено"));
        return result;
    }

    public <T> SearchResult<T> multipleSearch(Class<T> entityClass, Map<String, Object> criteria,
                                              String sortField, boolean ascending, int page, int size) {
        logger.info("Множественный поиск: " + entityClass.getSimpleName() + ", критерии: " + criteria);

        List<T> results = new ArrayList<>();
        long totalCount = 0;

        if (entityClass.equals(UserEntity.class)) {
            List<UserEntity> users = searchUsersWithCriteria(criteria);
            totalCount = users.size();
            results = (List<T>) paginateAndSort(users, sortField, ascending, page, size);
        } else if (entityClass.equals(FunctionEntity.class)) {
            List<FunctionEntity> functions = searchFunctionsWithCriteria(criteria);
            totalCount = functions.size();
            results = (List<T>) paginateAndSort(functions, sortField, ascending, page, size);
        }

        logger.info("Множественный поиск завершен. Найдено: " + totalCount + " всего, страница: " + page);
        return new SearchResult<>(results, totalCount, page, size);
    }

    private List<UserEntity> searchUsers(String searchTerm) {
        return userRepository.findByUsernameContainingIgnoreCase(searchTerm);
    }

    private List<FunctionEntity> searchFunctions(String searchTerm) {
        List<FunctionEntity> results = new ArrayList<>();
        results.addAll(functionRepository.findByFunctionNameContainingIgnoreCase(searchTerm));
        results.addAll(functionRepository.findByFunctionTypeContainingIgnoreCase(searchTerm));
        results.addAll(functionRepository.findByFunctionExpressionContainingIgnoreCase(searchTerm));
        return results.stream().distinct().collect(Collectors.toList());
    }

    private List<CompositeFunctionEntity> searchCompositeFunctions(String searchTerm) {
        return compositeFunctionRepository.findByCompositeNameContainingIgnoreCase(searchTerm);
    }

    private List<PointEntity> searchPoints(String searchTerm) {
        return pointRepository.findAll().stream()
                .filter(point -> String.valueOf(point.getXValue()).contains(searchTerm) ||
                        String.valueOf(point.getYValue()).contains(searchTerm))
                .collect(Collectors.toList());
    }

    private List<UserEntity> searchUsersWithCriteria(Map<String, Object> criteria) {
        return userRepository.findAll().stream()
                .filter(user -> matchesCriteria(user, criteria))
                .collect(Collectors.toList());
    }

    private List<FunctionEntity> searchFunctionsWithCriteria(Map<String, Object> criteria) {
        return functionRepository.findAll().stream()
                .filter(function -> matchesCriteria(function, criteria))
                .collect(Collectors.toList());
    }

    private boolean matchesCriteria(Object entity, Map<String, Object> criteria) {
        return criteria.entrySet().stream()
                .allMatch(entry -> {
                    try {
                        var field = entity.getClass().getDeclaredField(entry.getKey());
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        return value != null && value.toString().contains(entry.getValue().toString());
                    } catch (Exception e) {
                        return false;
                    }
                });
    }

    public boolean containsSearchTerm(Object entity, String searchTerm) {
        if (entity instanceof UserEntity) {
            UserEntity user = (UserEntity) entity;
            return user.getUsername().toLowerCase().contains(searchTerm) ||
                    user.getEmail().toLowerCase().contains(searchTerm);
        } else if (entity instanceof FunctionEntity) {
            FunctionEntity function = (FunctionEntity) entity;
            return function.getFunctionName().toLowerCase().contains(searchTerm) ||
                    function.getFunctionType().toLowerCase().contains(searchTerm) ||
                    function.getFunctionExpression().toLowerCase().contains(searchTerm);
        } else if (entity instanceof CompositeFunctionEntity) {
            CompositeFunctionEntity composite = (CompositeFunctionEntity) entity;
            return composite.getCompositeName().toLowerCase().contains(searchTerm);
        } else if (entity instanceof PointEntity) {
            PointEntity point = (PointEntity) entity;
            return String.valueOf(point.getXValue()).contains(searchTerm) ||
                    String.valueOf(point.getYValue()).contains(searchTerm);
        }
        return false;
    }

    private <T> List<T> paginateAndSort(List<T> list, String sortField, boolean ascending, int page, int size) {
        sortResults((List<Object>) list, sortField, ascending);

        int start = page * size;
        int end = Math.min(start + size, list.size());
        return start < list.size() ? list.subList(start, end) : new ArrayList<>();
    }

    private void sortResults(List<Object> results, String sortField, boolean ascending) {
        if (sortField == null || results.isEmpty()) return;

        results.sort((a, b) -> {
            try {
                var fieldA = a.getClass().getDeclaredField(sortField);
                var fieldB = b.getClass().getDeclaredField(sortField);
                fieldA.setAccessible(true);
                fieldB.setAccessible(true);

                Comparable valueA = (Comparable) fieldA.get(a);
                Comparable valueB = (Comparable) fieldB.get(b);

                int comparison = valueA.compareTo(valueB);
                return ascending ? comparison : -comparison;

            } catch (Exception e) {
                return 0;
            }
        });
    }

    public static class SearchResult<T> {
        private final List<T> results;
        private final long totalCount;
        private final int page;
        private final int size;

        public SearchResult(List<T> results, long totalCount, int page, int size) {
            this.results = results;
            this.totalCount = totalCount;
            this.page = page;
            this.size = size;
        }

        public List<T> getResults() { return results; }
        public long getTotalCount() { return totalCount; }
        public int getPage() { return page; }
        public int getSize() { return size; }
        public int getTotalPages() { return (int) Math.ceil((double) totalCount / size); }
    }
}