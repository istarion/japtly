package ru.s_zg.japtly;

import okhttp3.OkHttpClient;
import ru.s_zg.japtly.api.MiscApi;

import java.net.URL;

public class Japtly {
    private final URL aptlyEndpoint;
    public static final OkHttpClient okHttpClient = new OkHttpClient();

    public final MiscApi miscApi;

    public Japtly(URL aptlyEndpoint) {
        this.aptlyEndpoint = aptlyEndpoint;
        miscApi = new MiscApi(aptlyEndpoint);
    }
}
