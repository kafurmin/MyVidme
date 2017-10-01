package com.example.kif.myvidme.screen.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kif.myvidme.R;
import com.example.kif.myvidme.screen.fragment.FeedFragment;

public class UserVideos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_videos);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FeedFragment fragment_user_videos = new FeedFragment();
        fragmentTransaction.add(R.id.user_videos_container,fragment_user_videos);
        fragmentTransaction.commit();
        }

}
