package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import deepankur.com.keyboardapp.MessageEvent;
import deepankur.com.keyboardapp.enums.KeyBoardOptions;
import deepankur.com.keyboardapp.interfaces.GreenBotMessageKeyIds;
import utils.AppLibrary;

/**
 * Created by deepankur on 2/6/17.
 */

public class TabStripView extends LinearLayout implements GreenBotMessageKeyIds {
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

    private KeyBoardOptions[] keyBoardOptions = {KeyBoardOptions.QWERTY, KeyBoardOptions.FAVORITE_APPS, KeyBoardOptions.CLIP_BOARD};

    private void init(Context context) {
        EventBus.getDefault().register(this);
        int pixel = (int) AppLibrary.convertDpToPixel(8, context);
        this.setPadding(pixel, pixel, pixel, pixel);
        for (int i = 0; i < keyBoardOptions.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(String.valueOf(i + 1));
            textView.setPadding(pixel, pixel, pixel, pixel);
            textView.setTextColor(Color.WHITE);
            textView.setTag(keyBoardOptions[i]);
            textView.setOnClickListener(onClickListener);
            this.addView(textView);
        }
    }

    public void onEvent(MessageEvent event) {
        if (event.getMessageType() == SWITCH_TO_QWERTY) {
            View view = TabStripView.this.findViewWithTag(KeyBoardOptions.QWERTY);
            notifyItemClicked(view, (KeyBoardOptions) view.getTag());
        }
    }

    private KeyBoardOptions mCurrentKeyboardOption = KeyBoardOptions.QWERTY;
    private View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            notifyItemClicked(v, (KeyBoardOptions) v.getTag());
        }
    };

    private void notifyItemClicked(View v, KeyBoardOptions keyBoardOptions) {
        KeyBoardOptions tag = (KeyBoardOptions) v.getTag();
        if (tag == mCurrentKeyboardOption)
            return;
        mCurrentKeyboardOption = tag;
        if (onOptionClickedListener != null)
            onOptionClickedListener.onOptionClicked(tag);
        for (int i = 0; i < TabStripView.this.getChildCount(); i++) {
            TextView textView = (TextView) TabStripView.this.getChildAt(i);
            KeyBoardOptions tag1 = (KeyBoardOptions) textView.getTag();
            textView.setTextColor(tag == tag1 ? Color.BLUE : Color.WHITE);
        }
        switch (tag) {
            case FAVORITE_APPS:
                break;
            case CONTACTS:
            case CAMERA:
            case LOCATION:
            default:
                break;
        }
    }

    private OnOptionClickedListener onOptionClickedListener;

    public void setOnOptionClickedListener(OnOptionClickedListener onOptionClickedListener) {
        this.onOptionClickedListener = onOptionClickedListener;
    }

    public interface OnOptionClickedListener {
        void onOptionClicked(KeyBoardOptions keyBoardOptions);
    }
}
