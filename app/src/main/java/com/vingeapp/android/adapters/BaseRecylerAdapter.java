package com.vingeapp.android.adapters;

/**
 * Created by deepankursadana on 17/02/17.
 */

public abstract class BaseRecylerAdapter/*<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>*/ {
   public static final int ITEM_HOLDER = 11, HEADER_HOLDER = 22, FOOTER_HOLDER = 33;

//    public static class VH extends RecyclerView.ViewHolder {
//
//        public VH(View v) {
//            super(v);
//        }
//    }
//
//    static class VHItem extends RecyclerView.ViewHolder {
//
//        VHItem(View v) {
//            super(v);
////            initItemView(v);
//        }
//    }
//
//    public static class VHHeader extends RecyclerView.ViewHolder {
//
//        public VHHeader(View v) {
//            super(v);
//        }
//    }
//
//    public class VHFooter extends RecyclerView.ViewHolder {
//
//        public VHFooter(View v) {
//            super(v);
//            initItemView(v);
//        }
//    }
//
//    abstract void initItemView(View itemView);
//
//
//    void initFooterView(View footerView) {
//
//    }
//
//    void initHeaderView(View headerView) {
//    }
//
//
//    @Override
//    public  VH onCreateViewHolder(ViewGroup parent, int viewType){
//
//    }
//
//    @Override
//    public  void onBindViewHolder(VH holder, int position){
//
//    }
//
//    //
////    @Override
////    public abstract int getItemCount();
////
////    @Override
////    public abstract int getItemViewType(int position);
//    public abstract void onBindViewHolderCursor(VH holder, int ViewType);

}
