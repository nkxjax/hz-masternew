package com.example.finalhomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.provider.MediaStore;
import com.example.finalhomework.util_classes.RoundImageView;
import com.example.finalhomework.util_classes.User;
import com.example.finalhomework.util_classes.UserDBHelper;
import java.util.ArrayList;

public class RegistryActivity extends AppCompatActivity {

    private RoundImageView roundImageView;
    private EditText editText_register_nickname;
    private EditText editText_register_account;
    private EditText editText_register_password;
    private Button button_register;
    private UserDBHelper userDBHelper;

    private static final int REQUEST_CODE_PERMISSION = 101; // 权限请求码
    private static final int REQUEST_CODE_PICK_IMAGE = 102; // 图片选择请求码
    private Bitmap selectedImageBitmap; // 用来保存选择的头像图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 打开数据库链接
        userDBHelper = UserDBHelper.getInstance(this);
        userDBHelper.openWriteLink();
        userDBHelper.openReadLink();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userDBHelper.closeLink(); // 关闭数据库链接
    }

    private void initViews() {
        roundImageView = findViewById(R.id.imageView_registry_avatar);
        editText_register_account = findViewById(R.id.editText_register_account);
        editText_register_nickname = findViewById(R.id.editText_register_nickname);
        editText_register_password = findViewById(R.id.editTextTextPassword_register);
        button_register = findViewById(R.id.subbutton_two_register);

        // 点击头像时检查权限并选择图片
        roundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermissions();
            }
        });

        // 注册按钮逻辑
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText_register_nickname.getText().toString().trim()) ||
                        TextUtils.isEmpty(editText_register_password.getText().toString().trim()) ||
                        TextUtils.isEmpty(editText_register_account.getText().toString().trim())) {
                    Toast.makeText(RegistryActivity.this, "请输入完整！", Toast.LENGTH_SHORT).show();
                } else {
                    User u = new User();
                    u.setNickNameString(editText_register_nickname.getText().toString());
                    u.setAccountString(editText_register_account.getText().toString());
                    u.setPasswordString(editText_register_password.getText().toString());

                    userDBHelper.register(u);

                    Intent intent = new Intent();
                    ArrayList<String> userinfoString = new ArrayList<>();
                    userinfoString.add(u.getNickNameString());
                    userinfoString.add(u.getAccountString());

                    intent.putExtra("Registry", userinfoString);
                    setResult(Activity.RESULT_OK, intent);
                    Toast.makeText(RegistryActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 或更高版本
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        REQUEST_CODE_PERMISSION);
            } else {
                openImagePicker();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6.0 到 Android 12
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSION);
            } else {
                openImagePicker();
            }
        } else {
            // Android 5.1 或更低版本，无需动态权限
            openImagePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "权限被拒绝，无法选择图片", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                roundImageView.setImageBitmap(selectedImageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(RegistryActivity.this, "选择图片失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


