package com.example.kif.myvidme.api;

import com.google.gson.annotations.SerializedName;


public class Response {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("auth")
    private Auth auth;


    public Boolean getStatus() {
        return status;
    }


    public Auth getAuth() {
        return auth;
    }

}