package com.example.kif.myvidme.screen.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kif.myvidme.BuildConfig;
import com.example.kif.myvidme.InternetConnectivityUtil;
import com.example.kif.myvidme.R;
import com.example.kif.myvidme.api.ApiClient;
import com.example.kif.myvidme.api.VidmeApi;
import com.example.kif.myvidme.model.Video;
import com.example.kif.myvidme.model.Videos;
import com.example.kif.myvidme.screen.adapter.RecyclerViewAdapter;
import com.example.kif.myvidme.screen.activity.PlayerActivity;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeaturedFragment extends Fragment {
    public RecyclerViewAdapter featuredVideosAdapter;
    public List<Video> videos;
    public RecyclerView featuredVideoList;
    public SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_featured, container, false);
        featuredVideoList = (RecyclerView) rootView.findViewById(R.id.cardList);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
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
                featuredVideosAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);


            }
        });
        featuredVideoList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        featuredVideoList.setLayoutManager(llm);

        if (!InternetConnectivityUtil.isConnected(getContext())){
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        } else {
            try {
                getVideos();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void getVideos() throws IOException {
        VidmeApi apiService =
                ApiClient.getClient().create(VidmeApi.class);
        Call<Videos> call = apiService.getFeaturedVideo();
        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, final Response<Videos> response) {
                videos = response.body().videos;
                featuredVideosAdapter = new RecyclerViewAdapter(videos);
                featuredVideosAdapter.SetOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String video_url = response.body().videos.get(position).getFullUrl();
                        Intent intent = new Intent(getActivity(),PlayerActivity.class);
                        intent.putExtra("video_url",video_url);
                        startActivity(intent);
                    }
                });

                featuredVideoList.setAdapter(featuredVideosAdapter);
            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {

            }
        });
    }
}
