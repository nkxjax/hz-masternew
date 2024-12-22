package com.example.finalhomework;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.SharedPreferences;
import android.content.Intent;
import com.bumptech.glide.Glide;


public class OverAllActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private EditText editTextTitle;
    private ImageView imageView;
    private boolean isAdmin;  // 用于判断当前用户是否是管理员
    private SharedPreferences sharedPreferences;

    private static final int REQUEST_CODE_PERMISSION = 1; // 请求码，用于权限请求

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_all);

        // 初始化视图
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextTitle = findViewById(R.id.editTextTitle);
        imageView = findViewById(R.id.imageView);

        // 获取 SharedPreferences
        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        // 获取用户信息，判断是否是管理员
        isAdmin = sharedPreferences.getBoolean("isAdmin", false);  // 默认非管理员

        // 检查读取权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("Permissions", "读取权限未授予，请求权限");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            // 提示用户去设置页面手动授予权限
//            Toast.makeText(this, "请在设置中授予存储权限", Toast.LENGTH_LONG).show();

//            // 打开应用的设置页面
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            Uri uri = Uri.fromParts("package", getPackageName(), null);
//            intent.setData(uri);
//            startActivity(intent);
        } else {
            Log.d("Permissions", "读取权限已授予");
            loadSavedData();
        }

        // 检查写入权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permissions", "写入权限未授予，请求权限");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        } else {
            Log.d("Permissions", "写入权限已授予");
        }

        // 根据权限控制是否允许编辑
        if (isAdmin) {
            // 如果是管理员，显示可编辑控件
            textViewTitle.setVisibility(View.GONE);
            editTextTitle.setVisibility(View.VISIBLE);

            // 加载之前保存的标题
            String savedTitle = sharedPreferences.getString("savedTitle", "默认标题");
            editTextTitle.setText(savedTitle);  // 设置默认标题或上次保存的标题

            // 监听文本变化，自动保存
            editTextTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // 每次文本变化后保存标题
                    String newTitle = editable.toString().trim();
                    if (!newTitle.isEmpty()) {
                        // 保存到 SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("savedTitle", newTitle);  // 保存标题
                        editor.apply();
                    }
                }
            });
        } else {
            // 如果不是管理员，显示不可编辑的TextView
            textViewTitle.setVisibility(View.VISIBLE);
            editTextTitle.setVisibility(View.GONE);

            // 加载之前保存的标题
            String savedTitle = sharedPreferences.getString("savedTitle", "默认标题");
            textViewTitle.setText(savedTitle);  // 显示保存的标题
        }
    }

    // 加载保存的数据
    private void loadSavedData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            String savedImageUri = sharedPreferences.getString("savedImage", null);
            if (savedImageUri != null) {
                Uri imageUri = Uri.parse(savedImageUri);
                Glide.with(this)
                        .load(imageUri)
                        .into(imageView);  // 使用 Glide 加载保存的图片
            }
        } else {
            Toast.makeText(this, "没有权限访问图片", Toast.LENGTH_SHORT).show();
        }
    }



    // 当管理员点击图片时，允许选择新的图片
    public void onImageClick(View view) {
        if (isAdmin) {
            // 这里可以弹出一个图片选择器，允许管理员更改图片
            Toast.makeText(this, "管理员可以更改图片", Toast.LENGTH_SHORT).show();

            // 使用 Intent 来选择图片
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1001);  // 请求码 1001
        } else {
            Toast.makeText(this, "你不是管理员，无法更改图片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                Log.d("OverAllActivity", "Selected image URI: " + selectedImageUri.toString());

                // 检查是否有写入权限
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
                } else {
                    // 使用 Glide 加载图片
                    Glide.with(this)
                            .load(selectedImageUri)
                            .into(imageView);

                    // 保存图片 URI 到 SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("savedImage", selectedImageUri.toString());
                    editor.apply();
                }
            } else {
                Toast.makeText(this, "图片URI无效", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // 处理权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限授予，继续执行操作
                loadSavedData();
            } else {
                // 权限被拒绝，弹出提示
//                Toast.makeText(this, "权限被拒绝，无法继续操作", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
