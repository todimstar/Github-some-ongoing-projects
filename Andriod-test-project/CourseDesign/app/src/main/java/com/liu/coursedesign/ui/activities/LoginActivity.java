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

// 导入必要的类 (Import Necessary Classes)
import com.liu.coursedesign.Dao.UserRoles;
import com.liu.coursedesign.util.SessionManager;
import com.liu.coursedesign.database.AppDatabase;
import com.liu.coursedesign.Dao.UserDao;
import com.liu.coursedesign.model.User;
import com.liu.coursedesign.R;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 登录页面Activity
 * 对应布局文件: activity_login.xml
 */
public class LoginActivity extends AppCompatActivity {
    
    // 声明UI组件变量
    private TextInputEditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private ExecutorService executorService;
    private UserDao userDao;
    private SessionManager sessionManager;

    // 登录失败计数器 (Login Failure Counter)
    private static int warningCount = 0;
    // 是否有管理员账号
    private static boolean isExistAdmin = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // 绑定XML布局文件
        
        // 初始化组件
        initViews();
        initBusinessLogic();
        setupClickListeners();

        //检查管理员账号是否存在
        checkAdminExist();

        // 检查是否已经登录
        checkLoginStatus();
    }
    
    /**
     * 初始化UI组件
     * 通过findViewById找到XML中定义的组件
     */
    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
    }
    
    /**
     * 初始化业务逻辑组件
     */
    private void initBusinessLogic() {
        sessionManager = new SessionManager(this);

        // 业务逻辑组件
        AppDatabase db = AppDatabase.getDatabase(this);
        userDao = db.userDao();

        executorService = Executors.newFixedThreadPool(1);
    }
    
    /**
     * 设置点击事件监听器
     */
    private void setupClickListeners() {
        // 登录按钮点击事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
        
        // 注册链接点击事件
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
    }

    /**
     * 检查登录状态
     */
    private void checkLoginStatus() {
        if (sessionManager.isLoggedIn()) {
            // 已经登录，直接跳转到主页面 (Already logged in, navigate to main page)
            goToMainActivity();
        }
    }

    /**
     * 检查管理员账号是否存在，没有则创建默认管理员账号
     */
    private void checkAdminExist(){
        executorService.execute(() -> {
            if (!isExistAdmin){
                List<User> users = userDao.getAll();
                for (User u : users) {
                    if (UserRoles.ADMINISTRATOR.equals(u.role)) {
                        isExistAdmin = true;
                        return;
                    }
                }
            }
            if (!isExistAdmin) {
                long ok = userDao.insertAll(new User("admin", "admin123",UserRoles.ADMINISTRATOR));
                if (ok > 0) {
                    Log.d("LoginActivity", "未检测到admin账号，已创建默认管理员账号\n账号admin,密码admin123");
                } else {
                    Log.e("LoginActivity", "未检测到admin账号，但无法正常创建默认管理员账户");
                }
                long ok2 = userDao.insertAll(new User("administrator","admin123",UserRoles.ADMINISTRATOR));
                if (ok2 > 0) {
                    Log.d("LoginActivity", "未检测到admin账号，已创建2号默认管理员账号\n账号amdinistrator,密码admin123");
                } else {
                    Log.e("LoginActivity", "未检测到admin账号，但无法正常创建2号默认管理员账户");
                }
            }
        });
    }

    /**
     * 执行登录操作
     */
    private void performLogin() {
        // 获取用户输入
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // 输入验证
        if (!validateLoginInput(username, password)) {
            return; // 验证失败直接返回 (Return if validation fails)
        }
        
        executorService.execute(() -> {
            try {
                // 检查用户名是否存在 (Check if username exists)
                int usernameExists = userDao.checkUsernameExists(username);
                    
                // 尝试登录验证 (Attempt login verification)
                User user = userDao.login(username, password);
                
                // 记录调试信息 (Log debug information)
                Log.d("LoginActivity", "Username exists: " + usernameExists +
                                    ", User found: " + (user != null));
                
                // 回到主线程更新UI (Return to main thread to update UI)
                runOnUiThread(() -> {
                    handleLoginResult(usernameExists, user, username);
                });
            } catch (Exception e) {
                Log.e("LoginActivity", "登录过程中发生错误", e);
                runOnUiThread(() -> {
                    Toast.makeText(this, "登录失败，请稍后再试", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * 验证登录输入
     * Validate Login Input
     * 
     * @param username 用户名 (Username)
     * @param password 密码 (Password)
     * @return 验证是否通过 (Whether validation passes)
     */
    private boolean validateLoginInput(String username, String password) {
        // 检查用户名 (Check Username)
        if (username.isEmpty()) {
            etUsername.setError("请输入用户名");
            etUsername.requestFocus(); // Request Focus = 请求焦点，光标定位到此输入框
            return false;
        }
        
        // 检查密码 (Check Password)
        if (password.isEmpty()) {
            etPassword.setError("请输入密码");
            etPassword.requestFocus();
            return false;
        }
        
        return true;
    }

    private void handleLoginResult(int usernameExists, User user, String username) {
        if (usernameExists > 0 && user != null) {
            // 登录成功 (Login Success)
            warningCount = 0; // 重置警告计数 (Reset Warning Counter)
            
            // 保存用户会话信息 (Save User Session Information)
            sessionManager.saveUserSession(user.getId(), user.getUsername(), user.getRole());
            
            Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
            goToMainActivity(); // 跳转到主界面 (Navigate to Main Activity)
            
        } else if (usernameExists == 0) {
            // 用户名不存在 (Username does not exist)
            Toast.makeText(this, "用户名不存在，请注册后使用", Toast.LENGTH_SHORT).show();
            
        } else if (warningCount < 3) {
            // 密码错误，但未达到锁定次数 (Password incorrect, but not reached lock limit)
            warningCount++;
            Toast.makeText(this, "用户名或密码错误 (已尝试 " + warningCount + " 次)", 
                         Toast.LENGTH_SHORT).show();
            
        } else {
            // 超过3次错误，账户锁定 (Over 3 errors, account locked)
            // TODO: 需要将warningCount存入数据库，并添加倒计时功能
            // TODO: Need to store warningCount in database and add countdown function
            Toast.makeText(this, "输入错误超过3次，账号已锁定，请30秒后再试！", 
                         Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 跳转到主界面
     */
    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        // 设置Intent标志，清除任务栈 (Set Intent flags to clear task stack)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // 结束当前Activity (Finish current Activity)
    }

    /**
     * 跳转到注册界面
     */
    private void goToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    
}
