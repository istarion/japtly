package ru.s_zg.japtly;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class TestUtils {
    public static Properties getTestProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream is = TestUtils.class.getResourceAsStream("/application.properties")) {
            properties.load(is);
        }
        return properties;
    }

    public static Path getTestPackage() {
        return Paths.get(TestUtils.class.getResource("/gnome-shell-extension-ubuntu-dock_63ubuntu1_all.deb").getFile());
    }
}
