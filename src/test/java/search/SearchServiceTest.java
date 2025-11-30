package search;

import entity.*;
import repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private FunctionRepository functionRepository;
    
    @Mock
    private CompositeFunctionRepository compositeFunctionRepository;
    
    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private SearchService searchService;

    private UserEntity testUser;
    private FunctionEntity testFunction;
    private CompositeFunctionEntity testComposite;
    private PointEntity testPoint;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity("testuser", "password", "test@email.com");
        testUser.setUserId(1L);
        testUser.setCreatedAt(LocalDateTime.now());
        
        testFunction = new FunctionEntity(testUser, "TestFunc", "POLYNOMIAL", "x^2", 0.0, 10.0);
        testFunction.setFunctionId(1L);
        testFunction.setCreatedAt(LocalDateTime.now());
        
        testPoint = new PointEntity(testFunction, 2.0, 4.0);
        testPoint.setPointId(1L);
        testPoint.setComputedAt(LocalDateTime.now());
        
        testFunction.setComputedPoints(Arrays.asList(testPoint));
        testUser.setFunctions(Arrays.asList(testFunction));
        
        testComposite = new CompositeFunctionEntity(testUser, "composite", testFunction, testFunction);
        testComposite.setCompositeId(1L);
        testComposite.setCreatedAt(LocalDateTime.now());
        
        testUser.setCompositeFunctions(Arrays.asList(testComposite));
    }

    private Object invokePrivateMethod(String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
        Method method = SearchService.class.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(searchService, args);
    }

    @Test
    void testSingleSearchUserByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        Optional<UserEntity> result = searchService.singleSearch(UserEntity.class, "username", "testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void testSingleSearchUserByEmail() {
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(testUser));

        Optional<UserEntity> result = searchService.singleSearch(UserEntity.class, "email", "test@email.com");

        assertTrue(result.isPresent());
        assertEquals("test@email.com", result.get().getEmail());
    }

    @Test
    void testSingleSearchUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<UserEntity> result = searchService.singleSearch(UserEntity.class, "username", "unknown");

        assertFalse(result.isPresent());
    }

    @Test
    void testSingleSearchFunctionEntity() {
        Optional<FunctionEntity> result = searchService.singleSearch(FunctionEntity.class, "functionName", "test");

        assertFalse(result.isPresent());
    }

    @Test
    void testSingleSearchUnknownField() {
        Optional<UserEntity> result = searchService.singleSearch(UserEntity.class, "unknownField", "value");

        assertFalse(result.isPresent());
    }

    @Test
    void testMultipleSearchUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("username", "test");

        SearchService.SearchResult<UserEntity> result = 
            searchService.multipleSearch(UserEntity.class, criteria, "username", true, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
    }

    @Test
    void testMultipleSearchFunctions() {
        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction));

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("functionName", "Test");

        SearchService.SearchResult<FunctionEntity> result = 
            searchService.multipleSearch(FunctionEntity.class, criteria, "functionName", true, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
    }

    @Test
    void testMultipleSearchWithPagination() {
        UserEntity user2 = new UserEntity("user2", "pass", "user2@email.com");
        user2.setUserId(2L);
        user2.setCreatedAt(LocalDateTime.now());
        
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        Map<String, Object> criteria = new HashMap<>();

        SearchService.SearchResult<UserEntity> result = 
            searchService.multipleSearch(UserEntity.class, criteria, null, true, 0, 1);

        assertNotNull(result);
        assertEquals(2, result.getTotalCount());
        assertEquals(1, result.getResults().size());
        assertEquals(2, result.getTotalPages());
    }

    @Test
    void testMultipleSearchWithSorting() {
        UserEntity user2 = new UserEntity("auser", "pass", "auser@email.com");
        user2.setUserId(2L);
        user2.setCreatedAt(LocalDateTime.now());
        
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        Map<String, Object> criteria = new HashMap<>();

        // Ascending
        SearchService.SearchResult<UserEntity> resultAsc = 
            searchService.multipleSearch(UserEntity.class, criteria, "username", true, 0, 10);
        assertNotNull(resultAsc);

        // Descending
        SearchService.SearchResult<UserEntity> resultDesc = 
            searchService.multipleSearch(UserEntity.class, criteria, "username", false, 0, 10);
        assertNotNull(resultDesc);
    }

    @Test
    void testMultipleSearchEmptyResults() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        Map<String, Object> criteria = new HashMap<>();

        SearchService.SearchResult<UserEntity> result = 
            searchService.multipleSearch(UserEntity.class, criteria, null, true, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        assertTrue(result.getResults().isEmpty());
    }

    @Test
    void testMultipleSearchPageBeyondResults() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        Map<String, Object> criteria = new HashMap<>();

        SearchService.SearchResult<UserEntity> result = 
            searchService.multipleSearch(UserEntity.class, criteria, null, true, 10, 10);

        assertNotNull(result);
        assertTrue(result.getResults().isEmpty());
    }

    @Test
    void testContainsSearchTermUser() {
        assertTrue(searchService.containsSearchTerm(testUser, "testuser"));
        assertTrue(searchService.containsSearchTerm(testUser, "test@email"));
        assertFalse(searchService.containsSearchTerm(testUser, "notfound"));
    }

    @Test
    void testContainsSearchTermFunction() {
        assertTrue(searchService.containsSearchTerm(testFunction, "testfunc"));
        assertTrue(searchService.containsSearchTerm(testFunction, "polynomial"));
        assertTrue(searchService.containsSearchTerm(testFunction, "x^2"));
        assertFalse(searchService.containsSearchTerm(testFunction, "notfound"));
    }

    @Test
    void testContainsSearchTermComposite() {
        assertTrue(searchService.containsSearchTerm(testComposite, "composite"));
        assertFalse(searchService.containsSearchTerm(testComposite, "notfound"));
    }

    @Test
    void testContainsSearchTermPoint() {
        assertTrue(searchService.containsSearchTerm(testPoint, "2.0"));
        assertTrue(searchService.containsSearchTerm(testPoint, "4.0"));
        assertFalse(searchService.containsSearchTerm(testPoint, "notfound"));
    }

    @Test
    void testContainsSearchTermUnknownEntity() {
        Object unknownEntity = new Object();
        assertFalse(searchService.containsSearchTerm(unknownEntity, "any"));
    }

    @Test
    void testSearchResultMethods() {
        List<String> results = Arrays.asList("a", "b", "c");
        SearchService.SearchResult<String> searchResult = 
            new SearchService.SearchResult<>(results, 10, 1, 3);

        assertEquals(results, searchResult.getResults());
        assertEquals(10, searchResult.getTotalCount());
        assertEquals(1, searchResult.getPage());
        assertEquals(3, searchResult.getSize());
        assertEquals(4, searchResult.getTotalPages()); // ceil(10/3) = 4
    }

    @Test
    void testSearchResultTotalPagesCalculation() {
        // Exact division
        SearchService.SearchResult<String> result1 = 
            new SearchService.SearchResult<>(Arrays.asList(), 20, 0, 10);
        assertEquals(2, result1.getTotalPages());

        // With remainder
        SearchService.SearchResult<String> result2 = 
            new SearchService.SearchResult<>(Arrays.asList(), 21, 0, 10);
        assertEquals(3, result2.getTotalPages());

        // Single page
        SearchService.SearchResult<String> result3 = 
            new SearchService.SearchResult<>(Arrays.asList(), 5, 0, 10);
        assertEquals(1, result3.getTotalPages());
    }

    @Test
    void testMultipleSearchWithMultipleCriteria() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("username", "test");
        criteria.put("email", "email");

        SearchService.SearchResult<UserEntity> result = 
            searchService.multipleSearch(UserEntity.class, criteria, null, true, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
    }

    @Test
    void testMultipleSearchWithNonMatchingCriteria() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("username", "notfound");

        SearchService.SearchResult<UserEntity> result = 
            searchService.multipleSearch(UserEntity.class, criteria, null, true, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    void testMultipleSearchWithInvalidField() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("nonExistentField", "value");

        SearchService.SearchResult<UserEntity> result = 
            searchService.multipleSearch(UserEntity.class, criteria, null, true, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    void testMultipleSearchSortingWithInvalidField() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        Map<String, Object> criteria = new HashMap<>();

        SearchService.SearchResult<UserEntity> result = 
            searchService.multipleSearch(UserEntity.class, criteria, "invalidField", true, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
    }

    @Test
    void testMultipleSearchWithNullSortField() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        Map<String, Object> criteria = new HashMap<>();

        SearchService.SearchResult<UserEntity> result = 
            searchService.multipleSearch(UserEntity.class, criteria, null, true, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
    }

    @Test
    void testDeepUserHierarchy() throws Exception {
        Set<Long> visited = new HashSet<>();
        List<Object> results = new ArrayList<>();
        
        invokePrivateMethod("deepUserHierarchy", 
            new Class<?>[]{UserEntity.class, String.class, Set.class, List.class},
            testUser, "testuser", visited, results);
        
        assertFalse(results.isEmpty());
        assertTrue(visited.contains(testUser.getUserId()));
    }

    @Test
    void testDeepUserHierarchyAlreadyVisited() throws Exception {
        Set<Long> visited = new HashSet<>();
        visited.add(testUser.getUserId()); // Already visited
        List<Object> results = new ArrayList<>();
        
        invokePrivateMethod("deepUserHierarchy", 
            new Class<?>[]{UserEntity.class, String.class, Set.class, List.class},
            testUser, "testuser", visited, results);
        
        assertTrue(results.isEmpty()); // Should not add since already visited
    }

    @Test
    void testDeepFunctionHierarchy() throws Exception {
        Set<Long> visited = new HashSet<>();
        List<Object> results = new ArrayList<>();
        
        invokePrivateMethod("deepFunctionHierarchy", 
            new Class<?>[]{FunctionEntity.class, String.class, Set.class, List.class},
            testFunction, "testfunc", visited, results);
        
        assertFalse(results.isEmpty());
        assertTrue(visited.contains(testFunction.getFunctionId()));
    }

    @Test
    void testDeepFunctionHierarchyAlreadyVisited() throws Exception {
        Set<Long> visited = new HashSet<>();
        visited.add(testFunction.getFunctionId());
        List<Object> results = new ArrayList<>();
        
        invokePrivateMethod("deepFunctionHierarchy", 
            new Class<?>[]{FunctionEntity.class, String.class, Set.class, List.class},
            testFunction, "testfunc", visited, results);
        
        assertTrue(results.isEmpty());
    }

    @Test
    void testDeepFunctionHierarchyWithPointMatch() throws Exception {
        Set<Long> visited = new HashSet<>();
        List<Object> results = new ArrayList<>();
        
        invokePrivateMethod("deepFunctionHierarchy", 
            new Class<?>[]{FunctionEntity.class, String.class, Set.class, List.class},
            testFunction, "2.0", visited, results);
        
        // Should find the point with x=2.0
        assertTrue(results.stream().anyMatch(r -> r instanceof PointEntity));
    }

    @Test
    void testDeepCompositeHierarchy() throws Exception {
        Set<Long> visited = new HashSet<>();
        List<Object> results = new ArrayList<>();
        
        invokePrivateMethod("deepCompositeHierarchy", 
            new Class<?>[]{CompositeFunctionEntity.class, String.class, Set.class, List.class},
            testComposite, "composite", visited, results);
        
        assertFalse(results.isEmpty());
        assertTrue(visited.contains(testComposite.getCompositeId()));
    }

    @Test
    void testDeepCompositeHierarchyAlreadyVisited() throws Exception {
        Set<Long> visited = new HashSet<>();
        visited.add(testComposite.getCompositeId());
        List<Object> results = new ArrayList<>();
        
        invokePrivateMethod("deepCompositeHierarchy", 
            new Class<?>[]{CompositeFunctionEntity.class, String.class, Set.class, List.class},
            testComposite, "composite", visited, results);
        
        assertTrue(results.isEmpty());
    }

    @Test
    void testDeepCompositeHierarchyWithNullFunctions() throws Exception {
        CompositeFunctionEntity compositeWithNulls = new CompositeFunctionEntity();
        compositeWithNulls.setCompositeId(99L);
        compositeWithNulls.setCompositeName("nullfuncs");
        compositeWithNulls.setFirstFunction(null);
        compositeWithNulls.setSecondFunction(null);
        
        Set<Long> visited = new HashSet<>();
        List<Object> results = new ArrayList<>();
        
        invokePrivateMethod("deepCompositeHierarchy", 
            new Class<?>[]{CompositeFunctionEntity.class, String.class, Set.class, List.class},
            compositeWithNulls, "nullfuncs", visited, results);
        
        assertFalse(results.isEmpty());
    }

    @Test
    void testAddNeighborsToQueueUser() throws Exception {
        Queue<Object> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        
        invokePrivateMethod("addNeighborsToQueue", 
            new Class<?>[]{Object.class, Queue.class, Set.class},
            testUser, queue, visited);
        
        assertFalse(queue.isEmpty());
    }

    @Test
    void testAddNeighborsToQueueUserAlreadyVisited() throws Exception {
        Queue<Object> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        visited.add(testFunction.getFunctionId());
        visited.add(testComposite.getCompositeId());
        
        invokePrivateMethod("addNeighborsToQueue", 
            new Class<?>[]{Object.class, Queue.class, Set.class},
            testUser, queue, visited);
        
        assertTrue(queue.isEmpty()); // All already visited
    }

    @Test
    void testAddNeighborsToQueueFunction() throws Exception {
        Queue<Object> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        
        invokePrivateMethod("addNeighborsToQueue", 
            new Class<?>[]{Object.class, Queue.class, Set.class},
            testFunction, queue, visited);
        
        assertFalse(queue.isEmpty()); // Should add points
    }

    @Test
    void testAddNeighborsToQueueFunctionPointsVisited() throws Exception {
        Queue<Object> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        visited.add(testPoint.getPointId());
        
        invokePrivateMethod("addNeighborsToQueue", 
            new Class<?>[]{Object.class, Queue.class, Set.class},
            testFunction, queue, visited);
        
        assertTrue(queue.isEmpty());
    }

    @Test
    void testAddNeighborsToQueueComposite() throws Exception {
        Queue<Object> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        
        invokePrivateMethod("addNeighborsToQueue", 
            new Class<?>[]{Object.class, Queue.class, Set.class},
            testComposite, queue, visited);
        
        assertFalse(queue.isEmpty());
    }

    @Test
    void testAddNeighborsToQueueCompositeWithNulls() throws Exception {
        CompositeFunctionEntity compositeWithNulls = new CompositeFunctionEntity();
        compositeWithNulls.setCompositeId(99L);
        compositeWithNulls.setFirstFunction(null);
        compositeWithNulls.setSecondFunction(null);
        
        Queue<Object> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        
        invokePrivateMethod("addNeighborsToQueue", 
            new Class<?>[]{Object.class, Queue.class, Set.class},
            compositeWithNulls, queue, visited);
        
        assertTrue(queue.isEmpty());
    }

    @Test
    void testAddNeighborsToQueueCompositeFunctionsVisited() throws Exception {
        Queue<Object> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        visited.add(testFunction.getFunctionId());
        
        invokePrivateMethod("addNeighborsToQueue", 
            new Class<?>[]{Object.class, Queue.class, Set.class},
            testComposite, queue, visited);
        
        assertTrue(queue.isEmpty());
    }

    @Test
    void testAddNeighborsToQueueUnknownType() throws Exception {
        Queue<Object> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        
        invokePrivateMethod("addNeighborsToQueue", 
            new Class<?>[]{Object.class, Queue.class, Set.class},
            "unknown type", queue, visited);
        
        assertTrue(queue.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testSearchUsers() throws Exception {
        when(userRepository.findByUsernameContainingIgnoreCase("test"))
            .thenReturn(Arrays.asList(testUser));
        
        List<UserEntity> result = (List<UserEntity>) invokePrivateMethod("searchUsers", 
            new Class<?>[]{String.class}, "test");
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testSearchFunctions() throws Exception {
        when(functionRepository.findByFunctionNameContainingIgnoreCase("test"))
            .thenReturn(Arrays.asList(testFunction));
        when(functionRepository.findByFunctionTypeContainingIgnoreCase("test"))
            .thenReturn(Collections.emptyList());
        when(functionRepository.findByFunctionExpressionContainingIgnoreCase("test"))
            .thenReturn(Collections.emptyList());
        
        List<FunctionEntity> result = (List<FunctionEntity>) invokePrivateMethod("searchFunctions", 
            new Class<?>[]{String.class}, "test");
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testSearchFunctionsDistinct() throws Exception {
        when(functionRepository.findByFunctionNameContainingIgnoreCase("test"))
            .thenReturn(Arrays.asList(testFunction));
        when(functionRepository.findByFunctionTypeContainingIgnoreCase("test"))
            .thenReturn(Arrays.asList(testFunction)); // Same function
        when(functionRepository.findByFunctionExpressionContainingIgnoreCase("test"))
            .thenReturn(Collections.emptyList());
        
        List<FunctionEntity> result = (List<FunctionEntity>) invokePrivateMethod("searchFunctions", 
            new Class<?>[]{String.class}, "test");
        
        assertNotNull(result);
        assertEquals(1, result.size()); // Should be distinct
    }

    @Test
    @SuppressWarnings("unchecked")
    void testSearchCompositeFunctions() throws Exception {
        when(compositeFunctionRepository.findByCompositeNameContainingIgnoreCase("comp"))
            .thenReturn(Arrays.asList(testComposite));
        
        List<CompositeFunctionEntity> result = (List<CompositeFunctionEntity>) 
            invokePrivateMethod("searchCompositeFunctions", new Class<?>[]{String.class}, "comp");
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testSearchPoints() throws Exception {
        when(pointRepository.findAll()).thenReturn(Arrays.asList(testPoint));
        
        List<PointEntity> result = (List<PointEntity>) invokePrivateMethod("searchPoints", 
            new Class<?>[]{String.class}, "2.0");
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testSearchPointsByYValue() throws Exception {
        when(pointRepository.findAll()).thenReturn(Arrays.asList(testPoint));
        
        List<PointEntity> result = (List<PointEntity>) invokePrivateMethod("searchPoints", 
            new Class<?>[]{String.class}, "4.0");
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testSearchPointsNoMatch() throws Exception {
        when(pointRepository.findAll()).thenReturn(Arrays.asList(testPoint));
        
        List<PointEntity> result = (List<PointEntity>) invokePrivateMethod("searchPoints", 
            new Class<?>[]{String.class}, "999");
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMatchesCriteriaWithNullValue() throws Exception {
        UserEntity userWithNullEmail = new UserEntity("test", "pass", null);
        
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("email", "test");
        
        Boolean result = (Boolean) invokePrivateMethod("matchesCriteria", 
            new Class<?>[]{Object.class, Map.class}, userWithNullEmail, criteria);
        
        assertFalse(result);
    }

    @Test
    void testSortResultsDescending() throws Exception {
        UserEntity user2 = new UserEntity("auser", "pass", "a@test.com");
        user2.setUserId(2L);
        
        List<Object> results = new ArrayList<>(Arrays.asList(testUser, user2));
        
        invokePrivateMethod("sortResults", 
            new Class<?>[]{List.class, String.class, boolean.class},
            results, "username", false);
        
        // First should be "testuser" (comes after "auser" alphabetically, but descending)
        assertEquals("testuser", ((UserEntity) results.get(0)).getUsername());
    }

    @Test
    void testSortResultsEmptyList() throws Exception {
        List<Object> results = new ArrayList<>();
        
        invokePrivateMethod("sortResults", 
            new Class<?>[]{List.class, String.class, boolean.class},
            results, "username", true);
        
        assertTrue(results.isEmpty());
    }
}

