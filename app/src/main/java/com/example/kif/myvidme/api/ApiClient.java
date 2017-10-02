package com.example.kif.myvidme.api;


import android.support.annotation.NonNull;

import com.example.kif.myvidme.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kif on 30.09.2017.
 */

public class ApiClient {

    private static Retrofit retrofit = null;

    private static OkHttpClient sClient;

    private ApiClient() {
    }

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_ENDPOINT)
                  //  .client(getApiClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    @NonNull
    private static OkHttpClient getApiClient() {
        OkHttpClient client = sClient;
        if (client == null) {
            synchronized (ApiClient.class) {
                client = sClient;
                if (client == null) {
                    client = sClient = buildApiClient();
                }
            }
        }
        return client;
    }

    @NonNull
    private static OkHttpClient buildApiClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor.create())
                .build();
    }
}
