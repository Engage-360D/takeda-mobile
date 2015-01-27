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
import ru.com.cardiomagnil.util.Utils;
import ru.com.cardiomagnil.widget.CustomAnimation;
import ru.com.cardiomagnil.widget.CustomAnimation.OnAnimationEndListener;
import ru.com.cardiomagnil.util.TestMethods;

public class StartActivity extends BaseTrackedFragmentActivity {
    private ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initStartActivity();
//        customizeIfDebug();
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

    public void startSlidingMenu() {
        Intent intent = new Intent(StartActivity.this, SlidingMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void showProgressDialog() {
        Utils.hideKeyboard(this);

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
                customizeStartActivityAccordingCurrentFragment(customFragmentPagerAdapter, position);
            }
        });

        pager.setCurrentItem(0);
        ((BaseStartFragment) customFragmentPagerAdapter.getItem(0)).initParent(StartActivity.this);
    }

    private void customizeStartActivityAccordingCurrentFragment(CustomFragmentPagerAdapter customFragmentPagerAdapter, int position) {
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
}
