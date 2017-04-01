package com.vingeapp.android.activities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.vingeapp.android.MessageEvent;
import com.vingeapp.android.firebase.FireBaseHelper;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.models.PInfo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * async code for fetching all the installed app details from the device.
 */
class GetPackageTask extends AsyncTask<Void, Void, Void> implements GreenBotMessageKeyIds{

    private static final String TAG = GetPackageTask.class.getSimpleName();
    private Context context;
    private ArrayList<PInfo> pInfos;

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
            if ((!getSysPackages) && (p.versionName == null)) {
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
    }

    private void loadUserSettingsFromFireBase(){
        FireBaseHelper.getInstance(context).setOnShortCutAppChangedListener(new FireBaseHelper.OnShortCutAppChangedListener() {
            @Override
            public void onListUpdated(LinkedHashSet<String> newList) {

                if (newList == null) {
                    Log.d(TAG, "refreshList: newList is null returning");
                    return;
                }

                for (int i = 0; i < MyActivity.allPackagesinfo.size(); i++) {
                    PInfo pInfo = MyActivity.allPackagesinfo.get(i);
                    if (newList.contains(pInfo.pname)) {
                        MyActivity.allPackagesinfo.get(i).isChecked = true;
                    }
                }
                EventBus.getDefault().post(new MessageEvent(FAVOURITE_APPLICATIONS_LIST_CHANGED, null));
            }
        });
    }
}
