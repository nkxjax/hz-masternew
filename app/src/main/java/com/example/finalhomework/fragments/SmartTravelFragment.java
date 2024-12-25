package com.example.finalhomework.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.ArrayList;
import java.util.List;

public class SmartTravelFragment extends Fragment {

    private ListView listView;  // ListView 显示票务数据
    private TicketAdapter ticketMessAdapter;  // 适配器
    private TicketDBHelper ticketDBHelper;  // 数据库助手

    // 用户ID示例，实际应用中可以通过登录信息获取
    private int userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View view = inflater.inflate(R.layout.activity_ticket, container, false);

        // 初始化数据库助手
        ticketDBHelper = TicketDBHelper.getInstance(getContext());

        // 初始化ListView
        listView = view.findViewById(R.id.listView);

        // 直接调用refreshTicketData方法来刷新数据
        refreshTicketData();

        return view;
    }


    private BroadcastReceiver logoutBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.example.finalhomework.LOGOUT_ACTION".equals(intent.getAction())) {
                refreshTicketData();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("com.example.finalhomework.LOGOUT_ACTION");
        getActivity().registerReceiver(logoutBroadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);
        refreshTicketData();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(logoutBroadcastReceiver);
    }

    private void refreshTicketData() {
        try {
            // 获取当前用户的所有票务记录
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            userId = sharedPreferences.getInt("userId", -1);  // 默认值为 -1，如果没有找到 userId
            List<Ticket> ticketList = ticketDBHelper.getTicketsByUserId(userId);

            if (ticketList!= null &&!ticketList.isEmpty()) {
                if (ticketMessAdapter == null) {
                    // 如果适配器还未创建，创建并设置给ListView
                    ticketMessAdapter = new TicketAdapter(getContext(), ticketList);
                    listView.setAdapter(ticketMessAdapter);
                } else {
                    // 如果适配器已存在，直接更新数据
                    ticketMessAdapter.setTicketList(ticketList);
                    ticketMessAdapter.notifyDataSetChanged();
                }
                listView.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.GONE);
                Toast.makeText(getContext(), "没有找到相关票务记录", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("TicketDataError", "获取票务数据出现异常：" + e.getMessage());
            Toast.makeText(getContext(), "获取票务数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroyView();
        if (ticketMessAdapter!= null) {
            ticketMessAdapter.setTicketList(new ArrayList<Ticket>());
            ticketMessAdapter.notifyDataSetChanged();
        }
        listView.setAdapter(null);
        listView.setVisibility(View.VISIBLE);
    }
}