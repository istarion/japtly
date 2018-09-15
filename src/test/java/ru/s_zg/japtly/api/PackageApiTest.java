package ru.s_zg.japtly.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.s_zg.japtly.Japtly;
import ru.s_zg.japtly.TestUtils;
import ru.s_zg.japtly.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.s_zg.japtly.api.LocalReposApiTest.buildRepo;

public class PackageApiTest {
    private Japtly japtly;

    @BeforeEach
    public void before() throws IOException {
        URL aptlyEndpoint = new URL(TestUtils.getTestProperties().getProperty("aptly.endpoint"));
        japtly = new Japtly(aptlyEndpoint);
    }

    @Test
    public void showPackage() throws IOException {
        LocalRepository newRepo = buildRepo();
        japtly.localReposApi.createRepository(newRepo);

        FileUploadApiTest fileUploadApiTest = new FileUploadApiTest();
        FileUploadApiTest.beforeAll();
        fileUploadApiTest.before();
        fileUploadApiTest.uploadFile();
        japtly.localReposApi.addPackagesFromUpload(newRepo.getName(), "test");

        PackageApi.Response pkg = japtly.packageApi.show("Pall supersh 1.1-2 f075596fa8360fc2");
        assertEquals("ed40671c444c1768d41de0c1bed41910958ec80c", pkg.getSha1());
    }
}
