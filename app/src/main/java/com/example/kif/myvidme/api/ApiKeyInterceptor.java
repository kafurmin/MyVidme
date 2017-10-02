package com.example.kif.myvidme.api;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import com.example.kif.myvidme.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public final class ApiKeyInterceptor implements Interceptor {

    private String authStringEnc = null;

    private ApiKeyInterceptor() {
        String mApiKey = BuildConfig.API_KEY;
        byte[] authEncBytes = Base64.decode(mApiKey.getBytes(), Base64.DEFAULT);
        authStringEnc = new String(authEncBytes);
    }

    @NonNull
    public static Interceptor create() {
        return new ApiKeyInterceptor();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (TextUtils.isEmpty(authStringEnc)) {
            return chain.proceed(chain.request());
        }
        Request request = chain.request().newBuilder()
                .addHeader("Authorization: Basic " , authStringEnc)
                .build();
        return chain.proceed(request);
    }
}
