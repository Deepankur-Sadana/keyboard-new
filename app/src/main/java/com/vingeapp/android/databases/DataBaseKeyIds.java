package com.vingeapp.android.databases;

/**
 * Created by deepankursadana on 20/02/17.
 */

public interface DataBaseKeyIds {
    // Logcat tag
//    String LOG = DatabaseHelper.class.getName();

    // Database Version
    int DATABASE_VERSION = 1;

    // Database Name
    String DATABASE_NAME = "contactsManager";

    // Table Names
    String TABLE_CLIPBOARD = "clipboard";
    String TABLE_TAG = "tags";
    String TABLE_CLIPBOARD_TAG = "todo_tags";

    // Common column names
    String KEY_ID = "id";
    String KEY_CREATED_AT = "created_at";

    // NOTES Table - column nmaes
//    String KEY_CLIPBOARD = "todo";
    String KEY_STATUS = "status";
    String KEY_TITLE="title";
    String KEY_DESCRIPTION = "description";

    // TAGS Table - column names
    String KEY_TAG_NAME = "tag_name";

    // NOTE_TAGS Table - column names
    String KEY_CLIPBOARD_ID = "todo_id";
    String KEY_TAG_ID = "tag_id";
}
