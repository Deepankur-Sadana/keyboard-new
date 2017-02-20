package deepankur.com.keyboardapp.databases;

/**
 * Created by deepankursadana on 20/02/17.
 */

public interface DataBaseKeyIds {
    // Logcat tag
    String LOG = DatabaseHelper.class.getName();

    // Database Version
    int DATABASE_VERSION = 1;

    // Database Name
    String DATABASE_NAME = "contactsManager";

    // Table Names
    String TABLE_TODO = "todos";
    String TABLE_TAG = "tags";
    String TABLE_TODO_TAG = "todo_tags";

    // Common column names
    String KEY_ID = "id";
    String KEY_CREATED_AT = "created_at";

    // NOTES Table - column nmaes
    String KEY_TODO = "todo";
    String KEY_STATUS = "status";

    // TAGS Table - column names
    String KEY_TAG_NAME = "tag_name";

    // NOTE_TAGS Table - column names
    String KEY_TODO_ID = "todo_id";
    String KEY_TAG_ID = "tag_id";
}
