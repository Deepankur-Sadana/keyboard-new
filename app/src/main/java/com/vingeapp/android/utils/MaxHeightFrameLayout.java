package com.vingeapp.android.utils;

/**
 * Created by deepankursadana on 14/03/17.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.vingeapp.android.R;


/**
 * Created by deepankur on 3/5/16.
 * <p>
 * Helper Class to set an attribute MaxHeight to a FrameLayout
 */
public class MaxHeightFrameLayout extends FrameLayout {
    private int mMaxHeight;

    public void setmMaxHeight(int mMaxHeight) {
        this.mMaxHeight = mMaxHeight;
    }

    public MaxHeightFrameLayout(Context context) {
        super(context);
        mMaxHeight = 0;
    }

    public MaxHeightFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MaxHeightLinearLayout);
        mMaxHeight = a.getDimensionPixelSize(R.styleable.MaxHeightLinearLayout__max_height, Integer.MAX_VALUE);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (mMaxHeight > 0 && mMaxHeight < measuredHeight) {
            int measureMode = MeasureSpec.getMode(heightMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, measureMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}