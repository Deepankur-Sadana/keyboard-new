package com.vingeapp.android;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.vingeapp.android.apiHandling.RequestManager;

import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.EventBusBuilder;

/**
 * Created by deepankur on 2/11/17.
 */

public class MasterClass extends Application {
    private final static String TAG = MasterClass.class.getSimpleName();
    private static WeakReference<Context> mContext;
    public static CallbackManager callbackManager;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        mContext = new WeakReference<Context>(this);
        RequestManager.initialize(this);
    }

    public static Context getGlobalContext() {
        return mContext.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        msConfig = new AskPrefsImpl();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);

    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public String getPackageName() {
        return super.getPackageName();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate: ");
    }


    private static AskPrefs msConfig;

    public static AskPrefs getConfig() {
        return msConfig;
    }
    private static EventBus mEventBus;

    public static EventBus getEventBus() {
        if (mEventBus == null)
            buildEventBus();
        return mEventBus;
    }

    private static void buildEventBus() {
        EventBusBuilder builder = EventBus.builder();
        builder.eventInheritance(true);
        builder.logNoSubscriberMessages(true);
        builder.logSubscriberExceptions(true);
        builder.sendNoSubscriberEvent(true);
        builder.sendSubscriberExceptionEvent(true);
        builder.throwSubscriberException(true);

        mEventBus = builder.build();
    }
}
