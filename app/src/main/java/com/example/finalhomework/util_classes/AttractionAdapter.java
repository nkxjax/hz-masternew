package com.example.finalhomework.util_classes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalhomework.ActivityDetailActivity;
import com.example.finalhomework.R;
import com.example.finalhomework.AttractionDetailActivity;  // 引入详情页面

import java.util.ArrayList;

public class AttractionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Attraction> attractionList;
    private AttractionDBHelper dbHelper;

    static class ViewHolder {
        TextView nameTextView;
        TextView locationTextView;
        TextView descriptionTextView;
        Button removeButton;  // 添加按钮字段
    }

    public AttractionAdapter(Context context, ArrayList<Attraction> attractionList) {
        this.context = context;
        this.attractionList = attractionList;
        dbHelper = AttractionDBHelper.getInstance(context);  // 初始化数据库帮助类
    }

    @Override
    public int getCount() {
        return attractionList.size();
    }

    @Override
    public Object getItem(int position) {
        return attractionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.attracion_item, parent, false);

            holder.nameTextView = convertView.findViewById(R.id.textView_attraction_name);
            holder.locationTextView = convertView.findViewById(R.id.textView_attraction_location);
            holder.descriptionTextView = convertView.findViewById(R.id.textView_attraction_description);

            // 获取按钮
            holder.removeButton = convertView.findViewById(R.id.btn_remove);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 获取当前景点
        Attraction attraction = attractionList.get(position);

        // 设置景点信息
        holder.nameTextView.setText(attraction.getName());
        holder.locationTextView.setText(attraction.getLocation());
        holder.descriptionTextView.setText(attraction.getDescription());

        // 获取 SharedPreferences 实例
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);  // 默认值为 -1，表示没有登录

        // 判断 user_id 是否为 1
        if (isAdmin) {
            holder.removeButton.setVisibility(View.VISIBLE);  // 显示按钮
        } else {
            holder.removeButton.setVisibility(View.GONE);  // 隐藏按钮
        }

        // 设置按钮点击事件
        holder.removeButton.setOnClickListener(v -> {
            int attractionId = attraction.getAttractionId();

            // 调用数据库帮助类删除该景点
            dbHelper.deleteAttraction(attractionId);

            // 从列表中删除该景点
            attractionList.remove(position);

            // 通知适配器数据已更改，刷新 UI
            notifyDataSetChanged();

            // 显示删除提示
            Toast.makeText(context, "景点已删除", Toast.LENGTH_SHORT).show();
        });

        // 设置点击事件
        convertView.setOnClickListener(v -> {
            // 获取 SharedPreferences 实例
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);  // 默认值为 false

            if (!isLoggedIn) {
                // 如果未登录，提示用户
                Toast.makeText(context, "请先登录后再购票", Toast.LENGTH_SHORT).show();
            } else {
                // 创建 Intent 跳转到景点详情页面
                Intent intent = new Intent(context, AttractionDetailActivity.class);
                // 通过 Intent 传递景点的 ID
                intent.putExtra("attraction_id", attraction.getAttractionId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void updateData(ArrayList<Attraction> newAttractionList) {
        this.attractionList = newAttractionList;  // 更新景点列表数据
        notifyDataSetChanged();  // 通知适配器更新
    }

}

