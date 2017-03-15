package com.vingeapp.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vingeapp.android.R;
import com.vingeapp.android.interfaces.RecyclerViewClickInterface;
import com.vingeapp.android.models.ContactsModel;

import java.util.ArrayList;

import utils.AppLibrary;

import static com.vingeapp.android.adapters.BaseRecylerAdapter.HEADER_HOLDER;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class SearchContactsAdapter extends RecyclerView.Adapter<SearchContactsAdapter.VHItem> {
    private ArrayList<ContactsModel> contactList;
    private Context context;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public void setContactList(ArrayList<ContactsModel> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    public SearchContactsAdapter(ArrayList<ContactsModel> contactList, Context context,RecyclerViewClickInterface recyclerViewClickInterface) {
        this.contactList = contactList;
        this.context = context;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }


    @Override
    public VHItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHItem(View.inflate(parent.getContext(), R.layout.card_search_item, null));
    }

    @Override
    public void onBindViewHolder(VHItem holder, int position) {
        ContactsModel contactsModel = contactList.get(position);
        holder.itemName.setText(contactsModel.name);
        holder.rootView.setTag(contactsModel);
    }


    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size();
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
