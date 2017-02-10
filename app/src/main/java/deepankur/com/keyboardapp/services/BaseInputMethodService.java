package deepankur.com.keyboardapp.services;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.provider.Settings.SettingNotFoundException;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.crashlytics.android.Crashlytics;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.adapters.FavouriteApplicationsListAdapter;
import deepankur.com.keyboardapp.enums.KeyBoardOptions;
import deepankur.com.keyboardapp.keyboardCustomViews.ViewController;
import io.fabric.sdk.android.Fabric;

/**
 * Created by deepankur on 1/6/17.
 */

public class BaseInputMethodService extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;

    private boolean caps = false;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public View onCreateInputView() {
        Fabric.with(this, new Crashlytics());
        View view = getLayoutInflater().inflate(R.layout.keyboard, null);
        kv = (KeyboardView) view.findViewById(R.id.keyboardView);
        kv.setTag(KeyBoardOptions.QWERTY);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        new ViewController(this, view);
        FavouriteApplicationsListAdapter.setPackages(this);
        return view;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        switch (info.inputType) {

        }
        Log.d(TAG, "onStartInputView: " + info.inputType + " restarting " + restarting);
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
        Log.d(TAG, "onFinishInput: ");
    }

    @Override
    public void onStartCandidatesView(EditorInfo info, boolean restarting) {
        // Intentionally empty
    }

    @Override
    public void onFinishCandidatesView(boolean finishingInput) {
        Log.d(TAG, "onFinishCandidatesView: " + finishingInput);
    }

    @Override
    public boolean onShowInputRequested(int flags, boolean configChange) {
        Log.d(TAG, "onShowInputRequested: flags " + flags + " configchange " + configChange);
        return super.onShowInputRequested(flags, configChange);
    }

    @Override
    public void showWindow(boolean showInput) {
        Log.d(TAG, "showWindow: " + showInput);
        super.showWindow(showInput);
    }

    @Override
    public void hideWindow() {
        super.hideWindow();
        Log.d(TAG, "hideWindow: ");
    }

    @Override
    public void onWindowShown() {
        super.onWindowShown();
        Log.d(TAG, "onWindowShown: ");
    }

    @Override
    public void onWindowHidden() {
        super.onWindowHidden();
        Log.d(TAG, "onWindowHidden: ");
    }

    @Override
    public void onBindInput() {
        super.onBindInput();
        Log.d(TAG, "onBindInput: ");
    }

    @Override
    public void onUnbindInput() {
        super.onUnbindInput();
        Log.d(TAG, "onUnbindInput: ");
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        Log.d(TAG, "onStartInput: attribute " + attribute + " restarting " + restarting);
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        Log.d(TAG, "onFinishInputView: " + finishingInput);
    }


    private void changeBrightness(int changeBrightnessByThisMuch) {
        float curBrightnessValue = 0;
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(
                    getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }

        int screen_brightness = (int) curBrightnessValue;
        android.provider.Settings.System.putInt(getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS, screen_brightness + changeBrightnessByThisMuch);
    }

    private void playClick(int keyCode) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        String s = "";
        for (int i : keyCodes) {
            s += i;
            s += ", ";
        }
        Log.d(TAG, "onKey: " + primaryCode + "keyCodes " + s);
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code), 1);
        }
    }

    private final String TAG = getClass().getSimpleName();


    @Override
    public void onPress(int primaryCode) {
        Log.d(TAG, "onPress: " + primaryCode);
    }

    @Override
    public void onRelease(int primaryCode) {
        Log.d(TAG, "onRelease: " + primaryCode);
    }

    @Override
    public void onText(CharSequence text) {
        Log.d(TAG, "onText: " + text);
    }

    @Override
    public void swipeDown() {
        Log.d(TAG, "swipeDown: ");
    }

    @Override
    public void swipeLeft() {
        Log.d(TAG, "swipeLeft: ");
    }

    @Override
    public void swipeRight() {
        Log.d(TAG, "swipeRight: ");
    }

    @Override
    public void swipeUp() {
        Log.d(TAG, "swipeUp: ");
    }
}