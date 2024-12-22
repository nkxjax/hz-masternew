package com.example.finalhomework.util_classes;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.finalhomework.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TicketHistoryAdapter extends BaseAdapter {

    private Context context;
    private List<Ticket> ticketList;
    private AttractionDBHelper attractionDBHelper;  // 确保 AttractionDBHelper 被正确传递
    private TicketDBHelper ticketDBHelper;

    public TicketHistoryAdapter(Context context, List<Ticket> ticketList, AttractionDBHelper attractionDBHelper, TicketDBHelper ticketDBHelper) {
        this.context = context;
        this.ticketList = ticketList;
        if (attractionDBHelper == null) {
            Log.e("TicketHistoryAdapter", "AttractionDBHelper is null");
            throw new IllegalArgumentException("AttractionDBHelper must not be null");
        }
        Log.d("TicketHistoryAdapter", "AttractionDBHelper: " + attractionDBHelper);

        this.attractionDBHelper = attractionDBHelper;
        this.ticketDBHelper = ticketDBHelper;  // 确保TicketDBHelper也被传递
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_ticket_history, parent, false);
        }

        Ticket ticket = ticketList.get(position);
        String visitTimeStr = ticket.getVisitDate();

        SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");

        Date visitTime = null;
        try {
            visitTime = formats.parse(visitTimeStr);
        } catch (ParseException e) {
            Log.e("tickets", "Failed to parse with format: " + formats.toPattern(), e);
        }

        TextView attractionNameTextView = convertView.findViewById(R.id.textView_attraction_name);
        TextView visitTimeTextView = convertView.findViewById(R.id.textView_visit_time);
        Button refundButton = convertView.findViewById(R.id.button_refund);

        // 获取景点名称
        if (attractionDBHelper != null) {
            String attractionName = attractionDBHelper.getAttractionById(ticket.getAttractionId()).getName();
            attractionNameTextView.setText("景点：" + attractionName);
        } else {
            attractionNameTextView.setText("景点：未知");
        }

        visitTimeTextView.setText(visitTimeStr);

        // 根据票务状态设置退款按钮
        if (ticket.getStatus() == 0) {  // 状态为 0，表示已退票
            refundButton.setBackgroundColor(Color.parseColor("#808080")); // 灰色
            refundButton.setText("已退票");
            refundButton.setEnabled(false); // 禁用按钮
        } else {
            // 设置退款按钮颜色
            Date currentTime = new Date();
            if (visitTime != null && visitTime.after(currentTime)) {
                refundButton.setBackgroundColor(Color.parseColor("#800080")); // 紫色
            } else {
                refundButton.setBackgroundColor(Color.parseColor("#808080")); // 灰色
            }

            // 退票按钮点击事件
            refundButton.setOnClickListener(v -> {
                // 执行退票逻辑
                int ticketId = ticket.getId();
                try {
                    onRefundTicket(ticketId, refundButton);  // 传递refundButton作为参数
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return convertView;
    }


    private void onRefundTicket(int ticketId, Button refundButton) throws ParseException {
        // 执行退票操作
        int rowsUpdated = ticketDBHelper.updateTicketStatus(ticketId, 0);

        // 判断更新是否成功
        if (rowsUpdated > 0) {
            // 更新成功，进行相应的 UI 更新
            Toast.makeText(context, "退票成功", Toast.LENGTH_SHORT).show();
            refundButton.setEnabled(false);  // 禁用按钮
            refundButton.setBackgroundColor(Color.parseColor("#808080"));  // 灰色 // 设置按钮背景色为灰色
            refundButton.setText("已退票");  // 修改按钮文本为“已退票”
        } else {
            // 更新失败
            Toast.makeText(context, "退票失败，请稍后再试", Toast.LENGTH_SHORT).show();
        }
    }

}


