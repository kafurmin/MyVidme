package com.example.kif.myvidme.screen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
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


public class FeaturedFragment extends Fragment  {
    public RecyclerViewAdapter recyclerViewAdapter;
    public List<Video> videos;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout swipeRefreshLayout;
    public int offset;

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
        Call<Videos> call = vidmeApi.getFeaturedVideo(offset, BuildConfig.VISIBLE_ITEMS);
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
                //Toast.makeText(getContext(), "Oooops, smth goes wrong, try again laiter", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.LogOut_button:

                break;

        }
        return true;

    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // This does work
        MenuItem someMenuItem = menu.findItem(R.id.log_out_button);
        someMenuItem.setVisible(false);
    }
}
