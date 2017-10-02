package com.example.kif.myvidme.screen.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kif.myvidme.screen.fragment.FeaturedFragment;
import com.example.kif.myvidme.screen.fragment.SignInFragment;
import com.example.kif.myvidme.screen.fragment.NewFragment;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
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
}
