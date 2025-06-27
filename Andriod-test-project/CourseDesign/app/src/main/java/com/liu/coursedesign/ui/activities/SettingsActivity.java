package com.liu.coursedesign.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.liu.coursedesign.Dao.UserDao;
import com.liu.coursedesign.R;
import com.liu.coursedesign.model.User;
import com.liu.coursedesign.util.SessionManager;
import com.liu.coursedesign.database.AppDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 设置页面Activity
 * 对应布局文件: activity_settings.xml
 */
public class SettingsActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
    private TextView tvUsername, tvUserType;
    private TextInputEditText etCurrentPassword, etNewPassword, etConfirmNewPassword;
    private Button btnChangePassword, btnLogout;
    
    private SessionManager sessionManager;
    private UserDao userDao;
    // 线程池
    private ExecutorService executorService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        initViews();
        initBusinessLogic();
        loadUserInfo();
        setupClickListeners();
        
        // 检查是否直接显示修改密码
        checkShowChangePassword();
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvUsername = findViewById(R.id.tvUsername);
        tvUserType = findViewById(R.id.tvUserType);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);
        
        // 设置Toolbar
        setSupportActionBar(toolbar);
        // 显示返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void initBusinessLogic() {
        sessionManager = new SessionManager(this);
        AppDatabase db = AppDatabase.getDatabase(this);
        userDao = db.userDao();
        // 顺便线程池了
        executorService = Executors.newFixedThreadPool(1);
    }
    
    /**
     * 加载用户信息到界面
     * Load User Information to UI
     * 
     * 原理解释：
     * - Session Info = Session (会话) + Info (信息) - 用户会话信息
     * - User Role = User (用户) + Role (角色) - 用户权限角色
     * - 作用：从SessionManager获取当前登录用户的信息并显示在界面上
     */
    private void loadUserInfo() {
        // 获取当前用户会话信息 - Get Current User Session Info
        SessionManager.UserSessionInfo sessionInfo = sessionManager.getUserSessionInfo();
        String userRole = sessionManager.getUserRole();
        
        //有sessionInfo就是已登录用户
        if (sessionInfo != null) {
            tvUsername.setText(sessionInfo.username);
            // 根据角色显示
            tvUserType.setText("ADMINISTRATOR".equals(userRole) ? "管理员" : "普通用户");
        }else {
            // 会话信息获取失败，重登
            Toast.makeText(this, "获取用户信息失败，请重新登录", Toast.LENGTH_SHORT).show();
            Logout();
        }
    }
    
    private void setupClickListeners() {
        // 修改密码按钮
        btnChangePassword.setOnClickListener(v -> changePassword());
        // 退出登录按钮
        btnLogout.setOnClickListener(v -> showLogoutDialog());
        // Toolbar返回按钮
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    /**
     * 检查是否需要直接显示修改密码区域
     * Check if Need to Show Change Password Section Directly
     * 
     * 原理解释：
     * - Intent Extra = Intent (意图) + Extra (额外) - Intent携带的额外参数
     * - getIntent() = get (获取) + Intent (意图) - 获取启动当前Activity的Intent
     * - getBooleanExtra() = get (获取) + Boolean (布尔) + Extra (额外) - 获取布尔类型的额外参数
     */
    private void checkShowChangePassword() {
        // 从跳转来的Intent获取额外参数 
        boolean showChangePassword = getIntent().getBooleanExtra("show_change_password", false);
        if (showChangePassword) {
            // 让当前密码输入框获取焦点 
            etCurrentPassword.requestFocus();
        }
    }
    
    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmNewPassword.getText().toString().trim();
        
        // 输入验证
        if (!validatePasswordInput(currentPassword, newPassword, confirmPassword)) {
            return; 
        }
        
        // 验证当前密码
        String username = sessionManager.getUsername();
        executorService.execute(() -> {
            User user = userDao.login(username, currentPassword);
            
            runOnUiThread(() -> {
                if (user == null) {
                    etCurrentPassword.setError("当前密码错误");
                    etCurrentPassword.requestFocus();
                    Toast.makeText(this, "如果忘记密码请联系管理员修改", Toast.LENGTH_LONG).show();
                    return;
                }
            });
            Log.d("SettingActivity","验证的user:"+user);
            if (user == null) {return;}// 此时就是UI线程暂停了，我们这个线程也得停下
            // 亮点:拦截对默认管理员账号的修改
            if (user.username.equals("admin")){
                runOnUiThread(() -> {
                    Toast.makeText(this,"默认管理员账号密码不可改变，如需测试请登录2号默认管理员账号修改",Toast.LENGTH_LONG).show();
                });
                return;
            }
            // 更新密码
            int result = userDao.updatePassword(user.getId(), newPassword);

            runOnUiThread(() -> {
                if (result != 0) {
                    Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();

                    // 清空输入框
                    etCurrentPassword.setText("");
                    etNewPassword.setText("");
                    etConfirmNewPassword.setText("");
                } else {
                    Toast.makeText(this, "密码修改失败", Toast.LENGTH_SHORT).show();
                    Log.d("SettingsActivity", "密码修改错误 result=" + result);
                }
            });

        });
    }

    private boolean validatePasswordInput(String currentPassword, String newPassword, String confirmPassword) {
        // 空检查
        if (currentPassword.isEmpty()) {
            etCurrentPassword.setError("请输入当前密码");
            etCurrentPassword.requestFocus();
            return false;
        }
        if (newPassword.isEmpty()) {
            etNewPassword.setError("请输入新密码");
            etNewPassword.requestFocus();
            return false;
        }
        
        // 密码长度验证 - Password Length Validation
        if (newPassword.length() < 6) {
            etNewPassword.setError("密码长度至少6位");
            etNewPassword.requestFocus();
            return false;
        }
        
        // 检查新密码是否与当前密码相同 - Check if New Password Same as Current
        if (newPassword.equals(currentPassword)) {
            etNewPassword.setError("新密码不能与当前密码相同");
            etNewPassword.requestFocus();
            return false;
        }
        
        // 确认密码验证 - Confirm Password Validation
        if (!newPassword.equals(confirmPassword)) {
            etConfirmNewPassword.setError("两次密码输入不一致");
            etConfirmNewPassword.requestFocus();
            return false;
        }
            
        return true; // 所有验证通过 
    }
    
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("确认退出")
            .setMessage("确定要退出登录吗？")
            .setPositiveButton("确定", (dialog, which) -> Logout())
            .setNegativeButton("取消", null)
            .show();
    }

    private void Logout(){
        sessionManager.clearSession();
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
