package ru.com.cardiomagnyl.ui.slidingmenu.menu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.ui.base.BaseSlidingFragmentActivity;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;

public class SlidingMenuActivity extends BaseSlidingFragmentActivity {
    private SlidingMenuListFragment mSlidingMenuListFragment;
    private ProgressDialog mProgressDialog = null;

    private static final int MAIN_ITEM_POSITION = 0;
    private static final int TEST_ITEM_POSITION = 1;
    public static final MenuItem[] MENU_ITEMS = new MenuItem[]{
            MenuItem.item_main,
            MenuItem.item_risk_analysis,
            MenuItem.item_search_institutions,
            MenuItem.item_recommendations,
            MenuItem.item_analysis_results,
            MenuItem.item_journal,
            MenuItem.item_useful_to_know,
            MenuItem.item_publications,
            MenuItem.item_reports
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Both setBehindContentView must be called in onCreate in addition to setContentView.
        setContentView(R.layout.slidingmenu_menu_container);
        if (findViewById(R.id.menu_frame) == null) {
            setBehindContentView(getLayoutInflater().inflate(R.layout.slidingmenu_menu_frame, null));
        }

        initUI(savedInstanceState);
    }

    @Override
    protected void initTopOnFragmentChanged(final Fragment fragment, boolean buttonIsBack) {
        initMenuOrBackButton(buttonIsBack);
        if (fragment instanceof BaseItemFragment) {
            ViewGroup layoutHeader = (ViewGroup) findViewById(R.id.layoutHeader);
            ((BaseItemFragment) fragment).initTopBar(layoutHeader);
        }
    }

    private void initMenuOrBackButton(boolean buttonIsMenu) {
        View contentTopRightMenu = findViewById(R.id.contentTopRightMenu);
        View contentTopRightBack = findViewById(R.id.contentTopRightBack);

        contentTopRightMenu.setVisibility(buttonIsMenu ? View.INVISIBLE : View.VISIBLE);
        contentTopRightBack.setVisibility(buttonIsMenu ? View.VISIBLE : View.INVISIBLE);
    }

    private void initUI(Bundle savedInstanceState) {
        initMenu(savedInstanceState);
        initContentTopMenuButton();
    }

    private void initMenu(Bundle savedInstanceState) {
        // check if the content frame contains the menu frame
        getSlidingMenu().setSlidingEnabled(true);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        // set the Above View Fragment
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            String firstFragmentClassName = SlidingMenuActivity.MENU_ITEMS[getFistItem()].getItemClass().getName();
            fragment = Fragment.instantiate(this, firstFragmentClassName, null);
            replaceAllContent(fragment, true);
        } else {
            if (fragment.isDetached()) {
                mFragmentManager.beginTransaction().attach(fragment).commit();
            }
        }
        initTopOnFragmentChanged(fragment, false);

        mSlidingMenuListFragment = (SlidingMenuListFragment) mFragmentManager.findFragmentByTag(SlidingMenuListFragment.class.getName());
        if (mSlidingMenuListFragment == null) {
            mSlidingMenuListFragment = new SlidingMenuListFragment();
            mFragmentManager.beginTransaction().replace(R.id.menu_frame, mSlidingMenuListFragment, SlidingMenuListFragment.class.getName()).commit();
        } else if (mSlidingMenuListFragment.isDetached()) {
            mFragmentManager.beginTransaction().attach(fragment).commit();
        }

        // customize the SlidingMenu
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setBehindOffsetRes(R.dimen.plate_xlarge);
        slidingMenu.setShadowWidthRes(R.dimen.divider_1);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setBehindScrollScale(0.25f);
        slidingMenu.setFadeDegree(0.25f);
        slidingMenu.onFinishTemporaryDetach();

        lockMenuIfneed();
        unlockItemsIfneed();
    }

    private void lockMenuIfneed() {
        TestResult testResult = AppState.getInsnatce().getTestResult();

        if (testResult != null && !TextUtils.isEmpty(testResult.getId())) {
            unLockMenu();
        } else {
            lockMenu();
        }
    }

    private void unlockItemsIfneed() {
        TestResult testResult = AppState.getInsnatce().getTestResult();

        if (testResult != null && !TextUtils.isEmpty(testResult.getId())) {
            MenuItem.item_analysis_results.setItemIsEnabled(true);
            MenuItem.item_analysis_results.setItemIsVisible(true);
        }
    }

    public static int getFistItem() {
        TestResult testResult = AppState.getInsnatce().getTestResult();
        return (testResult != null && !TextUtils.isEmpty(testResult.getId())) ? MAIN_ITEM_POSITION : TEST_ITEM_POSITION;
    }

    private void initContentTopMenuButton() {
        View contentTopRightClickable = findViewById(R.id.contentTopRightClickable);

        contentTopRightClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLocked()) {
                    return;
                }

                if (!isBackStackEmpty()) {
                    makeContentStepBack(true);
                } else {
                    toggle();
                }
            }
        });

        initMenuOrBackButton(!isBackStackEmpty());
    }

    public void unselectCurrentItem() {
         mSlidingMenuListFragment.unselectCurrentItem();
    }

    public void refreshMenuItems() {
        mSlidingMenuListFragment.refreshMenuItems();
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