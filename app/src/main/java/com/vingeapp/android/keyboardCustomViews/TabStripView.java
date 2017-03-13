package com.vingeapp.android.keyboardCustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vingeapp.android.MessageEvent;
import com.vingeapp.android.R;
import com.vingeapp.android.enums.KeyBoardOptions;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;

import de.greenrobot.event.EventBus;
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

    private KeyBoardOptions[] keyBoardOptions = {KeyBoardOptions.QWERTY, KeyBoardOptions.FAVORITE_APPS, KeyBoardOptions.CLIP_BOARD
            , KeyBoardOptions.CONTACTS
    };

    private void init(Context context) {
        EventBus.getDefault().register(this);
        int pixel = (int) AppLibrary.convertDpToPixel(8, context);
        this.setGravity(Gravity.CENTER);
        for (int i = 0; i < keyBoardOptions.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setPadding(pixel, pixel, pixel, pixel);
            imageView.setImageResource(getResourceIdForTabStrip(keyBoardOptions[i]));
            imageView.setTag(keyBoardOptions[i]);
            imageView.setOnClickListener(onClickListener);
            imageView.setAlpha(keyBoardOptions[i] == KeyBoardOptions.QWERTY ? 0.9f : 0.2f);
            this.addView(imageView);
            ((LayoutParams) imageView.getLayoutParams()).leftMargin = getMargin(i);
            ((LayoutParams) imageView.getLayoutParams()).rightMargin = getMargin(i);
        }
    }

    int getMargin(int index) {
        return (int) AppLibrary.convertDpToPixel(23, getContext());
    }

    private int getResourceIdForTabStrip(KeyBoardOptions option) {
        switch (option) {
            case CONTACTS:
                return R.drawable.ic_wizard_contacts_on;
            case CLIP_BOARD:
                return R.drawable.ic_clipboard;
            case QWERTY:
                return R.drawable.ic_aa;
            default:
                return android.R.drawable.ic_menu_add;

        }
    }

    public void onEvent(MessageEvent event) {
        if (event.getMessageType() == SWITCH_TO_QWERTY) {
            switchToQwertyMode();
        }
    }

    public void switchToQwertyMode() {
        View view = TabStripView.this.findViewWithTag(KeyBoardOptions.QWERTY);
        notifyItemClicked(view, (KeyBoardOptions) view.getTag());
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
            ImageView imageView = (ImageView) TabStripView.this.getChildAt(i);
            KeyBoardOptions tag1 = (KeyBoardOptions) imageView.getTag();
            imageView.setAlpha(tag == tag1 ? 0.9f : 0.2f);
        }
        switch (tag) {
            case CONTACTS:
                EventBus.getDefault().post(new MessageEvent(POPUP_KEYBOARD_FOR_IN_APP_EDITING, null));
            default:
                break;
        }
    }

    public KeyBoardOptions getmCurrentKeyboardOption() {
        return mCurrentKeyboardOption;
    }

    private OnOptionClickedListener onOptionClickedListener;

    public void setOnOptionClickedListener(OnOptionClickedListener onOptionClickedListener) {
        this.onOptionClickedListener = onOptionClickedListener;
    }

    interface OnOptionClickedListener {
        void onOptionClicked(KeyBoardOptions keyBoardOptions);
    }
}
