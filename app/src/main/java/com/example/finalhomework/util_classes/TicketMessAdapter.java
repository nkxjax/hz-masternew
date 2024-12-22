package com.example.finalhomework.util_classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.finalhomework.R;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.finalhomework.R;

import java.util.List;

public class TicketMessAdapter extends ArrayAdapter<Ticket> {

    private Context context;
    private List<Ticket> ticketList;
    private UserDBHelper userDBHelper;
    private AttractionDBHelper attractionDBHelper;

    public TicketMessAdapter(Context context, List<Ticket> ticketList) {
        super(context, 0, ticketList);
        this.context = context;
        this.ticketList = ticketList;
        this.userDBHelper = UserDBHelper.getInstance(context);
        this.attractionDBHelper = AttractionDBHelper.getInstance(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ticket_item, parent, false);
        }

        // 获取当前 Ticket 对象
        Ticket ticket = ticketList.get(position);

        // 获取控件
        TextView messageTitle = convertView.findViewById(R.id.message_title);
        TextView userId = convertView.findViewById(R.id.userId);
        TextView attractionId = convertView.findViewById(R.id.attractionId);
        TextView quantity = convertView.findViewById(R.id.quantity);
        TextView purchaseTime = convertView.findViewById(R.id.purchaseTime);
        TextView visitingTime = convertView.findViewById(R.id.visitingTime);

        // 根据 Ticket 的 status 设置 message_title
        if (ticket.getStatus() == 0) {
            messageTitle.setText("退票成功");  // 状态为 0，显示 退票成功
            purchaseTime.setText("退票时间: " + ticket.getStatusChangeTime());
            visitingTime.setText("参观时间: " + " - ");
        } else if (ticket.getStatus() == 1) {
            messageTitle.setText("购票信息");  // 状态为 1，显示 购票信息
            purchaseTime.setText("购买时间: " + ticket.getStatusChangeTime());
            visitingTime.setText("参观时间: " + ticket.getVisitDate());
        }

        // 获取并设置 User 的昵称
        String nickname = userDBHelper.getUsernameByUserId(ticket.getUserId());
        userId.setText("用户名: " + (nickname != null ? nickname : "Unknown"));

        // 获取并设置 Attraction 的详细信息
        Attraction attraction = attractionDBHelper.getAttractionById(ticket.getAttractionId());
        if (attraction != null) {
            attractionId.setText("景点名称: " + attraction.getName() + " (" + attraction.getLocation() + ")");
        } else {
            attractionId.setText("景点名称: Unknown");
        }

        // 设置其他 Ticket 内容
        quantity.setText("数量: " + ticket.getQuantity());


        return convertView;
    }
}
