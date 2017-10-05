package com.example.kif.myvidme.api;


import com.example.kif.myvidme.model.Response;
import com.example.kif.myvidme.model.Videos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;



public interface VidmeApi {

    @Headers("Authorization: Basic ")
    @GET("videos/featured")
    Call<Videos> getFeaturedVideo(@Header("key") byte[] key, @Query("limit") int limit, @Query("offset") int offset);

    @GET("videos/new")
    Call<Videos> getNewVideo(@Query("limit") int limit, @Query("offset") int offset);

    @GET("videos/feed")
    Call<Videos> getFeedVideo(@Query("limit") int limit, @Query("offset") int offset, @Query("token") String token);

    @POST("auth/create")
    Call<Response> authCreate(@Query("username") String username, @Query("password") String password);


}