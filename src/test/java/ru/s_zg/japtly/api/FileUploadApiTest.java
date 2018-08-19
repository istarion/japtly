package ru.s_zg.japtly.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.s_zg.japtly.Japtly;
import ru.s_zg.japtly.TestUtils;
import ru.s_zg.japtly.exceptions.NotExistsException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUploadApiTest {

    private static Japtly japtly;

    @BeforeAll
    public static void beforeAll() throws IOException {
        URL aptlyEndpoint = new URL(TestUtils.getTestProperties().getProperty("aptly.endpoint"));
        japtly = new Japtly(aptlyEndpoint);
    }

    @BeforeEach
    public void before() throws IOException {
        cleanAll();
    }

    private void cleanAll() throws IOException {
        for (String directory : japtly.fileUploadApi.listDirectories()) {
            japtly.fileUploadApi.deleteDirectory(directory);
        }
    }

    @Test
    void listDirectories() throws IOException {
        assertEquals(Collections.emptyList(), japtly.fileUploadApi.listDirectories());
        uploadFile();
        assertEquals(Collections.singletonList("test"), japtly.fileUploadApi.listDirectories());
    }

    @Test
    void listFilesInDirectory() throws IOException {
        assertThrows(NotExistsException.class, () -> japtly.fileUploadApi.listFiles("test"));
        uploadFile();
        assertEquals(1, japtly.fileUploadApi.listFiles("test").size());
    }

    @Test
    void uploadFile() throws IOException {
        File pkg = TestUtils.getTestPackage().toFile();
        List<String> result = japtly.fileUploadApi.uploadFile("test", pkg);
        assertEquals(Collections.singletonList("test/" + pkg.getName()), result);
    }

    @Test
    void deleteFileFromDirectory() throws IOException {
        uploadFile();
        String filename = japtly.fileUploadApi.listFiles("test").get(0);
        japtly.fileUploadApi.deleteFileInDirectory("test", filename);
        assertEquals(Collections.emptyList(), japtly.fileUploadApi.listFiles("test"));
    }
}