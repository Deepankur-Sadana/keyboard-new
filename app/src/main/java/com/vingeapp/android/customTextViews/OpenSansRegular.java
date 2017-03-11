package com.vingeapp.android.customTextViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import utils.FontPicker;

/**
 * Created by deepankur on 3/11/17.
 */

public class OpenSansRegular extends TextView {


    Typeface font;
    FontPicker fontPicker;

    public OpenSansRegular(Context context) {
        super(context);
        init(context);
    }

    public OpenSansRegular(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);


    }

    public OpenSansRegular(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        fontPicker = FontPicker.getInstance(context);
        font = fontPicker.getOpenSansRegular();
        this.setTypeface(font);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        tf = font;
        super.setTypeface(tf, style);
    }

    @Override
    public void setTypeface(Typeface tf) {
        tf = font;
        super.setTypeface(tf);

    }
}
