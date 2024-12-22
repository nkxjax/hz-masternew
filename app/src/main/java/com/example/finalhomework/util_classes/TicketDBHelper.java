package com.example.finalhomework.util_classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TicketDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ticketMess.db";  // 数据库名称
    private static final int DATABASE_VERSION = 1;            // 数据库版本
    private static final String TABLE_TICKETS = "tickets";     // 表名

    private SQLiteDatabase mRDB = null;  // 读数据库
    private SQLiteDatabase mWDB = null;  // 写数据库
    private static TicketDBHelper mHelper;  // 单例对象

    private static final String CREATE_TICKETS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_TICKETS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "attraction_id INTEGER NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "total_price DECIMAL NOT NULL, " +
                    "purchase_time LONG NOT NULL, " +
                    "status INTEGER NOT NULL DEFAULT 0, " + // 默认状态为0（未退票）
                    "visit_date TEXT NOT NULL, " + // 新增字段：参观日期
                    "FOREIGN KEY (user_id) REFERENCES users(id), " +
                    "FOREIGN KEY (attraction_id) REFERENCES attractions(attraction_id)" +
                    ");";




    public TicketDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 获取单例对象
    public static TicketDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new TicketDBHelper(context);
        }
        return mHelper;
    }

    // 打开读连接
    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    // 打开写连接
    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            try {
                mWDB = getWritableDatabase();  // 确保获取写数据库连接
            } catch (Exception e) {
                Log.e("TicketDBHelper", "Failed to open writable database", e);
                return null;  // 如果数据库连接失败，返回null
            }
        }
        return mWDB;
    }


    // 关闭数据库连接
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

    // 创建表格
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TICKETS);  // 执行创建票务表的SQL语句
    }

    // 数据库升级时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 备份旧表数据（如果需要的话）
        db.execSQL("CREATE TEMPORARY TABLE tickets_backup AS SELECT * FROM " + TABLE_TICKETS);

        // 删除原有表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKETS);

        // 重新创建表
        onCreate(db);

        // 可选：从备份表恢复数据
        db.execSQL("INSERT INTO " + TABLE_TICKETS + " SELECT * FROM tickets_backup");

        // 删除备份表
        db.execSQL("DROP TABLE IF EXISTS tickets_backup");
    }


    // 插入一条票务记录
    public long addTicket(Ticket ticket) {
        // 确保获取到数据库写连接
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = openWriteLink(); // 确保数据库已初始化并打开
        }

        ContentValues values = new ContentValues();
        values.put("user_id", ticket.getUserId());
        values.put("attraction_id", ticket.getAttractionId());
        values.put("quantity", ticket.getQuantity());
        values.put("total_price", ticket.getTotalPrice());
        values.put("purchase_time", ticket.getPurchaseTime());
        values.put("status", ticket.getStatus());  // 设置状态字段
        values.put("visit_date", ticket.getVisitDate());

        Log.d("TicketPurchase", "ticketMess: " + values);
        // 插入数据并返回新插入记录的ID
        long result = mWDB.insert(TABLE_TICKETS, null, values);

        if (result == -1) {
            Log.e("TicketPurchase", "Failed to insert ticket into database.");
        } else {
            Log.d("TicketPurchase", "Ticket inserted successfully with ID: " + result);
        }
        return result;
    }


    // 根据用户ID获取所有票务记录
    public List<Ticket> getTicketsByUserId(int userId) {
        List<Ticket> tickets = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TICKETS + " WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor != null) {
            // 获取列索引，确保列存在
            int idIndex = cursor.getColumnIndex("id");
            int userIdIndex = cursor.getColumnIndex("user_id");
            int attractionIdIndex = cursor.getColumnIndex("attraction_id");
            int quantityIndex = cursor.getColumnIndex("quantity");
            int totalPriceIndex = cursor.getColumnIndex("total_price");
            int purchaseTimeIndex = cursor.getColumnIndex("purchase_time");
            int statusIndex = cursor.getColumnIndex("status");
            int visitDateIndex = cursor.getColumnIndex("visit_date");

            // 检查每个列索引是否有效
            if (idIndex != -1 && userIdIndex != -1 && attractionIdIndex != -1 &&
                    quantityIndex != -1 && totalPriceIndex != -1 && purchaseTimeIndex != -1 && statusIndex != -1 && visitDateIndex != -1) {

                while (cursor.moveToNext()) {
                    Ticket ticket = new Ticket();
                    ticket.setId(cursor.getInt(idIndex));
                    ticket.setUserId(cursor.getInt(userIdIndex));
                    ticket.setAttractionId(cursor.getInt(attractionIdIndex));
                    ticket.setQuantity(cursor.getInt(quantityIndex));
                    ticket.setTotalPrice(cursor.getDouble(totalPriceIndex));
                    ticket.setPurchaseTime(cursor.getLong(purchaseTimeIndex));
                    ticket.setStatus(cursor.getInt(statusIndex));  // 读取状态
                    ticket.setVisitDate(cursor.getString(visitDateIndex));

                    tickets.add(ticket);
                }
            } else {
                Log.e("TicketDBHelper", "One or more columns are missing in the query result." + idIndex + userIdIndex + attractionIdIndex +
                        quantityIndex + totalPriceIndex + purchaseTimeIndex + statusIndex);
            }
            cursor.close();
        }
        db.close();

        return tickets;
    }


    // 根据景点ID获取所有票务记录
    public Cursor getTicketsByAttractionId(int attractionId) {
        return mRDB.query(TABLE_TICKETS, null, "attraction_id = ?", new String[]{String.valueOf(attractionId)}, null, null, null);
    }

    // 根据票ID获取票务记录
    public Ticket getTicketById(int ticketId) {
        Ticket ticket = null;
        Cursor cursor = mRDB.query(TABLE_TICKETS, null, "id = ?", new String[]{String.valueOf(ticketId)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndexOrThrow("id");
                int userIdIndex = cursor.getColumnIndexOrThrow("user_id");
                int attractionIdIndex = cursor.getColumnIndexOrThrow("attraction_id");
                int quantityIndex = cursor.getColumnIndexOrThrow("quantity");
                int totalPriceIndex = cursor.getColumnIndexOrThrow("total_price");
                int purchaseTimeIndex = cursor.getColumnIndexOrThrow("purchase_time");
                int statusIndex = cursor.getColumnIndexOrThrow("status");
                int visitDateIndex = cursor.getColumnIndexOrThrow("visitDate");

                ticket = new Ticket(
                        cursor.getInt(idIndex),
                        cursor.getInt(userIdIndex),
                        cursor.getInt(attractionIdIndex),
                        cursor.getInt(quantityIndex),
                        cursor.getLong(purchaseTimeIndex),
                        cursor.getInt(statusIndex),
                        cursor.getString(visitDateIndex)
                );
            }
            cursor.close();
        }
        return ticket;
    }

    public int updateTicketStatus(int ticketId, int newStatus) {
        ContentValues values = new ContentValues();
        values.put("status", newStatus);  // 设置新的状态

        return mWDB.update(TABLE_TICKETS, values, "id = ?", new String[]{String.valueOf(ticketId)});
    }


}
