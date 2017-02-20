package deepankur.com.keyboardapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.interfaces.RecyclerViewClickInterface;
import deepankur.com.keyboardapp.models.ClipBoardItemModel;

/**
 * Created by deepankursadana on 17/02/17.
 */

public class ClipboardAdapter extends BaseRecylerAdapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ClipBoardItemModel> clipBoardItemsList;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private static final int ACTION_CREATE_NEW = 55;

    public ClipboardAdapter(Context context, ArrayList<ClipBoardItemModel> keyValueShortcuts, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.clipBoardItemsList = keyValueShortcuts;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @Override
    void initItemView(View itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HOLDER)
            return new VHItem(View.inflate(parent.getContext(), R.layout.card_clipboard, null));
        else if (viewType == HEADER_HOLDER)
            return new VHHeader(new ImageButton(parent.getContext()));
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

    private static class VHHeader extends BaseRecylerAdapter.VHHeader {


        VHHeader(View v) {
            super(v);

        }
    }

    private static class VHItem extends BaseRecylerAdapter.VHItem {
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
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG, "onLongClick: ");
                    return true;
                }
            });
        }
    }
}
