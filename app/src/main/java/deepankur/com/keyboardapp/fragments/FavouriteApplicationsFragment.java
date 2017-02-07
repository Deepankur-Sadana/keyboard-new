package deepankur.com.keyboardapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.adapters.AllApplicationsListAdapter;
import deepankur.com.keyboardapp.fragments.BaseFragment;
import deepankur.com.keyboardapp.interfaces.RecyclerViewClickInterface;

/**
 * Created by deepankur on 2/6/17.
 */

public class FavouriteApplicationsFragment extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourite_applications, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.favouriteApplicationsRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(new AllApplicationsListAdapter(getActivity(), new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int extras, Object data) {

            }
        }));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
