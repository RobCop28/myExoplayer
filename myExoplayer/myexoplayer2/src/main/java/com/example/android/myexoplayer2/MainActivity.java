package com.example.android.myexoplayer2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.net.InetAddress;
import java.util.ArrayList;
import com.google.android.material.snackbar.Snackbar;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity {
    protected SimpleExoPlayer player;
    protected ArrayList<ContentStructure> ContentList;
    protected ContentRecyclerView mRecyclerView;
    protected ContentAdapter mAdapter;

    protected boolean firstTime = true;
    private ImageView place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        place=(ImageView) findViewById(R.id.NoInternetImage);
        if( isNetworkConnected()){
          loadData(true);

        }
        else{
          showSnackbar();

        }
    }
    public void showSnackbar(){
        place.setVisibility(View.VISIBLE);
        Snackbar snackbar=Snackbar.make(findViewById(R.id.parentOfRecyclerView),R.string.snackbar_text, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.snackbar_action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(isNetworkConnected());
            }
        });
        snackbar.show();
    }


    public void loadData(Boolean isNetConnected){
        if(isNetConnected){
        place.setVisibility(View.GONE);
        ContentList=new ArrayList<>();
        initView();
        prepareVideoList();
        mRecyclerView.setList(ContentList);
        mAdapter = new ContentAdapter(ContentList, initGlide());
        mRecyclerView.setAdapter(mAdapter);
        if (firstTime) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.playVideo(false);
                }
            });
            firstTime = false;
        }}
        else{showSnackbar();}
    }




    protected boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }





    protected void initView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    protected RequestManager initGlide() {
        RequestOptions options = new RequestOptions();

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    protected void onDestroy() {
        if (mRecyclerView != null) {
            mRecyclerView.releasePlayer();
        }
        super.onDestroy();
    }

    protected void prepareVideoList() {
        ContentStructure content = new ContentStructure();
        content.setId(1);
        content.setPhotoUrl(
                "https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-1.png");
        content.setVideoUrl("https://androidwave.com/media/androidwave-video-1.mp4");

        ContentStructure content2 = new ContentStructure();
        content2.setId(2);
        content2.setPhotoUrl(
                "https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-2.png");
        content2.setVideoUrl("https://androidwave.com/media/androidwave-video-2.mp4");

        ContentStructure content3=new ContentStructure();
        content3.setId(3);
        content3.setPhotoUrl(
                "https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-2.png");
        content3.setVideoUrl("https://androidwave.com/media/androidwave-video-2.mp4");



        ContentList.add(content);
        ContentList.add(content2);
        ContentList.add(content3);
        }

}
