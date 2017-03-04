package deepankur.com.keyboardapp.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class PackageChangedReceiver extends BroadcastReceiver{
    private final String TAG = getClass().getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ++++++++++++ ");
    }
}
