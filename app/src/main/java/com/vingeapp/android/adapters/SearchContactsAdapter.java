package com.vingeapp.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vingeapp.android.R;
import com.vingeapp.android.models.ContactsModel;

import java.util.ArrayList;

import utils.AppLibrary;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class SearchContactsAdapter extends RecyclerView.Adapter<SearchContactsAdapter.VHItem> {
    private ArrayList<ContactsModel> contactList;
    private Context context;

    public void setContactList(ArrayList<ContactsModel> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    public SearchContactsAdapter(ArrayList<ContactsModel> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }


    @Override
    public VHItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHItem(View.inflate(parent.getContext(), R.layout.card_search_item, null));
    }

    @Override
    public void onBindViewHolder(VHItem holder, int position) {
        ContactsModel contactsModel = contactList.get(position);
        holder.itemName.setText(contactsModel.name);
    }


    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size();
    }

    class VHItem extends RecyclerView.ViewHolder {

        TextView itemName;

        VHItem(View itemView) {
            super(itemView);
            itemView.setMinimumHeight(AppLibrary.convertDpToPixels(itemView.getContext(), 50));
            itemName = (TextView) itemView.findViewById(R.id.itemNameTV);
        }
    }
}
