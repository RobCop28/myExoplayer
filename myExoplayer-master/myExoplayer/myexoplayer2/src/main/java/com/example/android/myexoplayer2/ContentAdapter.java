package com.example.android.myexoplayer2;


import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.offline.StreamKey;

import java.util.ArrayList;
import java.util.Collections;

public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ContentStructure> videoItems;
    private RequestManager requestManager;
    private Context context;
    private RelativeLayout layout;

    public ContentAdapter(ArrayList<ContentStructure> list,RequestManager manager){
        videoItems=list;
        requestManager=manager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_item,viewGroup,false));

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        final int position=i;
        ((ItemViewHolder)viewHolder).onBind(videoItems.get(i),requestManager);
        ((ItemViewHolder) viewHolder).share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout=((ItemViewHolder) viewHolder).layout;
                layout.setVisibility(View.VISIBLE);

                downloadResponse response=new downloadResponse(videoItems.get(position).getVideoUrl(),context,videoItems.get(position).getId());
                response.initRequest(layout);
               // Uri fileUri=response.getFileUri();


            }
        });
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }
}
