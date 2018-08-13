package ru.s_zg.japtly.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.s_zg.japtly.Japtly;
import ru.s_zg.japtly.TestUtils;
import ru.s_zg.japtly.model.Version;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class MiscApiTest {
    private Japtly japtly;

    @BeforeEach
    public void before() throws IOException {
        URL aptlyEndpoint = new URL(TestUtils.getTestProperties().getProperty("aptly.endpoint"));
        japtly = new Japtly(aptlyEndpoint);
    }

    @Test
    public void getVersion() throws IOException {
        Version expectedVersion = new Version();
        expectedVersion.setVersion(TestUtils.getTestProperties().getProperty("aptly.version"));
        assertEquals(expectedVersion, japtly.miscApi.getVersion());
    }
}