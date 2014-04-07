package com.cardiomagnil.ui.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.cardiomagnil.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        animate();
    }

    private void animate() {
        ImageView mImage = (ImageView) findViewById(R.id.imageViewSplash);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_fade);
        animation.setAnimationListener(mAnimationListener);
        mImage.startAnimation(animation);
    }

    AnimationListener mAnimationListener = new AnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {
            onAnimationEndHelper();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // Auto-generated method stub
        }

        @Override
        public void onAnimationStart(Animation animation) {
            // Auto-generated method stub
        }

        private void onAnimationEndHelper() {
            Intent intent = new Intent(SplashActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }
    };
}