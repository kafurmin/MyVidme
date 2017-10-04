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

        SignInFragment signInFragment = new SignInFragment();

        if(token == null) {
            signInFragment.setTargetFragment(RootFragment.this, 1001);
        }

        getFragmentManager().beginTransaction().replace(R.id.root_frame, (token == null? signInFragment: FeedFragment.newInstance(token))).commit();

		return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(token!=null) {

        }
        else{
            token = data.getStringExtra("token");
        }
    }
}