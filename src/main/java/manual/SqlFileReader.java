package manual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SqlFileReader {
    public static String readSqlFile(String fileName) {
        try {
            InputStream inputStream = SqlFileReader.class.getClassLoader()
                .getResourceAsStream("scripts/" + fileName + ".sql");
            if (inputStream == null) {
                throw new RuntimeException("SQL file not found: " + fileName);
            }
            
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            
            return content.toString().trim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read SQL file: " + fileName, e);
        }
    }
}