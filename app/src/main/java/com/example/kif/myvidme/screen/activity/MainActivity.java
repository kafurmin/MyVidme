package com.example.kif.myvidme.screen.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kif.myvidme.model.Auth;
import com.example.kif.myvidme.screen.InternetConnectivityUtil;
import com.example.kif.myvidme.R;
import com.example.kif.myvidme.screen.adapter.SectionsPagerAdapter;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    public SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ImageButton imageButton;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);

        SharedPreferences sharedPreferences = getSharedPreferences("Pref", MODE_PRIVATE);
        String user = sharedPreferences.getString("username",null);

        imageButton = (ImageButton) findViewById(R.id.log_out_button);
        imageButton.setVisibility(user == null ? View.GONE : View.VISIBLE);
        if (!InternetConnectivityUtil.isConnected(this))
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu_logout);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mSectionsPagerAdapter.notifyDataSetChanged();
                imageButton.setVisibility(View.GONE);
                SharedPreferences sharedPreferences = getSharedPreferences("Pref", MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                return true;
            }
        });
        popupMenu.show();
    }
}
