package com.example.kif.myvidme.api;

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
    private String userId;

    public String getToken() {
        return token;
    }

}
