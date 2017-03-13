package com.vingeapp.android.firebase;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.vingeapp.android.BuildConfig;
import com.vingeapp.android.R;

import utils.AppLibrary;

/**
 * Created by deepankursadana on 13/03/17.
 */

public class FireBaseHelper {
    private static FireBaseHelper sFireBaseHelper = new FireBaseHelper();
    private Context mContext;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public static synchronized FireBaseHelper getInstance(Context context) {
        String TAG = "FireBaseHelper";
        if (sFireBaseHelper == null) {
            sFireBaseHelper = new FireBaseHelper(context.getApplicationContext());
            Log.i(TAG, "initialized fireBase ");
        }
        return sFireBaseHelper;
    }

    private DatabaseReference mFireBase;


    private FireBaseHelper() {
    }

    private static boolean initializationDone;

    private FireBaseHelper(Context context) {
        this.mContext = context;
        if (mFireBase == null) {

            if (!initializationDone) {
                if (!AppLibrary.PRODUCTION_MODE)
                    FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);

                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            }

            mFireBase = FirebaseDatabase.getInstance().getReferenceFromUrl(FireBaseKEYIDS.fireBaseURL);
            initializationDone = true;
        }

        if (mFirebaseRemoteConfig == null) {
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            if (!AppLibrary.PRODUCTION_MODE) {
                FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(BuildConfig.DEBUG)
                        .build();
                mFirebaseRemoteConfig.setConfigSettings(configSettings);
            }
//            mFirebaseRemoteConfig.setDefaults(R.xml.app_defaults);
//            fetchServerValues();
        }

//        if (firebaseHelperThread == null) {
//            firebaseHelperThread = new HandlerThread("cameraStickerThread");
//            firebaseHelperThread.start();
//        }
//        if (firebaseHelperHandler == null)
//            firebaseHelperHandler = new Handler(firebaseHelperThread.getLooper());
//
//        if (getMyUserId() == null) return;
//
//
//        loadData();

    }

}
