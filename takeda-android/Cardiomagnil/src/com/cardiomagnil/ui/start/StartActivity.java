package com.cardiomagnil.ui.start;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.cardiomagnil.R;
import com.cardiomagnil.application.Tools;
import com.cardiomagnil.commands.UserAuthorization;
import com.cardiomagnil.commands.UserRegistration;
import com.cardiomagnil.ui.slidingmenu.SlidingMenuActivity;
import com.cardiomagnil.ui.start.CustomAnimation.OnAnimationEndListener;
import com.cardiomagnil.widget.TrackedFragmentActivity;

public class StartActivity extends TrackedFragmentActivity {
    private final int AMOUNT_OF_START_FRAGMENTS = 3;
    private CustomFragment[] mFragments = new CustomFragment[AMOUNT_OF_START_FRAGMENTS];

    private ProgressDialog mProgressDialog = null;
    private int mUserAuthorizationRequestId = -1;
    private int mUserRegistrationRequestId = -1;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("userAuthorizationRequestId", mUserAuthorizationRequestId);
        savedInstanceState.putInt("userRegistrationRequestId", mUserRegistrationRequestId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mUserAuthorizationRequestId = savedInstanceState.getInt("userAuthorizationRequestId");
        mUserRegistrationRequestId = savedInstanceState.getInt("userRegistrationRequestId");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initStartActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearFragments();
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
        Tools.hideKeyboard(this);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            initFragments(fragments);
        }

        ViewPager pager = (ViewPager) findViewById(R.id.viewPagerContent);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

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
                    mFragments[1] = new LoginFragment();
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
        mUserAuthorizationRequestId = getServiceHelper().executeCommand(new UserAuthorization());
        showProgressDialog();
    }

    /**
     * userRegistration -> startSlidingMenu
     */
    public void userRegistration() {
        mUserRegistrationRequestId = getServiceHelper().executeCommand(new UserRegistration());
        showProgressDialog();
    }

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
        super.onServiceCallback(requestId, requestIntent, resultCode, resultData);

        if (getServiceHelper().check(requestIntent, UserAuthorization.class)) {
            if (resultCode == UserAuthorization.RESPONSE_SUCCESS) {
                hideProgressDialog();
                startSlidingMenu();
            } else if (resultCode == UserAuthorization.RESPONSE_PROGRESS) {
                // do nothing
            } else {
                hideProgressDialog();
                Tools.showAlertDialog(this, resultData.getString("error"), false);
            }
        }
        
        if (getServiceHelper().check(requestIntent, UserRegistration.class)) {
            if (resultCode == UserRegistration.RESPONSE_SUCCESS) {
                hideProgressDialog();
                startSlidingMenu();
            } else if (resultCode == UserRegistration.RESPONSE_PROGRESS) {
                // do nothing
            } else {
                hideProgressDialog();
                Tools.showAlertDialog(this, resultData.getString("error"), false);
            }
        }
    }

    private void startSlidingMenu() {
        Intent intent = new Intent(this, SlidingMenuActivity.class);
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
}
