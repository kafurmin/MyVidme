package com.example.kif.myvidme.screen.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kif.myvidme.screen.fragment.OnItemClickListener;
import com.example.kif.myvidme.R;
import com.example.kif.myvidme.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    public List<Video> call;
    public OnItemClickListener mItemClickListener;
    public RecyclerViewAdapter(List<Video> call){
        this.call = call;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new ViewHolder(this, v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uri image_uri = Uri.parse(call.get(position).getThumbnailUrl());
        Context context = holder.thumbview.getContext();
        Picasso.with(context).load(image_uri).into(holder.thumbview);
        holder.videoName.setText(call.get(position).getTitle());
        holder.videoLikeCount.setText("Likes :"+String.valueOf(call.get(position).getLikesCount()));
    }

    @Override
    public int getItemCount() {
        return call.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;


    }
}
