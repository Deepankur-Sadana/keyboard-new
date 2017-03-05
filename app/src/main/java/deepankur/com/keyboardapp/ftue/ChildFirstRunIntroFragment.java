package deepankur.com.keyboardapp.ftue;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.fragments.BaseFragment;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class ChildFirstRunIntroFragment extends BaseFragment {
    int PAGE_NUMBER;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PAGE_NUMBER = getArguments().getInt("page_number", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_time_user_experiece, container, false);
        if (PAGE_NUMBER == 0) {
            rootView.findViewById(R.id.zero_screen_layout).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.subsequent_screen_layout).setVisibility(View.GONE);
            applyGradient((TextView) rootView.findViewById(R.id.zero_screenTV));
        }else {
            rootView.findViewById(R.id.zero_screen_layout).setVisibility(View.GONE);
            rootView.findViewById(R.id.subsequent_screen_layout).setVisibility(View.VISIBLE);
            applyGradient((TextView) rootView.findViewById(R.id.subsequent_screenTV));
        }
        return rootView;
    }

    void applyGradient(TextView textView){
        Shader textShader=new LinearGradient(0, 0, 0, 20,
                new int[]{Color.GREEN,Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);
    }
}
