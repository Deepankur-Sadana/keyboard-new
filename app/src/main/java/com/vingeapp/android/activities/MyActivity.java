package com.vingeapp.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.vingeapp.android.R;
import com.vingeapp.android.adapters.MyAdapter;
import com.vingeapp.android.firebase.FireBaseHelper;
import com.vingeapp.android.models.PInfo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by divisha on 3/27/17.
 */


public class MyActivity extends Activity {

    //array list representing all the packages installed on the system
    private static ArrayList<PInfo> allPackagesinfo = new ArrayList<>();
    private final String TAG = MyAdapter.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinkedHashSet<String> allPackagesLinkedHashSet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }


    //using the following boolean we ensure that the method is executed only once no matter what
    static boolean loadingStarted = false;

    public synchronized void loadAllThePackages(Context context) {
        if (loadingStarted) return;
        loadingStarted = true;
        new GetPackageTask(context, allPackagesinfo, null).execute();
    }


    /**
     * async code for fetching all the installed app details from the device.
     */
    private class GetPackageTask extends AsyncTask<Void, Void, Void> {

        private Context context;
        private ArrayList<PInfo> pInfos;
        private RecyclerView.Adapter adapter;

        GetPackageTask(Context context, ArrayList<PInfo> pInfos, RecyclerView.Adapter adapter) {
            this.context = context;
            this.pInfos = pInfos;
            this.adapter = adapter;
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
            FireBaseHelper.getInstance(context).setOnShortCutAppChangedListener(new FireBaseHelper.OnShortCutAppChangedListener() {
                @Override
                public void onListUpdated(LinkedHashSet<String> newList) {
                    allPackagesLinkedHashSet = newList;
                    if (allPackagesLinkedHashSet == null) {
                        Log.d(TAG, "refreshList: allPackagesLinkedHashSet is null returning");
                    }
                    for (int i = 0; i < allPackagesinfo.size(); i++) {
                        PInfo pInfo = allPackagesinfo.get(i);
                        if (allPackagesLinkedHashSet.contains(pInfo.pname)) {
                            allPackagesinfo.get(i).isChecked = true;
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < allPackagesinfo.size(); i++) {
            PInfo pInfo = allPackagesinfo.get(i);
            if (pInfo.isChecked)
                FireBaseHelper.getInstance(this).addPackageNameToPrefs(pInfo.pname);
            else
                FireBaseHelper.getInstance(this).deleteAppFromShortcut(pInfo.pname);

        }
    }
}
