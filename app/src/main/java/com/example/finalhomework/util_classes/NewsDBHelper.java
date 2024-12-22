package com.example.finalhomework.util_classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.finalhomework.util_classes.News;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news_db";  // 数据库名称
    private static final int DATABASE_VERSION = 2;  // 数据库版本号

    // 新闻表的创建语句
    private static final String CREATE_TABLE_NEWS =
            "CREATE TABLE IF NOT EXISTS news (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "content TEXT NOT NULL, " +
                    "publish_time DATETIME NOT NULL" + ");";

    // Column names constants
    public static final String TABLE_NAME = "news";
    public static final String COLUMN_ID = "id"; // Primary key
    public static final String COLUMN_TITLE = "title"; // News title
    public static final String COLUMN_CONTENT = "content"; // News content
    public static final String COLUMN_PUBLISH_TIME = "publish_time"; // News publish time

    public NewsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // 插入示例数据
        ContentValues values = new ContentValues();

        // 插入第一条新闻
        values.put(COLUMN_TITLE, "今冬杭州超山景区第一批梅花陆续绽放");
        values.put(COLUMN_CONTENT, "随着最近天气的冷暖交替，位于传统赏梅胜地的临平超山风景区，已经有一批梅花陆续开放了。\n" +
                "\n" +
                "12月20日，杭州网记者在超山风景区拍摄下了这一美妙时刻，点点白梅、红梅在梅树上绽放身姿。根据景区讲解员季佳丽介绍，等到一月上旬左右，东园的白梅渐次开放，相信在一月中下旬即将呈现十里梅花香雪海的盛景，而到了二月超山景区就将进入最佳赏梅季，到时候白梅、红梅、绿萼梅争奇斗艳，三月份是景区最晚开放的美人梅，也别有一番滋味。\n" +
                "\n");
        values.put(COLUMN_PUBLISH_TIME, "2024-12-22");
        db.insert(TABLE_NAME, null, values);

        // 插入第二条新闻
        values.put(COLUMN_TITLE, "杭州的浪漫：落叶不扫，归于诗意");
        values.put(COLUMN_CONTENT, "寒潮无声进入杭城，转眼间，城市的44条道路已被金黄的落叶覆盖。在杭州南山路上的中国美术学院门口，通体由落叶打造的大象滑滑梯、尖帽小人、落叶雪人成了网红打卡点。2016年，中国美术学院师生发起“秋叶艺术节”，将落叶重塑成充满生命力的艺术品。如今，这一节日不再是艺术家们个人表达的空间，而成为全民参与公共艺术的舞台。");
        values.put(COLUMN_PUBLISH_TIME, "2024-12-21");
        db.insert(TABLE_NAME, null, values);

        // 插入第三条新闻
        values.put(COLUMN_TITLE, "杭州植物园的梅花开了 今年提前近半个月亮相");
        values.put(COLUMN_CONTENT, "杭州植物园的梅花开了！今年的这位“花仙子”来得格外早，提前近半个月与大家见面。梅花已然亮相，冬季另一位主角，蜡梅的情况如何？\n" +
                "\n" +
                "因为盛放于寒冬，蜡梅也被称为“冬日里的最后一朵花”，花期可持续至1月中下旬，近年来灵峰探梅“二梅争春”的景象时有发生。");
        values.put(COLUMN_PUBLISH_TIME, "2024-12-20");
        db.insert(TABLE_NAME, null, values);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建新闻表
        db.execSQL(CREATE_TABLE_NEWS);
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库版本更新时的处理
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public News getNewsById(int newsId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TITLE + ", " + COLUMN_CONTENT + ", " + COLUMN_PUBLISH_TIME +
                        " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?",
                new String[]{String.valueOf(newsId)});
        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
            String publishTime = cursor.getString(cursor.getColumnIndex(COLUMN_PUBLISH_TIME));
            cursor.close();
            return new News(title, content, publishTime);
        } else {
            Log.e("NewsActivity", "No data found for newsId: " + newsId);
            cursor.close();
            return null;
        }
    }

    // 更新新闻内容
    public void updateNews(int newsId, String updatedTitle, String updatedContent, String updatedTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, updatedTitle);
        values.put(COLUMN_CONTENT, updatedContent);
        values.put(COLUMN_PUBLISH_TIME, updatedTime);

        // 更新指定 newsId 的记录
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(newsId)});
        db.close();
    }

    public List<News> getAllNews() {
        List<News> newsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询所有新闻
        Cursor cursor = db.query("news",  // 确保使用正确的表名
                new String[]{"title", "publish_time"},  // 列名应与数据库表中一致
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex("title"));  // 确保列名正确
                String publishTime = cursor.getString(cursor.getColumnIndex("publish_time"));  // 同样检查列名
                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                try {
                    Date date = inputFormat.parse(publishTime);
                    publishTime = outputFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                News news = new News(title, " ", publishTime);
                newsList.add(news);
            }
            cursor.close();
        }

        db.close();
        return newsList;
    }

}
