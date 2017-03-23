package com.vingeapp.android.keyboardCustomViews;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    Context context;


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
            , KeyBoardOptions.CONTACTS, KeyBoardOptions.MAPS, KeyBoardOptions.MY_PROFILE
    };

    private void init(Context context) {
        this.context = context;
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
        return (int) AppLibrary.convertDpToPixel(1, getContext());
    }

    private int getResourceIdForTabStrip(KeyBoardOptions option) {
        switch (option) {
            case CONTACTS:
                return R.drawable.contacts;
            case CLIP_BOARD:
                return R.drawable.ic_clipboard;
            case QWERTY:
                return R.drawable.ic_aa;
            case MAPS:
                return R.drawable.ic_maps;
            case MY_PROFILE:
                return android.R.drawable.ic_menu_add;
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

    final String TAG = getClass().getSimpleName();

    private void notifyItemClicked(View v, KeyBoardOptions keyBoardOptions) {
        KeyBoardOptions tag = (KeyBoardOptions) v.getTag();
        if (tag == mCurrentKeyboardOption)
            return;

        if (isViewLocked(tag)) {
            // Log.d(TAG, "notifyItemClicked: view is locked somehow, ignoring the click");
            return;
        }
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
                break;
//            case MAPS:
//                EventBus.getDefault().post(new MessageEvent(POPUP_KEYBOARD_FOR_IN_APP_EDITING, null));
//                break;
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

    /**
     * @param keyBoardOptions the option on which the user has clicked
     * @return true if we can't open the option due to some permissions and all,
     * false if everything is OK
     */
    private boolean isViewLocked(KeyBoardOptions keyBoardOptions) {
        if (keyBoardOptions == KeyBoardOptions.CONTACTS) {
            if (!AppLibrary.isPerMissionGranted(context, Manifest.permission.READ_CONTACTS)) {//checking if the contacts permission is granted or not
                Toast.makeText(context, "Please provide Contacts permissions in the settings ", Toast.LENGTH_SHORT).show();
                takeMeToSettings("contacts");
                return true;
            }

        } else if (keyBoardOptions == KeyBoardOptions.MAPS) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//                if (!AppLibrary.isPerMissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION) && !AppLibrary.isPerMissionGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(context, "Please provide Your location permissions in the settings ", Toast.LENGTH_SHORT).show();
                takeMeToSettings("contacts");
                return true;
            }
        }
        return false;
    }

    private void takeMeToSettings(String permissionName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

