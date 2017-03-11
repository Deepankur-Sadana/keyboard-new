package com.vingeapp.android.models;

/**
 * Created by deepankursadana on 17/02/17.
 */


public class ClipBoardItemModel {

    long id;
    String title;
    String description;
    int status;
    String created_at;

    public String getTitle() {
        return title;
    }

    // constructors
    public ClipBoardItemModel() {
    }

    public ClipBoardItemModel(String description, int status) {
        this.description = description;
        this.status = status;
    }

    public ClipBoardItemModel(int id, String description, int status) {
        this.id = id;
        this.description = description;
        this.status = status;
    }

    // setters
    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public int getStatus() {
        return this.status;
    }
}