package com.example.android.myexoplayer2;


import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.HashMap;

public class ItemViewHolder extends RecyclerView.ViewHolder {
   public FrameLayout frame;
   public ImageView Image;
   public ProgressBar bar;
   public RequestManager requestManager;
   private View parent;
   public ImageButton share;
   public RelativeLayout layout;
   public int type;



    public ItemViewHolder(@NonNull View itemView,int type){
       super(itemView);
       parent=itemView;
       frame=itemView.findViewById(R.id.VideoFrame);
       Image=itemView.findViewById(R.id.CoverImage);
       bar=itemView.findViewById(R.id.progressBar);
       share=itemView.findViewById(R.id.shareButton);
       layout=itemView.findViewById(R.id.progressLayout);
       this.type=type;


    }

    void onBind(ContentStructure contentStructure,RequestManager manager){
       parent.setTag(this);
       if(type==0){
       this.requestManager=manager;
       this.requestManager.load(contentStructure.getPhotoUrl()).into(Image);}

       if(type==1){
          try{
          Image.setImageBitmap(retriveVideoFrameFromVideo(contentStructure.getVideoUrl()));}
          catch (Throwable th){

          }
       }

    }

   public static Bitmap retriveVideoFrameFromVideo(String videoPath)throws Throwable
   {
      Bitmap bitmap = null;
      MediaMetadataRetriever mediaMetadataRetriever = null;
      try
      {
         mediaMetadataRetriever = new MediaMetadataRetriever();
         if (Build.VERSION.SDK_INT >= 14)
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
         else
            mediaMetadataRetriever.setDataSource(videoPath);
         //   mediaMetadataRetriever.setDataSource(videoPath);
         bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)"+ e.getMessage());
      }
      finally
      {
         if (mediaMetadataRetriever != null)
         {
            mediaMetadataRetriever.release();
         }
      }
      return bitmap;
   }

}
