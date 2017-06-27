package com.vingeapp.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private final String TAG = getClass().getSimpleName();

    public MyAdapter(Context context, ArrayList<PInfo> allPackagesinfo) {
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
        PInfo pInfo = allPackagesinfo.get(position);

        if (holder instanceof ViewHolder) {
            ViewHolder itemHolder = (ViewHolder) holder;
            itemHolder.imageView.setImageDrawable(pInfo.icon);
            itemHolder.name.setText(pInfo.appname);
            itemHolder.rootView.setTag(pInfo);
            itemHolder.checkBox.setChecked(pInfo.isChecked);
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + allPackagesinfo.size());
        return (allPackagesinfo.size());
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView imageView;
        CheckBox checkBox;
        View rootView;


        ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            imageView = ((ImageView) rootView.findViewById(R.id.applicationIV));
            name = ((TextView) rootView.findViewById(R.id.applicationTV));
            checkBox = (CheckBox) rootView.findViewById(R.id.checkbox);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean wasCheckBoxChecked = checkBox.isChecked();
                    if (wasCheckBoxChecked) {//check box was checked earlier, unchecking it now
                        checkBox.setChecked(false);
                        Log.d(TAG, "onClick: unchecked");
                    } else {
                        //check box was unchecked earlier, checking it now
                        checkBox.setChecked(true);
                        Log.d(TAG, "onClick: checked");
                    }

//                    checkBox.setChecked(!checkBox.isChecked());
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    PInfo tag = (PInfo) rootView.getTag();
                    tag.isChecked = isChecked;
                    Log.d(TAG, "onCheckedChanged: " + tag.pname + " isChecked " + isChecked);
                }
            });

        }
    }
}
