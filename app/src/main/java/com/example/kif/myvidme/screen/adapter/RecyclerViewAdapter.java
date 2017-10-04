package com.example.kif.myvidme.screen.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kif.myvidme.screen.fragment.OnItemClickListener;
import com.example.kif.myvidme.R;
import com.example.kif.myvidme.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

    public List<Video> list;
    public OnItemClickListener mItemClickListener;

    public RecyclerViewAdapter(List<Video> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Uri image_uri = Uri.parse(list.get(position).getThumbnailUrl());
        Context context = holder.thumbview.getContext();
        Picasso.with(context).load(image_uri).into(holder.thumbview);
        holder.videoName.setText(list.get(position).getTitle());
        holder.videoLikeCount.setText("Likes :"+String.valueOf(list.get(position).getLikesCount()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<Video> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView thumbview;
        public TextView videoName;
        public TextView videoLikeCount;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            thumbview = itemView.findViewById(R.id.thumb_view);
            videoName = itemView.findViewById(R.id.video_name);
            videoLikeCount = itemView.findViewById(R.id.video_likes);
        }

        @Override
        public void onClick(View view) {
            mItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
