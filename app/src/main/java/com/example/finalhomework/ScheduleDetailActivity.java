package com.example.finalhomework;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalhomework.util_classes.ScheduleDBHelper;
import com.example.finalhomework.model.Schedule;

public class ScheduleDetailActivity extends AppCompatActivity {

    private TextView titleTextView, durationTextView, contentTextView;
    private ScheduleDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);

        // 初始化数据库助手
        dbHelper = new ScheduleDBHelper(this);

        // 获取传递过来的数据
        int scheduleId = getIntent().getIntExtra("schedule_id", -1);  // 默认值为 -1，表示没有传递该数据

        // 从数据库获取具体的日程数据
        Schedule selectedSchedule = dbHelper.getScheduleById(scheduleId);

        if (selectedSchedule != null) {
            // 显示详细内容
            titleTextView = findViewById(R.id.schedule_detail_title);
            durationTextView = findViewById(R.id.schedule_detail_duration);
            contentTextView = findViewById(R.id.schedule_detail_content);

            titleTextView.setText(selectedSchedule.getTitle());
            durationTextView.setText(selectedSchedule.getDuration());
            contentTextView.setText(selectedSchedule.getContent());
        } else {
            // 如果没有找到对应的日程，处理错误情况
            titleTextView.setText("日程未找到");
            durationTextView.setText("");
            contentTextView.setText("");
        }
    }
}
