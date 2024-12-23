package com.example.finalhomework;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalhomework.util_classes.Attraction;
import com.example.finalhomework.util_classes.AttractionDBHelper;
import com.example.finalhomework.util_classes.AttractionAdapter;  // 引入适配器类

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddAttractionActivity extends AppCompatActivity {

    private EditText editTextName, editTextLocation, editTextDescription, editTextPrice, editTextOpen, editTextClose;
    private Button buttonSubmit;
    private AttractionDBHelper dbHelper;
    private Calendar calendar;
    private AttractionAdapter attractionAdapter;  // 用于通知适配器刷新数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attraction);

        // 获取传递过来的适配器实例
        attractionAdapter = (AttractionAdapter) getIntent().getSerializableExtra("adapter");

        // 初始化控件
        editTextName = findViewById(R.id.edit_text_name);
        editTextLocation = findViewById(R.id.edit_text_location);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPrice = findViewById(R.id.edit_text_price);
        editTextOpen = findViewById(R.id.edit_text_open);
        editTextClose = findViewById(R.id.edit_text_close);
        calendar = Calendar.getInstance();
        buttonSubmit = findViewById(R.id.button_submit);

        // 初始化数据库帮助类
        dbHelper = AttractionDBHelper.getInstance(this);

        // 设置时间选择器监听器
        setTimePickerDialog(editTextOpen);
        setTimePickerDialog(editTextClose);

        // 提交按钮点击事件
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入框中的数据
                String name = editTextName.getText().toString();
                String location = editTextLocation.getText().toString();
                String description = editTextDescription.getText().toString();
                String price = editTextPrice.getText().toString();
                String openTime = editTextOpen.getText().toString();
                String closeTime = editTextClose.getText().toString();

                // 检查输入字段是否为空
                if (name.isEmpty() || location.isEmpty() || description.isEmpty() || price.isEmpty() ||
                        openTime.isEmpty() || closeTime.isEmpty()) {
                    Toast.makeText(AddAttractionActivity.this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 创建 Attraction 对象
                Attraction attraction = new Attraction();
                attraction.setName(name);
                attraction.setLocation(location);
                attraction.setDescription(description);
                attraction.setTicketPrice(Double.parseDouble(price));
                attraction.setOpenTime(openTime);
                attraction.setCloseTime(closeTime);

                // 调用数据库帮助类的 addAttraction 方法添加景点
                long result = dbHelper.addAttraction(attraction);
                if (result != -1) {
                    // 插入成功
                    Toast.makeText(AddAttractionActivity.this, "景点添加成功！", Toast.LENGTH_SHORT).show();

                    // 从数据库中读取所有景点并更新到适配器中
                    ArrayList<Attraction> updatedAttractions = dbHelper.getAllAttractions();
                    attractionAdapter.updateData(updatedAttractions);  // 更新适配器的数据

                    // 关闭当前 Activity
                    finish();
                } else {
                    // 插入失败
                    Toast.makeText(AddAttractionActivity.this, "景点添加失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 提取重复的时间选择器创建代码
    private void setTimePickerDialog(final EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddAttractionActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // 设置时间到 Calendar
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                // 格式化时间并设置到 EditText
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                String time = sdf.format(calendar.getTime());
                                editText.setText(time);  // 更新对应的 EditText
                            }
                        },
                        calendar.get(Calendar.HOUR_OF_DAY), // 初始小时
                        calendar.get(Calendar.MINUTE), // 初始分钟
                        true // 24小时制
                );
                timePickerDialog.show();
            }
        });
    }
}





