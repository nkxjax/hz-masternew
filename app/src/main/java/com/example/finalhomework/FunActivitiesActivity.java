package com.example.finalhomework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalhomework.util_classes.ActivityDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FunActivitiesActivity extends AppCompatActivity {

    private ActivityDBHelper dbHelper;
    private List<Map<String, Object>> gridItems = new ArrayList<Map<String, Object>>();
    private ImageButton floatingActionButton;  // 悬浮按钮

    // 新增：广播接收器
    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.example.finalhomework.REFRESH_ACTIVITY_LIST".equals(intent.getAction())) {
                // 刷新活动数据
                refreshActivities();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_activities);

        dbHelper = new ActivityDBHelper(this);

        // 注册广播接收器
        IntentFilter filter = new IntentFilter("com.example.finalhomework.REFRESH_ACTIVITY_LIST");
        registerReceiver(refreshReceiver, filter, Context.RECEIVER_NOT_EXPORTED);

        // 初始化并加载活动数据
        refreshActivities();

        // 设置自定义适配器
        FunActivitiesAdapter myAdapter = new FunActivitiesAdapter(gridItems);
        GridView gridView = findViewById(R.id.fun_grid_view);
        gridView.setAdapter(myAdapter);

        // 点击项的处理
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 启动新的 Activity，传递数据
                Intent intent = new Intent(FunActivitiesActivity.this, ActivityDetailActivity.class);
                Map<String, Object> selectedItem = gridItems.get(position);
                Long id_tmp = (Long) selectedItem.get("id");
                intent.putExtra("activityId", id_tmp.toString());
                intent.putExtra("imageResId", (byte[]) selectedItem.get("img_res_id")); // 传递图片字节数据
                intent.putExtra("title", (String) selectedItem.get("title"));
                intent.putExtra("date", (String) selectedItem.get("date"));
                intent.putExtra("content", (String) selectedItem.get("content"));
                startActivity(intent);
            }
        });

        // 获取悬浮按钮
        floatingActionButton = findViewById(R.id.floatingActionButton);

        boolean isAdmin = checkIfAdmin();  // 判断当前用户是否是管理员

        if (isAdmin) {
            floatingActionButton.setVisibility(View.VISIBLE);  // 显示悬浮按钮
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转到新增活动页面
                    Intent intent = new Intent(FunActivitiesActivity.this, AddActivityActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            // 非管理员时，隐藏按钮
            floatingActionButton.setVisibility(View.GONE);
        }
    }

    // 判断是否为管理员
    private boolean checkIfAdmin() {
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


    // 刷新活动数据
    private void refreshActivities() {
        // 从数据库获取活动数据
        gridItems = dbHelper.getAllActivities();

        // 设置自定义适配器
        FunActivitiesAdapter myAdapter = new FunActivitiesAdapter(gridItems);
        GridView gridView = findViewById(R.id.fun_grid_view);
        gridView.setAdapter(myAdapter);
    }

    // 自定义适配器
    public class FunActivitiesAdapter extends android.widget.BaseAdapter {

        private List<Map<String, Object>> gridItems;

        public FunActivitiesAdapter(List<Map<String, Object>> gridItems) {
            this.gridItems = gridItems;
        }

        @Override
        public int getCount() {
            return gridItems.size();
        }

        @Override
        public Object getItem(int position) {
            return gridItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, android.view.ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.grid_view_item, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.imageView_grid_item);
            TextView titleTextView = convertView.findViewById(R.id.textView_grid_title);
            TextView dateTextView = convertView.findViewById(R.id.textView_grid_date);

            Map<String, Object> currentItem = gridItems.get(position);

            // 设置文本内容
            titleTextView.setText((String) currentItem.get("title"));
            dateTextView.setText((String) currentItem.get("date"));

            // 设置图片
            byte[] imageByteArray = (byte[]) currentItem.get("img_res_id");
            if (imageByteArray != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                imageView.setImageBitmap(bitmap);
            }

            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播接收器
        unregisterReceiver(refreshReceiver);
    }
}
