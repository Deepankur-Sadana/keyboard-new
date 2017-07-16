package com.vingeapp.android.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.vingeapp.android.MasterClass;
import com.vingeapp.android.MessageEvent;
import com.vingeapp.android.R;
import com.vingeapp.android.adapters.MyAdapter;
import com.vingeapp.android.firebase.FireBaseHelper;
import com.vingeapp.android.interfaces.AsyncListener;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.models.PInfo;
import com.vingeapp.android.preferences.PreferencesManager;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Created by divisha on 3/27/17.
 */


public class MyActivity extends Activity implements GreenBotMessageKeyIds {

    //array list representing all the packages installed on the system
    private final String TAG = MyActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private LinkedHashSet<String> allPackagesLinkedHashSet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        findViewById(R.id.saveTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.getInstance(v.getContext()).addPackageName(v.getContext(), getSelectedPackages());
                EventBus.getDefault().post(new MessageEvent(FAVOURITE_APP_PREFERRED_LIST_CHANGED, null));
                finish();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(this, MasterClass.allPackagesinfo);
        mRecyclerView.setAdapter(mAdapter);

        loadAllThePackages(this, asyncListener);

    }

    static GetPackageTask asyncTask;
    //using the following boolean we ensure that the method is executed only once no matter what
    static boolean loadingStarted = false;

    public static synchronized void loadAllThePackages(Context context, @Nullable AsyncListener asyncListener) {
        if (loadingStarted) return;
        loadingStarted = true;
        asyncTask = new GetPackageTask(context, MasterClass.allPackagesinfo);


        asyncTask.setAsyncListener(asyncListener);
        try {
            asyncTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        PreferencesManager.getInstance(this).addPackageName(this, getSelectedPackages());
        EventBus.getDefault().post(new MessageEvent(FAVOURITE_APP_PREFERRED_LIST_CHANGED, null));
    }

    Set<String> getSelectedPackages() {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < MasterClass.allPackagesinfo.size(); i++) {
            PInfo pInfo = MasterClass.allPackagesinfo.get(i);
            if (pInfo.isChecked)
                set.add(pInfo.pname);
        }
        return set;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        EventBus.getDefault().unregister(this);

        String s = "";
        for (int i = 0; i < MasterClass.allPackagesinfo.size(); i++) {
            PInfo pInfo = MasterClass.allPackagesinfo.get(i);
            if (pInfo.isChecked) {
                FireBaseHelper.getInstance(this).addPackageNameToPrefs(pInfo.pname);
                s += "\n";
                s += pInfo.pname;
            } else {
                FireBaseHelper.getInstance(this).deleteAppFromShortcut(pInfo.pname);
            }
        }
        Log.d(TAG, "onDestroy: " + s);
    }


    private AsyncListener asyncListener = new AsyncListener() {
        @Override
        public void onPreExecuteCalled() {
            Log.d(TAG, "onPreExecuteCalled: ");
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            findViewById(R.id.recyclerView).setVisibility(View.GONE);
        }

        @Override
        public void onPostExecuteCalled() {
            Log.d(TAG, "onPostExecuteCalled: ");
            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
        }
    };
}
