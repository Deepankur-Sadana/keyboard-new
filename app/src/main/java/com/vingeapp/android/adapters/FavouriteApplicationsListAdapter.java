package com.vingeapp.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vingeapp.android.R;
import com.vingeapp.android.activities.MyActivity;
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
        long l1 = System.currentTimeMillis();
        pInfoArrayList = MyActivity.allPackagesinfo;
        Log.d(TAG, "FavouriteApplicationsListAdapter: " + (System.currentTimeMillis() - l1));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM)
            return new VHItem(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_favourite_applications, parent, false));

        else if (viewType == TYPE_HEADER) {
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setImageResource(R.drawable.add_new);
            int pixel = (int) AppLibrary.convertDpToPixel(8, context);
            imageView.setPadding(pixel, pixel, pixel, pixel);
            imageView.setBackgroundColor(Color.GRAY);
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


    public void refresh() {
        Log.d(TAG, "refresh: ");
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
