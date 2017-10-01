package com.example.kif.myvidme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {


    @SerializedName("complete_url")
    @Expose
    private String complete_url;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnail_url;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("likes_count")
    @Expose
    private Integer likes_count;

    public String getComplete_url() {

        return complete_url;
    }

    public String getTitle() {

        return title;
    }

    public String getThumbnail_url() {

        return thumbnail_url;
    }

    public Integer getLikes_count() {

        return likes_count;
    }




}
