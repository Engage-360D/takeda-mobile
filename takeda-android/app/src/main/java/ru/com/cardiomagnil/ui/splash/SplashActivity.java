package ru.com.cardiomagnil.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppSharedPreferences;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.token.Ca_Token;
import ru.com.cardiomagnil.ca_model.token.Ca_TokenDao;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.ca_model.user.Ca_UserDao;
import ru.com.cardiomagnil.ui.base.BaseActivity;
import ru.com.cardiomagnil.ui.slidingmenu.SlidingMenuActivity;
import ru.com.cardiomagnil.ui.start.StartActivity;
import ru.com.cardiomagnil.util.CallbackOne;

public class SplashActivity extends BaseActivity implements AnimationListener {
    private int mMinAnimRepeats = 2; // >= 1, nice look for 2*n
    private boolean mActivityStarted;
    private boolean nInitializationFinished;
    private boolean mIsLogined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_heart);
        startInitialization();

//        // TODO: fix it ater tests
//        Intent intent = new Intent(SplashActivity.this, C_SlidingMenuActivity.class);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        tryToStartStartActivity();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        if (nInitializationFinished && --mMinAnimRepeats < 0)
            animation.cancel();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        --mMinAnimRepeats;
    }

    private void startInitialization() {
        animate();
        getToken();
    }

    private void animate() {
        ImageView mImage = (ImageView) findViewById(R.id.imageViewHeart);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_fade);
        Animation animation_first = ((AnimationSet) animation).getAnimations().get(1);
        animation_first.setAnimationListener(this);
        mImage.startAnimation(animation);
    }

    private void getToken() {
        final String tokenId = (String) AppSharedPreferences.get(AppSharedPreferences.Preference.tokenId);

        if (TextUtils.isEmpty(tokenId)) finishInitialization(null, null);

        Ca_TokenDao.getByTokenId(
                tokenId,
                new CallbackOne<Ca_Token>() {
                    @Override
                    public void execute(Ca_Token token) {
                        getUser(token);
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        finishInitialization(null, null);
                    }
                }
        );
    }

    private void getUser(final Ca_Token token) {
        Ca_UserDao.getById(
                token,
                new CallbackOne<Ca_User>() {
                    @Override
                    public void execute(Ca_User user) {
                        finishInitialization(token, user);
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        finishInitialization(token, null);
                    }
                },
                false
        );
    }

    private void finishInitialization(Ca_Token token, Ca_User user) {
        if (token == null || user == null) {
            initAppState(null, null);
            mIsLogined = false;
        } else {
            initAppState(token, user);
            mIsLogined = true;
        }
        nInitializationFinished = true;
    }

    private void initAppState(Ca_Token token, Ca_User user) {
        AppState.getInstatce().setToken(token);
        AppState.getInstatce().setUser(user);
    }

    private void tryToStartStartActivity() {
        if (!mActivityStarted && nInitializationFinished) {
            mActivityStarted = true;
            Intent intent;
            if (mIsLogined) {
                intent = new Intent(SplashActivity.this, SlidingMenuActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, StartActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }

}