package ru.com.cardiomagnyl.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppSharedPreferences;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.model.test.TestResultDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.token.TokenDao;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.model.user.UserDao;
import ru.com.cardiomagnyl.ui.base.BaseActivity;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;

public class SplashActivity extends BaseActivity implements AnimationListener {
    private int mMinAnimRepeats = 2; // >= 1, nice look for 2*n
    private boolean mActivityStarted;
    private boolean mInitializationFinished;
    private boolean mIsLogined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_heart);
        startInitialization();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        tryToStartStartActivity();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        if (mInitializationFinished && --mMinAnimRepeats < 0)
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

        if (TextUtils.isEmpty(tokenId)) {
            Response responseError = new Response.Builder(new ru.com.cardiomagnyl.model.common.Error()).create();
            finishInitialization(null, null, null, responseError);
            return;
        }

        TokenDao.getByTokenId(
                tokenId,
                new CallbackOne<Token>() {
                    @Override
                    public void execute(Token token) {
                        getUser(token);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        finishInitialization(null, null, null, responseError);
                    }
                }
        );
    }

    private void getUser(final Token token) {
        UserDao.getByToken(
                token,
                new CallbackOne<User>() {
                    @Override
                    public void execute(User user) {
                        getTestResult(token, user);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        finishInitialization(token, null, null, responseError);
                    }
                },
                false
        );
    }

    public void getTestResult(final Token token, final User user) {
        TestResultDao.getAll(
                token,
                new CallbackOne<List<TestResult>>() {
                    @Override
                    public void execute(List<TestResult> testResultList) {
                        finishInitialization(token, user, TestResultDao.getNewestResult(testResultList), null);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        finishInitialization(token, user, null, responseError);
                    }
                }
        );
    }

    private void finishInitialization(Token token, User user, TestResult testResult, Response responseError) {
        mIsLogined = (token != null && user != null);
        if (!mIsLogined) {
            initAppState(null, null, null);
        } else {
            initAppState(token, user, testResult);
        }
        mInitializationFinished = true;
    }

    private void initAppState(Token token, User user, TestResult testResult) {
        AppState.getInsnatce().setToken(token);
        AppState.getInsnatce().setUser(user);
        AppState.getInsnatce().setTestResult(testResult);
    }

    private void tryToStartStartActivity() {
        if (!mActivityStarted && mInitializationFinished) {
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