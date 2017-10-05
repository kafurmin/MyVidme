package com.example.kif.myvidme.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kif.myvidme.ui.activity.MainActivity;
import com.example.kif.myvidme.ui.fragment.FeaturedFragment;
import com.example.kif.myvidme.ui.fragment.FeedFragment;
import com.example.kif.myvidme.ui.fragment.NewFragment;
import com.example.kif.myvidme.ui.fragment.SignInFragment;


public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fragmentManager;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "FEATURED";
            case 1:
                return "NEW";
            case 2:
                return "FEED";
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FeaturedFragment();
            case 1:
                return new NewFragment();
            case 2:
                return new SignInFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}