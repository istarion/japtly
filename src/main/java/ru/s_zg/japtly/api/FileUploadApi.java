package ru.s_zg.japtly.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import ru.s_zg.japtly.Japtly;
import ru.s_zg.japtly.exceptions.AptlyException;
import ru.s_zg.japtly.exceptions.NotExistsException;

import java.io.*;
import java.net.URL;
import java.util.List;

public class FileUploadApi {
    private static final String ENDPOINT_FILES = "api/files";
    private final ObjectMapper jacksonMapper = new ObjectMapper();
    private final URL aptlyEndpoint;

    public FileUploadApi(URL aptlyEndpoint) {
        this.aptlyEndpoint = aptlyEndpoint;
    }

    public List<String> listDirectories() throws IOException {
        Request request = new Request.Builder()
                .url(new URL(aptlyEndpoint, ENDPOINT_FILES))
                .build();

        Response response = Japtly.okHttpClient.newCall(request).execute();

        if (response.body() == null) {
            throw new AptlyException(response.message());
        }
        return jacksonMapper.readValue(response.body().charStream(), new TypeReference<List<String>>() {});
    }

    public List<String> listFiles(String dir) throws IOException {
        Request request = new Request.Builder()
                .url(new URL(aptlyEndpoint, ENDPOINT_FILES + "/" + dir))
                .build();

        Response response = Japtly.okHttpClient.newCall(request).execute();
        if (response.code() == 404) {
            throw new NotExistsException("Directory doesn't exist");
        }

        if (response.body() == null) {
            throw new AptlyException(response.message());
        }
        return jacksonMapper.readValue(response.body().charStream(), new TypeReference<List<String>>() {});
    }

    public List<String> uploadFile(String dir, File file) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), file)
                ).build();

        Request request = new Request.Builder()
                .url(new URL(aptlyEndpoint, ENDPOINT_FILES + "/" + dir))
                .post(requestBody)
                .build();

        Response response = Japtly.okHttpClient.newCall(request).execute();
        if (response.body() == null) {
            throw new AptlyException(response.message());
        }
        return jacksonMapper.readValue(response.body().charStream(), new TypeReference<List<String>>() {});
    }

    public void deleteDirectory(String dir) throws IOException {
        Request request = new Request.Builder()
                .url(new URL(aptlyEndpoint, ENDPOINT_FILES + "/" + dir))
                .delete()
                .build();

        Response response = Japtly.okHttpClient.newCall(request).execute();

        if (response.code() != 200) {
            throw new AptlyException(response.message());
        }
    }

    public void deleteFileInDirectory(String dir, String fileName) throws IOException {
        Request request = new Request.Builder()
                .url(new URL(aptlyEndpoint, ENDPOINT_FILES + "/" + dir + "/" + fileName))
                .delete()
                .build();

        Response response = Japtly.okHttpClient.newCall(request).execute();

        if (response.code() != 200) {
            throw new AptlyException(response.message());
        }
    }
}
