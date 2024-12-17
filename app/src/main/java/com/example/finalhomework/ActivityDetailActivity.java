package com.example.finalhomework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityDetailActivity extends AppCompatActivity {

    private ArrayList<Comment> comments = new ArrayList<>();
    private LinearLayout commentsLayout;
    private EditText editTextComment;
    private Button btnSubmitComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 获取传递过来的数据
        Intent intent = getIntent();
        int imageResId = intent.getIntExtra("imageResId", 0);
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        String content = intent.getStringExtra("content");

        // 显示数据
        ImageView imageView = findViewById(R.id.imageView_detail);
        TextView titleTextView = findViewById(R.id.textView_detail_title);
        TextView dateTextView = findViewById(R.id.textView_detail_date);
        TextView contentTextView = findViewById(R.id.textView_detail_content);

        imageView.setImageResource(imageResId);
        titleTextView.setText(title);
        dateTextView.setText(date);
        contentTextView.setText(content);

        commentsLayout = findViewById(R.id.commentsLayout);
        editTextComment = findViewById(R.id.editText_comment);
        btnSubmitComment = findViewById(R.id.btn_submit_comment);

        // 假设你有一些评论数据
        comments.add(new Comment("用户A", "这是一条评论", getCurrentTime(), 0, false));
        comments.add(new Comment("用户B", "这是一条很有趣的评论", getCurrentTime(), 0, false));

        // 显示评论
        updateComments();

        // 设置提交评论按钮的点击监听
        btnSubmitComment.setOnClickListener(v -> {
            String commentText = editTextComment.getText().toString().trim();

            if (!commentText.isEmpty()) {
                // 创建新的评论对象并添加到评论列表
                Comment newComment = new Comment("用户C", commentText, getCurrentTime(), 0, false);
                comments.add(newComment);

                // 更新评论区
                updateComments();

                // 清空评论输入框
                editTextComment.setText("");

                // 提交成功提示
                Toast.makeText(ActivityDetailActivity.this, "评论提交成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivityDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 更新评论展示区域
    private void updateComments() {
        commentsLayout.removeAllViews(); // 清除旧的评论视图
        for (Comment comment : comments) {
            View commentView = getLayoutInflater().inflate(R.layout.comment_item, null);

            // 获取评论视图组件
            TextView authorTextView = commentView.findViewById(R.id.textView_author);
            TextView contentTextView = commentView.findViewById(R.id.textView_comment_content);
            TextView timeTextView = commentView.findViewById(R.id.textView_time);
            ImageView likeImageView = commentView.findViewById(R.id.imageView_like);
            TextView likeCountTextView = commentView.findViewById(R.id.textView_like_count);

            // 设置评论内容
            authorTextView.setText(comment.getAuthor());
            contentTextView.setText(comment.getContent());
            timeTextView.setText(comment.getTime());
            likeCountTextView.setText(String.valueOf(comment.getLikeCount()));

            // 设置点赞按钮状态
            if (comment.isLiked()) {
                likeImageView.setImageResource(R.drawable.ic_heart_filled);
            } else {
                likeImageView.setImageResource(R.drawable.ic_heart_empty);
            }

            // 点赞按钮点击事件
            likeImageView.setOnClickListener(v -> {
                if (!comment.isLiked()) {
                    comment.setLiked(true);
                    comment.incrementLikeCount();
                    likeImageView.setImageResource(R.drawable.ic_heart_filled); // 设置为实心红色爱心
                    likeCountTextView.setText(String.valueOf(comment.getLikeCount()));
                } else {
                    Toast.makeText(ActivityDetailActivity.this, "您已经点赞过了", Toast.LENGTH_SHORT).show();
                }
            });

            // 将评论添加到布局中
            commentsLayout.addView(commentView);
        }
    }

    // 获取当前时间
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    // 评论类
    private static class Comment {
        private String author;
        private String content;
        private String time;
        private int likeCount;
        private boolean isLiked;

        public Comment(String author, String content, String time, int likeCount, boolean isLiked) {
            this.author = author;
            this.content = content;
            this.time = time;
            this.likeCount = likeCount;
            this.isLiked = isLiked;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public String getTime() {
            return time;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void incrementLikeCount() {
            this.likeCount++;
        }

        public boolean isLiked() {
            return isLiked;
        }

        public void setLiked(boolean liked) {
            isLiked = liked;
        }
    }
}
