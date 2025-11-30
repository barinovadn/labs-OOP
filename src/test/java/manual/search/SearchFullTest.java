package manual.search;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SearchFullTest {

    @Test
    void testSearchCriteriaAllFunctionality() {
        SearchCriteria criteria = new SearchCriteria();
        assertNull(criteria.getFunctionName());
        assertNull(criteria.getFunctionType());
        assertNull(criteria.getMinX());
        assertNull(criteria.getMaxX());
        assertNotNull(criteria.getSortFields());
        assertTrue(criteria.getSortFields().isEmpty());
        assertTrue(criteria.isSortAscending());
        assertEquals(100, criteria.getLimit());
        assertFalse(criteria.hasFunctionNameFilter());
        assertFalse(criteria.hasFunctionTypeFilter());
        assertFalse(criteria.hasXRangeFilter());
        assertFalse(criteria.hasSorting());

        criteria.functionName("test");
        assertEquals("test", criteria.getFunctionName());
        assertTrue(criteria.hasFunctionNameFilter());

        criteria.functionName("");
        assertFalse(criteria.hasFunctionNameFilter());

        criteria.functionName(null);
        assertFalse(criteria.hasFunctionNameFilter());

        criteria.functionType("LINEAR");
        assertEquals("LINEAR", criteria.getFunctionType());
        assertTrue(criteria.hasFunctionTypeFilter());

        criteria.functionType("");
        assertFalse(criteria.hasFunctionTypeFilter());

        criteria.functionType(null);
        assertFalse(criteria.hasFunctionTypeFilter());

        criteria.xRange(0.0, 10.0);
        assertEquals(0.0, criteria.getMinX());
        assertEquals(10.0, criteria.getMaxX());
        assertTrue(criteria.hasXRangeFilter());

        criteria.xRange(5.0, null);
        assertTrue(criteria.hasXRangeFilter());

        criteria.xRange(null, 15.0);
        assertTrue(criteria.hasXRangeFilter());

        criteria.xRange(null, null);
        assertFalse(criteria.hasXRangeFilter());

        criteria.sortBy("name", true);
        assertTrue(criteria.hasSorting());
        assertEquals(1, criteria.getSortFields().size());
        assertTrue(criteria.isSortAscending());

        criteria.sortBy("type", false);
        assertEquals(2, criteria.getSortFields().size());
        assertFalse(criteria.isSortAscending());

        criteria.limit(50);
        assertEquals(50, criteria.getLimit());
    }

    @Test
    void testSearchAlgorithmAllCases() {
        assertEquals(SearchAlgorithm.QUICK, SearchAlgorithm.QUICK);
        assertEquals(SearchAlgorithm.DEEP, SearchAlgorithm.DEEP);
        assertEquals("QUICK", SearchAlgorithm.QUICK.name());
        assertEquals("DEEP", SearchAlgorithm.DEEP.name());
        assertEquals(0, SearchAlgorithm.QUICK.ordinal());
        assertEquals(1, SearchAlgorithm.DEEP.ordinal());

        assertEquals(SearchAlgorithm.QUICK, SearchAlgorithm.fromString("QUICK"));
        assertEquals(SearchAlgorithm.QUICK, SearchAlgorithm.fromString("quick"));
        assertEquals(SearchAlgorithm.QUICK, SearchAlgorithm.fromString("Quick"));
        assertEquals(SearchAlgorithm.DEEP, SearchAlgorithm.fromString("DEEP"));
        assertEquals(SearchAlgorithm.DEEP, SearchAlgorithm.fromString("deep"));
        assertEquals(SearchAlgorithm.DEEP, SearchAlgorithm.fromString("Deep"));
        assertEquals(SearchAlgorithm.QUICK, SearchAlgorithm.fromString("invalid"));
        assertEquals(SearchAlgorithm.QUICK, SearchAlgorithm.fromString("unknown"));
        assertEquals(SearchAlgorithm.QUICK, SearchAlgorithm.fromString(""));

        SearchAlgorithm[] values = SearchAlgorithm.values();
        assertEquals(2, values.length);
        assertEquals(SearchAlgorithm.QUICK, SearchAlgorithm.valueOf("QUICK"));
        assertEquals(SearchAlgorithm.DEEP, SearchAlgorithm.valueOf("DEEP"));
    }

    @Test
    void testFunctionGraphSearchCloseWhenOpen() throws SQLException {
        FunctionGraphSearch search = new FunctionGraphSearch();
        assertNotNull(search);
        assertDoesNotThrow(() -> search.close());
    }

    @Test
    void testFunctionGraphSearchCloseMultipleTimes() throws SQLException {
        FunctionGraphSearch search = new FunctionGraphSearch();
        search.close();
        assertDoesNotThrow(() -> search.close());
    }

    @Test
    void testSearchCriteriaChaining() {
        SearchCriteria criteria = new SearchCriteria()
                .functionName("poly")
                .functionType("POLYNOMIAL")
                .xRange(-10.0, 10.0)
                .sortBy("name", true)
                .limit(25);

        assertEquals("poly", criteria.getFunctionName());
        assertEquals("POLYNOMIAL", criteria.getFunctionType());
        assertEquals(-10.0, criteria.getMinX());
        assertEquals(10.0, criteria.getMaxX());
        assertEquals(25, criteria.getLimit());
        assertTrue(criteria.hasFunctionNameFilter());
        assertTrue(criteria.hasFunctionTypeFilter());
        assertTrue(criteria.hasXRangeFilter());
        assertTrue(criteria.hasSorting());
    }
}
