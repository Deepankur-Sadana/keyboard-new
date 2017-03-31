package com.vingeapp.android.adapters;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.vingeapp.android.R;
import com.vingeapp.android.models.PInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divisha on 3/27/17.
 */


public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ArrayList<PInfo> pInfoArrayList = new ArrayList<>();


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  ViewHolder) {
            PInfo pInfo = pInfoArrayList.get(position);
            ((ViewHolder) holder).imageView.setImageDrawable(pInfo.icon);
            ((ViewHolder) holder).name.setText(pInfo.appname);
            ((ViewHolder) holder).rootView.setTag(pInfo.pname);
            ((ViewHolder) holder).checkBox.isChecked();
        }

    }

    @Override
    public int getItemCount() {
        return (pInfoArrayList.size());
    }

    private  class ViewHolder  extends RecyclerView.ViewHolder{

        TextView name;
        ImageView imageView;
        CheckBox checkBox;
        View rootView;



        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView;
            checkBox = (CheckBox) rootView.findViewById(R.id.checkbox);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(!checkBox.isChecked());
                }
            });
    }
}

    public static void setPackages(Context context) {
        setPackages(context, null);
    }

    private static void setPackages(Context context, RecyclerView.Adapter adapter) {
        new GetPackageTask(context, pInfoArrayList, null).execute();

    }
    private static ArrayList<PInfo> getPackages(Context context) {
        ArrayList<PInfo> apps = getInstalledApps(context, false); /* false = no system packages */
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
            apps.get(i).prettyPrint();
        }
        return apps;
    }

    private static ArrayList<PInfo> getInstalledApps(Context context, boolean getSysPackages) {
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
    public static class GetPackageTask extends AsyncTask<Void,Void, Void> {

        private Context context;
        private ArrayList<PInfo> pInfos;
        private RecyclerView.Adapter adapter;

        GetPackageTask(Context context, ArrayList<PInfo> pInfos, RecyclerView.Adapter adapter) {
            this.context = context;
            this.pInfos = pInfos;
            this.adapter = adapter;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<PInfo> packages = getPackages(context);
            this.pInfos.clear();
            this.pInfos.addAll(packages);
            packages.clear();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }}







