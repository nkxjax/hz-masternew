package com.example.finalhomework;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalhomework.util_classes.AttractionDBHelper;
import com.example.finalhomework.util_classes.Ticket;
import com.example.finalhomework.util_classes.TicketDBHelper;
import com.example.finalhomework.util_classes.User;
import com.example.finalhomework.util_classes.UserDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TicketHistoryActivity extends AppCompatActivity {

    private ListView ticketListView;
    private TicketDBHelper dbHelper;
    private List<HashMap<String, String>> ticketList;
    private SimpleAdapter adapter;
    private AttractionDBHelper attractionDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);

        ticketListView = findViewById(R.id.listView_ticket_history);
        dbHelper = TicketDBHelper.getInstance(this);
        attractionDBHelper = AttractionDBHelper.getInstance(this);

        // 初始化 ListView 的数据源
        ticketList = new ArrayList<>();

        // 加载数据
        loadTicketHistory();

        // 创建适配器
        adapter = new SimpleAdapter(
                this,
                ticketList,
                R.layout.item_ticket_history,
                new String[]{"ticket_name", "visit_time"},
                new int[]{R.id.textView_attraction_name, R.id.textView_visit_time}
        );

        // 设置适配器
        ticketListView.setAdapter(adapter);
    }

    private void loadTicketHistory() {
        // 假设用户ID是 1（根据实际情况可以传入当前用户的ID）
        int userId = 1;

        // 获取数据库中的票务记录
        List<Ticket> tickets = dbHelper.getTicketsByUserId(userId);
        // 将查询结果转化为适配器需要的格式
        for (Ticket ticket : tickets) {
            // 获取当前系统时间（毫秒）
            long currentTime = System.currentTimeMillis();

            // 从Ticket对象中获取游览时间，并转换为Date对象
            String visitDateStr = ticket.getVisitDate();  // 假设返回的是一个字符串 "yyyy-MM-dd HH:mm"
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            long visitTime = -1;
            try {
                visitTime = sdf.parse(visitDateStr).getTime();  // 将字符串转换为时间戳
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // 获取景点名称
            String attractionName = attractionDBHelper.getAttractionById(ticket.getAttractionId()).getName();

            // 获取景点的开放时间
            String openTime = attractionDBHelper.getAttractionById(ticket.getAttractionId()).getOpenTime();
            String closeTime = attractionDBHelper.getAttractionById(ticket.getAttractionId()).getCloseTime();

            // 创建一个HashMap来存储票务信息
            HashMap<String, String> map = new HashMap<>();

            // 判断游览时间是否已经过期
            if (visitTime > currentTime) {
                // 如果游览时间大于当前时间，显示退票按钮
                map.put("ticket_name", "景点：" + attractionName);
                map.put("visit_time", "游览时间：" + visitDateStr + " " + openTime + " - " + closeTime);
                map.put("status", "未游览");  // 这里可以设置一个状态，表示票务还未游览
            } else {
                // 如果游览时间小于或等于当前时间，显示已游览
                map.put("ticket_name", "景点：" + attractionName);
                map.put("visit_time", "游览时间：" + visitDateStr + " " + openTime + " - " + closeTime);
                map.put("status", "已游览");  // 设置状态为已游览
            }

            // 将该条票务信息加入列表
            ticketList.add(map);
        }

    }
}


