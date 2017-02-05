package deepankur.com.keyboardapp.customViews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import deepankur.com.keyboardapp.enums.KeyBoardOptions;
import utils.AppLibrary;

/**
 * Created by deepankur on 2/6/17.
 */

public class TabStripView extends LinearLayout {
    public TabStripView(Context context) {
        super(context);
        init(context);
    }

    public TabStripView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabStripView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private KeyBoardOptions[] keyBoardOptions = KeyBoardOptions.values();

    private void init(Context context) {
        int pixel = (int) AppLibrary.convertDpToPixel(8, context);
        this.setPadding(pixel, pixel, pixel, pixel);
        for (int i = 0; i < keyBoardOptions.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(String.valueOf(i + 1));
            textView.setPadding(pixel, pixel, pixel, pixel);
            textView.setTextColor(Color.WHITE);
            textView.setTag(keyBoardOptions[i]);
            this.addView(textView);
        }
    }
}
