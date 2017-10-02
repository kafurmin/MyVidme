package com.example.kif.myvidme.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Videos {
    @SerializedName("videos")
    public List<Video> videos;

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
