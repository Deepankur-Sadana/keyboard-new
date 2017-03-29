package com.vingeapp.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.vingeapp.android.R;

/**
 * Created by divisha on 3/27/17.
 */


public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

//        if (holder instanceof AllApplicationsListAdapter.ViewHolder)

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private  class ViewHolder  extends RecyclerView.ViewHolder{

        TextView name;
        ImageView imageView;
        CheckBox checkBox;
        View rootView;



        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
