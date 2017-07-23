package com.vingeapp.android.setup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vingeapp.android.MessageEvent;
import com.vingeapp.android.R;
import com.vingeapp.android.adapters.SelectTabsAdapter;
import com.vingeapp.android.enums.KeyBoardOptions;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.keyboardCustomViews.TabStripView;
import com.vingeapp.android.models.TabModel;
import com.vingeapp.android.preferences.PreferencesManager;

import java.util.ArrayList;
import java.util.Set;

import de.greenrobot.event.EventBus;
import utils.AppLibrary;

/**
 * Created by deepankursadana on 23/07/17.
 */

public class WizardSelectTabsFragment extends WizardPageBaseFragment implements GreenBotMessageKeyIds{
    SelectTabsAdapter selectTabsAdapter;

    @Override
    protected boolean isStepCompleted(@NonNull Context context) {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.actionTV)).setText("DONE");
        String intro = "Yippee! All set. You are now ready to use the smartest keyboard ever on the planet. ";
        String s = "\nPlease select your favourite applications from the list";
        ((TextView) view.findViewById(R.id.instructionTV)).setText(intro + s);

        ((TextView) view.findViewById(R.id.step_number_TV)).setText("Ready to go!");
        ((RelativeLayout.LayoutParams) (view.findViewById(R.id.logo)).getLayoutParams()).topMargin = AppLibrary.convertDpToPixels(view.getContext(), 50);

        view.findViewById(R.id.actionTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectTabsAdapter != null)
                    PreferencesManager.getInstance(v.getContext()).addPrefferedTab(v.getContext(), selectTabsAdapter.getSelectedApps());
                getActivity().finish();
                EventBus.getDefault().post(new MessageEvent(TABS_LIST_CHANGED, null));


            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        selectTabsAdapter = new SelectTabsAdapter(getArrayList());
        recyclerView.setAdapter(selectTabsAdapter);
    }

    @Override
    protected boolean isStepPreConditionDone(@NonNull Context context) {
        return true;
    }

    @Override
    protected int getPageLayoutId() {
        return R.layout.fresh_onboarding_screens;
    }

    @Override
    void previousStepNotComplete() {

    }

    @Override
    void thisStepCompleted() {

    }

    @Override
    void nextStepNeedsSetup() {

    }

    public ArrayList<TabModel> getArrayList() {
        Set<String> allProfferedApplications = PreferencesManager.getInstance(context).getAllSelectedTabs(context);
        ArrayList<TabModel> list = new ArrayList<>();

        for (KeyBoardOptions keyBoardOptions : TabStripView.keyBoardOptions) {
            if (keyBoardOptions == KeyBoardOptions.QWERTY)
                continue;
            list.add(new TabModel(TabStripView.getResourceIdForTabStrip(keyBoardOptions),
                    keyBoardOptions,
                    allProfferedApplications.contains(keyBoardOptions.toString())));
        }

        return list;
    }


}
