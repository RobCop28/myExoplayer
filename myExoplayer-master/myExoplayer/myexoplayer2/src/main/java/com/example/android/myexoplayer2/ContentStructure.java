package com.example.android.myexoplayer2;

public class ContentStructure {
    private int id;
    private String videoUrl;
    private String photoUrl;

    //getter methods
    public String getVideoUrl(){return videoUrl;}
    public String getPhotoUrl(){return photoUrl;}
    public int getId(){return id;}
    //setter methods
    public void setId(int id){this.id=id;}
    public void setVideoUrl(String url){videoUrl=url;}
    public void setPhotoUrl(String url){photoUrl=url;}
}
