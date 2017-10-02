package com.example.kif.myvidme.model;

import com.example.kif.myvidme.model.Auth;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Response {

    @SerializedName("status")
    private Boolean status;

    @SerializedName("auth")
    private Auth auth;


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

}