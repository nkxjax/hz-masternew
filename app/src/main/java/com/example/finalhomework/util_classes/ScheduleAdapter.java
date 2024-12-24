package com.example.finalhomework.util_classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.finalhomework.R;
import com.example.finalhomework.ScheduleDetailActivity;
import com.example.finalhomework.model.Schedule;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private List<Schedule> scheduleList;
    private Context context;

    public ScheduleAdapter(List<Schedule> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.schedule_card_item, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.title.setText(schedule.getTitle());
        holder.duration.setText(schedule.getDuration());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ScheduleDetailActivity.class);
            intent.putExtra("schedule_id", schedule.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView title, duration;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.schedule_title);
            duration = itemView.findViewById(R.id.schedule_duration);
        }
    }
}
