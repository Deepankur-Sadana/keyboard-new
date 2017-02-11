package deepankur.com.keyboardapp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by deepankur on 2/11/17.
 */

public class MasterClass extends Application {
    private final static String TAG = MasterClass.class.getSimpleName();
    private static WeakReference<Context> mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getGlobalContext() {
        return mContext.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
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
}
