package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    public static String getProperty(String fileName, String key) {
        Properties properties = new Properties();
        String filePath = "src/test/resources/properties" + File.separator + fileName;

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            properties.load(fileInputStream);
            return properties.getProperty(key);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read property '" + key + "' from file: " + filePath, e);
        }
    }
}
