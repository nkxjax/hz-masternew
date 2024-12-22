package com.example.finalhomework.util_classes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import androidx.annotation.Nullable;


public class UserDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "hangzhou.db";
    private static final String TABLE_NAME = "users";
    private static final int DB_VERSION = 4;
    private static UserDBHelper mHelper = null;
    //数据库读写连接
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;


    private static final String CREATE_USERS =
            "CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME + "(" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "account VARCHAR NOT NULL," +
                    "password VARCHAR NOT NULL," +
                    "nickname VARCHAR," +
                    "is_admin INTEGER DEFAULT 0)";


    public UserDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //单例对象
    public static UserDBHelper getInstance(Context context){
        if(mHelper == null){
            mHelper = new UserDBHelper(context);
        }
        return mHelper;
    }

    //打开数据库读连接
    public SQLiteDatabase openReadLink(){
        if(mRDB == null || !mRDB.isOpen()){
            mRDB = mHelper.getWritableDatabase();
        }
        return mRDB;
    }

    //打开数据库写连接
    public SQLiteDatabase openWriteLink(){
        if(mWDB == null || !mWDB.isOpen()){
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    //关闭连接
    public void closeLink(){
        if(mRDB != null && mRDB.isOpen()){
            mRDB.close();
            mRDB = null;
        }

        if(mWDB != null && mWDB.isOpen()){
            mWDB.close();
            mWDB = null;
        }
    }

    //创建数据库，建立表格
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS);
    }

    //重建数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            // 增加 user_id 列，注意 SQLite 不支持 ALTER TABLE 直接增加列类型，所以需要重新创建表
            String upgradeQuery = "CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME + "_new (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," + // 添加 user_id 列
                    "account VARCHAR NOT NULL," +
                    "password VARCHAR NOT NULL," +
                    "nickname VARCHAR," +
                    "is_admin INTEGER DEFAULT 0)";
            // 创建新表
            db.execSQL(upgradeQuery);

            // 将旧表数据迁移到新表
            db.execSQL("INSERT INTO " + TABLE_NAME + "_new (account, password, nickname, is_admin) SELECT account, password, nickname, is_admin FROM " + TABLE_NAME);

            // 删除旧表
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            // 将新表重命名为原来的表名
            db.execSQL("ALTER TABLE " + TABLE_NAME + "_new RENAME TO " + TABLE_NAME);
        }
    }


    public long register(User user) {
        ContentValues values = new ContentValues();
        values.put("account", user.getAccountString());
        values.put("password", user.getPasswordString());
        values.put("nickname", user.getNickNameString());
        values.put("is_admin", 0); // 设置 is_admin 列

        long userRegistry = mWDB.insert(TABLE_NAME, null, values);
        return userRegistry;
    }

    @SuppressLint("Range")
    public User login(String accountInput, String passwordInput) {
        User user = null;

        // 先判断输入是否为空
        if (TextUtils.isEmpty(accountInput) || TextUtils.isEmpty(passwordInput)) {
            return null;  // 如果为空，直接返回 null
        }

        // 查询数据库，检查是否有该账号的记录
        Cursor cursor = null;
        try {
            cursor = mRDB.query(TABLE_NAME, null, "account = ?", new String[]{accountInput}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // 获取用户信息
                user = new User();
                user.setAccountString(cursor.getString(cursor.getColumnIndex("account")));
                user.setPasswordString(cursor.getString(cursor.getColumnIndex("password")));
                user.setNickNameString(cursor.getString(cursor.getColumnIndex("nickname")));
                user.setIsAdmin(cursor.getInt(cursor.getColumnIndex("is_admin")));  // 获取 is_admin

                // 密码验证，如果不匹配，返回 null
                if (!user.getPasswordString().equals(passwordInput)) {
                    user = null;  // 密码错误，返回 null
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  // 打印异常信息到 Logcat
            return null;  // 如果发生异常，返回 null
        } finally {
            if (cursor != null) {
                cursor.close();  // 确保 cursor 被关闭，防止内存泄漏
            }
        }

        return user;
    }




    @SuppressLint("Range")
    public String getUsernameByUserId(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 使用 user_id 进行查询
        Cursor cursor = db.rawQuery("SELECT nickname FROM " + TABLE_NAME + " WHERE user_id = ?", new String[]{String.valueOf(userId)});

        String username = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                username = cursor.getString(cursor.getColumnIndex("nickname"));
            }
            cursor.close();
        }
        db.close();
        return username;
    }


}