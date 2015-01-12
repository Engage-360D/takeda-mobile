package ru.com.cardiomagnil.ui.slidingmenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.concurrent.atomic.AtomicBoolean;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppSharedPreferences;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.util.Tools;
import ru.com.cardiomagnil.commands.GetTestResults;
import ru.com.cardiomagnil.ui.ca_content.Ca_MainFargment;

public class SlidingMenuActivity extends SlidingFragmentActivity {
    protected ListFragment mMenuListFragment;
    private Fragment mCurrentFragment;
    private Fragment mPreviousFragment;
    private AtomicBoolean mPending = new AtomicBoolean(false);
    private final long PENDING_TIME = 500;

    private ProgressDialog mProgressDialog = null;
    private int mGetTestResultRequestId = -1;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("getTestResultRequestId", mGetTestResultRequestId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGetTestResultRequestId = savedInstanceState.getInt("getTestResultRequestId");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();
        initMenu(savedInstanceState);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toggle();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        if (mPreviousFragment != null) {
            switchContentBack();
        } else {
            if (mPending.get()) {
                super.onBackPressed();
            } else {
                startPending();
            }
        }
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
                    ExeptionsHandler.getInstatce().handleException(SlidingMenuActivity.this.getParent(), e);
                }
            }
        }).start();
    }

    private void initActionBar() {
        ViewGroup viewGroup = Tools.initActionBar(getLayoutInflater(), getActionBar(), true);
        ImageView imageViewMenuDark = (ImageView) viewGroup.findViewById(R.id.imageViewMenuDark);
        imageViewMenuDark.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    private void initMenu(Bundle savedInstanceState) {
        // set the Behind View
        setBehindContentView(R.layout.slidingmenu_menu_container);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mMenuListFragment = new MenuListFragment();
            t.replace(R.id.menu_frame, mMenuListFragment);
            t.commit();
        } else {
            mMenuListFragment = (ListFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        // set the Above View
        setContentView(R.layout.slidingmenu_content_container);
        mCurrentFragment = new Ca_MainFargment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mCurrentFragment).commit();
        // getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new
        // TestResultsFargment()).commit();

        setSlidingActionBarEnabled(true);
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public void refreshMenuItems() {
        ((MenuListFragment) mMenuListFragment).refreshMenuItems();
    }

    public void switchContent(final Fragment fragment) {
        mPreviousFragment = null;
        mCurrentFragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                getSlidingMenu().showContent();
            }
        }, 50);
    }

    public void switchContent(final Fragment fragment, Fragment previousFragment) {
        mPreviousFragment = previousFragment;
        mCurrentFragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                getSlidingMenu().showContent();
            }
        }, 50);
    }

    public void switchContentBack() {
        if (mPreviousFragment != null) {
            switchContent(mPreviousFragment);
            mPreviousFragment = null;
        }
    }

    /**
     * getTestResult -> switchContent(TestResultsFargment)
     */
    public void getTestResult() {
        mGetTestResultRequestId = getServiceHelper().executeCommand(new GetTestResults());
        showProgressDialog();
    }

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
        super.onServiceCallback(requestId, requestIntent, resultCode, resultData);

        if (getServiceHelper().check(requestIntent, GetTestResults.class)) {
            if (resultCode == GetTestResults.RESPONSE_SUCCESS) {
                hideProgressDialog();
                storePreferences();
                switchContent(new TestResultsFargment());
                refreshMenuItems();
            } else if (resultCode == GetTestResults.RESPONSE_PROGRESS) {
                // do nothing
            } else {
                hideProgressDialog();
                SlidingMenu slidingMenu = getSlidingMenu();
                slidingMenu.showMenu();
                Tools.showAlertDialog(this, resultData.getString("error"), false);
            }
            return;
        }
    }

    private void storePreferences() {
        AppSharedPreferences appSharedPreferences = AppSharedPreferences.getInstatce();
        AppState appState = AppState.getInstatce();

        appSharedPreferences.load();
        appSharedPreferences.setPreference(AppSharedPreferences.PREFERENCES.results, appState.getTestResult().getAsJson().toString());
        appSharedPreferences.save();
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