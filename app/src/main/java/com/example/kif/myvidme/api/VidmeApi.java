package com.example.kif.myvidme.api;


import com.example.kif.myvidme.model.Response;
import com.example.kif.myvidme.model.Videos;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


public interface VidmeApi {

    @GET("videos/featured")
    Call<Videos> getFeaturedVideo(@Query("limit") int limit, @Query("offset") int offset);

    @GET("videos/new")
    Call<Videos> getNewVideo(@Query("limit") int limit, @Query("offset") int offset);

    @GET("videos/feed")
    Call<Videos> getFeedVideo(@Query("limit") int limit, @Query("offset") int offset, @Query("token") String token);

    @POST("auth/create")
    Call<Response> authCreate(@Query("username") String username, @Query("password") String password);


}