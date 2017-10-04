package com.example.kif.myvidme.screen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kif.myvidme.R;

public class RootFragment extends Fragment {

    public String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_root, container, false);

        if(token!=null) {
            FeedFragment feedFragment = FeedFragment.newInstance(token);
            feedFragment.setTargetFragment(RootFragment.this, 1001);
            getFragmentManager().beginTransaction().replace(R.id.root_frame, feedFragment).commit();
        }else{
            SignInFragment signInFragment = new SignInFragment();
            signInFragment.setTargetFragment(RootFragment.this, 1001);
            getFragmentManager().beginTransaction().replace(R.id.root_frame, signInFragment).commit();

        }

		return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
          token = data.getStringExtra("token");

    }
}