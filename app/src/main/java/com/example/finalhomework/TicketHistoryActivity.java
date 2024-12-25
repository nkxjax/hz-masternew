package com.example.finalhomework;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalhomework.R;
import com.example.finalhomework.util_classes.AttractionDBHelper;
import com.example.finalhomework.util_classes.Ticket;
import com.example.finalhomework.util_classes.TicketDBHelper;
import com.example.finalhomework.util_classes.TicketHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class TicketHistoryActivity extends AppCompatActivity {

    private ListView ticketListView;
    private TicketDBHelper ticketDBHelper;
    private AttractionDBHelper attractionDBHelper;  // Ensure it's initialized properly
    private List<Ticket> ticketList;
    private TicketHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);

        // Initialize DB helpers using the singleton
        ticketDBHelper = TicketDBHelper.getInstance(this);
        attractionDBHelper = AttractionDBHelper.getInstance(this);  // Use the singleton method here

        ticketListView = findViewById(R.id.listView_ticket_history);
        ticketList = new ArrayList<>();

        // Ensure the adapter is initialized and passed to the ListView
        adapter = new TicketHistoryAdapter(this, ticketList, attractionDBHelper, ticketDBHelper);
        ticketListView.setAdapter(adapter);

        // Load the ticket history data
        loadTicketHistory();
    }

    private void loadTicketHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);  // 默认值为 -1，如果没有找到 userId

        List<Ticket> tickets = ticketDBHelper.getTicketsByUserId(userId);
        Log.d("tickets", "tickets" + tickets);
        ticketList.clear();
        ticketList.addAll(tickets);
        adapter.notifyDataSetChanged();
    }
}