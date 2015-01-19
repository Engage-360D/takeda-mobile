package ru.com.cardiomagnil.ui.start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;

import ru.com.cardiomagnil.app.BuildConfig;
import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.base.BaseStartFragment;
import ru.com.cardiomagnil.ui.base.BaseTrackedFragmentActivity;
import ru.com.cardiomagnil.ui.slidingmenu.SlidingMenuActivity;
import ru.com.cardiomagnil.ui.start.CustomAnimation.OnAnimationEndListener;
import ru.com.cardiomagnil.util.TestMethods;

public class StartActivity extends BaseTrackedFragmentActivity {

    private ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initStartActivity();
        customizeIfDebug();
    }

    /**
     * @param direction direction = true - forward;
     *                  direction = false - backward
     */
    public void slideViewPager(boolean direction) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerContent);
        int item = viewPager.getCurrentItem() + (direction ? 1 : -1);
        viewPager.setCurrentItem(item);
    }

    private void customizeIfDebug() {
        if (BuildConfig.DEBUG) {
            View linearLayoutBottom = findViewById(R.id.linearLayoutProgress);

            linearLayoutBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TestMethods.testCurrentMethod();
                }
            });
        }
    }

//    private void initFragments(List<Fragment> customFragments) {
//        int amountOfFragments = (customFragments.size() < AMOUNT_OF_START_FRAGMENTS) ? customFragments.size() : AMOUNT_OF_START_FRAGMENTS;
//
//        for (int counter = 0; counter < amountOfFragments; ++counter) {
//            mFragments[counter] = (CustomFragment) customFragments.get(counter);
//        }
//    }

    private void initStartActivity() {
        final ViewPager pager = (ViewPager) findViewById(R.id.viewPagerContent);
        final CustomFragmentPagerAdapter customFragmentPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());

        pager.setOffscreenPageLimit((customFragmentPagerAdapter.getCount() + 1) / 2);
        pager.setAdapter(customFragmentPagerAdapter);
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
                initStartActivityAccordingCurrentFragment(customFragmentPagerAdapter, position);
            }
        });

        pager.setCurrentItem(0);
        ((BaseStartFragment)customFragmentPagerAdapter.getItem(0)).initParent(StartActivity.this);
    }

    private void initStartActivityAccordingCurrentFragment(CustomFragmentPagerAdapter customFragmentPagerAdapter, int position) {
        View linearLayoutTop = findViewById(R.id.linearLayoutTop);
        View textViewBottom = findViewById(R.id.textViewBottom);
        View linearLayoutProgress = findViewById(R.id.linearLayoutProgress);

        final BaseStartFragment currentFragment = (BaseStartFragment) customFragmentPagerAdapter.getItem(position);

        animateTopAndBottom(linearLayoutTop, position != 0);
        animateTopAndBottom(textViewBottom, position != 0);
        fadeOut(currentFragment, linearLayoutProgress);
    }

    private void animateTopAndBottom(View view, boolean visibilityToSet) {
        boolean viewIsVisible = (view.getAlpha() != 0) && (view.getVisibility() == View.VISIBLE);
        if (visibilityToSet && !viewIsVisible) {
            fadeIn(null, view);
        } else if (!visibilityToSet && viewIsVisible) {
            fadeOut(null, view);
        }
    }

    private void fadeOut(final BaseStartFragment customFragment, final View view) {
        view.setAlpha(1.0F);
        new CustomAnimation
        /**/.Builder(AnimationUtils.loadAnimation(this, R.anim.bottom_fade_out), view)
        /**/.setOnAnimationEndListener(new OnAnimationEndListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setAlpha(0.0F);
                if (customFragment != null) {
                    customFragment.initParent(StartActivity.this);
                    fadeIn(customFragment, view);
                }
            }
        })
        /**/.build()
        /**/.startAnimation();
    }

    private void fadeIn(final BaseStartFragment customFragment, final View view) {
        view.setAlpha(1.0F);
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

    // FIXME

    /**
     * userAuthorization -> startSlidingMenu
     */
    public void userAuthorization() {
//        Authorization authorization = new Authorization();
//        authorization.setClientId(AppConfig.CLIENT_ID);
//        authorization.setClientSecret(AppConfig.CLIENT_SECRET);
//        authorization.setGrantType(AppConfig.GRANT_TYPE);
//        authorization.setUsername(AppState.getInstatce().getUser().getEmail());
//        authorization.setPassword(AppState.getInstatce().getUser().getPlainPassword());
//        AppState.getInstatce().setAuthorization(authorization);
//
//        mUserAuthorizationRequestId = getServiceHelper().executeCommand(new UserAuthorization());
//        showProgressDialog();
    }

    // FIXME

    /**
     * userRegistration -> userAuthorization -> startSlidingMenu
     */
    public void userRegistration() {
//        mUserRegistrationRequestId = getServiceHelper().executeCommand(new UserRegistration());
//        showProgressDialog();
    }

    // FIXME

    /**
     * restorePassword -> showAlertDialog
     */
    public void restorePassword(String emai) {
//        mUserRegistrationRequestId = getServiceHelper().executeCommand(new RestorePassword(emai));
//        showProgressDialog();
    }

    // FIXME
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
//        super.onServiceCallback(requestId, requestIntent, resultCode, resultData);
//
//        if (getServiceHelper().check(requestIntent, UserAuthorization.class)) {
//            if (resultCode == UserAuthorization.RESPONSE_SUCCESS) {
//                hideProgressDialog();
//                storePreferences();
//                startSlidingMenu();
//            } else if (resultCode == UserAuthorization.RESPONSE_PROGRESS) {
//                // do nothing
//            } else {
//                hideProgressDialog();
//                Tools.showAlertDialog(this, resultData.getString("error"), false);
//            }
//            return;
//        }
//
//        if (getServiceHelper().check(requestIntent, UserRegistration.class)) {
//            if (resultCode == UserRegistration.RESPONSE_SUCCESS) {
//                userAuthorization();
//            } else if (resultCode == UserRegistration.RESPONSE_PROGRESS) {
//                // do nothing
//            } else {
//                hideProgressDialog();
//                Tools.showAlertDialog(this, resultData.getString("error"), false);
//            }
//            return;
//        }
//
//        if (getServiceHelper().check(requestIntent, RestorePassword.class)) {
//            if (resultCode == RestorePassword.RESPONSE_SUCCESS) {
//                hideProgressDialog();
//                Tools.showAlertDialog(this, getResources().getString(R.string.restoring_info_sent), false);
//            } else if (resultCode == RestorePassword.RESPONSE_PROGRESS) {
//                // do nothing
//            } else {
//                hideProgressDialog();
//                Tools.showAlertDialog(this, resultData.getString("error"), false);
//            }
//            return;
//        }
    }

    // FIXME
    private void storePreferences() {
//        AppSharedPreferences appSharedPreferences = AppSharedPreferences.getInstatce();
//        AppState appState = AppState.getInstatce();
//
//        appSharedPreferences.load();
//        appSharedPreferences.setPreference(AppSharedPreferences.PREFERENCES.token, appState.getToken().getAsJson().toString());
//        appSharedPreferences.setPreference(AppSharedPreferences.PREFERENCES.email, appState.getUser().getEmail());
//        appSharedPreferences.setPreference(AppSharedPreferences.PREFERENCES.plain_password, appState.getUser().getPlainPassword());
//        appSharedPreferences.save();
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
