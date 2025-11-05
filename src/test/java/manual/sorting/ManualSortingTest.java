package manual.sorting;

import manual.DatabaseConnection;
import manual.dto.FunctionResponse;
import manual.repository.FunctionRepository;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManualSortingTest {
    private Connection connection;
    private FunctionRepository functionRepository;

    @BeforeAll
    void setup() throws SQLException {
        connection = DatabaseConnection.getConnection();
        functionRepository = new FunctionRepository(connection);
    }

    @BeforeEach
    void cleanup() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");

            stmt.execute("INSERT INTO users (user_id, username, password, email) VALUES (1, 'testuser', 'pass', 'test@test.com')");
            stmt.execute("INSERT INTO functions (function_id, user_id, function_name, function_type, function_expression, x_from, x_to) VALUES " +
                    "(1, 1, 'funcC', 'SQR', 'x*x', 0.0, 5.0), " +
                    "(2, 1, 'funcA', 'LINEAR', '2*x', 1.0, 10.0), " +
                    "(3, 1, 'funcB', 'CONST', '5', -2.0, 2.0), " +
                    "(4, 1, 'funcD', 'SQR', 'x*x', 3.0, 8.0)");
        }
    }

    @AfterAll
    void cleanupAll() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testDatabaseSortByNameAsc() throws SQLException {
        List<FunctionResponse> functions = functionRepository.findAllOrderByNameAsc();

        assertEquals("funcA", functions.get(0).getFunctionName());
        assertEquals("funcB", functions.get(1).getFunctionName());
        assertEquals("funcC", functions.get(2).getFunctionName());
        assertEquals("funcD", functions.get(3).getFunctionName());
    }

    @Test
    void testDatabaseSortByNameDesc() throws SQLException {
        List<FunctionResponse> functions = functionRepository.findAllOrderByNameDesc();

        assertEquals("funcD", functions.get(0).getFunctionName());
        assertEquals("funcC", functions.get(1).getFunctionName());
        assertEquals("funcB", functions.get(2).getFunctionName());
        assertEquals("funcA", functions.get(3).getFunctionName());
    }

    @Test
    void testDatabaseSortByXFrom() throws SQLException {
        List<FunctionResponse> functions = functionRepository.findAllOrderByXFromAsc();

        assertEquals(-2.0, functions.get(0).getXFrom());
        assertEquals(0.0, functions.get(1).getXFrom());
        assertEquals(1.0, functions.get(2).getXFrom());
        assertEquals(3.0, functions.get(3).getXFrom());
    }

    @Test
    void testDatabaseSortByTypeAndName() throws SQLException {
        List<FunctionResponse> functions = functionRepository.findAllOrderByTypeAndName();

        assertEquals("CONST", functions.get(0).getFunctionType());
        assertEquals("funcB", functions.get(0).getFunctionName());
        assertEquals("LINEAR", functions.get(1).getFunctionType());
        assertEquals("funcA", functions.get(1).getFunctionName());
        assertEquals("SQR", functions.get(2).getFunctionType());
        assertEquals("funcC", functions.get(2).getFunctionName());
        assertEquals("SQR", functions.get(3).getFunctionType());
        assertEquals("funcD", functions.get(3).getFunctionName());
    }

    @Test
    void testMemorySortVsDatabaseSort() throws SQLException {
        List<FunctionResponse> dbSorted = functionRepository.findAllOrderByNameAsc();
        List<FunctionResponse> memorySorted = functionRepository.findAll();
        memorySorted.sort(Comparator.comparing(FunctionResponse::getFunctionName));

        assertEquals(dbSorted.size(), memorySorted.size());
        for (int i = 0; i < dbSorted.size(); i++) {
            assertEquals(dbSorted.get(i).getFunctionName(), memorySorted.get(i).getFunctionName());
        }
    }
}