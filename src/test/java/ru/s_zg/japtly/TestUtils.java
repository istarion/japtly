package ru.s_zg.japtly;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestUtils {
    public static Properties getTestProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream is = TestUtils.class.getResourceAsStream("/application.properties")) {
            properties.load(is);
        }
        return properties;
    }
}
