package com.example.kif.myvidme.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kif.myvidme.BuildConfig;
import com.example.kif.myvidme.ui.adapter.EndlessRecyclerViewScrollListener;
import com.example.kif.myvidme.ui.Utils.InternetConnectivityUtil;
import com.example.kif.myvidme.R;
import com.example.kif.myvidme.api.ApiClient;
import com.example.kif.myvidme.api.VidmeApi;
import com.example.kif.myvidme.model.Video;
import com.example.kif.myvidme.model.Videos;
import com.example.kif.myvidme.ui.adapter.RecyclerViewAdapter;
import com.example.kif.myvidme.ui.activity.PlayerActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewFragment extends Fragment  {
    public RecyclerViewAdapter recyclerViewAdapter;
    public List<Video> videos;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout swipeRefreshLayout;
    public int offset;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vidme, container, false);
        recyclerView = rootView.findViewById(R.id.cardList);
        swipeRefreshLayout = rootView.findViewById(R.id.root_featured);
        videos = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(),videos);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setOnScrollListener(new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getVideos();
            }
        });

        if (!InternetConnectivityUtil.isConnected(getContext())){
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        } else {  getVideos();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!InternetConnectivityUtil.isConnected(getContext())){
                    Toast.makeText(getContext(), "Please, check your internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    videos.clear();
                    getVideos();
                    recyclerView.getAdapter().notifyDataSetChanged();
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }

    private void getVideos() {
        ApiClient.getClient().getNewVideo(offset, BuildConfig.VISIBLE_ITEMS).enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, final Response<Videos> response) {
                videos.addAll(response.body().getVideos());
                recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
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
}
