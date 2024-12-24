package com.example.finalhomework.util_classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finalhomework.model.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDBHelper extends SQLiteOpenHelper {

    // 数据库名称和版本号
    private static final String DATABASE_NAME = "schedule_db";
    private static final int DATABASE_VERSION = 1;

    // 表名
    public static final String TABLE_SCHEDULE = "schedules";

    // 表的列名
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_CONTENT = "content";

    // 创建表的SQL语句
    private static final String CREATE_TABLE_SCHEDULE =
            "CREATE TABLE " + TABLE_SCHEDULE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DURATION + " TEXT, " +
                    COLUMN_CONTENT + " TEXT);";

    public ScheduleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 当数据库第一次创建时调用此方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SCHEDULE); // 创建schedule表
    }

    // 当数据库版本变化时调用此方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果需要进行数据库升级，可以在此执行修改表结构的操作
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        onCreate(db);
    }

    // 插入一个新的日程
    public long insertSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, schedule.getTitle());
        values.put(COLUMN_DURATION, schedule.getDuration());
        values.put(COLUMN_CONTENT, schedule.getContent());

        // 插入数据并返回插入的行号（id）
        return db.insert(TABLE_SCHEDULE, null, values);
    }

    // 根据ID获取单个日程
    public Schedule getScheduleById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询日程
        Cursor cursor = db.query(TABLE_SCHEDULE, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DURATION, COLUMN_CONTENT},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Schedule schedule = new Schedule(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DURATION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
            );
            cursor.close();
            return schedule;
        } else {
            return null; // 如果未找到该日程，返回null
        }
    }

    // 获取所有日程
    public List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询所有日程
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCHEDULE, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Schedule schedule = new Schedule(
                            cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DURATION)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
                    );
                    schedules.add(schedule);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return schedules;
    }

    // 删除日程
    public void deleteSchedule(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCHEDULE, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    // 更新日程
    public int updateSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, schedule.getTitle());
        values.put(COLUMN_DURATION, schedule.getDuration());
        values.put(COLUMN_CONTENT, schedule.getContent());

        return db.update(TABLE_SCHEDULE, values, COLUMN_ID + "=?", new String[]{String.valueOf(schedule.getId())});
    }
}
