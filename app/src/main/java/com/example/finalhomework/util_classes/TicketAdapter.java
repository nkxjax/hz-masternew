package com.example.finalhomework.util_classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.finalhomework.R;
import com.example.finalhomework.util_classes.Attraction;
import com.example.finalhomework.util_classes.AttractionDBHelper;
import com.example.finalhomework.util_classes.UserDBHelper;
import com.example.finalhomework.util_classes.Ticket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TicketAdapter extends BaseAdapter {
    private Context context;
    private List<Ticket> ticketList;
    private UserDBHelper userDBHelper;
    private AttractionDBHelper attractionDBHelper;

    public TicketAdapter(Context context, List<Ticket> tickets) {
        this.context = context;
        this.ticketList = tickets;
        try {
            this.userDBHelper = UserDBHelper.getInstance(context);
            this.attractionDBHelper = AttractionDBHelper.getInstance(context);
        } catch (Exception e) {
            Log.e("AdapterError", "初始化数据库助手失败：" + e.getMessage());
            // 这里可以考虑根据实际情况采取一些措施，比如提示用户或者设置默认值等
        }
    }

    // 添加如下的setTicketList方法，用于更新数据列表并刷新界面显示
    public void setTicketList(List<Ticket> newTicketList) {
        this.ticketList = newTicketList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ticketList.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ticketList.get(position).getId();
    }

    static class ViewHolder {
        TextView userIdTextView;
        TextView attractionIdTextView;
        TextView quantityTextView;
        TextView visitingTimeTextView;
        TextView purchaseTimeTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ticket_item, parent, false);
            holder = new ViewHolder();
            holder.userIdTextView = convertView.findViewById(R.id.userId);
            holder.attractionIdTextView = convertView.findViewById(R.id.attractionId);
            holder.quantityTextView = convertView.findViewById(R.id.quantity);
            holder.visitingTimeTextView = convertView.findViewById(R.id.visitingTime);
            holder.purchaseTimeTextView = convertView.findViewById(R.id.purchaseTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Ticket ticket = ticketList.get(position);

        String username = userDBHelper.getUsernameByUserId(ticket.getUserId());
        if (username!= null) {
            holder.userIdTextView.setText("预定人：" + username);
        } else {
            holder.userIdTextView.setText("预定人：未知");
        }

        Attraction attraction = attractionDBHelper.getAttractionById(ticket.getAttractionId());
        if (attraction!= null) {
            holder.attractionIdTextView.setText("景区名称：" + attraction.getName());
        } else {
            holder.attractionIdTextView.setText("景区名称：无对应景区信息");
        }

        holder.quantityTextView.setText("预定数量：" + ticket.getQuantity());

        if (attraction!= null) {
            holder.visitingTimeTextView.setText("游览时间：" + ticket.getVisitDate() + " " + attraction.getOpenTime() + " - " + attraction.getCloseTime());
        } else {
            holder.visitingTimeTextView.setText("游览时间：无对应景区开放时间信息");
        }

        Date date = new Date(ticket.getPurchaseTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("预定时间：" + "yyyy-MM-dd", Locale.getDefault());
        holder.purchaseTimeTextView.setText(dateFormat.format(date));

        return convertView;
    }
}