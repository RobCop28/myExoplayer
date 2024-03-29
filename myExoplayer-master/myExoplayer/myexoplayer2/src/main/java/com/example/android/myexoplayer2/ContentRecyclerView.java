package com.example.android.myexoplayer2;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;

import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheUtil;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;



public class ContentRecyclerView extends RecyclerView {
    private static final String TAG = "ExoPlayerRecyclerView";
    private static final String AppName = "myExoplayer";
   private ImageView coverImage;
   private ProgressBar bar;
   private View ViewHolder;
   private FrameLayout contentContainer;
   private PlayerView videoSurfaceView;
   private SimpleExoPlayer player;

   private ArrayList<ContentStructure> list=new ArrayList<>();
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private Context context;
    private int playPosition = -1;
    private boolean isVideoViewAdded;
    private RequestManager requestManager;
    private int type;

    public ContentRecyclerView(@NonNull Context context){
        super(context);
        start(context);
    }

    public ContentRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        start(context);
    }

    private void start(Context context){

        this.context=context.getApplicationContext();
        Display display=((WindowManager) Objects.requireNonNull(getContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay();
        Point point=new Point();
         display.getSize(point);

         videoSurfaceDefaultHeight=point.x;
         screenDefaultHeight=point.y;
         videoSurfaceView=new PlayerView(this.context);
         videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);


        BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory=new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector=new DefaultTrackSelector(videoTrackSelectionFactory);

        player= ExoPlayerFactory.newSimpleInstance(context,trackSelector);

        videoSurfaceView.setUseController(false);
        videoSurfaceView.setPlayer(player);

        addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==recyclerView.SCROLL_STATE_IDLE){
                    if(coverImage !=null) {
                        coverImage.setVisibility(VISIBLE);
                    }
                    if(!recyclerView.canScrollVertically(1))
                        playVideo(true);
                    else
                       playVideo(false);

                }
            }

            @Override
            public void onScrolled(@NonNull  RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                if (ViewHolder != null && ViewHolder.equals(view)) {
                    resetVideoView();
                }
            }
        });

        player.addListener(new Player.EventListener(){
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
               switch(playbackState){
                   case Player.STATE_BUFFERING:
                       if(bar!=null){
                           coverImage.setVisibility(GONE);
                           bar.setVisibility(VISIBLE);
                       }
                       break;

                   case Player.STATE_ENDED:
                       player.seekTo(0);
                       break;

                   case Player.STATE_IDLE:
                       break;

                   case Player.STATE_READY:
                       if(bar!=null)
                           bar.setVisibility(GONE);


                       if(!isVideoViewAdded)
                           addVideoView();

                       break;
                   default:
                       break;


               }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }
        });

    }

    public void playVideo(boolean isEndOfList) {

        int targetPosition;

        if (!isEndOfList) {
            int startPosition = ((LinearLayoutManager) Objects.requireNonNull(
                    getLayoutManager())).findFirstVisibleItemPosition();
            int endPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

            // if there is more than 2 list-items on the screen, set the difference to be 1
            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1;
            }

            // something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return;
            }

            // if there is more than 1 list-item on the screen
            if (startPosition != endPosition) {
                int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
                int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);

                targetPosition =
                        startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
            } else {
                targetPosition = startPosition;
            }
        } else {
            targetPosition =list.size() - 1;
        }



        // video is already playing so return
        if (targetPosition == playPosition) {
            return;
        }

        // set the position of the list-item that is to be played
        playPosition = targetPosition;
        if (videoSurfaceView == null) {
            return;
        }

        // remove any old surface views from previously playing videos
        videoSurfaceView.setVisibility(INVISIBLE);
        removeVideoView(videoSurfaceView);

        int currentPosition =
                targetPosition - ((LinearLayoutManager) Objects.requireNonNull(
                        getLayoutManager())).findFirstVisibleItemPosition();

        View child = getChildAt(currentPosition);
        if (child == null) {
            return;
        }

        ItemViewHolder holder = (ItemViewHolder) child.getTag();
        if (holder == null) {
            playPosition = -1;
            return;
        }
        coverImage = holder.Image;
        bar = holder.bar;

        ViewHolder = holder.itemView;
        requestManager = holder.requestManager;
        contentContainer = holder.frame;


        if(holder.type==1){

        videoSurfaceView.setPlayer(player);


        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, AppName));
        String mediaUrl = list.get(targetPosition).getVideoUrl();
        int id=list.get(targetPosition).getId();
        if (mediaUrl != null) {
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(mediaUrl),new CacheDataSourceFactory(context,100*1024*1024,5*1024*1024,id),new DefaultExtractorsFactory(),null,null);


            player.prepare(videoSource);
            player.setPlayWhenReady(true);
        }
    }


    }

    private int getVisibleVideoSurfaceHeight(int playPosition) {
        int at = playPosition - ((LinearLayoutManager) Objects.requireNonNull(
                getLayoutManager())).findFirstVisibleItemPosition();
        Log.d(TAG, "getVisibleVideoSurfaceHeight: at: " + at);

        View child = getChildAt(at);
        if (child == null) {
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);

        if (location[1] < 0) {
            return location[1] + videoSurfaceDefaultHeight;
        } else {
            return screenDefaultHeight - location[1];
        }
    }

    private void removeVideoView(PlayerView videoView) {
        ViewGroup parent = (ViewGroup) videoView.getParent();
        if (parent == null) {
            return;
        }

        int index = parent.indexOfChild(videoView);
        if (index >= 0) {
            parent.removeViewAt(index);
            isVideoViewAdded = false;
            ViewHolder.setOnClickListener(null);
        }
    }

    private void addVideoView() {
        contentContainer.addView(videoSurfaceView);
        isVideoViewAdded = true;
        videoSurfaceView.requestFocus();
        videoSurfaceView.setVisibility(VISIBLE);
        videoSurfaceView.setAlpha(1);
        coverImage.setVisibility(GONE);
    }

    private void resetVideoView() {
        if (isVideoViewAdded) {
            removeVideoView(videoSurfaceView);
            playPosition = -1;
            videoSurfaceView.setVisibility(INVISIBLE);
            coverImage.setVisibility(VISIBLE);
            bar.setVisibility(GONE);
        }
    }


    public void releasePlayer() {

        if (player != null) {
            player.release();
            player = null;
        }

        ViewHolder = null;
    }

    public void onPausePlayer() {
        if (player != null) {
            player.stop(true);
        }
    }

    public void setList(ArrayList<ContentStructure> content) {
        list = content;
    }









}
