package com.vingeapp.android.ftue;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
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

        if (PAGE_NUMBER == 0) {//first page
            id = R.layout.fragment_intro_logo;
        } else if (PAGE_NUMBER == 3) {//last page
            id = R.layout.fragment_facebook_login;
        } else {//all the pages in between
            id = R.layout.fragment_keyboard_intro;
        }
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

        initializeThePages(PAGE_NUMBER);
        Log.d(TAG, "onCreateView: page no. " + PAGE_NUMBER);
        return rootView;
    }

    private void initializeThePages(int pageNumber) {

        if (pageNumber == 0 || pageNumber == 3) {
            if (pageNumber ==0) {
                TextView zero_screenTv = (TextView) rootView.findViewById(R.id.zero_screenTV);
                applyGradient(zero_screenTv, HORIZONTAL);
            }

            return;// we don't need to alter the views in first and the last page, so returning
        }

        ImageView introIv = (ImageView) rootView.findViewById(R.id.introIV);
        TextView introTv = (TextView) rootView.findViewById(R.id.introTV);
        applyGradient(introTv, VERTICAL);
      if (pageNumber == 1) {
            introIv.setImageResource(R.drawable.image_f_0_0_2);
            introTv.setText("Intuitive shortcut bar");
        } else if (pageNumber == 2) {
            introIv.setImageResource(R.drawable.image_f_0_0_3);
            introTv.setText("Access to useful apps");
        }
    }


    final int HORIZONTAL = 1, VERTICAL = 2;

    void applyGradient(final TextView textView, final int direction) {
        Log.d(TAG, "applyGradient: textview " + textView);
        // Log.d(TAG, "applyGradient:on page" + PAGE_NUMBER + "textViewHeight" + textView.getHeight() + "at time:" + System.currentTimeMillis());
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Context context = textView.getContext();
                Shader textShader;
                if (direction == HORIZONTAL)
                    textShader = new LinearGradient(0, 0, textView.getWidth(), 0,
                            new int[]{context.getResources().getColor(R.color.turquoise), context.getResources().getColor(R.color.pale_purple)},
                            new float[]{0, 1}, Shader.TileMode.CLAMP);
                else
                    textShader = new LinearGradient(0, 0, 0, textView.getHeight(),
                            new int[]{context.getResources().getColor(R.color.turquoise), context.getResources().getColor(R.color.pale_purple)},
                            new float[]{0, 1}, Shader.TileMode.CLAMP);

                textView.getPaint().setShader(textShader);
            }
        });

    }

    public void enableFacebookButton() {
        rootView.findViewById(R.id.facebook_login_button).setEnabled(true);

    }
}
