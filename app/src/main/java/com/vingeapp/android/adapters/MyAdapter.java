package com.vingeapp.android.adapters;

import android.content.Context;
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

/**
 * Created by divisha on 3/27/17.
 */


public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<PInfo> allPackagesinfo;
    private Context context;

    public MyAdapter( Context context,ArrayList<PInfo> allPackagesinfo) {
        this.allPackagesinfo = allPackagesinfo;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            PInfo pInfo = allPackagesinfo.get(position);
            ((ViewHolder) holder).imageView.setImageDrawable(pInfo.icon);
            ((ViewHolder) holder).name.setText(pInfo.appname);
            ((ViewHolder) holder).rootView.setTag(pInfo.pname);
            ((ViewHolder) holder).checkBox.isChecked();
        }

    }

    @Override
    public int getItemCount() {
        return (allPackagesinfo.size());
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

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


}
