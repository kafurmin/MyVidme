package com.example.kif.myvidme.screen.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kif.myvidme.BuildConfig;
import com.example.kif.myvidme.R;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewFragment extends Fragment {
    public RecyclerViewAdapter newVideosAdapter;
    public List<Video> videos;
    public SwipeRefreshLayout refreshNew;
    public RecyclerView newVideosList;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new, container, false);
        newVideosList = (RecyclerView) rootView.findViewById(R.id.new_List);
       newVideosList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        newVideosList.setLayoutManager(llm);

        refreshNew = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh_new);
        refreshNew.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                newVideosAdapter.notifyDataSetChanged();
                refreshNew.setRefreshing(false);


            }
        });

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
        Call<Videos> call = videoApi.getNewVideo();
        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, final Response<Videos> response) {
                videos = response.body().videos;
                newVideosAdapter = new RecyclerViewAdapter(videos);
                newVideosAdapter.SetOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String video_url = response.body().videos.get(position).getFullUrl();
                        Intent intent = new Intent(getActivity(),PlayerActivity.class);
                        intent.putExtra("video_url",video_url);
                        startActivity(intent);
                    }
                });

                newVideosList.setAdapter(newVideosAdapter);
            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {

            }


        });
    }


}