package deepankur.com.keyboardapp.models;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import deepankur.com.keyboardapp.adapters.FavouriteApplicationsListAdapter;

/**
 * Created by deepankur on 2/7/17.
 */
public class PInfo {
    public String appname = "";
    public String pname = "";
    public String versionName = "";
    public int versionCode = 0;
    public Drawable icon;
    public boolean isChecked = false;

    @SuppressLint("LongLogTag")
    public void prettyPrint() {
//        Log.d("PInfo", appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
    }
}
