package com.vingeapp.android.models;

/**
 * Created by deepankursadana on 17/02/17.
 */


public class ClipBoardItemModel {

    public String id;
    public String title;
    public String description;
    public int status;
    public String created_at;

    public String getTitle() {
        return title;
    }

    // constructors
    public ClipBoardItemModel() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}