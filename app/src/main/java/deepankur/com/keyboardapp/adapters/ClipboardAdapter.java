package deepankur.com.keyboardapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.interfaces.RecyclerViewClickInterface;
import deepankur.com.keyboardapp.models.KeyValueShortcutModel;

/**
 * Created by deepankursadana on 17/02/17.
 */

public class ClipboardAdapter extends BaseRecylerAdapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<KeyValueShortcutModel> keyValueShortcuts;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public ClipboardAdapter(Context context, ArrayList<KeyValueShortcutModel> keyValueShortcuts, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.keyValueShortcuts = keyValueShortcuts;
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
            return new VHItem(View.inflate(parent.getContext(), R.layout.card_pre_programmable, null));
        return new VHItem(View.inflate(parent.getContext(), R.layout.keyboard_view_clipboard, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItem) {
            VHItem itemHolder = (VHItem) holder;
            
        }
    }

    @Override
    public int getItemCount() {
        return this.keyValueShortcuts == null ? 0 : this.keyValueShortcuts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == keyValueShortcuts.size() ? FOOTER_HOLDER : ITEM_HOLDER;
    }

    private static class VHItem extends BaseRecylerAdapter.VHItem {

        VHItem(View v) {
            super(v);
        }
    }
}
