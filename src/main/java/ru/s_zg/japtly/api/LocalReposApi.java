package ru.s_zg.japtly.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.s_zg.japtly.Japtly;
import ru.s_zg.japtly.exceptions.AptlyException;
import ru.s_zg.japtly.model.LocalRepository;

import java.io.IOException;
import java.net.URL;

public class LocalReposApi {
    private static final String ENDPOINT_REPOS = "/api/repos";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final ObjectMapper jacksonMapper = new ObjectMapper();
    private final URL aptlyEndpoint;

    public LocalReposApi(URL aptlyEndpoint1) {
        this.aptlyEndpoint = aptlyEndpoint1;
    }

    public LocalRepository createRepository(LocalRepository repository) throws IOException {
        RequestBody body = RequestBody.create(JSON, jacksonMapper.writeValueAsString(repository));
        Request request = new Request.Builder()
                .url(new URL(aptlyEndpoint, ENDPOINT_REPOS))
                .post(body)
                .build();

        Response response = Japtly.okHttpClient.newCall(request).execute();
        if (response.code() == 400) {
            throw new AptlyException("Repository with such name already exists");
        }

        if (response.body() == null) {
            throw new AptlyException(response.message());
        }
        return jacksonMapper.readValue(response.body().charStream(), LocalRepository.class);
    }

    public LocalRepository showRepository(String name) throws IOException {
        Request request = new Request.Builder()
                .url(new URL(aptlyEndpoint, ENDPOINT_REPOS + "/" + name))
                .build();

        Response response = Japtly.okHttpClient.newCall(request).execute();
        if (response.code() == 404) {
            throw new AptlyException("Repository with such name doesn’t exist");
        }

        if (response.body() == null) {
            throw new AptlyException(response.message());
        }
        return jacksonMapper.readValue(response.body().charStream(), LocalRepository.class);
    }

}
