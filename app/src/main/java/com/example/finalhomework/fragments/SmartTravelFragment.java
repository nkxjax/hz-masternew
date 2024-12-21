package com.example.finalhomework.fragments;

import android.os.Bundle;
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
import java.util.List;

public class SmartTravelFragment extends Fragment {

    private ListView listView;  // ListView 显示票务数据
    private TicketAdapter ticketAdapter;  // 适配器
    private TicketDBHelper ticketDBHelper;  // 数据库助手

    // 用户ID示例，实际应用中可以通过登录信息获取
    private int userId = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View view = inflater.inflate(R.layout.activity_ticket, container, false);

        // 初始化数据库助手
        ticketDBHelper = TicketDBHelper.getInstance(getContext());

        // 初始化 ListView
        listView = view.findViewById(R.id.listView);

        // 获取当前用户的所有票务记录
        List<Ticket> ticketList = ticketDBHelper.getTicketsByUserId(userId);

        if (ticketList != null && !ticketList.isEmpty()) {
            // 设置适配器，将数据绑定到 ListView
            ticketAdapter = new TicketAdapter(getContext(), ticketList);
            listView.setAdapter(ticketAdapter);
        } else {
//            Toast.makeText(getContext(), "没有找到相关票务记录", Toast.LENGTH_SHORT).show();
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
