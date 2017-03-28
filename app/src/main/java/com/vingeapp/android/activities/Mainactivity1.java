//package com.vingeapp.android.activities;
//
//import android.app.Activity;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.provider.Settings.System;
//import android.util.Log;
//import android.view.Window;
//import android.view.WindowManager.LayoutParams;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.TextView;
//
//import com.vingeapp.android.R;
//
///**
// * Created by divisha on 3/25/17.
// */
//
//
//public class Mainactivity1 extends Activity {
//
//    private SeekBar seekBar;
//    private int brightness;
//    private ContentResolver contentResolver;
//    private Window window;
//    TextView txtPerc;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.settings);
//        seekBar = (SeekBar) findViewById(R.id.seekBar);
//        txtPerc = (TextView) findViewById(R.id.txtPercentage);
//
//        contentResolver = getContentResolver();
//        window = getWindow();
//
//        seekBar.setMax(255);
//        seekBar.setKeyProgressIncrement(1);
//
//        try {
//            brightness = System.getInt(contentResolver, System.SCREEN_BRIGHTNESS);
//        } catch (Settings.SettingNotFoundException e) {
//            Log.d("Error", "cannot access system brightness");
//            e.printStackTrace();
//        }
//        seekBar.setProgress(brightness);
//        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//                System.putInt(contentResolver, System.SCREEN_BRIGHTNESS, brightness);
//                LayoutParams layoutpars = window.getAttributes();
//                layoutpars.screenBrightness = brightness / (float) 255;
//                window.setAttributes(layoutpars);
//
//            }
//
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (progress <= 20) {
//                    brightness = 20;
//                } else {
//                    brightness = progress;
//                }
//                float perc = (brightness / (float) 255) * 100;
//                txtPerc.setText((int) perc + " %");
//            }
//        });
//    }
//
//
//}