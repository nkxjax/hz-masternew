package com.example.finalhomework.util_classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AttractionDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "attractions.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ATTRACTIONS = "attractions";

    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;
    private static AttractionDBHelper mHelper;

    private static final String CREATE_ATTRACTIONS =
            "CREATE TABLE IF NOT EXISTS " +
                    TABLE_ATTRACTIONS + " (" +
                    "attraction_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name VARCHAR NOT NULL," +
                    "location VARCHAR NOT NULL," +
                    "description TEXT," +
                    "ticket_price DECIMAL," +
                    "open_time TIME," +
                    "close_time TIME)";

    public AttractionDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        if (db == null) {
            Log.e("AttractionDBHelper", "Failed to get writable database");
        }
        return db;
    }


    public static AttractionDBHelper getInstance(Context context) {
        if (mHelper == null) {
            if (context == null) {
                Log.e("AttractionDBHelper", "Context is null when getting instance");
            } else {
                mHelper = new AttractionDBHelper(context);
                Log.d("AttractionDBHelper", "AttractionDBHelper instance created with context: " + context);
            }
        }
        return mHelper;
    }


    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }

        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ATTRACTIONS);
        // 插入初始数据
        db.execSQL("INSERT INTO attractions (name, location, description, ticket_price, open_time, close_time) " +
                "VALUES ('九寨沟', '四川省阿坝州九寨沟县', '九寨沟是世界自然遗产，以其壮观的山水景色著称。', 169.00, '08:00', '17:00')");
        db.execSQL("INSERT INTO attractions (name, location, description, ticket_price, open_time, close_time) " +
                "VALUES ('黄山风景区', '安徽省黄山市', '黄山以奇松、怪石、云海、温泉、冬雪“五绝”闻名于世。', 190.00, '06:30', '17:30')");
        db.execSQL("INSERT INTO attractions (name, location, description, ticket_price, open_time, close_time) " +
                "VALUES ('西湖', '浙江省杭州市', '西湖是中国著名的淡水湖之一，素以美景与历史文化闻名。', 0.00, '全天开放', '全天开放')");
        db.execSQL("INSERT INTO attractions (name, location, description, ticket_price, open_time, close_time) " +
                "VALUES ('张家界国家森林公园', '湖南省张家界市', '中国第一个国家森林公园，因其独特的砂岩峰林地貌而著称。', 225.00, '07:00', '18:00')");
        db.execSQL("INSERT INTO attractions (name, location, description, ticket_price, open_time, close_time) " +
                "VALUES ('桂林漓江', '广西壮族自治区桂林市', '漓江两岸山水如画，被誉为“山水甲天下”。', 100.00, '07:30', '18:00')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTRACTIONS);
        onCreate(db);
    }

    public Attraction getAttractionByName(String name) {
        Attraction attraction = null;
        Cursor cursor = mRDB.query(TABLE_ATTRACTIONS, null, "name = ?", new String[]{name}, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndexOrThrow("attraction_id");
                    int nameIndex = cursor.getColumnIndexOrThrow("name");
                    int locationIndex = cursor.getColumnIndexOrThrow("location");
                    int descriptionIndex = cursor.getColumnIndexOrThrow("description");
                    int priceIndex = cursor.getColumnIndexOrThrow("ticket_price");
                    int openTimeIndex = cursor.getColumnIndexOrThrow("open_time");
                    int closeTimeIndex = cursor.getColumnIndexOrThrow("close_time");

                    attraction = new Attraction();
                    attraction.setAttractionId(cursor.getInt(idIndex));
                    attraction.setName(cursor.getString(nameIndex));
                    attraction.setLocation(cursor.getString(locationIndex));
                    attraction.setDescription(cursor.getString(descriptionIndex));
                    attraction.setTicketPrice(cursor.getDouble(priceIndex));
                    attraction.setOpenTime(cursor.getString(openTimeIndex));
                    attraction.setCloseTime(cursor.getString(closeTimeIndex));
                }
            } catch (IllegalArgumentException e) {
                Log.e("AttractionDBHelper", "Error while accessing the database", e);
            } finally {
                cursor.close();
            }
        }
        return attraction;
    }

    public Attraction getAttractionById(int attractionId) {
        SQLiteDatabase db = openReadLink();  // 获取数据库连接
        if (db == null || !db.isOpen()) {
            // 连接无效或已关闭，返回 null 或处理错误
            return null;
        }
        Attraction attraction = null;
        Cursor cursor = mRDB.query(TABLE_ATTRACTIONS, null, "attraction_id = ?", new String[]{String.valueOf(attractionId)}, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndexOrThrow("attraction_id");
                    int nameIndex = cursor.getColumnIndexOrThrow("name");
                    int locationIndex = cursor.getColumnIndexOrThrow("location");
                    int descriptionIndex = cursor.getColumnIndexOrThrow("description");
                    int priceIndex = cursor.getColumnIndexOrThrow("ticket_price");
                    int openTimeIndex = cursor.getColumnIndexOrThrow("open_time");
                    int closeTimeIndex = cursor.getColumnIndexOrThrow("close_time");

                    attraction = new Attraction();
                    attraction.setAttractionId(cursor.getInt(idIndex));
                    attraction.setName(cursor.getString(nameIndex));
                    attraction.setLocation(cursor.getString(locationIndex));
                    attraction.setDescription(cursor.getString(descriptionIndex));
                    attraction.setTicketPrice(cursor.getDouble(priceIndex));
                    attraction.setOpenTime(cursor.getString(openTimeIndex));
                    attraction.setCloseTime(cursor.getString(closeTimeIndex));
                }
            } catch (IllegalArgumentException e) {
                Log.e("AttractionDBHelper", "Error while accessing the database", e);
            } finally {
                cursor.close();
            }
        }
        return attraction;
    }

    public void deleteAttraction(int attractionId) {
        SQLiteDatabase db = openWriteLink();  // 获取可写数据库
        if (db != null && db.isOpen()) {
            db.delete(TABLE_ATTRACTIONS, "attraction_id = ?", new String[]{String.valueOf(attractionId)});
        }
    }

    public long addAttraction(Attraction attraction) {
        SQLiteDatabase db = openWriteLink();
        if (db == null || !db.isOpen()) {
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put("name", attraction.getName());
        values.put("location", attraction.getLocation());
        values.put("description", attraction.getDescription());
        values.put("ticket_price", attraction.getTicketPrice());
        values.put("open_time", attraction.getOpenTime());
        values.put("close_time", attraction.getCloseTime());

        long result = db.insert(TABLE_ATTRACTIONS, null, values);
        if (result == -1) {
            Log.e("AttractionDBHelper", "Failed to add attraction");
        }
        return result;
    }

    public ArrayList<Attraction> getAllAttractions() {
        ArrayList<Attraction> attractions = new ArrayList<>();
        SQLiteDatabase db = openReadLink();
        Cursor cursor = db.query(TABLE_ATTRACTIONS, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Attraction attraction = new Attraction();
                attraction.setAttractionId(cursor.getInt(cursor.getColumnIndex("attraction_id")));
                attraction.setName(cursor.getString(cursor.getColumnIndex("name")));
                attraction.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                attraction.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                attraction.setTicketPrice(cursor.getDouble(cursor.getColumnIndex("ticket_price")));
                attraction.setOpenTime(cursor.getString(cursor.getColumnIndex("open_time")));
                attraction.setCloseTime(cursor.getString(cursor.getColumnIndex("close_time")));
                attractions.add(attraction);
            }
            cursor.close();
        }
        return attractions;
    }

}
