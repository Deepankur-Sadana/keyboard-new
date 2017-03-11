package com.vingeapp.android;

import android.content.SharedPreferences;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class AskPrefsImpl implements AskPrefs {
    @Override
    public boolean alwaysUseFallBackUserDictionary() {
        return false;
    }

    @Override
    public String getDomainText() {
        return null;
    }

    @Override
    public boolean alwaysHideLanguageKey() {
        return false;
    }

    @Override
    public boolean getShowKeyPreview() {
        return false;
    }

    @Override
    public boolean getShowKeyboardNameText() {
        return false;
    }

    @Override
    public boolean getShowHintTextOnKeys() {
        return false;
    }

    @Override
    public boolean getUseCustomHintAlign() {
        return false;
    }

    @Override
    public int getCustomHintAlign() {
        return 0;
    }

    @Override
    public int getCustomHintVAlign() {
        return 0;
    }

    @Override
    public AnimationsLevel getAnimationsLevel() {
        return null;
    }

    @Override
    public boolean getSwitchKeyboardOnSpace() {
        return false;
    }

    @Override
    public boolean getUseFullScreenInputInLandscape() {
        return false;
    }

    @Override
    public boolean getUseFullScreenInputInPortrait() {
        return false;
    }

    @Override
    public boolean getUseRepeatingKeys() {
        return false;
    }

    @Override
    public float getKeysHeightFactorInPortrait() {
        return 0;
    }

    @Override
    public float getKeysHeightFactorInLandscape() {
        return 0;
    }

    @Override
    public boolean getInsertSpaceAfterCandidatePick() {
        return false;
    }

    @Override
    public int getGestureSwipeUpKeyCode(boolean fromSpaceBar) {
        return 0;
    }

    @Override
    public int getGestureSwipeDownKeyCode() {
        return 0;
    }

    @Override
    public int getGestureSwipeLeftKeyCode(boolean fromSpaceBar, boolean withTwoFingers) {
        return 0;
    }

    @Override
    public int getGestureSwipeRightKeyCode(boolean fromSpaceBar, boolean withTwoFingers) {
        return 0;
    }

    @Override
    public int getGesturePinchKeyCode() {
        return 0;
    }

    @Override
    public int getGestureSeparateKeyCode() {
        return 0;
    }

    @Override
    public boolean getActionKeyInvisibleWhenRequested() {
        return false;
    }

    @Override
    public boolean isDoubleSpaceChangesToPeriod() {
        return false;
    }

    @Override
    public boolean shouldShowPopupForLanguageSwitch() {
        return false;
    }

    @Override
    public boolean supportPasswordKeyboardRowMode() {
        return false;
    }

    @Override
    public boolean hideSoftKeyboardWhenPhysicalKeyPressed() {
        return false;
    }

    @Override
    public boolean use16KeysSymbolsKeyboards() {
        return false;
    }

    @Override
    public boolean useBackword() {
        return false;
    }

    @Override
    public boolean getCycleOverAllSymbols() {
        return false;
    }

    @Override
    public boolean useVolumeKeyForLeftRight() {
        return false;
    }

    @Override
    public boolean useCameraKeyForBackspaceBackword() {
        return false;
    }

    @Override
    public boolean useContactsDictionary() {
        return false;
    }

    @Override
    public int getAutoDictionaryInsertionThreshold() {
        return 0;
    }

    @Override
    public boolean isStickyExtensionKeyboard() {
        return false;
    }

    @Override
    public int getSwipeVelocityThreshold() {
        return 0;
    }

    @Override
    public int getSwipeDistanceThreshold() {
        return 0;
    }

    @Override
    public int getLongPressTimeout() {
        return 0;
    }

    @Override
    public int getMultiTapTimeout() {
        return 0;
    }

    @Override
    public boolean workaround_alwaysUseDrawText() {
        return false;
    }

    @Override
    public boolean useChewbaccaNotifications() {
        return false;
    }

    @Override
    public boolean showKeyPreviewAboveKey() {
        return false;
    }

    @Override
    public void addChangedListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void removeChangedListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public boolean shouldSwapPunctuationAndSpace() {
        return false;
    }

    @Override
    public int getFirstAppVersionInstalled() {
        return 0;
    }

    @Override
    public long getFirstTimeAppInstalled() {
        return 0;
    }

    @Override
    public long getTimeCurrentVersionInstalled() {
        return 0;
    }

    @Override
    public boolean getPersistLayoutForPackageId() {
        return false;
    }
}
