package ru.com.cardiomagnil.ui.start;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class CustomAnimation {
    private final Animation mAnimation;
    private final View mView;
    private final OnAnimationStartListener mOnAnimationStartListener;
    private final OnAnimationRepeatListener mOnAnimationRepeatListener;
    private final OnAnimationEndListener mOnAnimationEndListener;
    private final AnimationListener mAnimationListener;

    public static class Builder {
        // required parameters
        private final Animation mAnimation;
        private final View mView;

        // additional parameters
        private OnAnimationStartListener mOnAnimationStartListener;
        private OnAnimationRepeatListener mOnAnimationRepeatListener;
        private OnAnimationEndListener mOnAnimationEndListener;

        public Builder(Animation animation, View view) {
            this.mAnimation = animation;
            this.mView = view;
        }

        public Builder setOnAnimationStartListener(OnAnimationStartListener onAnimationStartListener) {
            this.mOnAnimationStartListener = onAnimationStartListener;
            return this;
        }

        public Builder setOnAnimationRepeatListener(OnAnimationRepeatListener onAnimationRepeatListener) {
            this.mOnAnimationRepeatListener = onAnimationRepeatListener;
            return this;
        }

        public Builder setOnAnimationEndListener(OnAnimationEndListener onAnimationEndListener) {
            this.mOnAnimationEndListener = onAnimationEndListener;
            return this;
        }

        public CustomAnimation build() {
            return new CustomAnimation(this);
        }
    }

    public CustomAnimation(Builder builder) {
        mAnimation = builder.mAnimation;
        mView = builder.mView;
        mOnAnimationStartListener = builder.mOnAnimationStartListener;
        mOnAnimationRepeatListener = builder.mOnAnimationRepeatListener;
        mOnAnimationEndListener = builder.mOnAnimationEndListener;

        mAnimationListener = new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (mOnAnimationStartListener != null) {
                    mOnAnimationStartListener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (mOnAnimationRepeatListener != null) {
                    mOnAnimationRepeatListener.onAnimationRepeat(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mOnAnimationEndListener != null) {
                    mOnAnimationEndListener.onAnimationEnd(animation);
                }
            }
        };

        mAnimation.setAnimationListener(mAnimationListener);
    }

    public void startAnimation() {
        mView.startAnimation(mAnimation);
    }

    public Animation getAnimation() {
        return mAnimation;
    }

    public View getView() {
        return mView;
    }

    public AnimationListener getAnimationListener() {
        return mAnimationListener;
    }

    public interface OnAnimationStartListener {
        void onAnimationStart(Animation animation);
    }

    public interface OnAnimationRepeatListener {
        void onAnimationRepeat(Animation animation);
    }

    public interface OnAnimationEndListener {
        void onAnimationEnd(Animation animation);
    }
}