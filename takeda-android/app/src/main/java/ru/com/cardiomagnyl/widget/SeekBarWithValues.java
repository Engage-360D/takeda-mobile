package ru.com.cardiomagnyl.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import ru.com.cardiomagnyl.app.R;

public class SeekBarWithValues extends RelativeLayout {
    private int mMin;
    private int mMax;
    private int mMaxValue;
    private int mCurrentValue;
    private TextView mTextViewMinValue;
    private TextView mTextViewMaxValue;
    private TextView mTextViewCurrentValue;
    private CustomSeekBar mSeekBar;
    private OnProgressChangedListener mOnProgressChangedListener;

    public SeekBarWithValues(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SeekBarWithValues, 0, 0);

        try {
            mMin = typedArray.getInt(R.styleable.SeekBarWithValues_min, 0);
            mMax = typedArray.getInt(R.styleable.SeekBarWithValues_max, 0);
        } finally {
            typedArray.recycle();
        }

        initSeekBarWithValues();
    }

    private void initSeekBarWithValues() {
        LayoutInflater.from(getContext()).inflate(R.layout.seek_bar_with_values, this);

        mTextViewMinValue = (TextView) findViewById(R.id.textViewMinValue);
        mTextViewMaxValue = (TextView) findViewById(R.id.textViewMaxValue);
        mTextViewCurrentValue = (TextView) findViewById(R.id.textViewCurrentValue);
        mSeekBar = (CustomSeekBar) findViewById(R.id.customSeekBar);

        initValues();

        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mCurrentValue = progress;
                Rect thumbRect = ((CustomSeekBar) seekBar).getThumb().getBounds();
                updateCurrentText(mCurrentValue, thumbRect.right - thumbRect.left);

                if (mOnProgressChangedListener != null) {
                    mOnProgressChangedListener.onProgressChanged(seekBar, getActualValue(), fromUser);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        mSeekBar.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // unregister listener (this is important)
                        mSeekBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        Rect thumbRect = mSeekBar.getThumb().getBounds();
                        updateCurrentText(mCurrentValue, thumbRect.right - thumbRect.left);
                    }
                });
    }

    public int getMin() {
        return mMin;
    }

    public void setMin(int min) {
        mMin = min;
        invalidate();
        requestLayout();
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        mMax = max;
        invalidate();
        requestLayout();
    }

    public int getActualValue() {
        return mMin + mCurrentValue;
    }

    public void setActualValue(int actualValue) {
        mCurrentValue = actualValue - mMin;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            initValues();
            Rect thumbRect = mSeekBar.getThumb().getBounds();
            updateCurrentText(mCurrentValue, thumbRect.right - thumbRect.left);
        }
    }

    // initial moving of the mTextViewCurrentValue on proper position
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (hasWindowFocus) {
            Rect thumbRect = mSeekBar.getThumb().getBounds();
            updateCurrentText(mCurrentValue, thumbRect.right - thumbRect.left);
        }
    }

    private void initValues() {
        mMaxValue = Math.abs(mMax - mMin);
        mCurrentValue = mMaxValue / 2;
        mTextViewMinValue.setText(String.valueOf(mMin));
        mTextViewMaxValue.setText(String.valueOf(mMax));
        mTextViewCurrentValue.setText(String.valueOf(mMin + mCurrentValue));
        mSeekBar.setProgress(mCurrentValue);
        mSeekBar.setMax(mMaxValue);
    }

    public void updateCurrentText(int newProgress, int thumbWidth) {
        mTextViewCurrentValue.setText(String.valueOf(mMin + newProgress));
        final int padding = mTextViewMinValue.getWidth() + mSeekBar.getPaddingLeft();
        final int totalSeekWidth = mSeekBar.getWidth() - thumbWidth;
        final RelativeLayout.LayoutParams lp = (LayoutParams) mTextViewCurrentValue.getLayoutParams();
        final int seekLocation = (newProgress * totalSeekWidth) / mMaxValue - mTextViewCurrentValue.getWidth() / 2;
        lp.leftMargin = seekLocation + padding;
        mTextViewCurrentValue.setLayoutParams(lp);

        requestLayout();
    }

    public SeekBar getSeekBar() {
        return mSeekBar;
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(SeekBar seekBar, int actualProgress, boolean fromUser);
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        mOnProgressChangedListener = onProgressChangedListener;
    }
}
