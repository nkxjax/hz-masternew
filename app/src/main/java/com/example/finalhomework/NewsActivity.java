package com.example.finalhomework;

import static java.security.AccessController.getContext;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalhomework.util_classes.News;
import com.example.finalhomework.util_classes.NewsDBHelper;

import java.util.Date;

public class NewsActivity extends AppCompatActivity {

    private EditText newsTitle;
    private EditText newsContent;
    private TextView newsTime;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // 获取控件
        newsTitle = findViewById(R.id.news_title);
        newsContent = findViewById(R.id.news_content);
        newsTime = findViewById(R.id.news_time);
        saveButton = findViewById(R.id.save_button);

        // 获取传递的 newsId 和 userId
        int newsId = getIntent().getIntExtra("newsId", -1);
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);  // 默认值为 -1，如果没有找到 userId

        Log.d("Button", "user_id" + userId);
        Log.d("Button", "news_id" + newsId);

        // 根据 userId 设置控件是否可编辑
        if (userId == 1) {
            // 允许编辑
            newsTitle.setFocusableInTouchMode(true);
            newsContent.setFocusableInTouchMode(true);
            newsTitle.setFocusable(true);
            newsContent.setFocusable(true);
            // 显示保存按钮
            saveButton.setVisibility(View.VISIBLE);

            // 设置保存按钮的点击事件
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveNewsContent();
                }
            });
        } else {
            // 不允许编辑
            newsTitle.setFocusable(false);
            newsContent.setFocusable(false);
            newsTitle.setFocusableInTouchMode(false);
            newsContent.setFocusableInTouchMode(false);
            // 隐藏保存按钮
            saveButton.setVisibility(View.GONE);
        }

        // 根据 newsId 加载相应的新闻内容
        loadNewsContent(newsId);
    }

    private void loadNewsContent(int newsId) {
        // 创建 NewsDBHelper 实例
        Log.d("loadNewsContent", "news_id" + newsId);
        NewsDBHelper dbHelper = new NewsDBHelper(this);
        // 从数据库中获取新闻数据
        News news = dbHelper.getNewsById(newsId);
        Log.d("loadNewsContent", "news" + news);
        // 检查是否找到了新闻
        if (news != null) {
            // 设置新闻内容到 UI
            newsTitle.setText(news.getTitle());
            newsContent.setText(news.getContent());
            newsTime.setText("发布时间：" + news.getPublishTime());
        } else {
            // 处理未找到新闻的情况
            // Assuming you're in an Activity or using a valid Context
            Toast.makeText(NewsActivity.this, "没有新闻加载", Toast.LENGTH_SHORT).show();


        }
    }


    private void saveNewsContent() {
        // 获取编辑后的新闻标题和内容
        String updatedTitle = newsTitle.getText().toString();
        String updatedContent = newsContent.getText().toString();
        // 获取更新时间（当前时间）
        Date updatedTime = new Date();

        // 获取要更新的 newsId
        int newsId = getNewsId(); // 获取当前新闻的 ID
        Log.d("NewsActivity", "newsId" + newsId);

        // 使用 NewsDBHelper 更新新闻
        NewsDBHelper newsDBHelper = new NewsDBHelper(this);
        newsDBHelper.updateNews(newsId, updatedTitle, updatedContent, String.valueOf(updatedTime));

        // 给用户提示保存成功
        Toast.makeText(this, "新闻已保存", Toast.LENGTH_SHORT).show();
    }

    private int getNewsId() {
        return getIntent().getIntExtra("newsId", -1);
    }
}
