package deepankur.com.keyboardapp.ftue;

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

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.fragments.BaseFragment;
import deepankur.com.keyboardapp.setup.MainSettingsActivity;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class ChildFirstRunIntroFragment extends BaseFragment {
    int PAGE_NUMBER;
    final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PAGE_NUMBER = getArguments().getInt("page_number", 0);
        Log.d(TAG, "onCreate: " + PAGE_NUMBER);

    }

    public void setPAGE_NUMBER(int PAGE_NUMBER) {
        this.PAGE_NUMBER = PAGE_NUMBER;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(PAGE_NUMBER != 3 ? R.layout.fragment_first_time_user_experiece : R.layout.fragment_facebook_login, container, false);
        if (PAGE_NUMBER == 0) {
            rootView.findViewById(R.id.zero_screen_layout).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.subsequent_screen_layout).setVisibility(View.GONE);
            applyGradient((TextView) rootView.findViewById(R.id.zero_screenTV));
        } else if (PAGE_NUMBER == 3) {
            rootView.findViewById(R.id.facebook_login_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainSettingsActivity) getActivity()).onFirstIntroDone();
                }
            });
        } else {
            rootView.findViewById(R.id.zero_screen_layout).setVisibility(View.GONE);
            rootView.findViewById(R.id.subsequent_screen_layout).setVisibility(View.VISIBLE);
            applyGradient((TextView) rootView.findViewById(R.id.subsequent_screenTV));
        }
        return rootView;
    }

    void applyGradient(TextView textView) {
        Shader textShader = new LinearGradient(0, 0, 0, 20,
                new int[]{Color.GREEN, Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);
    }
}
