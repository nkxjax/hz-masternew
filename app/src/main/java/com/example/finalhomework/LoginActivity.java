package com.example.finalhomework;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalhomework.util_classes.User;
import com.example.finalhomework.util_classes.UserDBHelper;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText editText_login_account;
    private EditText editText_login_password;
    private Button button_login;
    private UserDBHelper userDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        // 设置点击登录按钮时的逻辑
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = editText_login_account.getText().toString().trim();
                String password = editText_login_password.getText().toString().trim();

                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
                } else {
                    // 登录验证
                    User userLogin = userDBHelper.login(account, password);
                    if (userLogin != null) {
                        // 登录成功，返回用户信息
                        Intent intent = new Intent();
                        ArrayList<String> userinfoString = new ArrayList<>();
                        userinfoString.add(userLogin.getNickNameString());  // 获取昵称
                        userinfoString.add(userLogin.getAccountString());  // 获取账号
                        intent.putExtra("Login", userinfoString);
                        setResult(Activity.RESULT_OK, intent);
                        Toast.makeText(LoginActivity.this, "用户已登入", Toast.LENGTH_SHORT).show();

                        // 登录成功后存储登录状态
                        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);  // 标记为已登录
                        editor.putString("username", userLogin.getNickNameString());  // 存储用户名
                        editor.putBoolean("isAdmin", userLogin.isAdmin());
                        editor.apply();  // 保存

                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "登录失败，用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 初始化数据库连接
        userDBHelper = UserDBHelper.getInstance(this);
        userDBHelper.openReadLink();
        userDBHelper.openWriteLink();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 关闭数据库连接
        userDBHelper.closeLink();
    }

    private void initViews() {
        editText_login_account = findViewById(R.id.editText_login_account);
        editText_login_password = findViewById(R.id.editTextTextPassword_login);
        button_login = findViewById(R.id.subbutton_two_login);
    }
}
