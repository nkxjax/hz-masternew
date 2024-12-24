package com.example.finalhomework;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalhomework.model.Schedule;
import com.example.finalhomework.util_classes.ScheduleDBHelper;

public class AddScheduleActivity extends AppCompatActivity {

    private EditText titleEditText, durationEditText, contentEditText;
    private Button saveButton, deleteButton;  // 删除按钮
    private int scheduleId;
    private ScheduleDBHelper dbHelper; // 数据库帮助类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        // 初始化数据库帮助类
        dbHelper = new ScheduleDBHelper(this);

        // 获取传递的日程 ID
        scheduleId = getIntent().getIntExtra("schedule_id", -1);

        titleEditText = findViewById(R.id.title_edit_text);
        durationEditText = findViewById(R.id.duration_edit_text);
        contentEditText = findViewById(R.id.content_edit_text);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_button);  // 获取删除按钮

        // 如果有 schedule_id，加载数据（编辑）
        if (scheduleId != -1) {
            // 从数据库加载日程数据，填充到输入框
            Schedule schedule = dbHelper.getScheduleById(scheduleId);
            if (schedule != null) {
                titleEditText.setText(schedule.getTitle());
                durationEditText.setText(schedule.getDuration());
                contentEditText.setText(schedule.getContent());
            }
        } else {
            // 如果没有传递 scheduleId，说明是添加新日程
            deleteButton.setVisibility(View.GONE);  // 隐藏删除按钮
        }

        // 在 AddScheduleActivity 中，添加日程后返回
        saveButton.setOnClickListener(view -> {
            String title = titleEditText.getText().toString();
            String duration = durationEditText.getText().toString();
            String content = contentEditText.getText().toString();

            if (title.isEmpty() || duration.isEmpty() || content.isEmpty()) {
                Toast.makeText(AddScheduleActivity.this, "所有字段都必须填写", Toast.LENGTH_SHORT).show();
                return;
            }

            if (scheduleId == -1) {
                // 添加新日程
                Schedule newSchedule = new Schedule(0, title, duration, content);
                long result = dbHelper.insertSchedule(newSchedule);
                if (result != -1) {
                    Toast.makeText(AddScheduleActivity.this, "日程添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddScheduleActivity.this, "日程添加失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 编辑已有日程
                Schedule updatedSchedule = new Schedule(scheduleId, title, duration, content);
                int rowsUpdated = dbHelper.updateSchedule(updatedSchedule);
                if (rowsUpdated > 0) {
                    Toast.makeText(AddScheduleActivity.this, "日程更新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddScheduleActivity.this, "日程更新失败", Toast.LENGTH_SHORT).show();
                }
            }

            // 返回结果到 GuideHZ
            setResult(RESULT_OK);
            finish(); // 返回 GuideHZ
        });

        // 设置删除按钮的点击事件
        deleteButton.setOnClickListener(view -> {
            if (scheduleId != -1) {
                // 如果有 scheduleId，执行删除操作
                dbHelper.deleteSchedule(scheduleId);
                Toast.makeText(AddScheduleActivity.this, "日程删除成功", Toast.LENGTH_SHORT).show();

                // 返回结果到 GuideHZ
                setResult(RESULT_OK);
                finish(); // 删除后返回 GuideHZ
            }
        });
    }
}
