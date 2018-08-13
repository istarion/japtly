package ru.s_zg.japtly.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Request;
import okhttp3.Response;
import ru.s_zg.japtly.Japtly;
import ru.s_zg.japtly.model.Version;

import java.io.IOException;
import java.net.URL;

public class MiscApi {
    private static final String ENDPOINT_VERSION = "api/version";
    private final ObjectMapper jacksonMapper = new ObjectMapper();
    private final URL aptlyEndpoint;

    public MiscApi(URL aptlyEndpoint) {
        this.aptlyEndpoint = aptlyEndpoint;
    }

    public Version getVersion() throws IOException {
        Request request = new Request.Builder()
                .url(new URL(aptlyEndpoint, ENDPOINT_VERSION))
                .build();

        Response response = Japtly.okHttpClient.newCall(request).execute();
        if (response.body() == null) {
            throw new RuntimeException(response.message());
        }
        return jacksonMapper.readValue(response.body().charStream(), Version.class);
    }
}
