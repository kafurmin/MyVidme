package com.example.kif.myvidme.screen.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kif.myvidme.screen.InternetConnectivityUtil;
import com.example.kif.myvidme.R;
import com.example.kif.myvidme.screen.adapter.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    public SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static ImageButton imageButton;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        imageButton = (ImageButton) findViewById(R.id.log_out_button);
        imageButton.setVisibility(View.GONE);//App.user == null ? View.GONE : View.VISIBLE);


        ImageButton imageButton = (ImageButton) findViewById(R.id.log_out_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (!InternetConnectivityUtil.isConnected(this))
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

    }
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu_logout);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //App.user = null;
                        mSectionsPagerAdapter.notifyDataSetChanged();
                        imageButton.setVisibility(View.GONE);
                        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();
                        return true;
                    }
                });
        popupMenu.show();
    }
}
