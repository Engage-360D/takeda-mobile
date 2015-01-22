package ru.com.cardiomagnil.ui.slidingmenu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.base.BaseSlidingFragmentActivity;
import ru.com.cardiomagnil.ui.ca_base.Ca_BaseItemFragment;
import ru.com.cardiomagnil.ui.ca_content.Ca_MenuItem;

public class SlidingMenuActivity extends BaseSlidingFragmentActivity {
    private SlidingMenuListFragment mSlidingMenuListFragment;
    private ProgressDialog mProgressDialog = null;

    public static final int START_ITEM_POSITION = 0;
    public static final Ca_MenuItem[] MENU_ITEMS = new Ca_MenuItem[]{
            Ca_MenuItem.item_main,
            Ca_MenuItem.item_risk_analysis,
            Ca_MenuItem.item_diary,
            Ca_MenuItem.item_search_institutions,
            Ca_MenuItem.item_information,
            Ca_MenuItem.item_settings,
            Ca_MenuItem.item_recommendations,
            Ca_MenuItem.item_analysis_results,
            Ca_MenuItem.item_calendar,
            Ca_MenuItem.item_useful_to_know,
            Ca_MenuItem.item_publications,
            Ca_MenuItem.item_reports
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
    protected void initTopOnFragmentChanged(final Fragment fragment, boolean butonIsBack) {
        initMenuOrBackButton(butonIsBack);
        if (fragment instanceof Ca_BaseItemFragment) {
            ViewGroup layoutHeader = (ViewGroup) findViewById(R.id.layoutHeader);
            ((Ca_BaseItemFragment) fragment).initTopBar(layoutHeader);
        }
    }

    private void initMenuOrBackButton(boolean butonIsMenu) {
        View contentTopRightMenu = findViewById(R.id.contentTopRightMenu);
        View contentTopRightBack = findViewById(R.id.contentTopRightBack);

        contentTopRightMenu.setVisibility(butonIsMenu ? View.INVISIBLE : View.VISIBLE);
        contentTopRightBack.setVisibility(butonIsMenu ? View.VISIBLE : View.INVISIBLE);
    }

    private void initUI(Bundle savedInstanceState) {
        initMenu(savedInstanceState);
        initContentTopMenuButton();
    }

    private void initMenu(Bundle savedInstanceState) {
        // check if the content frame contains the menu frame
        getSlidingMenu().setSlidingEnabled(true);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        Fragment fragment = getCurrentFragment();

        // set the Above View Fragment
        if (fragment != null) {
            if (fragment.isDetached()) {
                mFragmentManager.beginTransaction().attach(fragment).commit();
            }
        } else {
            String fragmentClassName = SlidingMenuActivity.MENU_ITEMS[START_ITEM_POSITION].getItemClass().getName();
            fragment = Fragment.instantiate(this, fragmentClassName, null);
            replaceAllContent(fragment, true);
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
        slidingMenu.setBehindOffsetRes(R.dimen.ca_plate_xlarge);
        slidingMenu.setShadowWidthRes(R.dimen.ca_divider_1);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setBehindScrollScale(0.25f);
        slidingMenu.setFadeDegree(0.25f);
        slidingMenu.onFinishTemporaryDetach();
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

    public void refreshMenuItems() {
        ((SlidingMenuListFragment) mSlidingMenuListFragment).refreshMenuItems();
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