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
//import com.liu.coursedesign.R;
//import com.liu.coursedesign.database.AppDatabase;
//import com.liu.coursedesign.Dao.UserDao;
//import com.liu.coursedesign.model.User;
//
//public class RegisterActivityold extends AppCompatActivity{// 该类名的命名规范是驼峰吗？
//
//    // UI组件声明
//    private EditText editTextUsername;  // 用户名输入框
//    private EditText editTextPassword;  // 密码输入框
//    private EditText editTextConfirmPassword;  // 确认密码输入框
//    private Button buttonRegister;  // 注册按钮
//    private Button buttonBackToLogin;  // 返回登录按钮
//
//    // OnCreate方法
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register); // 设置布局文件(命名规范页面_功能)Java关联XML
//
//        initViews(); // 初始化UI组件
//
//        setupClickListeners(); // 设置点击监听器
//    }
//
//
//    // 初始化UI组件方法
//    private void initViews(){
//        try {
//            editTextUsername = findViewById(R.id.editTextRegUsername);
//            editTextPassword = findViewById(R.id.editTextRegPassword);
//            editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
//            buttonRegister = findViewById(R.id.buttonRegister);
//            buttonBackToLogin = findViewById(R.id.buttonBackToLogin);
//            if(editTextUsername == null || editTextPassword == null ||
//               editTextConfirmPassword == null || buttonRegister == null ||
//               buttonBackToLogin == null) {
//                throw new Exception("某些组件未正确初始化");
//            }
//        }catch(Exception e){
//            Log.e("RegisterActivity","initViews未获取到组件",e);
//            //怪事，之前Username和Password的id不对没有获取到组件,但是竟然不报错
//            //原来是findViewById()找不到直接返回null也不报错，好吧
//        }
//    }
//
//
//    // 设置点击监听器方法
//    private void setupClickListeners(){
//        // 注册按钮点击事件
//        buttonRegister.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                handleRegister(); // 处理注册逻辑
//            }
//        });
//
//        // 返回登录按钮点击事件
//        buttonBackToLogin.setOnClickListener(v -> goBackToLogin());
//    }
//
//
//    // 处理注册逻辑方法
//    private void handleRegister(){
//        // getText().toString().trim() = 获取输入框内容并去除首尾空格
//        String username,password,confirmPassword;
////        String username = editTextUsername.getText().toString().trim();
////        String password = editTextPassword.getText().toString().trim();
////        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
//       try {
//           username = editTextUsername.getText().toString().trim();//有trim()在，避免用户以空格起名
//           password = editTextPassword.getText().toString().trim();
//           confirmPassword = editTextConfirmPassword.getText().toString().trim();
//       }catch(Exception e){
//           Toast.makeText(this, "获取三个注册信息时失败", Toast.LENGTH_SHORT).show();
//           Log.e("RegisterActivity","获取三个注册信息时失败",e);
//           return;
//       }
//
//        // 输入验证
//        if (!validateInput(username, password, confirmPassword)) {
//            return; // 如果验证失败，直接返回
//        }
//
//        // 注册用户进数据库
//        new Thread(() ->{
//            try{
//                AppDatabase db = AppDatabase.getDatabase(this);
//                UserDao userDao = db.userDao();
//
//                // 检查用户名是否已存在
//                int existingUserCount = userDao.checkUsernameExists(username);
//
//                if (existingUserCount > 0){
//                    // 用户名已存在，提示用户
//                    runOnUiThread(() ->{
//                        Toast.makeText(this, "用户名已存在，请选择其他用户名", Toast.LENGTH_SHORT).show();
//                    });
//                    return;
//                }
//
//                // 创建新用户对象
//                User newUser = new User(username, password); // 默认角色为普通用户
//
//                // 插入新用户到数据库
//                userDao.insertAll(newUser);
//
//                // 注册成功，提示用户并返回登录页面
//                runOnUiThread(() ->{
//                    Toast.makeText(this,"注册成功！请登录", Toast.LENGTH_SHORT).show();
//                    goBackToLogin(); // 返回登录页面
//                });
//            }catch(Exception e){
//                Log.e("RegisterActivity", "注册失败", e);
//                runOnUiThread(() -> {
//                    Toast.makeText(this, "注册失败，请稍后再试", Toast.LENGTH_SHORT).show();
//                });
//            }
//        }).start();
//    }
//
//    /**
//     * 输入验证
//     * @param username 用户名
//     * @param password 密码
//     * @param confirmPassword 确认密码
//     * @return boolean 验证是否通过
//     */
//    private boolean validateInput(String username,String password,String confirmPassword){
//        //空检查
//        if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
//            Toast.makeText(this,"请填写检查注册信息是否填写完整",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        // 检查确认密码与密码是否相等
//        if (!password.equals(confirmPassword)){
//            Toast.makeText(this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//
//    /**
//     * 返回登录页面,目前只是简单的finish()，如果测试有问题再做复杂检查
//     */
//    private void goBackToLogin() {
//        finish(); // 关闭当前Activity，返回到登录页面
//    }
//}
