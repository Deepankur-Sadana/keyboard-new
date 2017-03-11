package com.vingeapp.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.vingeapp.android.R;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class SearchContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Pair> contactList;
    private Context context;

    public SearchContactsAdapter(ArrayList<Pair> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHItem(View.inflate(parent.getContext(), R.layout.card_clipboard, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size();
    }

    private class VHItem extends RecyclerView.ViewHolder {

        VHItem(View itemView) {
            super(itemView);
        }
    }
}
