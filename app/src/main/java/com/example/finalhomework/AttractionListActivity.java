package com.example.finalhomework;

import android.database.Cursor;  // 添加这个导入
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalhomework.util_classes.AttractionAdapter;
import com.example.finalhomework.util_classes.Attraction;
import com.example.finalhomework.util_classes.AttractionDBHelper;

import java.util.ArrayList;

public class AttractionListActivity extends AppCompatActivity {
    private ListView listView;
    private AttractionDBHelper dbHelper;
    private ArrayList<Attraction> attractionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_list);

        listView = findViewById(R.id.listView_attractions);
        dbHelper = AttractionDBHelper.getInstance(this);

        // 获取景点列表
        attractionList = getAllAttractions();

        // 通过 Adapter 显示景点列表
        AttractionAdapter adapter = new AttractionAdapter(this, attractionList);
        listView.setAdapter(adapter);
    }

    // 获取所有景点
    private ArrayList<Attraction> getAllAttractions() {
        ArrayList<Attraction> attractions = new ArrayList<>();
        // 查询数据库中的所有景点
        Cursor cursor = dbHelper.openReadLink().query("attractions", null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Attraction attraction = new Attraction();
                    attraction.setAttractionId(cursor.getInt(cursor.getColumnIndex("attraction_id")));
                    attraction.setName(cursor.getString(cursor.getColumnIndex("name")));
                    attraction.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                    attraction.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    attraction.setTicketPrice(cursor.getDouble(cursor.getColumnIndex("ticket_price")));
                    attraction.setOpenTime(cursor.getString(cursor.getColumnIndex("open_time")));
                    attraction.setCloseTime(cursor.getString(cursor.getColumnIndex("close_time")));
                    attractions.add(attraction);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return attractions;
    }
}



