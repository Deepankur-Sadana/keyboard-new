package com.vingeapp.android.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class PreferencesManager implements PrefsKeyIds {

    private static final String PREF_NAME = "com.example.app.PREF_NAME";
    private static final String KEY_VALUE = "com.example.app.KEY_VALUE";

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    public static synchronized PreferencesManager getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
        return sInstance;
    }

    public void setValue(long value) {
        mPref.edit().putLong(KEY_VALUE, value).commit();
    }

    public long getValue() {
        return mPref.getLong(KEY_VALUE, 0);
    }


    public boolean clear() {
        return mPref.edit().clear().commit();
    }


    public void addPackageName(Context context, Set<String> packageName) {
        // parse Preference file
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Set<String> set = preferences.getStringSet(PREFERRED_APPLICATIONS_LIST, null);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(PREFERRED_APPLICATIONS_LIST, packageName);
        editor.commit();

    }

    public Set<String> getAllProfferedApplications(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getStringSet(PREFERRED_APPLICATIONS_LIST, new HashSet<String>());
    }
}
