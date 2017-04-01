package com.vingeapp.android.keyboardCustomViews;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.vingeapp.android.R;

/**
 * Created by divisha on 3/25/17.
 */


public class SettingsView extends FrameLayout {

    private Context context;
    private static final String TAG = SettingsView.class.getSimpleName();
    private View rootView;
    private SeekBar seekBar;

    public SettingsView(@NonNull Context context) {
        super(context);
        init(context);
    }


    public SettingsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SettingsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private int brightness;


    private void init(final Context context) {

        this.context = context;
        rootView = inflate(context, R.layout.settings, null);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        this.addView(rootView);

        seekBar.setMax(255);
        seekBar.setKeyProgressIncrement(1);
//        getScreenBrightness();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged:  progress: " + progress + " fromUser " + fromUser);
//                if (progress <= 20) {
//                    brightness = 20;
//                } else {
//                    brightness = progress;
//                }
//                float perc = (brightness / (float) 255) * 100;
//                txtPerc.setText((int) perc + " %");
                boolean b = Settings.System.putInt(context.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, progress);
                Log.d(TAG, "onProgressChanged: "+b);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch: ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch: ");
                getScreenBrightness();

            }
        });

    }

    void getScreenBrightness() {
        try {
            int oldBrightness = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);

            int previousScreenBrightness = Settings.System.getInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS);

            Log.d(TAG, "getScreenBrightness: " + previousScreenBrightness);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }
}
