//package com.vingeapp.android.databases;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import com.vingeapp.android.models.ClipBoardItemModel;
//import com.vingeapp.android.models.Tag;
//
///**
// * Created by deepankursadana on 17/02/17.
// */
//
//public class DatabaseHelper extends SQLiteOpenHelper implements DataBaseKeyIds {
//
//    // Table Create Statements
//    // ClipBoardItemModel table create statement
//    private static final String CREATE_TABLE_CLIPBOARD = "CREATE TABLE "
//            + TABLE_CLIPBOARD + "(" +
//            KEY_ID + " INTEGER PRIMARY KEY," +
//            KEY_TITLE + " TEXT," +
//            KEY_DESCRIPTION + " TEXT," +
//            KEY_STATUS + " INTEGER," +
//            KEY_CREATED_AT + " DATETIME"
//            + ")";
//
//    // Tag table create statement
//    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG
//            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT,"
//            + KEY_CREATED_AT + " DATETIME" + ")";
//
//    // todo_tag table create statement
//    private static final String CREATE_TABLE_CLIPBOARD_TAG = "CREATE TABLE "
//            + TABLE_CLIPBOARD_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
//            + KEY_CLIPBOARD_ID + " INTEGER," + KEY_TAG_ID + " INTEGER,"
//            + KEY_CREATED_AT + " DATETIME" + ")";
//
//    private DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    private static DatabaseHelper sDatabaseHelper;
//
//    public synchronized static DatabaseHelper getDataBaseHelper(Context context) {
//        if (sDatabaseHelper == null)
//            sDatabaseHelper = new DatabaseHelper(context);
//        return sDatabaseHelper;
//    }
//
//
//    private static final String TAG = DatabaseHelper.class.getSimpleName();
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//        Log.d(TAG, "onCreate: ");
//        // creating required tables
//        db.execSQL(CREATE_TABLE_CLIPBOARD);
//        db.execSQL(CREATE_TABLE_TAG);
//        db.execSQL(CREATE_TABLE_CLIPBOARD_TAG);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Log.d(TAG, "onUpgrade: " + db);
//        // on upgrade drop older tables
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIPBOARD);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIPBOARD_TAG);
//
//        // create new tables
//        onCreate(db);
//    }
//
//    // ------------------------ "todos" table methods ----------------//
//
//    /**
//     * Creating a todo
//     */
//    public long createClipboardItem(ClipBoardItemModel todo, long[] tag_ids) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_STATUS, todo.getStatus());
//        values.put(KEY_CREATED_AT, getDateTime());
//        values.put(KEY_TITLE, todo.getTitle());
//        values.put(KEY_DESCRIPTION, todo.getDescription());
//
//        // insert row
//        long todo_id = db.insert(TABLE_CLIPBOARD, null, values);
//
//        // insert tag_ids
//        for (long tag_id : tag_ids) {
//            createClipboardTag(todo_id, tag_id);
//        }
//
//        todo.setId(todo_id);
//        return todo_id;
//    }
//
//    /**
//     * get single todo
//     */
//    public ClipBoardItemModel getTodo(long todo_id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String selectQuery = "SELECT  * FROM " + TABLE_CLIPBOARD + " WHERE "
//                + KEY_ID + " = " + todo_id;
//
//        Log.e(LOG, selectQuery);
//
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        if (c != null)
//            c.moveToFirst();
//
//        ClipBoardItemModel td = new ClipBoardItemModel();
//        td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
//        td.setDescription((c.getString(c.getColumnIndex(KEY_DESCRIPTION))));
//        td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
//
//        return td;
//    }
//
//    /**
//     * getting all todos
//     */
//    public List<ClipBoardItemModel> getAllToDos() {
//        List<ClipBoardItemModel> todos = new ArrayList<ClipBoardItemModel>();
//        String selectQuery = "SELECT  * FROM " + TABLE_CLIPBOARD;
//
//        Log.e(LOG, selectQuery);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                ClipBoardItemModel td = new ClipBoardItemModel();
//                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
//                td.setDescription((c.getString(c.getColumnIndex(KEY_DESCRIPTION))));
//                td.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));
//                td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
//
//                // adding to todo list
//                todos.add(td);
//            } while (c.moveToNext());
//        }
//
//        return todos;
//    }
//
//    /**
//     * getting all todos under single tag
//     */
//    public List<ClipBoardItemModel> getAllToDosByTag(String tag_name) {
//        List<ClipBoardItemModel> todos = new ArrayList<ClipBoardItemModel>();
//
//        String selectQuery = "SELECT  * FROM " + TABLE_CLIPBOARD + " td, "
//                + TABLE_TAG + " tg, " + TABLE_CLIPBOARD_TAG + " tt WHERE tg."
//                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
//                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
//                + "tt." + KEY_CLIPBOARD_ID;
//
//        Log.e(LOG, selectQuery);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                ClipBoardItemModel td = new ClipBoardItemModel();
//                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
//                td.setDescription((c.getString(c.getColumnIndex(KEY_DESCRIPTION))));
//                td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
//
//                // adding to todo list
//                todos.add(td);
//            } while (c.moveToNext());
//        }
//
//        return todos;
//    }
//
//    /**
//     * getting todo count
//     */
//    public int getToDoCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_CLIPBOARD;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        int count = cursor.getCount();
//        cursor.close();
//
//        // return count
//        return count;
//    }
//
//    /**
//     * Updating a todo
//     */
//    public int updateToDo(ClipBoardItemModel todo) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_DESCRIPTION, todo.getDescription());
//        values.put(KEY_TITLE, todo.getTitle());
//        values.put(KEY_STATUS, todo.getStatus());
//
//        // updating row
//        return db.update(TABLE_CLIPBOARD, values, KEY_ID + " = ?",
//                new String[]{String.valueOf(todo.getId())});
//    }
//
//    /**
//     * Deleting a todo
//     */
//    public void deleteToDo(long tado_id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CLIPBOARD, KEY_ID + " = ?",
//                new String[]{String.valueOf(tado_id)});
//    }
//
//    // ------------------------ "tags" table methods ----------------//
//
//    /**
//     * Creating tag
//     */
//    public long createTag(Tag tag) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_TAG_NAME, tag.getTagName());
//        values.put(KEY_CREATED_AT, getDateTime());
//
//        // insert row
//        long tag_id = db.insert(TABLE_TAG, null, values);
//
//        return tag_id;
//    }
//
//    /**
//     * getting all tags
//     */
//    public List<Tag> getAllTags() {
//        List<Tag> tags = new ArrayList<>();
//        String selectQuery = "SELECT  * FROM " + TABLE_TAG;
//
//        Log.e(LOG, selectQuery);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                Tag t = new Tag();
//                t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
//                t.setTagName(c.getString(c.getColumnIndex(KEY_TAG_NAME)));
//
//                // adding to tags list
//                tags.add(t);
//            } while (c.moveToNext());
//        }
//        return tags;
//    }
//
//    /**
//     * Updating a tag
//     */
//    public int updateTag(Tag tag) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_TAG_NAME, tag.getTagName());
//
//        // updating row
//        return db.update(TABLE_TAG, values, KEY_ID + " = ?",
//                new String[]{String.valueOf(tag.getId())});
//    }
//
//    /**
//     * Deleting a tag
//     */
//    public void deleteTag(Tag tag, boolean should_delete_all_tag_todos) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // before deleting tag
//        // check if todos under this tag should also be deleted
//        if (should_delete_all_tag_todos) {
//            // get all todos under this tag
//            List<ClipBoardItemModel> allTagToDos = getAllToDosByTag(tag.getTagName());
//
//            // delete all todos
//            for (ClipBoardItemModel keyValueShortcut : allTagToDos) {
//                // delete keyValueShortcut
//                deleteToDo(keyValueShortcut.getId());
//            }
//        }
//
//        // now delete the tag
//        db.delete(TABLE_TAG, KEY_ID + " = ?",
//                new String[]{String.valueOf(tag.getId())});
//    }
//
//    // ------------------------ "todo_tags" table methods ----------------//
//
//    /**
//     * Creating todo_tag
//     */
//    public long createClipboardTag(long todo_id, long tag_id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_CLIPBOARD_ID, todo_id);
//        values.put(KEY_TAG_ID, tag_id);
//        values.put(KEY_CREATED_AT, getDateTime());
//
//        long id = db.insert(TABLE_CLIPBOARD_TAG, null, values);
//
//        return id;
//    }
//
//    /**
//     * Updating a todo tag
//     */
//    public int updateNoteTag(long id, long tag_id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_TAG_ID, tag_id);
//
//        // updating row
//        return db.update(TABLE_CLIPBOARD, values, KEY_ID + " = ?",
//                new String[]{String.valueOf(id)});
//    }
//
//    /**
//     * Deleting a todo tag
//     */
//    public void deleteToDoTag(long id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CLIPBOARD, KEY_ID + " = ?",
//                new String[]{String.valueOf(id)});
//    }
//
//    // closing database
//    public void closeDB() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        if (db != null && db.isOpen())
//            db.close();
//    }
//
//    /**
//     * get datetime
//     */
//    private String getDateTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Date date = new Date();
//        return dateFormat.format(date);
//    }
//}
