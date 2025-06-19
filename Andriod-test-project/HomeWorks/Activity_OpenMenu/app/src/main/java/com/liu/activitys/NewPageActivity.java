package com.liu.activitys;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

public class NewPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_page);

        // 设置ActionBar的返回按钮和标题
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.new_page_title);
    }

    // 处理ActionBar的返回按钮点击事件
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // 或者 finish();
        return true;
    }
}