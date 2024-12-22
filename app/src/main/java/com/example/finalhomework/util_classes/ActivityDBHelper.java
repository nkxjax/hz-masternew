package com.example.finalhomework.util_classes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fun_activities.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "activities";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_IMG = "img_res_id";  // 存储图片的字节数据

    public ActivityDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_CONTENT + " TEXT, "
                + COLUMN_IMG + " BLOB" + ");";  // 使用 BLOB 类型存储图片字节数据
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 插入活动数据
    public long insertActivity(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_NAME, null, values);
//        db.close();
        return result;
    }

    // 获取所有活动
    @SuppressLint("Range")
    public List<Map<String, Object>> getAllActivities() {
        List<Map<String, Object>> activities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询活动表中的所有数据
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Map<String, Object> activity = new HashMap<>();
                activity.put(COLUMN_ID, cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                activity.put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                activity.put(COLUMN_DATE, cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                activity.put(COLUMN_CONTENT, cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));

                // 获取图片字节数据
                byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMG));
                activity.put(COLUMN_IMG, imageByteArray);

                activities.add(activity);
            } while (cursor.moveToNext());

            cursor.close();
        }

//        db.close();
        return activities;
    }
}
