package com.vingeapp.android.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vingeapp.android.R;

import utils.AppLibrary;

/**
 * Created by deepankursadana on 14/03/17.
 */

public class PagerDots extends LinearLayout {
    public PagerDots(Context context) {
        super(context);
    }

    public PagerDots(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerDots(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //    private ViewPager viewPager;
    private final String TAG = getClass().getSimpleName();

    public void setViewPager(ViewPager viewPager) {
        if (viewPager == null) throw new NullPointerException();
//        this.viewPager = viewPager;
        this.removeAllViews();
        int childCount = viewPager.getAdapter().getCount();
        Log.d(TAG, "setViewPager: childCount " + childCount);
        int margin = AppLibrary.convertDpToPixels(getContext(), 5);
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = new ImageView(getContext());
            this.addView(imageView);
            ((LayoutParams) imageView.getLayoutParams()).leftMargin = margin;
            ((LayoutParams) imageView.getLayoutParams()).rightMargin = margin;
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected " + position);
                onItemSelected(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged " + state);
            }
        });
    }


    public void onItemSelected(int position) {
        for (int i = 0; i < this.getChildCount(); i++) {
            ((ImageView) this.getChildAt(i)).setImageResource(i == position ? R.drawable.ellipse_1 : R.drawable.ellipse_3);
        }
    }
}
