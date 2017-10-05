package com.example.kif.myvidme.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kif.myvidme.R;
import com.example.kif.myvidme.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

    public List<Video> list;
    public OnItemClickListener mItemClickListener;

    private LayoutInflater mInflater;
    private Context context;


    public RecyclerViewAdapter(android.content.Context context, List<Video> videoList){
        mInflater = LayoutInflater.from(context);
        this.context = context;
        list = new ArrayList<>(videoList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.card_view, null);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Video list_pos = list.get(position);
        Uri image_uri = Uri.parse(list_pos.getThumbnailUrl());

        Picasso.with(context).load(image_uri).into(holder.thumbview);

        holder.videoName.setText(list_pos.getTitle());
        holder.videoLikeCount.setText("Likes: "+ String.valueOf(list_pos.getLikesCount()));

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void update(List<Video> list){
        this.list = list;
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
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

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }
}
/*
package com.example.kif.myvidme.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kif.myvidme.R;
import com.example.kif.myvidme.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

    public List<Video> list;
    private LayoutInflater mInflater;
    public AdapterView.OnItemClickListener mItemClickListener;

    private Context context;

    public RecyclerViewAdapter(android.content.Context context, List<Video> videoList){
        mInflater = LayoutInflater.from(context);
        this.context = context;
        list = new ArrayList(videoList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.card_view, null);

        ViewHolder viewHolder = new ViewHolder(v, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Video list_pos = list.get(position);
        Uri image_uri = Uri.parse(list_pos.getThumbnailUrl());

        Picasso.with(context).load(image_uri).into(holder.thumbview);

        holder.videoName.setText(list_pos.getTitle());
        holder.videoLikeCount.setText(String.format("%s %s", R.string.likes, list_pos.getLikesCount().toString()));

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void update(List<Video> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
*/
