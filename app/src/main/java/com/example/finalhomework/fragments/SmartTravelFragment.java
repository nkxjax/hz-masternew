package com.example.finalhomework.fragments;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.finalhomework.R;
import com.example.finalhomework.util_classes.Ticket;
import com.example.finalhomework.util_classes.TicketAdapter;
import com.example.finalhomework.util_classes.TicketDBHelper;
import com.example.finalhomework.util_classes.TicketMessAdapter;

import java.util.List;

public class SmartTravelFragment extends Fragment {

    private ListView listView;  // ListView 显示票务数据
    private TicketMessAdapter ticketMessAdapter;  // 适配器
    private TicketDBHelper ticketDBHelper;  // 数据库助手

    // 用户ID示例，实际应用中可以通过登录信息获取
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View view = inflater.inflate(R.layout.activity_ticket, container, false);

        // 初始化数据库助手
        ticketDBHelper = TicketDBHelper.getInstance(getContext());

        // 初始化 ListView
        listView = view.findViewById(R.id.listView);

        // 获取当前用户的所有票务记录
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);  // 默认值为 -1，如果没有找到 userId
        List<Ticket> ticketList = ticketDBHelper.getTicketsByUserId(userId);

        if (ticketList != null && !ticketList.isEmpty()) {
            // 设置适配器，将数据绑定到 ListView
            ticketMessAdapter = new TicketMessAdapter(getContext(), ticketList);
            listView.setAdapter(ticketMessAdapter);
        } else {
            Toast.makeText(getContext(), "没有找到相关票务记录", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 关闭数据库连接
        if (ticketDBHelper != null) {
            ticketDBHelper.closeLink();
        }
    }
}