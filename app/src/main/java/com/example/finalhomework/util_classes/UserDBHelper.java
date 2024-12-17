package com.example.finalhomework.util_classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class UserDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "hangzhou.db";
    private static final String TABLE_NAME = "users";
    private static final int DB_VERSION = 2;
    private static UserDBHelper mHelper = null;
    //数据库读写连接
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;


    private static final String CREATE_USERS =
            "CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME + "(" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," + // 添加 user_id 列，设置为主键
                    "account VARCHAR NOT NULL," +
                    "password VARCHAR NOT NULL," +
                    "nickname VARCHAR)";


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
            mRDB = mHelper.getReadableDatabase();
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
        if (oldVersion < 2) {
            // 增加 user_id 列，注意 SQLite 不支持 ALTER TABLE 直接增加列类型，所以需要重新创建表
            String upgradeQuery = "CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME + "_new (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," + // 添加 user_id 列
                    "account VARCHAR NOT NULL," +
                    "password VARCHAR NOT NULL," +
                    "nickname VARCHAR)";

            // 创建新表
            db.execSQL(upgradeQuery);

            // 将旧表数据迁移到新表
            db.execSQL("INSERT INTO " + TABLE_NAME + "_new (account, password, nickname) SELECT account, password, nickname FROM " + TABLE_NAME);

            // 删除旧表
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            // 将新表重命名为原来的表名
            db.execSQL("ALTER TABLE " + TABLE_NAME + "_new RENAME TO " + TABLE_NAME);
        }
    }


    public long register(User user){
        ContentValues values = new ContentValues();
        values.put("account",user.getAccountString());
        values.put("password",user.getAccountString());
        values.put("nickname",user.getNickNameString());

        long user_registry = mWDB.insert(TABLE_NAME,null,values);
        return user_registry;
    }

    public User login(String accountInput, String passwordInput){
        User user = new User();
        Cursor cursor = mRDB.query(TABLE_NAME,null,"account like ?",new String[]{accountInput},null,null,null);
        if (cursor!= null){
            while(cursor.moveToNext()){
                user.setAccountString(cursor.getString(0));
                user.setPasswordString(cursor.getString(1));
                user.setNickNameString(cursor.getString(2));
            }
            return user;
        }
        return null;
    }

    public String getUsernameByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
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
