package com.vingeapp.android.models;

import com.vingeapp.android.enums.KeyBoardOptions;

/**
 * Created by deepankursadana on 23/07/17.
 */

public class TabModel {
    private int resourceId;
    private KeyBoardOptions keyBoardOptions;
    private boolean isSelected;

    public TabModel(int resourceId, KeyBoardOptions keyBoardOptions, boolean isSelected) {
        this.resourceId = resourceId;
        this.keyBoardOptions = keyBoardOptions;
        this.isSelected = isSelected;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public KeyBoardOptions getKeyBoardOptions() {
        return keyBoardOptions;
    }

    public void setKeyBoardOptions(KeyBoardOptions keyBoardOptions) {
        this.keyBoardOptions = keyBoardOptions;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
