package com.example.kif.myvidme.api;



import com.example.kif.myvidme.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kif on 30.09.2017.
 */

public class ApiClient {

    private static VidmeApi retrofit;

    public static VidmeApi getClient() {
        if (retrofit==null) {
           Retrofit retrofit_build = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofit = retrofit_build.create(VidmeApi.class);
        }
        return retrofit;
    }
}
