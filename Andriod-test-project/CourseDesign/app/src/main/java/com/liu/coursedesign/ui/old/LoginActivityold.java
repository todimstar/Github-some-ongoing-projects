//package com.liu.coursedesign.ui.old;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.liu.coursedesign.Dao.UserDao;
//import com.liu.coursedesign.R;
//import com.liu.coursedesign.database.AppDatabase;
//import com.liu.coursedesign.model.User;
//import com.liu.coursedesign.ui.activities.MainActivity;
//import com.liu.coursedesign.ui.activities.RegisterActivity;
//import com.liu.coursedesign.util.SessionManager;
//
//public class LoginActivityold extends AppCompatActivity {
//
//    // UI 组件声明
//    private EditText editTextUsername;  // 用户名输入框
//    private EditText editTextPassword;  // 密码输入框
//    private Button buttonLogin;         // 登录按钮
//    private Button buttonRegister;      // 注册按钮
//
//    // 会话管理器，记录登录状态和用户信息
//    private SessionManager sessionManager;
//
//    //计数器
//    private static int warningCount = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login); // 设置布局文件
//
//        // 初始化会话管理器
//        sessionManager = new SessionManager(this);
//        // 检查是否已登录，如果已登录则跳转到主界面
//        if (sessionManager.isLoggedIn()){
//            goToMainActivityWithSession(); // 使用会话信息跳转到主界面
//            return;
//        }
//
//        // 初始化 UI 组件
//        initViews();
//
//        // 设置点击事件
//        setupClickListeners();
//    }
//
//    /**
//     * 初始化视图组件
//     * findViewById() = 通过ID找到视图组件
//     */
//    private void initViews() {
//        editTextUsername = findViewById(R.id.editTextUsername);
//        editTextPassword = findViewById(R.id.editTextPassword);
//        buttonLogin = findViewById(R.id.buttonLogin);
//        buttonRegister = findViewById(R.id.buttonRegister);
//    }
//
//    /**
//     * 设置点击监听器
//     * OnClickListener = 点击监听器（当用户点击时执行的代码）
//     */
//    private void setupClickListeners() {
//        // 登录按钮点击事件
//        buttonLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleLogin(); // 处理登录逻辑
//            }
//        });
//
//
//        buttonRegister.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                goToRegister(); // 跳转到注册页面
//            }
//        });
//    }
//
//    /**
//     * 处理登录逻辑（暂时简化）
//     */
//    private void handleLogin() {
//        String username = editTextUsername.getText().toString().trim();
//        String password = editTextPassword.getText().toString().trim();
//
//
//        // 输入验证
//        if (username.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        new Thread(() -> {
//            try{
//                // 通过获取数据库实例拿到userDao
//                AppDatabase db = AppDatabase.getDatabase(this);
//                UserDao userDao = db.userDao();
//
//                // 用userDao检查用户名和密码是否匹配，现在是明文密码匹配和内存判定三次warningCount版
//                // TODO: 需要加密密码再存储和验证
//                int usernameExists = userDao.checkUsernameExists(username);
//                User againuser = userDao.login(username, password);
//                Log.d("LoginActivity","UsernamExists:"+usernameExists+"\nagaginuser=:"+againuser);
////                boolean passwardTure = false;
////                if (user!=null && againuser!=null) passwardTure = (user.equals(againuser));//6.23这个equals引出判断id==0的瞬态对象, 11:59要特判没有找到的情况
//                // 其实，有没有可能，当againuser不为NULL即代表username存在且密码正确，所以肯定能进入正常登录，所以不用重写equals都行
//                // 虽然目前来说是等价的
//                if (usernameExists >0 && againuser!=null){
//                    // 存储用户会话信息
//                    sessionManager.saveUserSession(againuser.id, againuser.username, againuser.role);
//
//                    // 存在用户且密码匹配
//                    runOnUiThread(()->{
//                        Toast.makeText(this, "登陆成功!", Toast.LENGTH_SHORT).show();
//                        goToMainActivityWithSession(); // 跳转到主界面
//                    });
//                    warningCount = 0; // 重置警告计数
//
//                }else if(usernameExists == 0){
//                    runOnUiThread(() -> {
//                        Toast.makeText(this, "用户名不存在，请注册后使用", Toast.LENGTH_SHORT).show();
//                    });
//                }
//                else if (warningCount < 3){
//                    // 账号或密码不匹配
//                    warningCount++;
//                    runOnUiThread(() -> {
//                        Toast.makeText(this, "用户名或密码错误(已尝试"+warningCount+"次", Toast.LENGTH_SHORT).show();
//                    });
//                }else{
//                    //TODO:需要将warningCount放入数据库存储，还要加上倒计时功能
//                    runOnUiThread(() -> {
//                        Toast.makeText(this, "输出错误超过3次，账号已锁定，请30s后再试！", Toast.LENGTH_SHORT).show();
//                    });
//                }
//
//            }catch (Exception e){
//                Log.e("LoginActivity", "登录失败", e);
//                runOnUiThread(() -> {
//                    Toast.makeText(this, "登录失败，请稍后再试", Toast.LENGTH_SHORT).show();
//                });
//            }
//        }).start();
//
//    }
//
//    /**
//     * 跳转到主界面
//     */
//    private void goToMainActivityWithSession() {
//        try{
//            Intent intent = new Intent(this, MainActivity.class);// 创建意图对象，指定目标Activity
//            // 用不着了因为主页面可以直接通过SessionManager拿到用户信息
//            // //加入用户信息到意图中作为extra额外数据,用SeessionManager中存的用户信息
//            // SessionManager.UserSessionInfo sessionInfo = sessionManager.getUserSessionInfo();
//            // if (sessionInfo == null) {
//            //     intent.putExtra("username", sessionInfo.username);
//            //     intent.putExtra("role", sessionInfo.role); // 后续判断展示页面
//            //     intent.putExtra("id", sessionInfo.id); // 用户ID，可能有用
//            // }
//            startActivity(intent);  //通过意图启动目标Activity
//            finish(); // 关闭登录页面
//        }catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "跳转失败，请稍后再试", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    /**
//     * 跳转到注册界面
//     */
//    private void goToRegister() {
//        // Toast.makeText(this, "注册功能即将实现", Toast.LENGTH_SHORT).show();
//        // TODO: 后面创建注册Activity后取消注释
//        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent);
//    }
//}