package com.example.kif.myvidme.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kif on 01.10.2017.
 */
public class Auth {

    @SerializedName("token")
    private String token;

    @SerializedName("expires")
    private String expires;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("user")
    private String user;


    public String getToken() {
        return token;
    }

}