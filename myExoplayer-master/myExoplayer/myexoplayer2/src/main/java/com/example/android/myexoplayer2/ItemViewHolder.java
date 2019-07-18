package com.example.android.myexoplayer2;


import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

public class ItemViewHolder extends RecyclerView.ViewHolder {
   public FrameLayout frame;
   public ImageView Image;
   public ProgressBar bar;
   public RequestManager requestManager;
   private View parent;
   public ImageButton share;
   public RelativeLayout layout;


    public ItemViewHolder(@NonNull View itemView){
       super(itemView);
       parent=itemView;
       frame=itemView.findViewById(R.id.VideoFrame);
       Image=itemView.findViewById(R.id.CoverImage);
       bar=itemView.findViewById(R.id.progressBar);
       share=itemView.findViewById(R.id.shareButton);
       layout=itemView.findViewById(R.id.progressLayout);

    }

    void onBind(ContentStructure contentStructure,RequestManager manager){
       this.requestManager=manager;
       parent.setTag(this);
       this.requestManager.load(contentStructure.getPhotoUrl()).into(Image);
    }

}
