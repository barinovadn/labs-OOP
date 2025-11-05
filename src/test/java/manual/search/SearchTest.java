package manual.search;

import manual.DatabaseConnection;
import manual.dto.FunctionResponse;
import manual.dto.CompositeFunctionResponse;
import manual.repository.FunctionRepository;
import manual.repository.CompositeFunctionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchTest {

    private FunctionRepository functionRepository;
    private CompositeFunctionRepository compositeRepository;
    private FunctionGraphSearch graphSearch;

    @BeforeEach
    void setUp() throws SQLException {
        functionRepository = mock(FunctionRepository.class);
        compositeRepository = mock(CompositeFunctionRepository.class);

        graphSearch = new FunctionGraphSearch() {
            {
                try {
                    var functionField = FunctionGraphSearch.class.getDeclaredField("functionRepository");
                    var compositeField = FunctionGraphSearch.class.getDeclaredField("compositeRepository");
                    functionField.setAccessible(true);
                    compositeField.setAccessible(true);
                    functionField.set(this, functionRepository);
                    compositeField.set(this, compositeRepository);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Test
    void testSearchAlgorithm() {
        assertEquals(SearchAlgorithm.BFS, SearchAlgorithm.fromString("bfs"));
        assertEquals(SearchAlgorithm.DFS, SearchAlgorithm.fromString("dfs"));
        assertEquals(SearchAlgorithm.BFS, SearchAlgorithm.fromString("unknown"));
    }

    @Test
    void testSearchCriteria() {
        SearchCriteria criteria = new SearchCriteria()
                .functionName("test")
                .functionType("SQR")
                .xRange(0.0, 10.0)
                .sortBy("name", true)
                .limit(50);

        assertEquals("test", criteria.getFunctionName());
        assertEquals("SQR", criteria.getFunctionType());
        assertEquals(0.0, criteria.getMinX());
        assertEquals(10.0, criteria.getMaxX());
        assertEquals(1, criteria.getSortFields().size());
        assertEquals("name", criteria.getSortFields().get(0));
        assertTrue(criteria.isSortAscending());
        assertEquals(50, criteria.getLimit());

        assertTrue(criteria.hasFunctionNameFilter());
        assertTrue(criteria.hasFunctionTypeFilter());
        assertTrue(criteria.hasXRangeFilter());
        assertTrue(criteria.hasSorting());

        SearchCriteria empty = new SearchCriteria();
        assertFalse(empty.hasFunctionNameFilter());
        assertFalse(empty.hasFunctionTypeFilter());
        assertFalse(empty.hasXRangeFilter());
        assertFalse(empty.hasSorting());
    }

    @Test
    void testSearchCriteriaPartialXRanges() {
        SearchCriteria minOnly = new SearchCriteria().xRange(5.0, null);
        assertTrue(minOnly.hasXRangeFilter());
        assertEquals(5.0, minOnly.getMinX());
        assertNull(minOnly.getMaxX());

        SearchCriteria maxOnly = new SearchCriteria().xRange(null, 10.0);
        assertTrue(maxOnly.hasXRangeFilter());
        assertNull(maxOnly.getMinX());
        assertEquals(10.0, maxOnly.getMaxX());
    }

    @Test
    void testSearchCriteriaEmptyStrings() {
        SearchCriteria emptyName = new SearchCriteria().functionName("");
        assertFalse(emptyName.hasFunctionNameFilter());

        SearchCriteria nullName = new SearchCriteria().functionName(null);
        assertFalse(nullName.hasFunctionNameFilter());
    }

    @Test
    void testObjectCreation() {
        assertNotNull(new SearchCriteria());
        assertNotNull(SearchAlgorithm.BFS);
        assertNotNull(SearchAlgorithm.DFS);
    }

    @Test
    void testSearchAlgorithmValues() {
        assertEquals("BFS", SearchAlgorithm.BFS.name());
        assertEquals("DFS", SearchAlgorithm.DFS.name());
    }

    @Test
    void testFunctionGraphSearchCreation() throws SQLException {
        assertDoesNotThrow(() -> {
            FunctionGraphSearch search = new FunctionGraphSearch();
            assertNotNull(search);
        });
    }

    @Test
    void testBFSWithNullStart() throws SQLException {
        SearchCriteria criteria = new SearchCriteria().limit(10);

        List<FunctionResponse> result = graphSearch.breadthFirstSearch(null, criteria);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDFSWithNullStart() throws SQLException {
        SearchCriteria criteria = new SearchCriteria().limit(10);

        List<FunctionResponse> result = graphSearch.depthFirstSearch(null, criteria);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMatchesCriteriaFunction() {
        FunctionResponse function = new FunctionResponse(1L, 1L, "testFunc", "SQR", "x*x", 0.0, 10.0, LocalDateTime.now());

        SearchCriteria criteria = new SearchCriteria()
                .functionName("test")
                .functionType("SQR")
                .xRange(0.0, 15.0);

        assertDoesNotThrow(() -> {
            var method = FunctionGraphSearch.class.getDeclaredMethod("matchesCriteria", FunctionResponse.class, SearchCriteria.class);
            method.setAccessible(true);
            boolean matches = (boolean) method.invoke(graphSearch, function, criteria);
            assertTrue(matches);
        });
    }

    @Test
    void testMatchesCriteriaNoFilters() {
        FunctionResponse function = new FunctionResponse(1L, 1L, "anyFunc", "ANY", "x", 5.0, 15.0, LocalDateTime.now());

        SearchCriteria criteria = new SearchCriteria();

        assertDoesNotThrow(() -> {
            var method = FunctionGraphSearch.class.getDeclaredMethod("matchesCriteria", FunctionResponse.class, SearchCriteria.class);
            method.setAccessible(true);
            boolean matches = (boolean) method.invoke(graphSearch, function, criteria);
            assertTrue(matches);
        });
    }

    @Test
    void testMatchesCriteriaFunctionNameMismatch() {
        FunctionResponse function = new FunctionResponse(1L, 1L, "different", "SQR", "x*x", 0.0, 10.0, LocalDateTime.now());

        SearchCriteria criteria = new SearchCriteria().functionName("test");

        assertDoesNotThrow(() -> {
            var method = FunctionGraphSearch.class.getDeclaredMethod("matchesCriteria", FunctionResponse.class, SearchCriteria.class);
            method.setAccessible(true);
            boolean matches = (boolean) method.invoke(graphSearch, function, criteria);
            assertFalse(matches);
        });
    }

    @Test
    void testMatchesCriteriaXRangeMismatch() {
        FunctionResponse function = new FunctionResponse(1L, 1L, "test", "SQR", "x*x", 20.0, 30.0, LocalDateTime.now());

        SearchCriteria criteria = new SearchCriteria().xRange(0.0, 15.0);

        assertDoesNotThrow(() -> {
            var method = FunctionGraphSearch.class.getDeclaredMethod("matchesCriteria", FunctionResponse.class, SearchCriteria.class);
            method.setAccessible(true);
            boolean matches = (boolean) method.invoke(graphSearch, function, criteria);
            assertFalse(matches);
        });
    }

    @Test
    void testGraphSearchCloseAlreadyClosed() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);

        FunctionGraphSearch search = new FunctionGraphSearch();

        assertDoesNotThrow(search::close);
        verify(connection, never()).close();
    }

    @Test
    void testBFSWithMockData() throws SQLException {
        Long startId = 1L;
        SearchCriteria criteria = new SearchCriteria().limit(10);

        FunctionResponse function = new FunctionResponse(startId, 1L, "func1", "SQR", "x*x", 0.0, 10.0, LocalDateTime.now());
        when(functionRepository.findById(startId)).thenReturn(function);
        when(compositeRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        List<FunctionResponse> result = graphSearch.breadthFirstSearch(startId, criteria);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("func1", result.get(0).getFunctionName());
    }

    @Test
    void testDFSWithMockData() throws SQLException {
        Long startId = 1L;
        SearchCriteria criteria = new SearchCriteria().limit(10);

        FunctionResponse function = new FunctionResponse(startId, 1L, "func1", "SQR", "x*x", 0.0, 10.0, LocalDateTime.now());
        when(functionRepository.findById(startId)).thenReturn(function);
        when(compositeRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        List<FunctionResponse> result = graphSearch.depthFirstSearch(startId, criteria);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("func1", result.get(0).getFunctionName());
    }

    @Test
    void testHierarchicalSearchWithMockData() throws SQLException {
        Long startId = 1L;
        SearchCriteria criteria = new SearchCriteria().limit(10);

        FunctionResponse function = new FunctionResponse(startId, 1L, "func1", "SQR", "x*x", 0.0, 10.0, LocalDateTime.now());
        when(functionRepository.findById(startId)).thenReturn(function);
        when(compositeRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        List<FunctionResponse> result = graphSearch.hierarchicalSearch(startId, criteria);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("func1", result.get(0).getFunctionName());
    }
}