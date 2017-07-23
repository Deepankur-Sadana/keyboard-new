package com.vingeapp.android.setup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vingeapp.android.R;

import utils.AppLibrary;

/**
 * Created by deepankursadana on 23/07/17.
 */

public class WizardSelectTabsFragment extends WizardPageBaseFragment {
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
        view.findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);

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
}
