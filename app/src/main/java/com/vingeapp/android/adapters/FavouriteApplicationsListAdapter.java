package com.vingeapp.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vingeapp.android.MasterClass;
import com.vingeapp.android.R;
import com.vingeapp.android.activities.GetPackageTask;
import com.vingeapp.android.activities.MyActivity;
import com.vingeapp.android.interfaces.AsyncListener;
import com.vingeapp.android.interfaces.RecyclerViewClickInterface;
import com.vingeapp.android.models.PInfo;

import java.util.ArrayList;

import utils.AppLibrary;

/**
 * Created by deepankur on 30/4/16.
 */
@SuppressLint("LongLogTag")
public class FavouriteApplicationsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private static final int TYPE_HEADER = 11111;
    private static final int TYPE_ITEM = 55555;
    private static final String TAG = "FavouriteApplicationsListAdapter";

    private RecyclerViewClickInterface recyclerViewClickInterface;
    private ArrayList<PInfo> pInfoArrayList;

    public FavouriteApplicationsListAdapter(Context context, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        setPreferredApps();
    }


    public void setPreferredApps() {
        int size = 0;
        if (pInfoArrayList != null)
            size = pInfoArrayList.size();
        Log.d(TAG, "setPreferredApps: " + pInfoArrayList + " size " + size);
        if (pInfoArrayList == null)
            pInfoArrayList = new ArrayList<>();
        else pInfoArrayList.clear();


        if (MasterClass.allPackagesinfo == null || MasterClass.allPackagesinfo.size() == 0) {
            loadDevicePackage();
        } else {
            addSelectedApplicationsToTheViews(MasterClass.allPackagesinfo);
        }
    }

    private void loadDevicePackage() {
        final ArrayList<PInfo> pInfoArrayList = new ArrayList<>();
        GetPackageTask getPackageTask = new GetPackageTask(context, pInfoArrayList);
        getPackageTask.setAsyncListener(new AsyncListener() {
            @Override
            public void onPreExecuteCalled() {

            }

            @Override
            public void onPostExecuteCalled() {
                addSelectedApplicationsToTheViews(pInfoArrayList);
                notifyDataSetChanged();
            }
        });
        getPackageTask.execute();
    }

    private void addSelectedApplicationsToTheViews(ArrayList<PInfo> pInfos) {
        for (int i = 0; i < pInfos.size(); i++) {
            PInfo pInfo = pInfos.get(i);
            if (pInfo.isChecked) {
                pInfoArrayList.add(pInfo);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        if (viewType == TYPE_ITEM)
            return new VHItem(LayoutInflater.from(context)
                    .inflate(R.layout.card_favourite_applications, parent, false));

        else if (viewType == TYPE_HEADER) {
            ImageView imageView = new ImageView(context);
            PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
            Drawable d = context.getResources().getDrawable(R.drawable.add_new);
            d.setColorFilter(Color.DKGRAY, mMode);
            imageView.setImageDrawable(d);
            int pixel = (int) AppLibrary.convertDpToPixel(8, this.context);
            imageView.setPadding(pixel, pixel, pixel, pixel);
            return new VHHeader(imageView);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + "  wtf ");
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItem) {
            PInfo pInfo = pInfoArrayList.get(position - 1);
            ((VHItem) holder).imageView.setImageDrawable(pInfo.icon);
            ((VHItem) holder).name.setText(pInfo.appname);
            ((VHItem) holder).rootView.setTag(pInfo.pname);
        }

    }


    @Override
    public int getItemCount() {
        if (pInfoArrayList == null) return 1;
        else return (pInfoArrayList.size() + 1);
    }

    /**
     * @param position the position from 0 to array list size
     * @return header type if the array List item is alphabet
     * new group if index is 0
     * normal otherwise
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else return TYPE_ITEM;

    }


    private class VHItem extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;
        View rootView;

        VHItem(View itemView) {
            super(itemView);
            rootView = itemView;
            imageView = ((ImageView) rootView.findViewById(R.id.fav_applicationIV));
            name = (TextView) rootView.findViewById(R.id.fav_applicationTV);
            rootView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (recyclerViewClickInterface != null)
                        recyclerViewClickInterface.onItemClick(RecyclerViewClickInterface.CLICK_TYPE_NORMAL, TYPE_ITEM, rootView.getTag());
                }
            });
        }
    }

    private class VHHeader extends RecyclerView.ViewHolder {
        View rootView;

        VHHeader(View itemView) {
            super(itemView);
            rootView = itemView;
            rootView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
