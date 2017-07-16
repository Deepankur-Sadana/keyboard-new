package com.vingeapp.android.activities;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.vingeapp.android.MasterClass;
import com.vingeapp.android.MessageEvent;
import com.vingeapp.android.interfaces.AsyncListener;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.models.PInfo;
import com.vingeapp.android.preferences.PreferencesManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * async code for fetching all the installed app details from the device.
 */
class GetPackageTask extends AsyncTask<Void, Void, Void> implements GreenBotMessageKeyIds {

    private static final String TAG = GetPackageTask.class.getSimpleName();
    private Context context;
    private ArrayList<PInfo> pInfos;
    private AsyncListener asyncListener;

    GetPackageTask(Context context, ArrayList<PInfo> pInfos) {
        this.context = context;
        this.pInfos = pInfos;
    }

    private ArrayList<PInfo> getPackages(Context context) {
        ArrayList<PInfo> apps = getInstalledApps(context, false); /* false = no system packages */
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
            apps.get(i).prettyPrint();
        }
        return apps;
    }


    private ArrayList<PInfo> getInstalledApps(Context context, boolean getSysPackages) {
        ArrayList<PInfo> res = new ArrayList<>();
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);

            if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String appName = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
                Log.d(TAG, Integer.toString(i) + "name: " + appName);
            } else {
                String appName = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
                Log.d(TAG, Integer.toString(i) + "system app name: " + appName);
                continue;
            }

            PInfo newInfo = new PInfo();
            newInfo.appname = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(context.getPackageManager());
            res.add(newInfo);
        }


        return res;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (asyncListener != null)
            asyncListener.onPreExecuteCalled();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<PInfo> packages = getPackages(context);
        this.pInfos.clear();
        this.pInfos.addAll(packages);
        packages.clear();
        return null;
    }

    //in post post execute we load the user preferences ie. all those packages that user has selected
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        EventBus.getDefault().post(new MessageEvent(FAVOURITE_APPLICATIONS_LIST_CHANGED, null));

        loadUserSettingsFromFireBase();
        if (asyncListener != null)
            asyncListener.onPostExecuteCalled();
    }

    private void loadUserSettingsFromFireBase() {

        Set<String> allProfferedApplications = PreferencesManager.getInstance(context).getAllProfferedApplications(context);
        for (int i = 0; i < MasterClass.allPackagesinfo.size(); i++) {
            PInfo pInfo = MasterClass.allPackagesinfo.get(i);
            if (allProfferedApplications.contains(pInfo.pname)) {
                MasterClass.allPackagesinfo.get(i).isChecked = true;
            }
        }
        EventBus.getDefault().post(new MessageEvent(FAVOURITE_APPLICATIONS_LIST_CHANGED, null));

    }

    void setAsyncListener(AsyncListener asyncListener) {
        this.asyncListener = asyncListener;
    }
}
