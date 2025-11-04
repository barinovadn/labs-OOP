package manual;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SqlFileReader {
    public static String readSqlFile(String fileName) {
        try {
            return Files.readString(Paths.get("src/main/resources/scripts/" + fileName + ".sql")).trim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read SQL file: " + fileName, e);
        }
    }
}