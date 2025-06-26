package com.liu.coursedesign.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.liu.coursedesign.Dao.UserDao;
import com.liu.coursedesign.R;
import com.liu.coursedesign.model.User;
import com.liu.coursedesign.database.AppDatabase;

/**
 * 注册页面Activity
 * 对应布局文件: activity_register.xml
 */
public class RegisterActivity extends AppCompatActivity {
    
    // UI组件声明
    private TextInputEditText etUsername, etPassword, etConfirmPassword;//,etEmail;不搞了累
    private Button btnRegister; //注册按钮
    private TextView tvLogin;   // 返回登录按钮
    private UserDao userDao;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        initViews();
        initBusinessLogic();
        setupClickListeners();
    }
    
    // 初始化UI组件方法
    private void initViews() {
        try{
            etUsername = findViewById(R.id.etUsername);
            // etEmail = findViewById(R.id.etEmail);
            etPassword = findViewById(R.id.etPassword);
            etConfirmPassword = findViewById(R.id.etConfirmPassword);
            btnRegister = findViewById(R.id.btnRegister);
            tvLogin = findViewById(R.id.tvLogin);
            if(etUsername == null || //etEmail == null || 
            etPassword == null || btnRegister == null || 
            tvLogin == null) {
             throw new Exception("某些组件未正确初始化");
         }
     }catch(Exception e){
         Log.e("RegisterActivity","initViews未获取到组件",e);
         //怪事，之前Username和Password的id不对没有获取到组件,但是竟然不报错
         //原来是findViewById()找不到直接返回null也不报错，好吧
     }
    }
    
    private void initBusinessLogic() {
        // 业务逻辑之数据库
        AppDatabase db = AppDatabase.getDatabase(this);
        userDao = db.userDao();
    }
    
    private void setupClickListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();  //注册操作
            }
        });
        
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToLogin();    //简单finish
            }
        });
    }
    
    // 处理注册逻辑方法
    private void handleRegister() {
        String username,password,confirmPassword;//,email;

        try {
           username = etUsername.getText().toString().trim();//有trim()在，避免用户以空格起名
           password = etPassword.getText().toString().trim();
           confirmPassword = etConfirmPassword.getText().toString().trim();
       }catch(Exception e){
           Toast.makeText(this, "获取注册信息时失败", Toast.LENGTH_SHORT).show();
           Log.e("RegisterActivity","获取四个注册信息时失败",e);
           return;
       }
        
        // 输入验证
        if (!validateInput(username, password, confirmPassword)) {
            return; // 验证失败直接返回
        }
        
        // 在后台线程中处理注册逻辑 
        new Thread(() -> {
            try {
                // 检查用户名是否已存在,因为Login已经注册过管理员账号，所以不会发生默认管理员账号被重名情况
                int existingUserCount = userDao.checkUsernameExists(username);
                if (existingUserCount > 0) {
                    // 用户名已存在，回到主线程显示提示 
                    runOnUiThread(() -> {
                        etUsername.setError("用户名已存在，请选择其他用户名");
                        Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                
                // 创建新用户对象
                User newUser = new User(username,password); // 默认是普通用户
                                
                // 插入新用户到数据库
                long result = userDao.insertAll(newUser);
                
                // 回到主线程更新UI
                runOnUiThread(() -> {
                    if (result > 0) {
                        // 注册成功
                        Toast.makeText(this, "注册成功！请登录", Toast.LENGTH_SHORT).show();
                        goBackToLogin();
                    } else {
                        // 注册失败
                        Toast.makeText(this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                });
                
            } catch (Exception e) {
                // 异常处理
                Log.e("RegisterActivity", "注册过程中发生错误", e);
                runOnUiThread(() -> {
                    Toast.makeText(this, "注册失败，请稍后再试", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    /**
     * 输入验证规则检验
     * @param username 用户名
     * @param password 密码
     * @param confirmPassword 确认密码
     * @return boolean 验证是否通过
     */
    private boolean validateInput(String username,String password,String confirmPassword){
        //空检查
        if (username.isEmpty()) {
            etUsername.setError("请输入用户名");
            etUsername.requestFocus(); // 让输入框获得焦点
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("请输入密码");
            etPassword.requestFocus();
            return false;
        }
        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("请再次输入密码");
            etConfirmPassword.requestFocus();
            return false;
        }
        //长度检查
        if (username.length() < 3) {
            etUsername.setError("用户名至少3个字符");
            etUsername.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            etPassword.setError("密码至少6个字符");
            etPassword.requestFocus();
            return false;
        }

        // 检查确认密码与密码是否相等
        if (!password.equals(confirmPassword)){
            etConfirmPassword.setError("两次密码输入不一致");
            etConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * 返回登录页面,目前只是简单的finish()，如果测试有问题再做复杂检查
     * 所以没有单独的注册页面进入需求，都必须从登录页面跳转注册页面
     */
    private void goBackToLogin() {
        finish(); // 关闭当前Activity，返回到登录页面
    }

}
