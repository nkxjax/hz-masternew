package com.example.finalhomework.util_classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.finalhomework.R;
import com.example.finalhomework.AttractionDetailActivity;  // 引入详情页面

import java.util.ArrayList;

public class AttractionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Attraction> attractionList;

    static class ViewHolder {
        TextView nameTextView;
        TextView locationTextView;
        TextView descriptionTextView;
    }

    public AttractionAdapter(Context context, ArrayList<Attraction> attractionList) {
        this.context = context;
        this.attractionList = attractionList;
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

        // 设置点击事件
        convertView.setOnClickListener(v -> {
            // 创建 Intent 跳转到详情页面
            Intent intent = new Intent(context, AttractionDetailActivity.class);
            // 通过 Intent 传递景点的 ID 或名称，具体字段根据需要选择
            intent.putExtra("attraction_id", attraction.getAttractionId());
            context.startActivity(intent);
        });

        return convertView;
    }
}



