package ru.com.cardiomagnil.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.Constants;

public abstract class BaseSlidingFragmentActivity extends SlidingFragmentActivity {
    protected FragmentManager mFragmentManager;
    private final long CONTENT_DELAY = 50;
    private final long PENDING_TIME = 500;
    private final long TOGGLE_DELAY = 1000;
    private boolean mIsLocked = false;
    private AtomicBoolean mPending = new AtomicBoolean(false);

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
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
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // https://github.com/jfeinstein10/SlidingMenu/issues/344
        // "Cannot interact with menu's view when enable TOUCHMODE_FULLSCREEN #344"
        getSlidingMenu().setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
        getSlidingMenu().invalidate();

        mIsLocked = false;
    }

    private void startPending() {
        mPending.set(true);
        Toast.makeText(this, getResources().getString(R.string.press_back_twice), Toast.LENGTH_SHORT).show();
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

    // -------------------- implements fragment stack ---------------------||
    // --------------------------------------------------------------------\/
    public void putContentOnTop(final Fragment newTopFragment, final boolean withSwitch) {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            String tag = newTopFragment.getTag() == null ? getTimestampTag() : newTopFragment.getTag();
            fragmentTransaction.replace(R.id.content_frame, newTopFragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();

            initFragmentTop(newTopFragment, false);
            if (withSwitch) {
                showContentDelayed();
            }
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

            initFragmentTop(newTopFragment, false);
            if (withSwitch) {
                showContentDelayed();
            }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeContentStepBack(final boolean withSwitch) {
        try {
            if (mFragmentManager.getBackStackEntryCount() > 1) {
                mFragmentManager.popBackStackImmediate();
            }

            initFragmentTop(getCurrentFragment(), false);
            if (withSwitch) {
                showContentDelayed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean isBackStackEmpty() {
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

    protected void initFragmentTop(final Fragment fragment, boolean withBack) {
        try {
            Class clazz = ((Object) fragment).getClass();
            Field itemField = clazz.getDeclaredField(Constants.ITEM_NAME);
            itemField.setAccessible(true);
            int itemNameId = (Integer) itemField.get(null);
            String itemNameString = getResources().getString(itemNameId);

            TextView textViewCurrentFragment = (TextView) findViewById(R.id.textViewCurrentFragment);
            textViewCurrentFragment.setText(itemNameString);

            if (withBack) {
                // TODO: add button 'back' to top
            } else {
                // TODO: remove button 'back' from top
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
