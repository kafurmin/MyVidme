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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}