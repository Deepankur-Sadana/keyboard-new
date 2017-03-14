package com.vingeapp.android.customTextViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import utils.FontPicker;

/**
 * Created by deepankur on 3/12/17.
 */

public class OpenSansBold extends TextView {

    Typeface font;
    FontPicker fontPicker;

    public OpenSansBold(Context context) {
        super(context);
        init(context);
    }

    public OpenSansBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);


    }

    public OpenSansBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        fontPicker = FontPicker.getInstance(context);
        font = fontPicker.getOpenSansBold();
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
