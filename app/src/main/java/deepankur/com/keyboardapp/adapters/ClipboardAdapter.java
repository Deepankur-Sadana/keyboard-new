package deepankur.com.keyboardapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.interfaces.RecyclerViewClickInterface;
import deepankur.com.keyboardapp.models.ClipBoardItemModel;

import static deepankur.com.keyboardapp.adapters.BaseRecylerAdapter.HEADER_HOLDER;
import static deepankur.com.keyboardapp.adapters.BaseRecylerAdapter.ITEM_HOLDER;

/**
 * Created by deepankursadana on 17/02/17.
 */

public class ClipboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ClipBoardItemModel> clipBoardItemsList;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private static final int ACTION_CREATE_NEW = 55;
//    public static final int HEADER_CLICKED = 11, ITEM_CLICKED = 22;

    public ClipboardAdapter(Context context, ArrayList<ClipBoardItemModel> keyValueShortcuts, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.clipBoardItemsList = keyValueShortcuts;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HOLDER)
            return new VHItem(View.inflate(parent.getContext(), R.layout.card_clipboard, null));
        else if (viewType == HEADER_HOLDER) {
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setImageResource(android.R.drawable.ic_input_add);
            return new VHHeader(imageView);
        }
        throw new IllegalArgumentException("view type " + viewType + " not supported in: " + TAG);
    }

    private static final String TAG = ClipboardAdapter.class.getSimpleName();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItem) {
            ClipBoardItemModel clipBoardItem = clipBoardItemsList.get(position + 1);
            VHItem itemHolder = (VHItem) holder;
            itemHolder.itemKeyTv.setText(clipBoardItem.getNote());

        }
    }

    @Override
    public int getItemCount() {
        return this.clipBoardItemsList == null ? 1 : this.clipBoardItemsList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER_HOLDER : ITEM_HOLDER;
    }

    private class VHHeader extends RecyclerView.ViewHolder {
        View rootView;

        VHHeader(View v) {
            super(v);
            rootView = v;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewClickInterface != null) {
                        recyclerViewClickInterface.onItemClick(RecyclerViewClickInterface.CLICK_TYPE_NORMAL, HEADER_HOLDER, null);
                    }
                }
            });
        }
    }

    private class VHItem extends RecyclerView.ViewHolder{
        View rootView;
        TextView itemKeyTv;

        VHItem(View v) {
            super(v);
            rootView = v;
            itemKeyTv = (TextView) rootView.findViewById(R.id.itemKeyTV);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: new clipboard item adding request");
                    if (recyclerViewClickInterface != null)
                        recyclerViewClickInterface.onItemClick(RecyclerViewClickInterface.CLICK_TYPE_NORMAL, ITEM_HOLDER, v.getTag());
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG, "onLongClick: ");
                    recyclerViewClickInterface.onItemClick(RecyclerViewClickInterface.CLICK_TYPE_NORMAL, ITEM_HOLDER, v.getTag());
                    return true;
                }
            });
        }
    }
}
