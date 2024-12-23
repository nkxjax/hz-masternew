package com.example.finalhomework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;  // 添加这个导入
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
    private ImageButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_list);

        listView = findViewById(R.id.listView_attractions);
        dbHelper = AttractionDBHelper.getInstance(this);
        floatingActionButton = findViewById(R.id.floatingActionButton); // 获取浮动按钮


        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        boolean isAdmin =  sharedPreferences.getBoolean("isAdmin", false); // 默认返回 0，如果没有找到 user_id
        if (isAdmin) { // 如果 user_id 为 1，则显示按钮
            floatingActionButton.setVisibility(View.VISIBLE);
        } else { // 否则隐藏按钮
            floatingActionButton.setVisibility(View.GONE);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个 Intent 来启动新的 Activity
                Intent intent = new Intent(AttractionListActivity.this, AddAttractionActivity.class);
                startActivity(intent); // 启动 Activity
            }
        });

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



