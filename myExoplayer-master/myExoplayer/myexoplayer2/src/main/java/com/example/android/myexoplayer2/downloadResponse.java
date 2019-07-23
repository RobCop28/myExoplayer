package com.example.android.myexoplayer2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class downloadResponse implements Response.Listener<byte[]>, Response.ErrorListener {
    String url;
    Context context;
    private downloadVideo request;
    int count;
    private RelativeLayout layout;
    private FileOutputStream stream;
    Uri fileUri;
    int id;
    int type;


    public downloadResponse(String url,Context context,int id,int type){this.url=url; this.context=context; this.id=id; this.type=type;
     }

    @Override
    public void onResponse(byte[] response) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (response!=null) {
                try{
                    long lenghtOfFile = response.length;

                    //covert reponse to input stream
                    InputStream input = new ByteArrayInputStream(response);

                    if(type==1) {
                        File myFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), "videos.mp4");


                        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(myFile));
                        byte[] data = new byte[1024];

                        long total = 0;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            output.write(data, 0, count);
                        }

                        // output.flush();

                        output.close();
                        input.close();
                        layout.setVisibility(View.GONE);

                        fileUri = FileProvider.getUriForFile(context, "com.example.android.myexoplayer2.fileprovider", myFile);

                        Toast.makeText(context, "Downloaded" + fileUri, Toast.LENGTH_LONG).show();
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.setType("video/*");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);

                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(Intent.createChooser(shareIntent, "Share Video"));
                    }

                    if(type==0){
                        File myFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "images.jpg");


                        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(myFile));
                        byte[] data = new byte[1024];

                        long total = 0;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            output.write(data, 0, count);
                        }

                        // output.flush();

                        output.close();
                        input.close();
                        layout.setVisibility(View.GONE);

                        fileUri = FileProvider.getUriForFile(context, "com.example.android.myexoplayer2.fileprovider", myFile);

                        Toast.makeText(context, "Downloaded" + fileUri, Toast.LENGTH_LONG).show();
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.setType("images/*");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);

                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(Intent.createChooser(shareIntent, "Share Photo"));

                    }


                }catch(IOException e){
                    e.printStackTrace();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
            e.printStackTrace();
        }

    }



    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE. ERROR:: "+error.getMessage());

    }

    public void initRequest(RelativeLayout layout){
        this.layout=layout;
        request = new downloadVideo(Request.Method.GET, url, downloadResponse.this, downloadResponse.this, null);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context,
                new HurlStack());
        mRequestQueue.add(request);
    }
    public Uri getFileUri(){return fileUri;}
}
