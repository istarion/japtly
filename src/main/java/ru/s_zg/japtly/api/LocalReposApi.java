package ru.s_zg.japtly.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import ru.s_zg.japtly.Japtly;
import ru.s_zg.japtly.exceptions.AptlyException;
import ru.s_zg.japtly.exceptions.NotExistsException;
import ru.s_zg.japtly.model.AddPackagesResponse;
import ru.s_zg.japtly.model.DebPackage;
import ru.s_zg.japtly.model.LocalRepository;
import ru.s_zg.japtly.model.SearchQuery;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

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

    public List<DebPackage> search(String repositoryName, SearchQuery searchQuery) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.get(aptlyEndpoint + ENDPOINT_REPOS).newBuilder();

        urlBuilder.addPathSegment(repositoryName).addPathSegment("packages");

        Map<String, String> query = jacksonMapper.convertValue(searchQuery, new TypeReference<Map<String, String>>() {
        });
        query.forEach(urlBuilder::addQueryParameter);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();
        Response response = Japtly.okHttpClient.newCall(request).execute();

        if (response.body() == null) {
            throw new AptlyException(response.message());
        }
        return jacksonMapper.readValue(response.body().charStream(), new TypeReference<List<DebPackage>>() {
        });
    }

    public LocalRepository editRepository(String repoName, LocalRepository newRepo) throws IOException {
        RequestBody body = RequestBody.create(JSON, jacksonMapper.writeValueAsString(newRepo));
        Request request = new Request.Builder()
                .url(new URL(aptlyEndpoint, ENDPOINT_REPOS + "/" + repoName))
                .put(body)
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

    public List<LocalRepository> list() throws IOException {
        Request request = new Request.Builder()
                .url(new URL(aptlyEndpoint, ENDPOINT_REPOS))
                .build();

        Response response = Japtly.okHttpClient.newCall(request).execute();

        if (response.body() == null) {
            throw new AptlyException(response.message());
        }
        return jacksonMapper.readValue(response.body().charStream(), new TypeReference<List<LocalRepository>>() {
        });
    }

    public void delete(String name) throws IOException {
        delete(name, 0);
    }

    /**
     * Delete local repository
     *
     * @param name  - Repository name
     * @param force - When value is set to 1, delete local repository even if it has snapshots
     */
    public void delete(String name, int force) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.get(aptlyEndpoint + ENDPOINT_REPOS).newBuilder();
        urlBuilder.addPathSegment(name);
        urlBuilder.addQueryParameter("force", String.valueOf(force));

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .delete()
                .build();

        Response response = Japtly.okHttpClient.newCall(request).execute();

        if (response.code() == 404) {
            throw new AptlyException("Repository with such name doesn’t exist");
        }

        if (response.code() == 409) {
            throw new AptlyException("Repository can’t be dropped: " + response.message());
        }
    }

    public AddPackagesResponse addPackagesFromUpload(String repoName, String dir) throws IOException {
        return addPackagesFromUpload(repoName, dir, null, false, false);
    }

    public AddPackagesResponse addPackagesFromUpload(String repoName, String dir, String fileName) throws IOException {
        return addPackagesFromUpload(repoName, dir, fileName, false ,false);
    }

    public AddPackagesResponse addPackagesFromUpload(
            String repoName, String dir, String fileName, boolean noRemove, boolean forceReplace
    ) throws IOException {
        HttpUrl queryUrl = makeAddQueryUrl(repoName, dir, fileName, noRemove, forceReplace);

        Request request = new Request.Builder()
                .url(queryUrl)
                .post(RequestBody.create(MediaType.parse("application/json"), "{}"))
                .build();

        Response response = Japtly.okHttpClient.newCall(request).execute();

        if (response.code() == 404) {
            throw new NotExistsException("Repository with such name doesn’t exist");
        }

        if (response.body() == null) {
            throw new AptlyException(response.message());
        }
        return jacksonMapper.readValue(response.body().charStream(), AddPackagesResponse.class);
    }

    private HttpUrl makeAddQueryUrl(String repoName, String dir, String fileName, boolean noRemove, boolean forceReplace) {
        HttpUrl.Builder urlBuilder = HttpUrl.get(aptlyEndpoint + ENDPOINT_REPOS).newBuilder();
        urlBuilder.addPathSegment(repoName);
        urlBuilder.addPathSegment("file");
        urlBuilder.addPathSegment(dir);
        if (fileName != null) {
            urlBuilder.addPathSegment(fileName);
        }
        if (noRemove) {
            urlBuilder.addQueryParameter("noRemove", "1");
        }
        if (forceReplace) {
            urlBuilder.addQueryParameter("forceReplace", "1");
        }
        return urlBuilder.build();
    }
}
