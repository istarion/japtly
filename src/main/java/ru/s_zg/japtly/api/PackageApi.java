package ru.s_zg.japtly.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import okhttp3.Request;
import okhttp3.Response;
import ru.s_zg.japtly.Japtly;
import ru.s_zg.japtly.exceptions.NotExistsException;
import ru.s_zg.japtly.model.DebPackage;
import ru.s_zg.japtly.model.Version;

import java.io.IOException;
import java.net.URL;

public class PackageApi {
    private static final String ENDPOINT_PACKAGES = "api/packages";
    private final ObjectMapper jacksonMapper = new ObjectMapper();
    private final URL aptlyEndpoint;

    public PackageApi(URL aptlyEndpoint) {
        this.aptlyEndpoint = aptlyEndpoint;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class Response extends DebPackage{
        @JsonProperty("Key")
        private String key;

        @JsonProperty("ShortKey")
        private String shortKey;

        @JsonProperty("FilesHash")
        private String filesHash;
    }

    public Response show(String key) throws IOException {
        Request request = new Request.Builder()
                .url(new URL(aptlyEndpoint, ENDPOINT_PACKAGES + "/" + key))
                .build();

        okhttp3.Response response = Japtly.okHttpClient.newCall(request).execute();
        if (response.code() == 404) {
            throw new NotExistsException("Package with such key doesn’t exist");
        }
        if (response.body() == null) {
            throw new RuntimeException(response.message());
        }
        return jacksonMapper.readValue(response.body().charStream(), Response.class);
    }
}
