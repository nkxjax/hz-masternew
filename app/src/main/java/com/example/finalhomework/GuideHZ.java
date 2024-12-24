package com.example.finalhomework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalhomework.util_classes.ScheduleDBHelper;
import com.example.finalhomework.model.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class GuideHZ extends AppCompatActivity {

    private ListView listView;
    private CustomAdapter adapter;
    private List<Schedule> scheduleList;
    private FloatingActionButton fabAddSchedule;
    private ScheduleDBHelper dbHelper;
    private boolean isAdmin = false;  // 假设你有方式判断当前用户是否是管理员

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        listView = findViewById(R.id.list_view);
        fabAddSchedule = findViewById(R.id.fab_add_schedule);
        dbHelper = new ScheduleDBHelper(this);
        scheduleList = new ArrayList<>();

        // 假设你通过某种方式判断是否为管理员
        isAdmin = checkIfUserIsAdmin();

        // 如果是管理员，显示悬浮按钮
        if (isAdmin) {
            fabAddSchedule.setVisibility(View.VISIBLE);
        } else {
            fabAddSchedule.setVisibility(View.GONE);
        }

        // 从数据库加载所有日程
        loadSchedules();

        // 创建适配器并绑定到 ListView
        adapter = new CustomAdapter(this, scheduleList);
        listView.setAdapter(adapter);

        // 设置点击事件
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Schedule selectedSchedule = scheduleList.get(position);
            int scheduleId = selectedSchedule.getId();
            Intent intent;

            // 判断用户角色
            if (isAdmin) {
                // 管理员：跳转到可以编辑日程的页面
                intent = new Intent(GuideHZ.this, AddScheduleActivity.class);
                intent.putExtra("schedule_id", scheduleId); // 传递日程 ID
            } else {
                // 普通用户：跳转到查看日程详情的页面
                intent = new Intent(GuideHZ.this, ScheduleDetailActivity.class);
                intent.putExtra("schedule_id", scheduleId); // 传递日程 ID
            }

            startActivityForResult(intent, 1); // 启动并等待返回结果
        });

        // 设置悬浮按钮点击事件
        fabAddSchedule.setOnClickListener(v -> {
            // 跳转到添加日程的页面
            Intent intent = new Intent(GuideHZ.this, AddScheduleActivity.class);
            startActivityForResult(intent, 1); // 启动并等待返回结果
        });
    }

    private boolean checkIfUserIsAdmin() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);  // 获取登录状态
        if (!isLoggedIn) {
            // 如果未登录，默认返回不是管理员
            return false;
        }

        // 获取是否为管理员的标识
        boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);  // 获取管理员标识
        return isAdmin;
    }

    // 加载日程列表
    private void loadSchedules() {
        scheduleList.clear(); // 清空当前列表
        scheduleList.addAll(dbHelper.getAllSchedules()); // 从数据库获取所有日程
    }

    // 重写 onActivityResult 方法，处理返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // 返回时刷新日程列表
            loadSchedules();
            adapter.notifyDataSetChanged();  // 通知适配器刷新视图
            Toast.makeText(this, "日程已更新", Toast.LENGTH_SHORT).show();
        }
    }
}
