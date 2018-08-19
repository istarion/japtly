package ru.s_zg.japtly;

import okhttp3.OkHttpClient;
import ru.s_zg.japtly.api.FileUploadApi;
import ru.s_zg.japtly.api.LocalReposApi;
import ru.s_zg.japtly.api.MiscApi;

import java.net.URL;

public class Japtly {
    public static final OkHttpClient okHttpClient = new OkHttpClient();

    public final MiscApi miscApi;
    public final LocalReposApi localReposApi;
    public final FileUploadApi fileUploadApi;

    public Japtly(URL aptlyEndpoint) {
        miscApi = new MiscApi(aptlyEndpoint);
        localReposApi = new LocalReposApi(aptlyEndpoint);
        fileUploadApi = new FileUploadApi(aptlyEndpoint);
    }
}
