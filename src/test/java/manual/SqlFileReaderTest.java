package manual;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SqlFileReaderTest {

    @Test
    void testReadExistingSqlFile() {
        String sql = SqlFileReader.readSqlFile("UserCreate");
        assertNotNull(sql);
        assertTrue(sql.contains("INSERT INTO"));
    }

    @Test
    void testReadNonExistentSqlFile() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            SqlFileReader.readSqlFile("NonExistentFile");
        });
        assertTrue(exception.getMessage().contains("Failed to read SQL file"));
    }
}