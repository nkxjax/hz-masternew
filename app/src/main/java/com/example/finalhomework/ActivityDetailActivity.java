package com.example.finalhomework;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalhomework.util_classes.ActivityDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        byte[] imageResId = intent.getByteArrayExtra("imageResId"); // 获取图片字节数组
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        String content = intent.getStringExtra("content");
        String activityId = intent.getStringExtra("activityId");
        // 初始化悬浮按钮
        FloatingActionButton fabDeleteActivity = findViewById(R.id.fab_delete_activity);

        // 初始化界面元素
        ImageView imageView = findViewById(R.id.imageView_detail);
        TextView titleTextView = findViewById(R.id.textView_detail_title);
        TextView dateTextView = findViewById(R.id.textView_detail_date);
        TextView contentTextView = findViewById(R.id.textView_detail_content);
        commentsLayout = findViewById(R.id.commentsLayout);  // 初始化评论区布局
        editTextComment = findViewById(R.id.editText_comment);
        btnSubmitComment = findViewById(R.id.btn_submit_comment);

        // 设置图片
        if (imageResId != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageResId, 0, imageResId.length);
            imageView.setImageBitmap(bitmap);
        }

        titleTextView.setText(title);
        dateTextView.setText(date);
        contentTextView.setText(content);

        // 提交评论按钮点击事件
        btnSubmitComment.setOnClickListener(v -> {
            String commentContent = editTextComment.getText().toString().trim();
            if (commentContent.isEmpty()) {
                Toast.makeText(ActivityDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
            } else {
                // 检查登录状态
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);  // 默认值为 false

                if (!isLoggedIn) {
                    // 如果未登录，提示用户并不允许发表评论
                    Toast.makeText(ActivityDetailActivity.this, "请先登录后发表评论", Toast.LENGTH_SHORT).show();
                } else {
                    // 获取登录用户的昵称
                    String username = sharedPreferences.getString("username", "");  // 获取用户名，如果没有则为空字符串
                    if (username.isEmpty()) {
                        // 如果用户名为空，说明未成功获取用户名，提示错误
                        Toast.makeText(ActivityDetailActivity.this, "无法获取用户名，请重新登录", Toast.LENGTH_SHORT).show();
                    } else {
                        // 创建评论对象并插入到数据库
                        Comment newComment = new Comment(username, commentContent, getCurrentTime(), 0, false, activityId);
                        insertComment(newComment);  // 同步插入评论
                        editTextComment.setText("");  // 清空输入框
                    }
                }

            }
        });

        // 加载评论
        loadComments(activityId);  // 同步加载评论

        boolean isAdmin = checkIfAdmin();  // 判断当前用户是否是管理员

        if (isAdmin) {
            fabDeleteActivity.setVisibility(View.VISIBLE);  // 显示悬浮按钮
            // 悬浮按钮点击事件，删除活动
            fabDeleteActivity.setOnClickListener(v -> {
                deleteActivity(activityId);  // 调用删除活动的方法
            });
        } else {
            // 非管理员时，隐藏按钮
            fabDeleteActivity.setVisibility(View.GONE);
        }
    }

    // 判断是否为管理员
    private boolean checkIfAdmin() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);  // 获取登录状态
        if (!isLoggedIn) {
            // 如果未登录，默认返回不是管理员
            return false;
        }

        // 获取是否为管理员的标识
        boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);  // 获取管理员标识
        return isAdmin;
    }

    // 删除活动
    private void deleteActivity(String activityId) {
        ActivityDBHelper dbHelper = new ActivityDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 删除活动
        int rowsDeleted = db.delete(ActivityDBHelper.TABLE_NAME,
                "id=?", new String[]{activityId});

        db.close();

        if (rowsDeleted > 0) {
            Toast.makeText(this, "活动已删除", Toast.LENGTH_SHORT).show();

            // 发送广播通知父页面刷新活动列表
            Intent intent = new Intent("com.example.finalhomework.REFRESH_ACTIVITY_LIST");
            sendBroadcast(intent);  // 发送广播

            // 返回父页面
            finish();
        } else {
            Toast.makeText(this, "删除失败，请稍后再试", Toast.LENGTH_SHORT).show();
        }
    }


    // 获取当前时间
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    // 同步插入评论到数据库
    private void insertComment(Comment comment) {
        DatabaseHelper dbHelper = new DatabaseHelper(ActivityDetailActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_AUTHOR, comment.getAuthor());
        values.put(DatabaseHelper.COLUMN_CONTENT, comment.getContent());
        values.put(DatabaseHelper.COLUMN_TIME, comment.getTime());
        values.put(DatabaseHelper.COLUMN_LIKE_COUNT, comment.getLikeCount());
        values.put(DatabaseHelper.COLUMN_IS_LIKED, comment.isLiked() ? 1 : 0);
        values.put(DatabaseHelper.COLUMN_ACTIVITY_ID, comment.getActivityId());

        db.insert(DatabaseHelper.TABLE_COMMENTS, null, values);
        db.close();

        // 插入评论后直接刷新评论列表
        loadComments(comment.getActivityId());
    }

    // 同步加载评论
    private void loadComments(String activityId) {
        ArrayList<Comment> loadedComments = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(ActivityDetailActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_COMMENTS, null,
                DatabaseHelper.COLUMN_ACTIVITY_ID + "=?",
                new String[]{activityId}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String author = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHOR));
                String content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTENT));
                String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME));
                int likeCount = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_LIKE_COUNT));
                boolean isLiked = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_IS_LIKED)) == 1;
                String activityIdFromDb = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_ID));

                loadedComments.add(new Comment(author, content, time, likeCount, isLiked, activityIdFromDb));
            } while (cursor.moveToNext());
            cursor.close();
        }
        updateComments(loadedComments);  // 更新评论展示
    }

    // 更新评论展示区域
    private void updateComments(ArrayList<Comment> loadedComments) {
        commentsLayout.removeAllViews();
        for (Comment comment : loadedComments) {
            View commentView = getLayoutInflater().inflate(R.layout.comment_item, null);

            TextView authorTextView = commentView.findViewById(R.id.textView_author);
            TextView contentTextView = commentView.findViewById(R.id.textView_comment_content);
            TextView timeTextView = commentView.findViewById(R.id.textView_time);
            ImageView likeImageView = commentView.findViewById(R.id.imageView_like);
            TextView likeCountTextView = commentView.findViewById(R.id.textView_like_count);

            authorTextView.setText(comment.getAuthor());
            contentTextView.setText(comment.getContent());
            timeTextView.setText(comment.getTime());
            likeCountTextView.setText(String.valueOf(comment.getLikeCount()));

            if (comment.isLiked()) {
                likeImageView.setImageResource(R.drawable.ic_heart_filled);
            } else {
                likeImageView.setImageResource(R.drawable.ic_heart_empty);
            }

            likeImageView.setOnClickListener(v -> {
                // 检查登录状态
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);  // 默认值为 false

                if (!isLoggedIn) {
                    Toast.makeText(ActivityDetailActivity.this, "登录后才能点赞哦", Toast.LENGTH_SHORT).show();
                } else {
                    if (!comment.isLiked()) {
                        comment.setLiked(true);
                        comment.incrementLikeCount();
                        likeImageView.setImageResource(R.drawable.ic_heart_filled);
                        likeCountTextView.setText(String.valueOf(comment.getLikeCount()));
                        updateLikeStatus(comment);  // 同步更新点赞状态
                    } else {
                        comment.setLiked(false);
                        comment.decreaseLikeCount();
                        likeImageView.setImageResource(R.drawable.ic_heart_empty);
                        likeCountTextView.setText(String.valueOf(comment.getLikeCount()));
                        updateLikeStatus(comment);  // 同步更新点赞状态
                    }
                }
            });

            commentsLayout.addView(commentView);
        }
    }

    // 同步更新点赞状态到数据库
    private void updateLikeStatus(Comment comment) {
        DatabaseHelper dbHelper = new DatabaseHelper(ActivityDetailActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LIKE_COUNT, comment.getLikeCount());
        values.put(DatabaseHelper.COLUMN_IS_LIKED, comment.isLiked() ? 1 : 0);

        db.update(DatabaseHelper.TABLE_COMMENTS, values,
                DatabaseHelper.COLUMN_ACTIVITY_ID + "=? AND " + DatabaseHelper.COLUMN_AUTHOR + "=?",
                new String[]{comment.getActivityId(), comment.getAuthor()});

        db.close();
    }

    // 评论类
    private static class Comment {
        private String author;
        private String content;
        private String time;
        private int likeCount;
        private boolean isLiked;
        private String activityId;

        public Comment(String author, String content, String time, int likeCount, boolean isLiked, String activityId) {
            this.author = author;
            this.content = content;
            this.time = time;
            this.likeCount = likeCount;
            this.isLiked = isLiked;
            this.activityId = activityId;
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

        public boolean isLiked() {
            return isLiked;
        }

        public void setLiked(boolean liked) {
            isLiked = liked;
        }

        public String getActivityId() {
            return activityId;
        }

        public void incrementLikeCount() {
            likeCount++;
        }

        public void decreaseLikeCount() { likeCount--; }
    }
}
