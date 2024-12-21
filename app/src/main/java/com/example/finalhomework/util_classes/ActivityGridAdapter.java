package com.example.finalhomework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class ActivityGridAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> gridItems;

    public ActivityGridAdapter(Context context, List<Map<String, Object>> gridItems) {
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_view_item, parent, false);
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
