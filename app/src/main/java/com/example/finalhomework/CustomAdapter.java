package com.example.finalhomework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.finalhomework.model.Schedule;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Schedule> {

    private Context context;
    private List<Schedule> scheduleList;

    public CustomAdapter(Context context, List<Schedule> scheduleList) {
        super(context, 0, scheduleList);
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.schedule_item, parent, false);
        }

        Schedule schedule = scheduleList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.schedule_title);
        TextView durationTextView = convertView.findViewById(R.id.schedule_duration);

        titleTextView.setText(schedule.getTitle());
        durationTextView.setText(schedule.getDuration());

        return convertView;
    }
}

