package ru.s_zg.japtly.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.s_zg.japtly.Japtly;
import ru.s_zg.japtly.TestUtils;
import ru.s_zg.japtly.model.LocalRepository;

import java.io.IOException;
import java.net.URL;
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
        LocalRepository newRepo = LocalRepository.builder()
                .name(UUID.randomUUID().toString())
                .comment("1")
                .defaultComponent("test")
                .defaultDistribution("test")
                .build();

        LocalRepository created = japtly.localReposApi.createRepository(newRepo);
        assertEquals(newRepo, created);
    }

    @Test
    void showRepository() throws IOException {
        LocalRepository newRepo = LocalRepository.builder()
                .name(UUID.randomUUID().toString())
                .comment("1")
                .defaultComponent("test")
                .defaultDistribution("test")
                .build();

        japtly.localReposApi.createRepository(newRepo);

        assertEquals(newRepo, japtly.localReposApi.showRepository(newRepo.getName()));
    }
}