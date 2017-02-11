package deepankur.com.keyboardapp;

import android.app.Application;
import android.util.Log;

/**
 * Created by deepankur on 2/11/17.
 */

public class MasterClass extends Application {
    private final static String TAG = MasterClass.class.getSimpleName();
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
