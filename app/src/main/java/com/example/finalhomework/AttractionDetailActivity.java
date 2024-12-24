package com.example.finalhomework;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalhomework.util_classes.Attraction;
import com.example.finalhomework.util_classes.AttractionDBHelper;
import com.example.finalhomework.util_classes.Ticket;
import com.example.finalhomework.util_classes.TicketDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.alipay.sdk.app.PayTask;

public class AttractionDetailActivity extends AppCompatActivity {
    private TextView nameTextView, locationTextView, descriptionTextView, ticketPriceTextView;
    private EditText quantityEditText;
    private Button buyTicketButton;
    private TicketDBHelper dbHelper;
    private Button buttonSelectDate;
    private TextView textViewSelectDate;

    private static final int ALIPAY_REQUEST_CODE = 10001; // Request code for Alipay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);  // Set layout

        // Initialize UI elements
        textViewSelectDate = findViewById(R.id.textView_select_date);
        nameTextView = findViewById(R.id.textView_attraction_name);
        locationTextView = findViewById(R.id.textView_attraction_location);
        descriptionTextView = findViewById(R.id.textView_attraction_description);
        ticketPriceTextView = findViewById(R.id.textView_ticket_price);
        quantityEditText = findViewById(R.id.editText_ticket_quantity);
        buyTicketButton = findViewById(R.id.button_buy_ticket);
        buttonSelectDate = findViewById(R.id.button_select_date);

        dbHelper = TicketDBHelper.getInstance(this);

        // Get the attraction ID passed from the previous activity
        int attractionId = getIntent().getIntExtra("attraction_id", -1);
        Attraction attraction = getAttractionById(attractionId);

        if (attraction != null) {
            nameTextView.setText(attraction.getName());
            locationTextView.setText(attraction.getLocation());
            descriptionTextView.setText(attraction.getDescription());
            ticketPriceTextView.setText("票价：￥" + attraction.getTicketPrice());
        }

        // Date picker dialog
        buttonSelectDate.setOnClickListener(v -> showDatePickerDialog());

        // Buy ticket button action
        buyTicketButton.setOnClickListener(v -> {
            // Get user input for ticket quantity and selected date
            String quantityText = quantityEditText.getText().toString();
            String visitDate = textViewSelectDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentDate = new Date();
            String statusChangeTime = sdf.format(currentDate);

            if (quantityText.isEmpty()) {
                Toast.makeText(this, "请输入购买数量", Toast.LENGTH_SHORT).show();
            } else {
                int quantity = Integer.parseInt(quantityText);
                if (quantity <= 0) {
                    Toast.makeText(this, "数量必须大于 0", Toast.LENGTH_SHORT).show();
                } else {
                    double ticketPrice = attraction.getTicketPrice();
                    double totalPrice = ticketPrice * quantity;
                    SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                    int userId = sharedPreferences.getInt("userId", -1);

                    // Create a new Ticket object
                    Ticket ticket = new Ticket(userId, attractionId, quantity, totalPrice, System.currentTimeMillis(), 1, visitDate, statusChangeTime);
                    long result = dbHelper.addTicket(ticket);  // Add the ticket to DB

                    if (result != -1) {
                        Toast.makeText(this, "购买成功", Toast.LENGTH_SHORT).show();
                        // Proceed to Alipay payment
                        initiateAlipayPayment(totalPrice);
                    }
                }
            }
        });
    }

    // Method to fetch Attraction details by ID
    private Attraction getAttractionById(int id) {
        AttractionDBHelper dbHelper = AttractionDBHelper.getInstance(this);
        return dbHelper.getAttractionById(id);
    }

    // Show the date picker dialog
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day);

        long currentTimeInMillis = calendar.getTimeInMillis();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                    textViewSelectDate.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(currentTimeInMillis);
        datePickerDialog.show();
    }

    // Initiate Alipay payment
    private void initiateAlipayPayment(double totalPrice) {
        String orderInfo = generateOrderInfo(totalPrice);
        String sign = signOrderInfo(orderInfo);

        // Create the Alipay payment task
        PayTask payTask = new PayTask(this);
        String result = payTask.pay(orderInfo + "&sign=" + sign, true);  // Alipay SDK call to initiate payment

        // Process the result (can be enhanced with background processing)
        handleAlipayResult(result);
    }

    // Generate order info string for Alipay
    private String generateOrderInfo(double totalPrice) {
        // Example order info (add your own fields based on Alipay API)
        return "app_id=YOUR_APP_ID&method=alipay.trade.app.pay&total_amount=" + totalPrice;
    }

    // Sign the order information
    private String signOrderInfo(String orderInfo) {
        // Sign the order info using your private key (implement this properly)
        return "SIGNED_ORDER_INFO"; // Replace with actual signing process
    }

    // Handle Alipay result (success or failure)
    private void handleAlipayResult(String result) {
        if (result.contains("success")) {
            Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
            // Optionally navigate to a payment success page
        } else {
            Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
        }
    }
}
