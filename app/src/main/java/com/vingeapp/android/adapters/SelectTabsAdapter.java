package com.vingeapp.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.vingeapp.android.R;
import com.vingeapp.android.models.TabModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by deepankursadana on 23/07/17.
 */

public class SelectTabsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TabModel> tabModels;

    public SelectTabsAdapter(ArrayList<TabModel> tabModels) {
        this.tabModels = tabModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(View.inflate(parent.getContext(), R.layout.item_select_tabs, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        TabModel tabModel = tabModels.get(position);
        itemHolder.itemImage.setImageResource(tabModel.getResourceId());
        itemHolder.itemSwitch.setChecked(tabModel.isSelected());
        itemHolder.itemName.setText(tabModel.getKeyBoardOptions().toString());

    }

    @Override
    public int getItemCount() {
        return tabModels.size();
    }

    public Set<String> getSelectedApps() {
        Set<String> strings = new HashSet<>();
        for (int i = 0; i < tabModels.size(); i++) {
            if (tabModels.get(i).isSelected())
                strings.add(tabModels.get(i).getKeyBoardOptions().toString());
        }
        return strings;
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private TextView itemName;
        private ImageView itemImage;
        private Switch itemSwitch;

        ItemHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.tabTV);
            itemImage = (ImageView) itemView.findViewById(R.id.tabIV);
            itemSwitch = (Switch) itemView.findViewById(R.id.itemSwitch);


        }
    }
}
