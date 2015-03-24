package ru.com.cardiomagnyl.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.util.Utils;

public abstract class BaseSlidingFragmentActivity extends SlidingFragmentActivity {
    protected FragmentManager mFragmentManager;
    private final long CONTENT_DELAY = 50;
    private final long PENDING_TIME = 500;
    private final long TOGGLE_DELAY = 1000;
    private boolean mIsLocked = false;
    private AtomicBoolean mPending = new AtomicBoolean(false);
    private final boolean TOGGLE_ON_START = false;

    abstract protected void initTopOnFragmentChanged(final Fragment fragment, boolean withBack);

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
        CardiomagnylApplication.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        CardiomagnylApplication.getInstance().clearActivityIfCurrent(this);
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
                if (TOGGLE_ON_START) toggle();
            }
        }, TOGGLE_DELAY);

        getSlidingMenu().setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                Utils.hideKeyboard(getCurrentFocus());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mIsLocked) {
            return;
        }

        if (!isBackStackEmpty()) {
            makeContentStepBack(true);
        } else {
            if (mPending.get()) {
                super.onBackPressed();
            } else {
                startPending();
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mIsLocked) {
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            toggle();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean isLocked() {
        return mIsLocked;
    }

    public void lockMenu() {
        mIsLocked = true;

        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        getSlidingMenu().setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);
    }

    public void unLockMenu() {
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        // https://github.com/jfeinstein10/SlidingMenu/issues/344
        // "Cannot interact with menu's view when enable TOUCHMODE_FULLSCREEN #344"
        getSlidingMenu().setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
        getSlidingMenu().invalidate();

        mIsLocked = false;
    }

    private void startPending() {
        mPending.set(true);
        Tools.showToast(this, R.string.press_back_twice, Toast.LENGTH_SHORT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(PENDING_TIME);
                    mPending.set(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // FIXME: Fragment.instantiate - "Create a new instance of a Fragment with the given class name. This is the same as calling its empty constructor."
    // -------------------- implements fragment stack ---------------------||
    // --------------------------------------------------------------------\/
    public void putContentOnTop(final Fragment newTopFragment, final boolean withSwitch) {
        try {
            String tag = newTopFragment.getTag() == null ? getTimestampTag() : newTopFragment.getTag();

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, newTopFragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();

            initTopOnFragmentChanged(newTopFragment, mFragmentManager.getBackStackEntryCount() > 0);
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
            int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
            FragmentManager.BackStackEntry backStackEntry = mFragmentManager.getBackStackEntryAt(backStackEntryCount - 1);
            Fragment fragment = mFragmentManager.findFragmentByTag(backStackEntry.getName());
            String tag = fragment.getTag();

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.replace(R.id.content_frame, newTopFragment, tag);
            fragmentTransaction.commit();

            initTopOnFragmentChanged(newTopFragment, !isBackStackEmpty());
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
            String tag = newTopFragment.getTag() == null ? getTimestampTag() : newTopFragment.getTag();

            // blocking of fragment creation on popBackStack
            if (mFragmentManager.getBackStackEntryCount() > 0) {
                for (int counter = 0; counter < mFragmentManager.getBackStackEntryCount(); ++counter) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

                    Fragment fragment = mFragmentManager.findFragmentByTag(mFragmentManager.getBackStackEntryAt(counter).getName());
                    fragmentTransaction.remove(fragment);
                    Fragment newFragment = new Fragment();
                    fragmentTransaction.replace(R.id.content_frame, newFragment, fragment.getTag());

                    fragmentTransaction.commit();
                }
                mFragmentManager.executePendingTransactions();

                mFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, newTopFragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();

            initTopOnFragmentChanged(newTopFragment, false);
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

            // blocking of fragment creation on popBackStack
            ArrayList<Pair<Fragment, String>> fragmentsTagsList = new ArrayList<>();
            if (!isBackStackEmpty()) {
                Fragment currentFragment = null;
                String currentTag = null;

                for (int counter = 0; counter < mFragmentManager.getBackStackEntryCount(); ++counter) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

                    Fragment fragment = mFragmentManager.findFragmentByTag(mFragmentManager.getBackStackEntryAt(counter).getName());
                    fragmentTransaction.detach(fragment);

                    if (counter == mFragmentManager.getBackStackEntryCount() - 2) {
                        currentFragment = fragment;
                        currentTag = fragment.getTag();
                    } else {
                        fragmentTransaction.detach(fragment);
                    }

                    fragmentTransaction.commit();
                }
                mFragmentManager.executePendingTransactions();
                mFragmentManager.popBackStackImmediate();

                if (currentFragment != null && currentTag != null) {
                    mFragmentManager.popBackStackImmediate();

                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, currentFragment, currentTag);
                    fragmentTransaction.addToBackStack(currentTag);
                    fragmentTransaction.commit();

                    mFragmentManager.executePendingTransactions();
                }

                initTopOnFragmentChanged(currentFragment, !isBackStackEmpty());
            }
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
        if (mFragmentManager.getBackStackEntryCount() == 0) return null;

        FragmentManager.BackStackEntry backEntry = mFragmentManager.getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1);
        String string = backEntry.getName();
        Fragment fragment = mFragmentManager.findFragmentByTag(string);
        Log.d("Test", fragment.getClass().getName());
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

}