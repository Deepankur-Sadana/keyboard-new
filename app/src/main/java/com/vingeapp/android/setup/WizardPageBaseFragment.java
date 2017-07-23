package com.vingeapp.android.setup;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vingeapp.android.R;
import com.vingeapp.android.utils.CustomTypefaceSpan;

import java.util.LinkedHashMap;
import java.util.Map;

import utils.FontPicker;


public abstract class WizardPageBaseFragment extends Fragment {
    /**
     * calculate whether the step has completed. This should check OS configuration.
     *
     * @return true if step setup is valid in OS
     */
    protected abstract boolean isStepCompleted(@NonNull Context context);

    /**
     * calculate whether the step's pre-configurations are done.
     */
    protected abstract boolean isStepPreConditionDone(@NonNull Context context);

//    protected ImageView mStateIcon;

    @Override
    public void onStart() {
        super.onStart();
        //enabling or disabling the views.
        refreshFragmentUi();
    }

    @LayoutRes
    protected abstract int getPageLayoutId();

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NestedScrollView scrollView = (NestedScrollView) inflater.inflate(R.layout.keyboard_setup_wizard_page_base_layout, container, false);

        View actualPageView = inflater.inflate(getPageLayoutId(), scrollView, false);
        scrollView.addView(actualPageView);
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
//        mStateIcon = (ImageView) view.findViewById(R.id.step_state_icon);
        initViews(view);
    }

    protected void refreshWizardPager() {
        refreshFragmentUi();
        //re-triggering UI update
        Fragment owningFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.main_ui_content);
        if (owningFragment == null || !(owningFragment instanceof SetUpKeyboardWizardFragment))
            return;
        SetUpKeyboardWizardFragment wizardFragment = (SetUpKeyboardWizardFragment) owningFragment;
        wizardFragment.refreshFragmentsUi();
    }

    @CallSuper
    public void refreshFragmentUi() {
        if (getActivity() == null) {
            //if the fragment is not shown, we will call refresh in onStart
            return;
        }
//        final View previousStepNotCompleted = getView().findViewById(R.id.previous_step_not_complete);
//        final View thisStepCompleted = getView().findViewById(R.id.this_step_complete);
//        final View thisStepNeedsSetup = getView().findViewById(R.id.this_step_needs_setup);

//        previousStepNotCompleted.setVisibility(View.GONE);
//        thisStepCompleted.setVisibility(View.GONE);
//        thisStepNeedsSetup.setVisibility(View.GONE);

        if (!isStepPreConditionDone(getActivity())) {
//            previousStepNotCompleted.setVisibility(View.VISIBLE);
            previousStepNotComplete();
        } else if (isStepCompleted(getActivity())) {
//            thisStepCompleted.setVisibility(View.VISIBLE);
            thisStepCompleted();
        } else {
//            thisStepNeedsSetup.setVisibility(View.VISIBLE);
            nextStepNeedsSetup();
        }
    }

    abstract void previousStepNotComplete();

    abstract void thisStepCompleted();

    abstract void nextStepNeedsSetup();


    private void initViews(View rootView) {
        int stepNumber = -222222;

        LinkedHashMap<String, Boolean> stringBooleanLinkedHashMap = new LinkedHashMap<>();//true for bold , false for normal texts
        /**
         * Note as this  linkedHashMap will FAIL in case same key is inserted more than once.
         * SomeOtherDataStructure must be used in that case for example ArrayList of pairs.
         */
        TextView descriptionTv = (TextView) rootView.findViewById(R.id.instructionTV);
        TextView actionTv = (TextView) rootView.findViewById(R.id.actionTV);
        if (this instanceof WizardPageEnableKeyboardFragment) {
            stepNumber = 1;
            stringBooleanLinkedHashMap.put("Select ", false);
            stringBooleanLinkedHashMap.put("Vinge Keyboard ", true);
            stringBooleanLinkedHashMap.put("in your Language & Input settings", false);
            actionTv.setText("ENABLE IN SETTINGS");

        } else if (this instanceof WizardPageSwitchToKeyboardFragment) {
            stepNumber = 2;
            stringBooleanLinkedHashMap.put("Select ", false);
            stringBooleanLinkedHashMap.put("Vinge Keyboard", true);
            stringBooleanLinkedHashMap.put(" as your default \ninput method.", false);
            actionTv.setText("SELECT INPUT METHOD");

        } else if (this instanceof WizardFreshPermissions) {
            stepNumber = 3;
            stringBooleanLinkedHashMap.put("Set ", false);
            stringBooleanLinkedHashMap.put("App Permission ", true);
            stringBooleanLinkedHashMap.put(" to use them in shortcut bar", false);
            actionTv.setText("NEXT");
        } else if (this instanceof WizardSelectTabsFragment) {
            stepNumber = 3;
            stringBooleanLinkedHashMap.put("Yippee! All set. You are now ready to use the smartest keyboard ever on the planet. ", false);
        }
        if (this instanceof WizardSelectTabsFragment){
            //skip
        }
        else
            ((TextView) rootView.findViewById(R.id.step_number_TV)).setText("Step " + String.valueOf(stepNumber));
        setSpannable(stringBooleanLinkedHashMap, descriptionTv);
        stringBooleanLinkedHashMap.clear();
    }


    Context context;

    private void setSpannable(LinkedHashMap<String, Boolean> stringBooleanLinkedHashMap, TextView textView) {
        String buffer = "";
        Typeface regular = FontPicker.getInstance(context).getMontserratRegular();
        Typeface bold = FontPicker.getInstance(context).getMontserratBold();

        String s = "";
        for (Map.Entry<String, Boolean> entry : stringBooleanLinkedHashMap.entrySet()) {
            s += entry.getKey();
        }
        Spannable spannable = new SpannableString(s);
        for (Map.Entry<String, Boolean> entry : stringBooleanLinkedHashMap.entrySet()) {
            spannable.setSpan(new CustomTypefaceSpan("regular", entry.getValue() ? bold : regular), buffer.length(), (buffer.length() + entry.getKey().length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            buffer += entry.getKey();
        }
        textView.setText(spannable);
    }

}
