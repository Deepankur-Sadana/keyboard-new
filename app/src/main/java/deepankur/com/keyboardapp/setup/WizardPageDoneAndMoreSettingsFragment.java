package deepankur.com.keyboardapp.setup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import deepankur.com.keyboardapp.R;


public class WizardPageDoneAndMoreSettingsFragment extends WizardPageBaseFragment implements View.OnClickListener {

//    private DemoAnyKeyboardView mDemoAnyKeyboardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.keyboard_setup_wizard_page_additional_settings_layout, container, false);
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
//        MainSettingsActivity activity = (MainSettingsActivity) getActivity();
//        switch (v.getId()) {
//            case R.id.go_to_languages_action:
//                activity.addFragmentToUi(new KeyboardAddOnBrowserFragment(), TransitionExperiences.DEEPER_EXPERIENCE_TRANSITION);
//                break;
//            case R.id.go_to_theme_action:
//                activity.addFragmentToUi(new KeyboardThemeSelectorFragment(), TransitionExperiences.DEEPER_EXPERIENCE_TRANSITION);
//                break;
//            case R.id.go_to_all_settings_action:
//                activity.onNavigateToRootClicked(v);
//                activity.openDrawer();
//                break;
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        AnyKeyboard defaultKeyboard = KeyboardFactory.getEnabledKeyboards(getContext()).get(0).createKeyboard(getContext(), Keyboard.KEYBOARD_ROW_MODE_NORMAL);
//        defaultKeyboard.loadKeyboard(mDemoAnyKeyboardView.getThemedKeyboardDimens());
//        mDemoAnyKeyboardView.setKeyboard(defaultKeyboard, null, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mDemoAnyKeyboardView.onViewNotRequired();
    }
}
