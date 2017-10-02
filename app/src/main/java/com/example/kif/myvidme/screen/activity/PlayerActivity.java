package com.example.kif.myvidme.screen.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.example.kif.myvidme.R;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by Kif on 02.10.2017.
 */

public class PlayerActivity extends AppCompatActivity {
    private String videoPath = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";

    SimpleExoPlayerView videoView;

    SimpleExoPlayer player;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);

        videoView = (SimpleExoPlayerView) findViewById(R.id.videoView);
        Intent intent = getIntent();

        videoPath = intent.getStringExtra("video_url");

        playVideo();
    }

    void playVideo() {
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        player =
                ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        videoView.setPlayer(player);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "MyVidme"));
        HlsDataSourceFactory hlsDataSourceFactory = new DefaultHlsDataSourceFactory(dataSourceFactory);
        Handler handler = new Handler();
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new HlsMediaSource(Uri.parse(videoPath), hlsDataSourceFactory
                , 1, null, null);
        player.prepare(videoSource);
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.stop();
    }
}