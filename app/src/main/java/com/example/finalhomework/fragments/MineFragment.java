package com.example.finalhomework.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalhomework.LoginActivity;
import com.example.finalhomework.R;
import com.example.finalhomework.RegistryActivity;
import com.example.finalhomework.TicketHistoryActivity;
import com.example.finalhomework.util_classes.RoundImageView;

import java.util.ArrayList;

public class MineFragment extends Fragment {

    private ListView mine_listView;
    private String userName_now = "未登录";
    private String userAccount_now = "用户账号";
    private int userImgId_now = R.mipmap.ic_launcher;
    private TextView textView_userName;
    private TextView textView_userAccount;
    private RoundImageView avatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        textView_userName = view.findViewById(R.id.textView_mine_username);
        textView_userAccount = view.findViewById(R.id.textView_mine_userId);
        avatar = view.findViewById(R.id.roundImageView_mine);
        mine_listView = view.findViewById(R.id.listView_mine_list);

        // 获取 SharedPreferences 中的用户信息
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            String userName = sharedPreferences.getString("username", "未登录");
            String userAccount = sharedPreferences.getString("userAccount", "用户账号");
            boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);
            // 默认头像
            int userImgId = R.mipmap.ic_launcher;
            if (isLoggedIn) {
                // 如果用户已登录，根据实际情况修改头像
                userImgId = R.drawable.pic_jiuzhai_overall;
            }
            // 更新视图
            userName_now = userName;
            userAccount_now = userAccount;
            userImgId_now = userImgId;
            updateView();
        }

        mine_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getContext(), TicketHistoryActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        // 注册
                        intent = new Intent(getContext(), RegistryActivity.class);
                        startActivityForResult(intent, 1);
                        break;
                    case 2:
                        // 登录
                        intent = new Intent(getContext(), LoginActivity.class);
                        startActivityForResult(intent, 0);
                        break;
                    case 3:
                        // 退出登录
                        LogOut();
                        break;
                }
            }
        });

        return view;
    }

    private void updateView() {
        textView_userAccount.setText(userAccount_now);
        textView_userName.setText(userName_now);
        avatar.setImageResource(userImgId_now);
    }

    private void LogOut() {
        if (!userAccount_now.equals("用户账号") || !userName_now.equals("未登录") || userImgId_now != R.mipmap.ic_launcher) {
            userAccount_now = "用户账号";
            userName_now = "未登录";
            userImgId_now = R.mipmap.ic_launcher;
            updateView();

            // 用户点击退出登录按钮时清除登录状态
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);  // 标记为未登录
            editor.putString("username", "");  // 清空用户名
            editor.putString("userAccount", "");  // 清空用户账号
            editor.putBoolean("isAdmin", false); // 清空管理员标志
            editor.apply();  // 保存更改

            Toast.makeText(getContext(), "用户已登出", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "请先登入", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null){
            return;
        }
        if(requestCode == 0 && resultCode == Activity.RESULT_OK){ // 登录后获取用户信息
            ArrayList<String> userStringInfo = data.getStringArrayListExtra("Login");
            userName_now = userStringInfo.get(0);
            userAccount_now = userStringInfo.get(1);
            userImgId_now = R.drawable.pic_jiuzhai_overall; // 设置头像
            updateView();
        } else if(requestCode == 1 && resultCode == Activity.RESULT_OK) { // 注册后获取用户信息
            ArrayList<String> userStringInfo = data.getStringArrayListExtra("Registry");
            userName_now = userStringInfo.get(0);
            userAccount_now = userStringInfo.get(1);
            userImgId_now = R.drawable.pic_jiuzhai_overall; // 设置头像
            updateView();
        }
    }
}

