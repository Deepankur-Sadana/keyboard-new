package com.vingeapp.android.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import utils.AppLibrary;

/**
 * Created by deepankur on 2/6/17.
 */

public abstract class BaseActivity extends FragmentActivity {
    private static final int PERMISSION_ACCESS_CAMERA_MICROPHONE = 0;

    private boolean processingPermissions = false;

    /**
     * The method will ignore any further calls until the permissions are accepted/rejected by the system.
     * After which It will it will either take us to the settings or call {@link #onPermissionsGranted(String[])}
     */
    public void requestCameraPermissionsAndProceed() {
        if (processingPermissions) return;
        processingPermissions = true;

        if (!AppLibrary.allPermissionsGranted(this)) {

            if (currentPopCount < MAX_POPUP_ATTEMPTS) {
                ++currentPopCount;
                ActivityCompat.requestPermissions(this, AppLibrary.allPermissionsRequired,
                        PERMISSION_ACCESS_CAMERA_MICROPHONE);
            } else {
                takeMeToSettings("Contacts");
            }
        } else {
            this.onPermissionsGranted(null);
        }
    }

    private final String TAG = BaseActivity.class.getSimpleName();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        String s = "", g = "";
        for (String permission : permissions) {
            s += permission + " ";
        }
        for (int grantResult : grantResults) {
            g += grantResult + " ";
        }
        Log.d(TAG, "onRequestPermissionsResult: permissions " + s + " grantResult " + g);
        switch (requestCode) {
            case PERMISSION_ACCESS_CAMERA_MICROPHONE:
                processingPermissions = false;
                if (AppLibrary.verifyPermissions(grantResults)) {
                    this.onPermissionsGranted(null);
                } else {
                    requestCameraPermissionsAndProceed();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private final static int MAX_POPUP_ATTEMPTS = 3;
    private int currentPopCount = 0;


    private void takeMeToSettings(String permissionName) {
        Toast.makeText(this, "Please provide " + permissionName + " permissions in the settings ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    /**
     * @param permissions currently not being used as the method is only called
     *                    when {@link #android.Manifest.permission.CAMERA} &
     *                    {@link #Manifest.permission.RECORD_AUDIO} are granted
     */
    protected abstract void onPermissionsGranted(String[] permissions);
}
