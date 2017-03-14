package com.vingeapp.android.ftue;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vingeapp.android.R;
import com.vingeapp.android.fragments.BaseFragment;
import com.vingeapp.android.setup.MainSettingsActivity;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class ChildFirstRunIntroFragment extends BaseFragment {
    int PAGE_NUMBER;
    final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PAGE_NUMBER = getArguments().getInt("page_number", 0);i
        Log.d(TAG, "onCreate: " + PAGE_NUMBER);

    }

    public void setPAGE_NUMBER(int PAGE_NUMBER) {
        this.PAGE_NUMBER = PAGE_NUMBER;
    }

    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        int id;
        if (PAGE_NUMBER == 0)
            id = R.layout.fragment_intro_logo;
        else if (PAGE_NUMBER >= 1 && PAGE_NUMBER < 3) {
            id = R.layout.fragment_keyboard_intro;
        } else id = R.layout.fragment_facebook_login;//for last item
        Log.d("***********", "PAGE" + PAGE_NUMBER);
        rootView = inflater.inflate(id, container, false);
        if (PAGE_NUMBER == 3) {

            rootView.findViewById(R.id.facebook_login_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainSettingsActivity) getActivity()).setChildIntroFragment(ChildFirstRunIntroFragment.this);
                    rootView.findViewById(R.id.facebook_login_button).setEnabled(false);
                    ((MainSettingsActivity) getActivity()).onLoginButtonClicked();
                }
            });
        }
        return rootView;
    }

    void applyGradient(TextView textView) {
        Shader textShader = new LinearGradient(0, 0, 0, 20,
                new int[]{Color.GREEN, Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);
    }

    public void enableFacebookButton() {
        rootView.findViewById(R.id.facebook_login_button).setEnabled(true);

    }
}
