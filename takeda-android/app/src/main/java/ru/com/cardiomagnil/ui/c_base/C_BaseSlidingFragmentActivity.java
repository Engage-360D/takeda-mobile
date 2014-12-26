package ru.com.cardiomagnil.ui.c_base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.utils.Utils;

public abstract class C_BaseSlidingFragmentActivity extends SlidingFragmentActivity {
    protected FragmentManager mFragmentManager;
    private final long CONTENT_DELAY = 50;
    private final long TOGGLE_DELAY = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getResources().getBoolean(R.bool.ga_analytics_enabled)) {
            EasyTracker.getInstance(this).activityStart(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CardiomagnilApplication.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        CardiomagnilApplication.getInstance().clearActivityIfCurrent(this);
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (getResources().getBoolean(R.bool.ga_analytics_enabled)) {
            EasyTracker.getInstance(this).activityStop(this);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toggle();
            }
        }, TOGGLE_DELAY);

        getSlidingMenu().setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                Utils.hideKeyboard(getCurrentFocus());
            }
        });
    }

    // -------------------- implements fragment stack ---------------------||
    // --------------------------------------------------------------------\/
    public void putContentOnTop(final Fragment newTopFragment, final boolean withSwitch) {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            String tag = newTopFragment.getTag() == null ? getTimestampTag() : newTopFragment.getTag();
            fragmentTransaction.replace(R.id.content_frame, newTopFragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();

            initFragmentTop(newTopFragment, mFragmentManager.getBackStackEntryCount() > 0);
            if (withSwitch) {
                showContentDelayed();
            }

            Utils.hideKeyboard(getCurrentFocus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceContentOnTop(final Fragment newTopFragment, final boolean withSwitch) {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentManager.popBackStack();
            String tag = newTopFragment.getTag() == null ? getTimestampTag() : newTopFragment.getTag();
            fragmentTransaction.replace(R.id.content_frame, newTopFragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();

            initFragmentTop(newTopFragment, mFragmentManager.getBackStackEntryCount() > 1);
            if (withSwitch) {
                showContentDelayed();
            }

            Utils.hideKeyboard(getCurrentFocus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceAllContent(final Fragment newTopFragment, final boolean withSwitch) {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            String tag = newTopFragment.getTag() == null ? getTimestampTag() : newTopFragment.getTag();
            fragmentTransaction.replace(R.id.content_frame, newTopFragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();

            initFragmentTop(newTopFragment, false);
            if (withSwitch) {
                showContentDelayed();
            }

            Utils.hideKeyboard(getCurrentFocus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeContentStepBack(final boolean withSwitch) {
        try {
            Utils.hideKeyboard(getCurrentFocus());

            if (!isBackStackEmpty()) {
                mFragmentManager.popBackStackImmediate();
            }

            initFragmentTop(getCurrentFragment(), mFragmentManager.getBackStackEntryCount() > 2);
            if (withSwitch) {
                showContentDelayed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isBackStackEmpty() {
        return !(mFragmentManager.getBackStackEntryCount() > 1);
    }

    public Fragment getCurrentFragment() {
        FragmentManager.BackStackEntry backEntry = mFragmentManager.getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1);
        String string = backEntry.getName();
        Fragment fragment = mFragmentManager.findFragmentByTag(string);
        return fragment;
    }

    protected String getTimestampTag() {
        return String.valueOf(System.currentTimeMillis());
    }
    // -------------------- implements fragment stack ---------------------/\
    // --------------------------------------------------------------------||

    private void showContentDelayed() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getSlidingMenu().showContent();
            }
        }, CONTENT_DELAY);
    }

    // FIXME: initFragmentTop
    protected void initFragmentTop(final Fragment fragment, boolean withBack) {
//        if (fragment instanceof C_BaseItemFragment) {
//            TextView textViewCurrentFragment = (TextView) findViewById(R.id.textViewCurrentFragment);
//            AutoloadingImageView contentTopRightPhoto = (AutoloadingImageView) findViewById(R.id.contentTopRightPhoto);
//            ImageView contentTopRightIco = (ImageView) findViewById(R.id.contentTopRightIco);
//
//            String itemName = ((C_BaseItemFragment) fragment).getItemTitle();
//            View.OnClickListener onClickListener = ((C_BaseItemFragment) fragment).getTopRightIcoOnClickListener();
//            int icoId = ((C_BaseItemFragment) fragment).getTopRightIcoId();
//
//            textViewCurrentFragment.setText(itemName);
//            contentTopRightPhoto.setVisibility(View.GONE);
//            contentTopRightIco.setOnClickListener(onClickListener);
//            if (icoId > 0) {
//                contentTopRightIco.setImageResource(icoId);
//                contentTopRightIco.setVisibility(View.VISIBLE);
//            } else {
//                contentTopRightIco.setVisibility(View.INVISIBLE);
//                contentTopRightIco.setImageDrawable(null);
//            }
//        }
//
//        ImageView buttonContentTopMenu = (ImageView) findViewById(R.id.contentTopMenu);
//        buttonContentTopMenu.setImageDrawable(withBack ? getResources().getDrawable(R.drawable.ic_back) : getResources().getDrawable(R.drawable.ic_menu));
    }
}
