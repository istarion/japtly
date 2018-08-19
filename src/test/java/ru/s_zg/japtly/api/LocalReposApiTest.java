package ru.s_zg.japtly.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.s_zg.japtly.Japtly;
import ru.s_zg.japtly.TestUtils;
import ru.s_zg.japtly.exceptions.AptlyException;
import ru.s_zg.japtly.model.AddPackagesResponse;
import ru.s_zg.japtly.model.DebPackage;
import ru.s_zg.japtly.model.LocalRepository;
import ru.s_zg.japtly.model.SearchQuery;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocalReposApiTest {
    private Japtly japtly;

    @BeforeEach
    public void before() throws IOException {
        URL aptlyEndpoint = new URL(TestUtils.getTestProperties().getProperty("aptly.endpoint"));
        japtly = new Japtly(aptlyEndpoint);
    }



    @Test
    void createRepository() throws IOException {
        LocalRepository newRepo = buildRepo();

        LocalRepository created = japtly.localReposApi.createRepository(newRepo);
        assertEquals(newRepo, created);
    }

    private LocalRepository buildRepo() {
        return LocalRepository.builder()
                .name(UUID.randomUUID().toString())
                .comment("1")
                .defaultComponent("test")
                .defaultDistribution("test")
                .build();
    }

    @Test
    void editRepository() throws IOException {
        LocalRepository newRepo = buildRepo();
        japtly.localReposApi.createRepository(newRepo);

        newRepo.setComment("2");
        newRepo.setDefaultDistribution("test2");
        LocalRepository editedRepo = japtly.localReposApi.editRepository(newRepo.getName(), newRepo);

        assertEquals(newRepo, editedRepo);
    }

    @Test
    void showRepository() throws IOException {
        LocalRepository newRepo = buildRepo();

        japtly.localReposApi.createRepository(newRepo);

        assertEquals(newRepo, japtly.localReposApi.showRepository(newRepo.getName()));
    }

    @Test
    void searchPackage() throws IOException {
        LocalRepository newRepo = buildRepo();

        japtly.localReposApi.createRepository(newRepo);

        SearchQuery searchQuery = SearchQuery.builder()
                .format(SearchQuery.ResultFormat.DETAILS)
                .build();

        List<DebPackage> debPackages = japtly.localReposApi.search(newRepo.getName(), searchQuery);
        assertTrue(debPackages.isEmpty());
    }

    @Test
    void list() throws IOException {
        LocalRepository newRepo = buildRepo();
        japtly.localReposApi.createRepository(newRepo);

        List<LocalRepository> localRepositories = japtly.localReposApi.list();
        assertTrue(localRepositories.contains(newRepo));
    }

    @Test
    void delete() throws IOException {
        LocalRepository newRepo = buildRepo();
        japtly.localReposApi.createRepository(newRepo);

        japtly.localReposApi.delete(newRepo.getName());

        assertThrows(AptlyException.class, () -> japtly.localReposApi.showRepository(newRepo.getName())) ;
    }

    @Test
    void addPackages() throws IOException {
        LocalRepository newRepo = buildRepo();
        japtly.localReposApi.createRepository(newRepo);

        FileUploadApiTest fileUploadApiTest = new FileUploadApiTest();
        FileUploadApiTest.beforeAll();
        fileUploadApiTest.before();
        fileUploadApiTest.uploadFile();
        String uploadedFilename = japtly.fileUploadApi.listFiles("test").get(0);

        AddPackagesResponse addResult = japtly.localReposApi.addPackagesFromUpload(newRepo.getName(), "test");
        AddPackagesResponse expectedResponse = new AddPackagesResponse();
        expectedResponse.setFailedFiles(Collections.emptyList());
        AddPackagesResponse.Report expectedReport = new AddPackagesResponse.Report();
        expectedReport.setAdded(Collections.singletonList(
                uploadedFilename.split("\\.deb")[0] + " added"
        ));
        expectedReport.setRemoved(Collections.emptyList());
        expectedReport.setWarnings(Collections.emptyList());
        expectedResponse.setReport(expectedReport);

        assertEquals(expectedResponse, addResult);
    }
}