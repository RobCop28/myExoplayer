package com.example.android.myexoplayer2;

import android.content.Context;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;


public class CacheDataSourceFactory implements DataSource.Factory {
    private  Context context;
    private final DefaultDataSourceFactory dataSourceFactory;
    private  long maxFileSize,maxCacheSize;
    private LeastRecentlyUsedCacheEvictor evictor;
    private static SimpleCache simpleCache;
    private int id;

    CacheDataSourceFactory(Context context, long maxCacheSize, long maxFileSize,int id){
        this.context=context;
        this.maxCacheSize=maxCacheSize;
        this.maxFileSize=maxFileSize;
        this.id=id;
        String user= Util.getUserAgent(context,context.getString(R.string.app_name));
        DefaultBandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();
        dataSourceFactory=new DefaultDataSourceFactory(this.context,bandwidthMeter,new DefaultDataSourceFactory(this.context,user));
         evictor = new LeastRecentlyUsedCacheEvictor(maxCacheSize);
         if(simpleCache ==null)
         simpleCache = new SimpleCache(new File(context.getCacheDir(), "media"), evictor);

    }


    @Override
    public DataSource createDataSource() {



        return new CacheDataSource(simpleCache, dataSourceFactory.createDataSource(),
                new FileDataSource(), new CacheDataSink(simpleCache, maxFileSize),
                CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
    }
}
