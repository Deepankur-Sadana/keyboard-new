package com.vingeapp.android.ftue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.vingeapp.android.R;
import com.vingeapp.android.adapters.GenericViewPagerAdapter;
import com.vingeapp.android.fragments.BaseFragment;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class ParentFirstRunIntroFragment extends BaseFragment {
    ViewPager viewPager;
    final int PAGE_COUNT = 4;
    final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_intro_parent, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        initViewPager();
        return rootView;
    }

    private void initViewPager() {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < PAGE_COUNT; i++) {
            ChildFirstRunIntroFragment introFragment = new ChildFirstRunIntroFragment();
            introFragment.setPAGE_NUMBER(i);
            fragmentList.add(introFragment);
        }
        GenericViewPagerAdapter adapter = new GenericViewPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
    }
}
