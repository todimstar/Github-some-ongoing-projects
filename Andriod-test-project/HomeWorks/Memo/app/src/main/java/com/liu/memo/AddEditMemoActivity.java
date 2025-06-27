package com.liu.memo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditMemoActivity extends AppCompatActivity {

    private EditText editTextMemoTitle;
    private EditText editTextMemoContent;
    private Button buttonSaveMemo;
    private TextView textViewAddEditTitle;

    private DatabaseHelper dbHelper;
    private int currentUserId;
    private int memoId = -1; // -1表示添加新备忘录，否则为编辑

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_memo);

        dbHelper = new DatabaseHelper(this);

        editTextMemoTitle = findViewById(R.id.editTextMemoTitle);
        editTextMemoContent = findViewById(R.id.editTextMemoContent);
        buttonSaveMemo = findViewById(R.id.buttonSaveMemo);
        textViewAddEditTitle = findViewById(R.id.textViewAddEditTitle);

        Intent intent = getIntent();
        if (intent != null) {
            currentUserId = intent.getIntExtra("USER_ID", -1);
            if (currentUserId == -1) {
                Toast.makeText(this, "错误：用户信息丢失", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            if (intent.hasExtra("MEMO_ID")) {
                memoId = intent.getIntExtra("MEMO_ID", -1);
                String title = intent.getStringExtra("MEMO_TITLE");
                String content = intent.getStringExtra("MEMO_CONTENT");

                editTextMemoTitle.setText(title);
                editTextMemoContent.setText(content);
                textViewAddEditTitle.setText("编辑备忘录");
                buttonSaveMemo.setText("更新");
            } else {
                textViewAddEditTitle.setText("添加新备忘录");
                buttonSaveMemo.setText("保存");
            }
        }

        buttonSaveMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemo();
            }
        });
    }

    private void saveMemo() {
        String title = editTextMemoTitle.getText().toString().trim();
        String content = editTextMemoContent.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "请输入备忘录标题", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MEMO_USER_ID, currentUserId);
        values.put(DatabaseHelper.COLUMN_MEMO_TITLE, title);
        values.put(DatabaseHelper.COLUMN_MEMO_CONTENT, content);

        String currentTime = getCurrentTimestamp();

        if (memoId == -1) { // 添加新备忘录
            values.put(DatabaseHelper.COLUMN_MEMO_CREATE_TIME, currentTime);
            values.put(DatabaseHelper.COLUMN_MEMO_UPDATE_TIME, currentTime);
            long newRowId = db.insert(DatabaseHelper.TABLE_MEMOS, null, values);
            if (newRowId != -1) {
                Toast.makeText(this, "备忘录已保存", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        } else { // 更新现有备忘录
            values.put(DatabaseHelper.COLUMN_MEMO_UPDATE_TIME, currentTime);
            int rowsAffected = db.update(DatabaseHelper.TABLE_MEMOS, values, DatabaseHelper.COLUMN_MEMO_ID + "=?", new String[]{String.valueOf(memoId)});
            if (rowsAffected > 0) {
                Toast.makeText(this, "备忘录已更新", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
            }
        }
        db.close();
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}