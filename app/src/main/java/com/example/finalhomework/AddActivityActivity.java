package com.example.finalhomework;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.finalhomework.model.Schedule;
import com.example.finalhomework.util_classes.ActivityDBHelper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddActivityActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // 图片选择请求码
    private EditText editTitle, editContent;
    private DatePicker datePicker;
    private ImageView imageViewCover;
    private ActivityDBHelper dbHelper;
    private Button btnSaveActivity, btnSelectImage;
    private Bitmap selectedImageBitmap;  // 存储选择的图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);

        dbHelper = new ActivityDBHelper(this);

        // 初始化视图组件
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        datePicker = findViewById(R.id.datePicker);
        imageViewCover = findViewById(R.id.imageViewCover);
        btnSaveActivity = findViewById(R.id.btnSaveActivity);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        // 检查权限
        checkPermissions();

        // 选择封面图片
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动图片选择器
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");  // 限制选择图片
                startActivityForResult(intent, PICK_IMAGE_REQUEST);  // 启动图片选择器
            }
        });

        // 保存活动
        btnSaveActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveActivity();
            }
        });
    }

    // 请求存储权限
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    // 处理图片选择结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
                imageViewCover.setImageBitmap(selectedImageBitmap);  // 显示选择的图片
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 将 Bitmap 转换为字节数组
    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);  // 将图片压缩为PNG格式
        return byteArrayOutputStream.toByteArray();
    }

    // 保存活动
    private void saveActivity() {
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        String date = year + "-" + (month + 1) + "-" + day;  // 格式化日期

        if (title.isEmpty() || content.isEmpty() || selectedImageBitmap == null) {
            Toast.makeText(AddActivityActivity.this, "请填写完整的活动信息", Toast.LENGTH_SHORT).show();
            return;
        }

        // 将图片转换为字节数组
        byte[] imageByteArray = convertBitmapToByteArray(selectedImageBitmap);

        // 插入数据到数据库
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("date", date);
        values.put("content", content);
        values.put("img_res_id", imageByteArray);  // 存储字节数组

        long result = dbHelper.insertActivity(values);

        if (result != -1) {
            Toast.makeText(AddActivityActivity.this, "活动已保存", Toast.LENGTH_SHORT).show();

            // 保存成功后，发送广播通知刷新列表
            Intent intent = new Intent("com.example.finalhomework.REFRESH_ACTIVITY_LIST");
            sendBroadcast(intent);  // 发送广播
            finish();  // 保存成功后关闭界面
        } else {
            Toast.makeText(AddActivityActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

}
