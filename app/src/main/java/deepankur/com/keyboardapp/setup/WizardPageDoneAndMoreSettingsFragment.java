package deepankur.com.keyboardapp.setup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import net.evendanan.chauffeur.lib.experiences.TransitionExperiences;

import deepankur.com.keyboardapp.R;


public class WizardPageDoneAndMoreSettingsFragment extends WizardPageBaseFragment implements View.OnClickListener {
//    private DemoAnyKeyboardView mDemoAnyKeyboardView;

    @Override
    protected int getPageLayoutId() {
        return R.layout.keyboard_setup_wizard_page_additional_settings_layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    protected boolean isStepCompleted(@NonNull Context context) {
        return false;//this step is never done! You can always configure more :)
    }

    @Override
    protected boolean isStepPreConditionDone(@NonNull Context context) {
        return SetupSupport.isThisKeyboardSetAsDefaultIME(context);
    }

    @Override
    public void onClick(View v) {
        MainSettingsActivity activity = (MainSettingsActivity) getActivity();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mDemoAnyKeyboardView.onViewNotRequired();
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
