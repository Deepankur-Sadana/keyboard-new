package com.vingeapp.android.setup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.vingeapp.android.R;


public class WizardPageSwitchToKeyboardFragment extends WizardPageBaseFragment {
    TextView actionTv;

    @Override
    protected int getPageLayoutId() {
        return R.layout.fresh_onboarding_screens;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View.OnClickListener showSwitchImeDialog = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showInputMethodPicker();
            }
        };
        actionTv = (TextView) view.findViewById(R.id.actionTV);
//        view.findViewById(R.id.go_to_switch_keyboard_action).setOnClickListener(showSwitchImeDialog);
        actionTv.setOnClickListener(showSwitchImeDialog);
    }

    @Override
    public void refreshFragmentUi() {
        super.refreshFragmentUi();
        if (getActivity() != null) {
            final boolean isActive = isStepCompleted(getActivity());
            final boolean isEnabled = isStepPreConditionDone(getActivity());
//            mStateIcon.setImageResource(isActive ?
//                    R.drawable.ic_wizard_switch_on
//                    : R.drawable.ic_wizard_switch_off);
//            actionTv.setText(isActive ?
//                    DONE :
//                    DO_IT_NOW
//            );
//
            actionTv.setClickable(isEnabled && !isActive);

        }
    }

//    private final String DO_IT_NOW = "SELECT INPUT METHOD", DONE = DO_IT_NOW;


    @Override
    void previousStepNotComplete() {

    }

    @Override
    void thisStepCompleted() {

    }

    @Override
    void nextStepNeedsSetup() {
//        actionTv.setText(DONE);
    }

    @Override
    protected boolean isStepCompleted(@NonNull Context context) {
        return SetupSupport.isThisKeyboardSetAsDefaultIME(context);
    }

    @Override
    protected boolean isStepPreConditionDone(@NonNull Context context) {
        return SetupSupport.isThisKeyboardEnabled(context);
    }
}
