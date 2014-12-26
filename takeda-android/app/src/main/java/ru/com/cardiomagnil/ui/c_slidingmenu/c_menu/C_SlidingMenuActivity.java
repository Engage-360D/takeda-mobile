package ru.com.cardiomagnil.ui.c_slidingmenu.c_menu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.c_base.C_BaseArgumens;
import ru.com.cardiomagnil.ui.c_base.C_BaseSlidingFragmentActivity;
import ru.com.cardiomagnil.ui.c_slidingmenu.c_content.C_StartFragment;


public class C_SlidingMenuActivity extends C_BaseSlidingFragmentActivity {
    private C_MenuItems mStartFragment;
    private final long PENDING_TIME = 500;
    private AtomicBoolean mPending = new AtomicBoolean(false);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        int startItemIndex = C_MenuItems.item_start.ordinal();
        if (bundle != null) {
            startItemIndex = bundle.getInt(C_BaseArgumens.START_FRAGMENT, C_MenuItems.item_start.ordinal());
        }
        mStartFragment = C_MenuItems.values()[startItemIndex];

        // Both setBehindContentView must be called in onCreate in addition to setContentView.
        setContentView(R.layout.slidingmenu_menu_container);
        setBehindContentView(findViewById(R.id.menu_frame) == null ? getLayoutInflater().inflate(R.layout.c_slidingmenu_menu_frame, null) : new View(this));

        initUI(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
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

    private void initUI(Bundle savedInstanceState) {
        initMenu(savedInstanceState);
        initContentTopMenuButton();
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
                Constructor<?> ctor = mStartFragment.getValue().getConstructor();
                fragment = (Fragment) ctor.newInstance(new Object[]{});
            } catch (Exception e) {
                e.printStackTrace();
                fragment = new C_StartFragment();
            }
            putContentOnTop(fragment, true);
        }

        // set the Behind View Fragment
        C_SlidingmenuMenuFragment menuFragment = new C_SlidingmenuMenuFragment();
        mFragmentManager.beginTransaction().replace(R.id.menu_frame, menuFragment).commit();

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setShadowWidthRes(R.dimen.c_slidingmenu_shadow_width);
        sm.setShadowDrawable(R.drawable.shadow_slidingmenu);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);

        initFragmentTop(fragment, false);
    }

    // when top has 'back' button instead 'menu'
    private void initContentTopMenuButton() {
        ImageView buttonContentTopMenu = (ImageView) findViewById(R.id.contentTopMenu);

//        buttonContentTopMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isBackStackEmpty()) {
//                    makeContentStepBack(true);
//                } else {
//                    toggle();
//                }
//            }
//        });
    }

    public static C_SlidingMenuActivity getSlidingMenuActivity(Context context) {
        C_SlidingMenuActivity slidingMenuActivity = null;
        if (context != null && context instanceof C_SlidingMenuActivity) {
            slidingMenuActivity = (C_SlidingMenuActivity) context;
        }
        return slidingMenuActivity;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            toggle();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
