package com.example.finalhomework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 数据库名称和版本
    private static final String DATABASE_NAME = "comments.db";
    private static final int DATABASE_VERSION = 2;

    // 评论表名和字段名
    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_LIKE_COUNT = "like_count";
    public static final String COLUMN_IS_LIKED = "is_liked";
    public static final String COLUMN_ACTIVITY_ID = "activity_id"; // 新增字段

    // 数据库创建语句
    private static final String CREATE_TABLE_COMMENTS = "CREATE TABLE " + TABLE_COMMENTS + " ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_AUTHOR + " TEXT, "
            + COLUMN_CONTENT + " TEXT, "
            + COLUMN_TIME + " TEXT, "
            + COLUMN_LIKE_COUNT + " INTEGER, "
            + COLUMN_IS_LIKED + " INTEGER, "
            + COLUMN_ACTIVITY_ID + " TEXT);"; // 添加活动ID字段

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(CREATE_TABLE_COMMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // 升级时，添加活动ID字段
            String addColumnQuery = "ALTER TABLE " + TABLE_COMMENTS + " ADD COLUMN " + COLUMN_ACTIVITY_ID + " TEXT";
            db.execSQL(addColumnQuery);
        }
    }
}
