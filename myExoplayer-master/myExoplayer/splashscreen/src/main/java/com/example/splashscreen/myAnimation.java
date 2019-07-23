package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class myAnimation extends AppCompatActivity {
    public Animation animation1,animation2,animation3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_animation);
        RelativeLayout layout=(RelativeLayout)findViewById(R.id.mainLayout);
        animation1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation1);
        animation1.setStartTime(50);
        layout.startAnimation(animation1);
        ImageView view=(ImageView)findViewById(R.id.text);
        animation2=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slidein);
        animation2.setStartTime(125);
        view.startAnimation(animation2);


    }
}
