package com.cardiomagnil.ui.slidingmenu;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.ViewGroup;

import com.cardiomagnil.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class SlidingMenuActivity extends SlidingFragmentActivity {

    protected ListFragment mFrag;
    private Fragment mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();
        initMenu(savedInstanceState);
    }

    private void initMenu(Bundle savedInstanceState) {
        // set the Behind View
        setBehindContentView(R.layout.menu_fragment);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mFrag = new MenuListFragment();
            t.replace(R.id.menu_frame, mFrag);
            t.commit();
        } else {
            mFrag = (ListFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
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
        setContentView(R.layout.content_container);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new StartContentFragment()).commit();

        setSlidingActionBarEnabled(true);
    }

    private void initActionBar() {
        ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.custom_action_bar, null);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.action_bar_background));
    }

    public void switchContent(final Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                getSlidingMenu().showContent();
            }
        }, 50);
    }
}
