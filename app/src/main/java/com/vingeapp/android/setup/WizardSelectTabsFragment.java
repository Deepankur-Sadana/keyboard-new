package com.vingeapp.android.setup;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vingeapp.android.R;

/**
 * Created by deepankursadana on 23/07/17.
 */

public class WizardSelectTabsFragment extends WizardPageBaseFragment {
    @Override
    protected boolean isStepCompleted(@NonNull Context context) {
        return false;
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
