package ru.com.cardiomagnil.ui.start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;

import java.util.List;

import ru.com.cardiomagnil.app.BuildConfig;
import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppConfig;
import ru.com.cardiomagnil.application.AppSharedPreferences;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.application.Tools;
import ru.com.cardiomagnil.commands.RestorePassword;
import ru.com.cardiomagnil.commands.UserAuthorization;
import ru.com.cardiomagnil.commands.UserRegistration;
import ru.com.cardiomagnil.model.Authorization;
import ru.com.cardiomagnil.ui.slidingmenu.SlidingMenuActivity;
import ru.com.cardiomagnil.ui.start.CustomAnimation.OnAnimationEndListener;
import ru.com.cardiomagnil.util.TestMethods;
import ru.com.cardiomagnil.util.Utils;
import ru.com.cardiomagnil.widget.TrackedFragmentActivity;

public class StartActivity extends TrackedFragmentActivity {
    private final int AMOUNT_OF_START_FRAGMENTS = 3;
    private CustomFragment[] mFragments = new CustomFragment[AMOUNT_OF_START_FRAGMENTS];
    private int mPreviousAction = -1;
    private float mTouchX = 0;
    private float mTouchY = 0;

    private ProgressDialog mProgressDialog = null;
    private int mUserAuthorizationRequestId = -1;
    private int mUserRegistrationRequestId = -1;
    private int mRestorePasswordRequestId = -1;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("userAuthorizationRequestId", mUserAuthorizationRequestId);
        savedInstanceState.putInt("userRegistrationRequestId", mUserRegistrationRequestId);
        savedInstanceState.putInt("restorePasswordRequestId", mRestorePasswordRequestId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mUserAuthorizationRequestId = savedInstanceState.getInt("userAuthorizationRequestId");
        mUserRegistrationRequestId = savedInstanceState.getInt("userRegistrationRequestId");
        mRestorePasswordRequestId = savedInstanceState.getInt("restorePasswordRequestId");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getActionBar().hide();
        Tools.initActionBar(getLayoutInflater(), getActionBar(), false);
        initStartActivity();

        customizeIfDebug();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearFragments();
    }

    private void customizeIfDebug() {
        if (BuildConfig.DEBUG) {
            View layoutBottomOutside = findViewById(R.id.layoutBottomOutside);

            layoutBottomOutside.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TestMethods.testCurrentMethod();
                }
            });
        }
    }

    private void clearFragments() {
        for (int counter = 0; counter < mFragments.length; ++counter) {
            mFragments[counter] = null;
        }
    }

    private void initFragments(List<Fragment> customFragments) {
        int amountOfFragments = (customFragments.size() < AMOUNT_OF_START_FRAGMENTS) ? customFragments.size() : AMOUNT_OF_START_FRAGMENTS;

        for (int counter = 0; counter < amountOfFragments; ++counter) {
            mFragments[counter] = (CustomFragment) customFragments.get(counter);
        }
    }

    private void initStartActivity() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            initFragments(fragments);
        }

        ViewPager pager = (ViewPager) findViewById(R.id.viewPagerContent);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        final ViewPager viewPagerContent = (ViewPager) findViewById(R.id.viewPagerContent);
        final View layoutBottomInside = findViewById(R.id.layoutBottomInside);

        layoutBottomInside.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mTouchX = event.getRawX();
                    mTouchY = event.getRawY();
                }

                float inRegionX = mTouchX - event.getRawX();
                float inRegionY = mTouchY - event.getRawY();

                if (event.getAction() == MotionEvent.ACTION_UP &&
                /**/(mPreviousAction == MotionEvent.ACTION_DOWN)) {
                    mPreviousAction = event.getAction();

                    layoutBottomInside.performClick();
                    return true;
                } else {
                    if (!(event.getAction() == MotionEvent.ACTION_MOVE && inRegionX == 0 && inRegionY == 0)) {
                        mPreviousAction = event.getAction();
                    }
                    return viewPagerContent.onTouchEvent(event);
                }
                // return false;
            }
        });

        pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int state) {
                // do nothing
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // do nothing
            }

            @Override
            public void onPageSelected(int position) {
                initStartActivityAccordingCurrentFragment(position);
            }
        });
    }

    private void initStartActivityAccordingCurrentFragment(int position) {
        View layoutBottomInside = (View) findViewById(R.id.linearLayoutBottomInsideContent);
        View layoutBottomOutside = (View) findViewById(R.id.linearLayoutBottomOutsideContent);

        final CustomFragment currentFragment = mFragments[position];

        if (position == 0) {
            getActionBar().hide();
        } else {
            getActionBar().show();
        }

        fadeOut(currentFragment, layoutBottomInside);
        fadeOut(currentFragment, layoutBottomOutside);
    }

    private void fadeOut(final CustomFragment customFragment, final View view) {
        new CustomAnimation
        /**/.Builder(AnimationUtils.loadAnimation(this, R.anim.bottom_fade_out), view)
        /**/.setOnAnimationEndListener(new OnAnimationEndListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                if (customFragment != null) {
                    customFragment.initParent();
                }
                fadeIn(customFragment, view);
            }
        })
        /**/.build()
        /**/.startAnimation();
    }

    private void fadeIn(final CustomFragment CustomFragment, final View view) {
        new CustomAnimation
        /**/.Builder(AnimationUtils.loadAnimation(this, R.anim.bottom_fade_in), view)
        /**/.setOnAnimationEndListener(new OnAnimationEndListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                // do something
            }
        })
        /**/.build()
        /**/.startAnimation();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    if (mFragments[0] == null) {
                        mFragments[0] = new WelcomeFragment();
                        mFragments[0].setInitParentAtFirstTime(true);
                    }
                    return mFragments[0];
                case 1:
                    if (mFragments[1] == null) {
                        mFragments[1] = new LoginOrRestoreFragment();
                    }
                    return mFragments[1];
                case 2:
                    if (mFragments[2] == null) {
                        mFragments[2] = new RegistrationFragment();
                    }
                    return mFragments[2];
            }
            return null;
        }

        @Override
        public int getCount() {
            return AMOUNT_OF_START_FRAGMENTS;
        }
    }

    /**
     * userAuthorization -> startSlidingMenu
     */
    public void userAuthorization() {
        Authorization authorization = new Authorization();
        authorization.setClientId(AppConfig.CLIENT_ID);
        authorization.setClientSecret(AppConfig.CLIENT_SECRET);
        authorization.setGrantType(AppConfig.GRANT_TYPE);
        authorization.setUsername(AppState.getInstatce().getUser().getEmail());
        authorization.setPassword(AppState.getInstatce().getUser().getPlainPasswordFirst());
        AppState.getInstatce().setAuthorization(authorization);

        mUserAuthorizationRequestId = getServiceHelper().executeCommand(new UserAuthorization());
        showProgressDialog();
    }

    /**
     * userRegistration -> userAuthorization -> startSlidingMenu
     */
    public void userRegistration() {
        mUserRegistrationRequestId = getServiceHelper().executeCommand(new UserRegistration());
        showProgressDialog();
    }

    /**
     * restorePassword -> showAlertDialog
     */
    public void restorePassword(String emai) {
        mUserRegistrationRequestId = getServiceHelper().executeCommand(new RestorePassword(emai));
        showProgressDialog();
    }

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
        super.onServiceCallback(requestId, requestIntent, resultCode, resultData);

        if (getServiceHelper().check(requestIntent, UserAuthorization.class)) {
            if (resultCode == UserAuthorization.RESPONSE_SUCCESS) {
                hideProgressDialog();
                storePreferences();
                startSlidingMenu();
            } else if (resultCode == UserAuthorization.RESPONSE_PROGRESS) {
                // do nothing
            } else {
                hideProgressDialog();
                Tools.showAlertDialog(this, resultData.getString("error"), false);
            }
            return;
        }

        if (getServiceHelper().check(requestIntent, UserRegistration.class)) {
            if (resultCode == UserRegistration.RESPONSE_SUCCESS) {
                userAuthorization();
            } else if (resultCode == UserRegistration.RESPONSE_PROGRESS) {
                // do nothing
            } else {
                hideProgressDialog();
                Tools.showAlertDialog(this, resultData.getString("error"), false);
            }
            return;
        }

        if (getServiceHelper().check(requestIntent, RestorePassword.class)) {
            if (resultCode == RestorePassword.RESPONSE_SUCCESS) {
                hideProgressDialog();
                Tools.showAlertDialog(this, getResources().getString(R.string.restoring_info_sent), false);
            } else if (resultCode == RestorePassword.RESPONSE_PROGRESS) {
                // do nothing
            } else {
                hideProgressDialog();
                Tools.showAlertDialog(this, resultData.getString("error"), false);
            }
            return;
        }
    }

    private void storePreferences() {
        AppSharedPreferences appSharedPreferences = AppSharedPreferences.getInstatce();
        AppState appState = AppState.getInstatce();

        appSharedPreferences.load();
        appSharedPreferences.setPreference(AppSharedPreferences.PREFERENCES.token, appState.getToken().getAsJson().toString());
        appSharedPreferences.setPreference(AppSharedPreferences.PREFERENCES.email, appState.getUser().getEmail());
        appSharedPreferences.setPreference(AppSharedPreferences.PREFERENCES.plain_password_first, appState.getUser().getPlainPasswordFirst());
        appSharedPreferences.setPreference(AppSharedPreferences.PREFERENCES.plain_password_second, appState.getUser().getPlainPasswordSecond());
        appSharedPreferences.save();
    }

    private void startSlidingMenu() {
        Intent intent = new Intent(StartActivity.this, SlidingMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(this.getString(R.string.progress_dialog_text));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void onToggle(View view) {
        ((RadioGroup) view.getParent()).check(view.getId());
    }

}
