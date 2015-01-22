package ru.com.cardiomagnil.ui.slidingmenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.lang.reflect.Constructor;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.base.BaseSlidingFragmentActivity;
import ru.com.cardiomagnil.ui.ca_content.Ca_MainFargment;
import ru.com.cardiomagnil.ui.ca_content.Ca_StartFragment;

public class SlidingMenuActivity extends BaseSlidingFragmentActivity {
    // TODO: refactor
    private Class mStartFragment = Ca_MainFargment.class;
    private SlidingMenuListFragment mMenuListFragment;
    private ProgressDialog mProgressDialog = null;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        initActionBar();
//        initMenu(savedInstanceState);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Both setBehindContentView must be called in onCreate in addition to setContentView.
        setContentView(R.layout.slidingmenu_menu_container);
        setBehindContentView(findViewById(R.id.menu_frame) == null ?
                getLayoutInflater().inflate(R.layout.slidingmenu_menu_frame, null) : new View(this));

        initUI(savedInstanceState);
    }

    private void initUI(Bundle savedInstanceState) {
        initMenu(savedInstanceState);
        // FIXME!!!
        //        initContentTopMenuButton();
    }

    private void initMenu(Bundle savedInstanceState) {
        // check if the content frame contains the menu frame
        getSlidingMenu().setSlidingEnabled(true);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        Fragment fragment;

        // set the Above View Fragment
        if (!isBackStackEmpty()) {
            fragment = getCurrentFragment();
            mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } else {
            try {
                // TODO: refactor
                Constructor<?> ctor = mStartFragment.getConstructor();
                fragment = (Fragment) ctor.newInstance(new Object[]{});
            } catch (Exception e) {
                e.printStackTrace();
                fragment = new Ca_MainFargment();
            }
            putContentOnTop(fragment, true);
        }

        // set the Behind View Fragment
        mMenuListFragment = new SlidingMenuListFragment();
        mFragmentManager.beginTransaction().replace(R.id.menu_frame, mMenuListFragment).commit();

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.xxxxlarge_space);
        sm.setShadowWidthRes(R.dimen.xxsmall_space);
        sm.setShadowDrawable(R.drawable.shadow_slidingmenu);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);

        initFragmentTop(fragment, false);
    }

    public void refreshMenuItems() {
        ((SlidingMenuListFragment) mMenuListFragment).refreshMenuItems();
    }


    /**
     * getTestResult -> switchContent(TestResultsFargment)
     */
    public void getTestResult() {
//        mGetTestResultRequestId = getServiceHelper().executeCommand(new GetTestResults());
//        showProgressDialog();
    }

    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
//        super.onServiceCallback(requestId, requestIntent, resultCode, resultData);
//
//        if (getServiceHelper().check(requestIntent, GetTestResults.class)) {
//            if (resultCode == GetTestResults.RESPONSE_SUCCESS) {
//                hideProgressDialog();
//                storePreferences();
//                switchContent(new TestResultsFargment());
//                refreshMenuItems();
//            } else if (resultCode == GetTestResults.RESPONSE_PROGRESS) {
//                // do nothing
//            } else {
//                hideProgressDialog();
//                SlidingMenu slidingMenu = getSlidingMenu();
//                slidingMenu.showMenu();
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
//        appSharedPreferences.setPreference(AppSharedPreferences.PREFERENCES.results, appState.getTestResult().getAsJson().toString());
//        appSharedPreferences.save();
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