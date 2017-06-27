package com.vingeapp.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vingeapp.android.R;
import com.vingeapp.android.googleLocationApiResponse.Result;
import com.vingeapp.android.interfaces.RecyclerViewClickInterface;

import java.util.ArrayList;

import utils.AppLibrary;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class SearchMapsAdapter extends RecyclerView.Adapter<SearchMapsAdapter.VHItem> {
    private ArrayList<Result> locationModelArrayList;
    private Context context;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public void setContactList(ArrayList<Result> contactList) {
        this.locationModelArrayList = contactList;
        notifyDataSetChanged();
    }

    public SearchMapsAdapter(ArrayList<Result> contactList, Context context, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.locationModelArrayList = contactList;
        this.context = context;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }


    @Override
    public VHItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHItem(View.inflate(parent.getContext(), R.layout.card_search_item, null));
    }

    @Override
    public void onBindViewHolder(VHItem holder, int position) {
        Result locationModel = locationModelArrayList.get(position);
        holder.itemName.setText(locationModel.getFormatted_address());
        holder.rootView.setTag(locationModel);
    }


    @Override
    public int getItemCount() {
        return locationModelArrayList == null ? 0 : locationModelArrayList.size();
    }

    class VHItem extends RecyclerView.ViewHolder {

        TextView itemName;
        View rootView;

        VHItem(View itemView) {
            super(itemView);
            itemView.setMinimumHeight(AppLibrary.convertDpToPixels(itemView.getContext(), 50));
            rootView = itemView;
            itemName = (TextView) itemView.findViewById(R.id.itemNameTV);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewClickInterface != null) {
                        recyclerViewClickInterface.onItemClick(RecyclerViewClickInterface.CLICK_TYPE_NORMAL, -1, rootView.getTag());
                    }
                }
            });
        }
    }
}
