package com.example.finalhomework.util_classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

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
        this.userDBHelper = UserDBHelper.getInstance(context);
        this.attractionDBHelper = AttractionDBHelper.getInstance(context);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ticket_item, parent, false);
        }

        Ticket ticket = ticketList.get(position);

        TextView userIdTextView = convertView.findViewById(R.id.userId);
        String username = userDBHelper.getUsernameByUserId(ticket.getUserId());
        userIdTextView.setText("购票人：" + (username != null ? username : "Unknown"));

        TextView attractionIdTextView = convertView.findViewById(R.id.attractionId);
        Attraction attraction = attractionDBHelper.getAttractionById(ticket.getAttractionId());
        attractionIdTextView.setText("景区名称：" + String.valueOf(attraction.getName()));

        TextView quantityTextView = convertView.findViewById(R.id.quantity);
        quantityTextView.setText(String.valueOf("购票数量：" + ticket.getQuantity()));

        TextView visitingTimeTextView = convertView.findViewById(R.id.visitingTime);
        visitingTimeTextView.setText("游览时间：" + ticket.getVisitDate() + " " + attraction.getOpenTime() + " - " + attraction.getCloseTime());

        TextView purchaseTimeTextView = convertView.findViewById(R.id.purchaseTime);
        Date date = new Date(ticket.getPurchaseTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("购票时间：" + "yyyy-MM-dd", Locale.getDefault());
        purchaseTimeTextView.setText(dateFormat.format(date));

        return convertView;
    }
}
