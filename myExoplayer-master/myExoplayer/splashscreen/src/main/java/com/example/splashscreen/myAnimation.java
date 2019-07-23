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
        final RelativeLayout layout=(RelativeLayout)findViewById(R.id.mainLayout);
        animation1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation1);
        animation1.setStartTime(50);
        layout.startAnimation(animation1);

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ImageView view=(ImageView)findViewById(R.id.text);
                view.setVisibility(View.VISIBLE);
                animation2=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slidein);
                animation2.setStartTime(2000);
                view.startAnimation(animation2);
                animation2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ConstraintLayout parent=(ConstraintLayout)findViewById(R.id.parentLayout);

                        animation3=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.lastpart);
                        animation3.setStartTime(2150);
                        parent.startAnimation(animation3);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




    }
}
