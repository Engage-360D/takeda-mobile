package ru.com.cardiomagnil.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppConfig;
import ru.com.cardiomagnil.application.AppSharedPreferences;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.commands.UserAuthorization;
import ru.com.cardiomagnil.model.Authorization;
import ru.com.cardiomagnil.ui.slidingmenu.SlidingMenuActivity;
import ru.com.cardiomagnil.widget.TrackedFragmentActivity;

public class SplashActivity extends TrackedFragmentActivity {
    private Animation mAnimation;
    private Boolean mIsLogined = null;
    private int mAnimationRepeatCounter = 0;
    private boolean mActivityStarted = false;
    private final int ANIMATION_REPEATS_MIN = 1;

    private int mUserAuthorizationRequestId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        // TODO: fix it ater tests
//        Intent intent = new Intent(SplashActivity.this, C_SlidingMenuActivity.class);
//        startActivity(intent);
//        finish();

        // TODO: fix it ater tests
        Intent intent = new Intent(SplashActivity.this, TestActivity.class);
        startActivity(intent);
        finish();

//        animate();
//        userAuthorization();
    }

    private void animate() {
        ImageView mImage = (ImageView) findViewById(R.id.imageViewSplash);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_fade);
        Animation animation_first = ((AnimationSet) mAnimation).getAnimations().get(1);
        animation_first.setAnimationListener(mAnimationListener);
        mImage.startAnimation(mAnimation);
    }

    AnimationListener mAnimationListener = new AnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {
            // Auto-generated method stub
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            mAnimationRepeatCounter++;
            tryToStartStartActivity();
        }

        @Override
        public void onAnimationStart(Animation animation) {
            // Auto-generated method stub
        }
    };

    /**
     * userAuthorization -> tryToStartStartActivity
     */
    public void userAuthorization() {
        AppState appState = AppState.getInstatce();

        if (appState.getUser() != null &&
        /**/appState.getUser().getEmail() != null &&
        /**/appState.getUser().getPlainPasswordFirst() != null) {

            Authorization authorization = new Authorization();
            authorization.setClientId(AppConfig.CLIENT_ID);
            authorization.setClientSecret(AppConfig.CLIENT_SECRET);
            authorization.setGrantType(AppConfig.GRANT_TYPE);
            authorization.setUsername(appState.getUser().getEmail());
            authorization.setPassword(appState.getUser().getPlainPasswordFirst());
            appState.setAuthorization(authorization);

            mUserAuthorizationRequestId = getServiceHelper().executeCommand(new UserAuthorization());
        } else {
            mIsLogined = false;
        }
    }

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
        super.onServiceCallback(requestId, requestIntent, resultCode, resultData);

        if (getServiceHelper().check(requestIntent, UserAuthorization.class)) {
            if (resultCode == UserAuthorization.RESPONSE_SUCCESS) {
                mIsLogined = true;
            } else if (resultCode == UserAuthorization.RESPONSE_PROGRESS) {
                // do nothing
            } else {
                mIsLogined = false;
            }
            tryToStartStartActivity();
        }
    }

    private void tryToStartStartActivity() {
        if (!mActivityStarted && mAnimationRepeatCounter > ANIMATION_REPEATS_MIN && mIsLogined != null) {
            mActivityStarted = true;
            mAnimation.cancel();
            Intent intent;
            if (mIsLogined) {
                intent = new Intent(SplashActivity.this, SlidingMenuActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, StartActivity.class);
            }
            storePreferences();
            startActivity(intent);
            finish();
        }
    }

    private void storePreferences() {
        AppSharedPreferences appSharedPreferences = AppSharedPreferences.getInstatce();
        AppState appState = AppState.getInstatce();

        appSharedPreferences.load();
        appSharedPreferences.setPreference(AppSharedPreferences.PREFERENCES.token, appState.getToken().getAsJson() == null ? null : appState.getToken().getAsJson().toString());
        appSharedPreferences.save();
    }
}