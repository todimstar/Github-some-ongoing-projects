package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // 声明控件
    private EditText nameEditText;
    private EditText keyEditText;
    private RadioGroup radioGroup;
    private RadioButton radioButton3; // 男
    private RadioButton radioButton8; // 女
    private Switch hideSex;
    //兴趣爱好们
    private CheckBox peCheckBox;
    private CheckBox MusicCheckBox;
    private CheckBox workCheckBox;
    private CheckBox readCheckBox;
    private Spinner spinner;
    //提交按钮
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//入口逻辑
        super.onCreate(savedInstanceState);//调用父类的onCreate方法基础初始化
        EdgeToEdge.enable(this);    //启用边缘到边缘布局
        setContentView(R.layout.activity_main);//设置布局文件
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> { //设置窗口插入监听器
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 自己的初始化控件方法
        initViews();

        // 设置提交按钮点击事件
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();   //处理表单提交
            }
        });
    }

    // 初始化控件
    private void initViews() {
        nameEditText = findViewById(R.id.nameEditText);
        keyEditText = findViewById(R.id.keyEditText);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton8 = findViewById(R.id.radioButton8);
        hideSex = findViewById(R.id.hideSex);
        peCheckBox = findViewById(R.id.peCheckBox);
        MusicCheckBox = findViewById(R.id.MusicCheckBox);
        workCheckBox = findViewById(R.id.workCheckBox);
        readCheckBox = findViewById(R.id.readCheckBox);
        spinner = findViewById(R.id.spinner);
        submitButton = findViewById(R.id.submitButton);
    }

    // 提交表单
    private void submitForm() {
        // 获取用户名
        String username = nameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {  //如果用户名为空
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取性别
        boolean isMale = radioButton3.isChecked();
        String gender = isMale ? "先生" : "女士";

        // 判断是否隐藏性别
        boolean hideGender = hideSex.isChecked();

        // 获取选中的城市
        String city = spinner.getSelectedItem().toString();

        // 获取兴趣爱好
        StringBuilder hobbies = new StringBuilder();
        if (peCheckBox.isChecked()) {
            hobbies.append("体育；");
        }
        if (MusicCheckBox.isChecked()) {
            hobbies.append("音乐；");
        }
        if (workCheckBox.isChecked()) {
            hobbies.append("绘画；");
        }
        if (readCheckBox.isChecked()) {
            hobbies.append("阅读；");
        }

        // 如果没有选择兴趣爱好
        if (hobbies.length() == 0) {
            hobbies.append("无");
        } else {
            // 删除最后一个分号
            hobbies.deleteCharAt(hobbies.length() - 1);
        }

        // 用Java的StringBuilder构建显示信息
        StringBuilder message = new StringBuilder();
        message.append(username);

        // 根据是否隐藏性别决定是否显示先生/女士
        if (!hideGender) {
            message.append(gender);
        }

        message.append("您好！\n");
        message.append("您所在的城市是：").append(city).append("\n");
        message.append("您的兴趣爱好是：").append(hobbies);

        // 显示对话框
        new AlertDialog.Builder(this)
                .setTitle("注册信息")
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
    }
}