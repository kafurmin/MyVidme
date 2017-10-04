package com.example.kif.myvidme.screen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kif.myvidme.BuildConfig;
import com.example.kif.myvidme.screen.EndlessRecyclerViewScrollListener;
import com.example.kif.myvidme.screen.InternetConnectivityUtil;
import com.example.kif.myvidme.R;
import com.example.kif.myvidme.api.ApiClient;
import com.example.kif.myvidme.api.VidmeApi;
import com.example.kif.myvidme.model.Video;
import com.example.kif.myvidme.model.Videos;
import com.example.kif.myvidme.screen.adapter.RecyclerViewAdapter;
import com.example.kif.myvidme.screen.activity.PlayerActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class FeedFragment extends Fragment  {
    public RecyclerViewAdapter recyclerViewAdapter;
    public List<Video> videos;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout swipeRefreshLayout;
    public int offset;
    public String token;

    public static FeedFragment newInstance(String mParam1) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString("token", mParam1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString("token");

            Intent i = new Intent().putExtra("token", token);

            Fragment targetFragment = getTargetFragment();
            if (targetFragment != null) {
                targetFragment.onActivityResult(1001, 1, i);
            }
        }
    }

    public FeedFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_featured, container, false);
        recyclerView = rootView.findViewById(R.id.cardList);
        swipeRefreshLayout = rootView.findViewById(R.id.root_featured);
        videos = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(videos);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setOnScrollListener(new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                try {
                    getVideos();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        if (!InternetConnectivityUtil.isConnected(getContext())){
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        } else {
            try {
                getVideos();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    if (!InternetConnectivityUtil.isConnected(getContext())){
                        Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    } else {
                        videos.clear();
                        getVideos();
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }

    private void getVideos() throws IOException {
        VidmeApi vidmeApi = ApiClient.getClient().create(VidmeApi.class);
        Call<Videos> call = vidmeApi.getFeedVideo(0, BuildConfig.VISIBLE_ITEMS,token);
        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, final Response<Videos> response) {
                videos.addAll(response.body().getVideos());
                recyclerViewAdapter.SetOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String video_url = response.body().videos.get(position).getFullUrl();
                        Intent intent = new Intent(getActivity(),PlayerActivity.class);
                        intent.putExtra("video_url",video_url);
                        startActivity(intent);
                    }
                });

                recyclerViewAdapter.update(videos);
                offset += BuildConfig.VISIBLE_ITEMS;

            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_logout,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.LogOut_button:

                token = null;

                Intent i = new Intent().putExtra("token", token);

                Fragment targetFragment = getTargetFragment();
                if (targetFragment != null) {
                    targetFragment.onActivityResult(1001, 1, i);
                }

                getFragmentManager().beginTransaction().replace(R.id.root_feed, new SignInFragment()).commitNowAllowingStateLoss();

                item.setVisible(false);

                break;

        }
        return true;

    }
}

/*
package com.example.kif.myvidme.screen.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.transition.Visibility;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kif.myvidme.BuildConfig;
import com.example.kif.myvidme.R;
import com.example.kif.myvidme.api.VidmeApi;
import com.example.kif.myvidme.model.Video;
import com.example.kif.myvidme.model.Videos;
import com.example.kif.myvidme.screen.adapter.RecyclerViewAdapter;
import com.example.kif.myvidme.screen.activity.PlayerActivity;
import com.example.kif.myvidme.screen.adapter.SectionsPagerAdapter;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FeedFragment extends Fragment {
    public RecyclerViewAdapter userVideosAdapter;
    public List<Video> videos;
    public RecyclerView userVideosList;
    public SwipeRefreshLayout swipeRefreshLayout;
    public String token;

    public static FeedFragment newInstance(String mParam1) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString("token", mParam1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString("token");
            }
    }

    public FeedFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        userVideosList = rootView.findViewById(R.id.user_videos_list);
        setHasOptionsMenu(true);
        swipeRefreshLayout = rootView.findViewById(R.id.user_videos_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    refreshItems();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            public void refreshItems() throws IOException {
                getVideos();
                OnItemLoadComplete();
            }
            public void OnItemLoadComplete(){
                userVideosAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);


            }
        });
        userVideosList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        userVideosList.setLayoutManager(llm);


        try {
            getVideos();
        } catch (IOException e) {
            e.printStackTrace();

        }

        return rootView;
    }

    private void getVideos() throws IOException {


        Retrofit retrofitAdapter = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_ENDPOINT)
                .build();
        final VidmeApi videoApi = retrofitAdapter.create(VidmeApi.class);
        //SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        //token = sharedPreferences.getString("token",null);
        Log.d("token in user videos",token);
        Call<Videos> call = videoApi.getFeedVideo(0,10,token);
        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, final Response<Videos> response) {


                videos = response.body().videos;
                userVideosAdapter = new RecyclerViewAdapter(videos);
                userVideosAdapter.SetOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String video_url = response.body().videos.get(position).getFullUrl();
                        Intent intent = new Intent(getActivity(),PlayerActivity.class);
                        intent.putExtra("video_url",video_url);
                        startActivity(intent);
                    }
                });

                userVideosList.setAdapter(userVideosAdapter);
            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    inflater.inflate(R.menu.menu_logout,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.LogOut_button:
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.clear();
                edit.apply();

                getFragmentManager().popBackStackImmediate();

                //.notifyDataSetChanged();*/
/*
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();

                trans.detach(this);
                trans.commitNow();

                item.setVisible(false);


                break;

        }
        return true;

    }
}

*/
