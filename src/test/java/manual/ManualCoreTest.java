package manual;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ManualCoreTest {

    @Test
    void testDatabaseConnectionAllCases() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        assertNotNull(conn);
        assertFalse(conn.isClosed());
        assertTrue(conn.isValid(5));
        assertTrue(conn.getAutoCommit());
        assertNotNull(conn.getCatalog());
        assertNotNull(conn.getMetaData());
        assertEquals("PostgreSQL", conn.getMetaData().getDatabaseProductName());
        conn.close();

        Connection conn2 = DatabaseConnection.getConnection();
        assertNotNull(conn2);
        conn2.close();
    }

    @Test
    void testSqlFileReaderAllCases() {
        assertNotNull(SqlFileReader.readSqlFile("UserCreate"));
        assertFalse(SqlFileReader.readSqlFile("UserCreate").isEmpty());
        assertNotNull(SqlFileReader.readSqlFile("UserRead"));
        assertNotNull(SqlFileReader.readSqlFile("UserUpdate"));
        assertNotNull(SqlFileReader.readSqlFile("UserDelete"));
        assertNotNull(SqlFileReader.readSqlFile("UserReadAll"));
        assertNotNull(SqlFileReader.readSqlFile("UserReadByUsername"));
        assertNotNull(SqlFileReader.readSqlFile("FunctionCreate"));
        assertNotNull(SqlFileReader.readSqlFile("FunctionRead"));
        assertNotNull(SqlFileReader.readSqlFile("FunctionUpdate"));
        assertNotNull(SqlFileReader.readSqlFile("FunctionDelete"));
        assertNotNull(SqlFileReader.readSqlFile("FunctionReadByUserId"));
        assertNotNull(SqlFileReader.readSqlFile("PointsCreate"));
        assertNotNull(SqlFileReader.readSqlFile("PointsRead"));
        assertNotNull(SqlFileReader.readSqlFile("PointsUpdate"));
        assertNotNull(SqlFileReader.readSqlFile("PointsDelete"));
        assertNotNull(SqlFileReader.readSqlFile("PointsReadByFunction"));
        assertNotNull(SqlFileReader.readSqlFile("PointsReadByFunctionAndX"));
        assertNotNull(SqlFileReader.readSqlFile("CompositeFunctionCreate"));
        assertNotNull(SqlFileReader.readSqlFile("CompositeFunctionRead"));
        assertNotNull(SqlFileReader.readSqlFile("CompositeFunctionUpdate"));
        assertNotNull(SqlFileReader.readSqlFile("CompositeFunctionDelete"));
        assertNotNull(SqlFileReader.readSqlFile("CompositeFunctionReadByUserId"));

        Exception ex1 = assertThrows(RuntimeException.class, () -> 
            SqlFileReader.readSqlFile("TotallyFakeFile"));
        assertTrue(ex1.getMessage().contains("SQL file not found"));

        Exception ex2 = assertThrows(RuntimeException.class, () -> 
            SqlFileReader.readSqlFile(""));
        assertTrue(ex2.getMessage().contains("SQL file not found"));
    }
}

