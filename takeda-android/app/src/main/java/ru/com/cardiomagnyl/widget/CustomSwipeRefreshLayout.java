package ru.com.cardiomagnyl.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {
    public CustomSwipeRefreshLayout(Context context) {
        super(context);
        initCustomSwipeRefreshLayout();
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCustomSwipeRefreshLayout();
    }

    private void initCustomSwipeRefreshLayout() {
        if (isInEditMode()) return;

        setSize(SwipeRefreshLayout.LARGE);
        setDistanceToTriggerSync(150);
    }
}
