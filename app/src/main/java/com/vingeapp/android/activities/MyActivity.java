package com.vingeapp.android.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vingeapp.android.R;
import com.vingeapp.android.adapters.MyAdapter;
import com.vingeapp.android.firebase.FireBaseHelper;
import com.vingeapp.android.models.PInfo;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by divisha on 3/27/17.
 */


public class MyActivity extends Activity {

    //array list representing all the packages installed on the system
    public static ArrayList<PInfo> allPackagesinfo = new ArrayList<>();
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
        mAdapter = new MyAdapter(this, allPackagesinfo);
        mRecyclerView.setAdapter(mAdapter);
        loadAllThePackages(this);
    }


    //using the following boolean we ensure that the method is executed only once no matter what
    static boolean loadingStarted = false;

    public static synchronized void loadAllThePackages(Context context) {
        if (loadingStarted) return;
        loadingStarted = true;
        new GetPackageTask(context, allPackagesinfo).execute();
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
