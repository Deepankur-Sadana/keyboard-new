package com.vingeapp.android.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.vingeapp.android.MessageEvent;
import com.vingeapp.android.R;
import com.vingeapp.android.adapters.MyAdapter;
import com.vingeapp.android.firebase.FireBaseHelper;
import com.vingeapp.android.interfaces.AsyncListener;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.models.PInfo;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import de.greenrobot.event.EventBus;

/**
 * Created by divisha on 3/27/17.
 */


public class MyActivity extends Activity implements GreenBotMessageKeyIds {

    //array list representing all the packages installed on the system
    public static ArrayList<PInfo> allPackagesinfo = new ArrayList<>();
    private final String TAG = MyActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private LinkedHashSet<String> allPackagesLinkedHashSet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        EventBus.getDefault().register(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(this, allPackagesinfo);
        mRecyclerView.setAdapter(mAdapter);

        loadAllThePackages(this);
        if (asyncTask != null) {
            asyncTask.setAsyncListener(asyncListener);
            try {
                asyncTask.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static GetPackageTask asyncTask;
    //using the following boolean we ensure that the method is executed only once no matter what
    static boolean loadingStarted = false;

    public static synchronized void loadAllThePackages(Context context) {
        if (loadingStarted) return;
        loadingStarted = true;
        asyncTask = new GetPackageTask(context, allPackagesinfo);
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
        EventBus.getDefault().post(new MessageEvent(FAVOURITE_APP_PREFERRED_LIST_CHANGED, null));
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
        for (int i = 0; i < allPackagesinfo.size(); i++) {
            PInfo pInfo = allPackagesinfo.get(i);
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

    @SuppressWarnings("unused")
    public void onEvent(MessageEvent event) {
        if (event.getMessageType() == FAVOURITE_APPLICATIONS_LIST_CHANGED) {
            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();
        }
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
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
        }
    };
}