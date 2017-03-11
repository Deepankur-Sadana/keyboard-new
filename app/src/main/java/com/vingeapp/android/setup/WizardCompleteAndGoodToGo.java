package com.vingeapp.android.setup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vingeapp.android.R;
import com.vingeapp.android.fragments.BaseFragment;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class WizardCompleteAndGoodToGo extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fresh_onboarding_screens, container, false);
        initGoodToGo(rootView);
        return rootView;
    }

    private void initGoodToGo(View rootView) {
        ((TextView) rootView.findViewById(R.id.step_number_TV)).setText("Ready to go!");
        ((TextView) rootView.findViewById(R.id.actionTV)).setText("Yippee! All set. You are now ready to use\n" +
                "the most smart keyboard ever\n" +
                "on the planet");
        ((TextView) rootView.findViewById(R.id.actionTV)).setText("DONE");
    }
}
