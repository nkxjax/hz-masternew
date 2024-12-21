package com.example.finalhomework;

import android.app.DatePickerDialog;
import android.content.Context;
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

import java.util.Calendar;

public class AttractionDetailActivity extends AppCompatActivity {
    private TextView nameTextView, locationTextView, descriptionTextView, ticketPriceTextView;
    private EditText quantityEditText;
    private Button buyTicketButton;
    private TicketDBHelper dbHelper;
    private Button buttonSelectDate;
    private TextView textViewSelectDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);  // 设置布局
        textViewSelectDate = findViewById(R.id.textView_select_date);
        nameTextView = findViewById(R.id.textView_attraction_name);
        locationTextView = findViewById(R.id.textView_attraction_location);
        descriptionTextView = findViewById(R.id.textView_attraction_description);
        ticketPriceTextView = findViewById(R.id.textView_ticket_price);
        quantityEditText = findViewById(R.id.editText_ticket_quantity);
        buyTicketButton = findViewById(R.id.button_buy_ticket);
        buttonSelectDate = findViewById(R.id.button_select_date);

        dbHelper = TicketDBHelper.getInstance(this);
        Log.d("TicketPurchase", "Database helper initialized: " + dbHelper);

        // 获取传递的景点 ID
        int attractionId = getIntent().getIntExtra("attraction_id", -1);

        // 根据景点 ID 查询并显示详细信息（此部分根据需要进一步实现）
        // 例如，可以使用 AttractionDBHelper 查询数据库中的景点信息
        Attraction attraction = getAttractionById(attractionId);

        if (attraction != null) {
            nameTextView.setText(attraction.getName());
            locationTextView.setText(attraction.getLocation());
            descriptionTextView.setText(attraction.getDescription());
            ticketPriceTextView.setText("票价：￥" + attraction.getTicketPrice());
        }
        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        // 设置买票按钮的点击事件
        buyTicketButton.setOnClickListener(v -> {
            // 获取用户输入的购买数量
            String quantityText = quantityEditText.getText().toString();
            String visitDate = textViewSelectDate.getText().toString(); // 获取选择的参观日期

            if (quantityText.isEmpty()) {
                Toast.makeText(this, "请输入购买数量", Toast.LENGTH_SHORT).show();
            } else {
                int quantity = Integer.parseInt(quantityText);
                if (quantity <= 0) {
                    Toast.makeText(this, "数量必须大于 0", Toast.LENGTH_SHORT).show();
                } else {
                    // 计算总票价
                    double ticketPrice = attraction.getTicketPrice();
                    double totalPrice = ticketPrice * quantity;
                    int userId = 1;

                    Ticket ticket = new Ticket(userId, attractionId, quantity, totalPrice, System.currentTimeMillis(), 1, visitDate);
                    long result = dbHelper.addTicket(ticket);  // 使用addTicket方法插入数据，返回的是插入的ID

                    if(result != -1) Toast.makeText(this, "购买成功", Toast.LENGTH_SHORT).show();
                    // 这里可以添加跳转到支付页面的代码
                    // 例如：startActivity(new Intent(this, PaymentActivity.class));
                }
            }
        });
    }

    // 根据景点 ID 获取景点信息的示例方法
    private Attraction getAttractionById(int id) {
        // 这里调用数据库或其他方式查询景点信息
        // 这是一个示例方法，实际代码可能需要查询数据库
        AttractionDBHelper dbHelper = AttractionDBHelper.getInstance(this);
        return dbHelper.getAttractionById(id);
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    // 格式化并显示选择的日期
                    String selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                    textViewSelectDate.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }
}


//买票数据存到ticket数据库