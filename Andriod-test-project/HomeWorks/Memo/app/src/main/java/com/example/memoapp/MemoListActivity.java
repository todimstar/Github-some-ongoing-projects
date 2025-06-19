package com.example.memoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MemoListActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_MEMO = 1;
    private static final int REQUEST_CODE_EDIT_MEMO = 2;

    private ListView listViewMemos;
    private Button buttonAddMemo;
    private TextView textViewWelcome;
    private DatabaseHelper dbHelper;
    private List<Memo> memoList;
    private ArrayAdapter<Memo> memoAdapter;
    private int currentUserId;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);

        dbHelper = new DatabaseHelper(this);

        listViewMemos = findViewById(R.id.listViewMemos);
        buttonAddMemo = findViewById(R.id.buttonAddMemo);
        textViewWelcome = findViewById(R.id.textViewWelcome);

        // 获取从LoginActivity传递过来的用户ID和用户名
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_ID")) {
            currentUserId = intent.getIntExtra("USER_ID", -1);
            if (intent.hasExtra("USER_NAME")) {
                currentUsername = intent.getStringExtra("USER_NAME");
                textViewWelcome.setText("欢迎您，" + currentUsername + "！这是您的备忘录：");
            } else {
                textViewWelcome.setText("我的备忘录");
            }
        } else {
            // 如果没有用户信息，理论上不应该进入此Activity，可以做错误处理或返回登录
            Toast.makeText(this, "用户未登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        memoList = new ArrayList<>();
        loadMemos();

        memoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, memoList);
        listViewMemos.setAdapter(memoAdapter);

        buttonAddMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(MemoListActivity.this, AddEditMemoActivity.class);
                addIntent.putExtra("USER_ID", currentUserId);
                startActivityForResult(addIntent, REQUEST_CODE_ADD_MEMO);
            }
        });

        listViewMemos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo selectedMemo = memoList.get(position);
                Intent editIntent = new Intent(MemoListActivity.this, AddEditMemoActivity.class);
                editIntent.putExtra("MEMO_ID", selectedMemo.getId());
                editIntent.putExtra("USER_ID", currentUserId); // 也传递USER_ID，虽然编辑时主要靠MEMO_ID
                editIntent.putExtra("MEMO_TITLE", selectedMemo.getTitle());
                editIntent.putExtra("MEMO_CONTENT", selectedMemo.getContent());
                startActivityForResult(editIntent, REQUEST_CODE_EDIT_MEMO);
            }
        });

        listViewMemos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Memo selectedMemo = memoList.get(position);
                new AlertDialog.Builder(MemoListActivity.this)
                        .setTitle("删除备忘录")
                        .setMessage("您确定要删除这条备忘录吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteMemoFromDb(selectedMemo.getId());
                                loadMemos(); // 重新加载数据
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true; // 返回true表示消费了长按事件
            }
        });
    }

    private void loadMemos() {
        memoList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_MEMOS,
                new String[]{DatabaseHelper.COLUMN_MEMO_ID, DatabaseHelper.COLUMN_MEMO_TITLE, DatabaseHelper.COLUMN_MEMO_CONTENT, DatabaseHelper.COLUMN_MEMO_CREATE_TIME, DatabaseHelper.COLUMN_MEMO_UPDATE_TIME},
                DatabaseHelper.COLUMN_MEMO_USER_ID + "=?",
                new String[]{String.valueOf(currentUserId)},
                null, null, DatabaseHelper.COLUMN_MEMO_UPDATE_TIME + " DESC"); // 按更新时间降序排列

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEMO_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEMO_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEMO_CONTENT));
                String createTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEMO_CREATE_TIME));
                String updateTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEMO_UPDATE_TIME));
                memoList.add(new Memo(id, currentUserId, title, content, createTime, updateTime));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // db.close(); // 通常在Activity的onDestroy中关闭dbHelper
        if (memoAdapter != null) {
            memoAdapter.notifyDataSetChanged();
        }
    }

    private void deleteMemoFromDb(int memoId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = db.delete(DatabaseHelper.TABLE_MEMOS, DatabaseHelper.COLUMN_MEMO_ID + "=?", new String[]{String.valueOf(memoId)});
        // db.close();
        if (deletedRows > 0) {
            Toast.makeText(this, "备忘录已删除", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_ADD_MEMO || requestCode == REQUEST_CODE_EDIT_MEMO) && resultCode == RESULT_OK) {
            loadMemos(); // 当添加或编辑成功后，重新加载备忘录列表
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close(); // 关闭数据库连接
        super.onDestroy();
    }
}