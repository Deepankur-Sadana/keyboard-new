package com.vingeapp.android.setup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import net.evendanan.chauffeur.lib.FragmentChauffeurActivity;
import net.evendanan.chauffeur.lib.experiences.TransitionExperiences;

import com.vingeapp.android.R;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";
    private AnimationDrawable mNotConfiguredAnimation = null;
    private AsyncTask<Bitmap, Void, Palette.Swatch> mPaletteTask;
//    private DemoAnyKeyboardView mDemoAnyKeyboardView;

    public static void setupLink(View root, int showMoreLinkId, ClickableSpan clickableSpan, boolean reorderLinkToLastChild) {
        TextView clickHere = (TextView) root.findViewById(showMoreLinkId);
        if (reorderLinkToLastChild) {
            ViewGroup rootContainer = (ViewGroup) root;
            rootContainer.removeView(clickHere);
            rootContainer.addView(clickHere);
        }

        SpannableStringBuilder sb = new SpannableStringBuilder(clickHere.getText());
        sb.clearSpans();//removing any previously (from instance-state) set click spans.
        sb.setSpan(clickableSpan, 0, clickHere.getText().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        clickHere.setMovementMethod(LinkMovementMethod.getInstance());
        clickHere.setText(sb);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            //I to prevent leaks and duplicate ID errors, I must use the getChildFragmentManager
            //to add the inner fragments into the UI.
            //See: https://github.com/AnySoftKeyboard/AnySoftKeyboard/issues/285
//            FragmentManager fragmentManager = getChildFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.change_log_fragment, new ChangeLogFragment.CardedChangeLogFragment())
//                    .commit();
        }
//        View testingView = view.findViewById(R.id.testing_build_message);
//        testingView.setVisibility(BuildConfig.TESTING_BUILD ? View.VISIBLE : View.GONE);
//        mDemoAnyKeyboardView = (DemoAnyKeyboardView) view.findViewById(R.id.demo_keyboard_view);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //I'm doing the setup of the link in onViewStateRestored, since the links will be restored too
        //and they will probably refer to a different scoop (Fragment).
        //setting up the underline and click handler in the keyboard_not_configured_box layout
        TextView clickHere = (TextView) getView().findViewById(R.id.not_configured_click_here);
        mNotConfiguredAnimation = clickHere.getVisibility() == View.VISIBLE ?
                (AnimationDrawable) clickHere.getCompoundDrawables()[0] : null;

        String fullText = getString(R.string.not_configured_with_click_here);
        String justClickHereText = getString(R.string.not_configured_with_just_click_here);
        SpannableStringBuilder sb = new SpannableStringBuilder(fullText);
        // Get the index of "click here" string.
        int start = fullText.indexOf(justClickHereText);
        int length = justClickHereText.length();
        if (start == -1) {
            //this could happen when the localization is not correct
            start = 0;
            length = fullText.length();
        }
        ClickableSpan csp = new ClickableSpan() {
            @Override
            public void onClick(View v) {
                FragmentChauffeurActivity activity = (FragmentChauffeurActivity) getActivity();
                activity.addFragmentToUi(new SetUpKeyboardWizardFragment(), TransitionExperiences.DEEPER_EXPERIENCE_TRANSITION);
            }
        };
        sb.setSpan(csp, start, start + length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        clickHere.setMovementMethod(LinkMovementMethod.getInstance());
        clickHere.setText(sb);
    }

    @Override
    public void onStart() {
        super.onStart();
//        MainSettingsActivity.setActivityTitle(this, getString(R.string.how_to_pointer_title));

        View notConfiguredBox = getView().findViewById(R.id.not_configured_click_here);
        //checking if the IME is configured
        final Context context = getActivity().getApplicationContext();

        if (SetupSupport.isThisKeyboardSetAsDefaultIME(context)) {
            notConfiguredBox.setVisibility(View.GONE);
        } else {
            notConfiguredBox.setVisibility(View.VISIBLE);
        }

        mPaletteTask = new AsyncTask<Bitmap, Void, Palette.Swatch>() {
            @Override
            protected Palette.Swatch doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                Palette p = Palette.from(bitmap).generate();
                Palette.Swatch highestSwatch = null;
                for (Palette.Swatch swatch : p.getSwatches()) {
                    if (highestSwatch == null || highestSwatch.getPopulation() < swatch.getPopulation())
                        highestSwatch = swatch;
                }
                return highestSwatch;
            }

            @Override
            protected void onPostExecute(Palette.Swatch swatch) {
                super.onPostExecute(swatch);
                if (!isCancelled()) {
                    final View rootView = getView();
                    if (swatch != null && rootView != null) {
                        final int backgroundRed = Color.red(swatch.getRgb());
                        final int backgroundGreed = Color.green(swatch.getRgb());
                        final int backgroundBlue = Color.blue(swatch.getRgb());
                        final int backgroundColor = Color.argb(200/*~80% alpha*/, backgroundRed, backgroundGreed, backgroundBlue);
//                        TextView gplusLink = (TextView) rootView.findViewById(R.id.ask_gplus_link);
//                        gplusLink.setTextColor(swatch.getTitleTextColor());
//                        gplusLink.setBackgroundColor(backgroundColor);
                    }
                }
            }
        };

        if (mNotConfiguredAnimation != null)
            mNotConfiguredAnimation.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPaletteTask.cancel(false);
        mPaletteTask = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}