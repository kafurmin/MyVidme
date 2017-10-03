package com.example.kif.myvidme.screen.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kif.myvidme.R;

/**
 * Created by Kif on 01.10.2017.
 */
public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public RecyclerViewAdapter recyclerViewAdapter;
    public CardView cardView;

    public TextView videoName;
    public TextView videoLikeCount;

    public ImageView thumbview;

    public ViewHolder(RecyclerViewAdapter recyclerViewAdapter, View itemView) {
        super(itemView);
        this.recyclerViewAdapter = recyclerViewAdapter;
        itemView.setClickable(true);
        itemView.setOnClickListener(this);
        thumbview = itemView.findViewById(R.id.thumb_view);
        cardView = itemView.findViewById(R.id.card_view);
        videoName = itemView.findViewById(R.id.textViewTitle);
        videoLikeCount = itemView.findViewById(R.id.like_textview);
    }

    @Override
    public void onClick(View v) {
        recyclerViewAdapter.mItemClickListener.onItemClick(v, getAdapterPosition());
    }
}
