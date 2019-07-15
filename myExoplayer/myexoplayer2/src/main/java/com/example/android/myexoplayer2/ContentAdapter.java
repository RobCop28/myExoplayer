package com.example.android.myexoplayer2;


import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ContentStructure> videoItems;
    private RequestManager requestManager;

    public ContentAdapter(ArrayList<ContentStructure> list,RequestManager manager){
        videoItems=list;
        requestManager=manager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ItemViewHolder)viewHolder).onBind(videoItems.get(i),requestManager);
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }
}
