package com.liu.coursedesign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.liu.coursedesign.Dao.KnowledgeDao;
import com.liu.coursedesign.Dao.UserDao;
import com.liu.coursedesign.database.AppDatabase;
import com.liu.coursedesign.model.Knowledge;
import com.liu.coursedesign.model.User;
import com.liu.coursedesign.ui.activities.LoginActivity;
import com.liu.coursedesign.util.SessionManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private UserDao userDao;
    private KnowledgeDao knowledgeDao;
    
    private SessionManager sessionManager; // 会话管理器

    // Ui组件
    private TextView textViewUserInfo;
    private Button buttonLogout;
    private Button buttonBrowseKnowledge;
    private Button buttonChangePassword;

    //管理员Ui组件
    private LinearLayout layoutAdminFeatures;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);//设置页面文件
        
        if(!initSessionManager())return; // 根据结果终止初始化主页面

        initViews();
        displayUserInfo(); 
        setupClickListeners();

        // 初始化users数据库
        initDatabase();
        testDatabase();
        
        
    }

    private void initViews(){
        textViewUserInfo = findViewById(R.id.textViewUserInfo);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonBrowseKnowledge = findViewById(R.id.buttonBrowseKnowledge);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);
    }
    
    /**
     * 初始化会话管理器，如果未登录则跳转到登录界面
     * @return true 如果会话管理器初始化成功且用户已登录，返回true
     * @return false 如果用户未登录，返回false并跳转到登录界面
     */
    private boolean initSessionManager(){
        sessionManager = new SessionManager(this); // 初始化会话管理器

        if(!sessionManager.isLoggedIn()){
            goToLoginActivity(); // 如果未登录，跳转到登录界面
            return false;
        }return true;
    }

    /**
     * 显示用户信息
     */
    private void displayUserInfo(){
        SessionManager.UserSessionInfo sessionInfo = sessionManager.getUserSessionInfo();
        if(sessionInfo != null) {
            textViewUserInfo.setText(sessionInfo.username + " (" + sessionInfo.role + ")");
        }
    }

    /**
     * 设置点击监听器
     */
    private void setupClickListeners(){
        //登出按钮
        buttonLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                logout();
            }
        });
        //其他再说吧
    }

    /**
     * 跳转到登录界面
     */
    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        // 设置Intent标志，清除任务栈中除此页面的其他页面，之后将无法返回此页面之前的页面
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // 结束当前活动，防止返回到此页面
        // 两次清除成功实现进入登录页面无法返回到主页面
    }

    /**
     * 用户退出登录
     */
    public void logout() {
        // 清除会话信息
        sessionManager.clearSession();

        goToLoginActivity();
    }

    private void initDatabase() {
        // 初始化数据库
        db = AppDatabase.getDatabase(this);
        
        // 获取UserDao实例
        userDao = db.userDao();
        // 获取KnowledgeDao实例
        knowledgeDao = db.knowledgeDao();
    }

    private void testDatabase(){
        new Thread(() -> {
            try{
                // 先检查表是否为空，如果为空则创建管理员用户
                List<User> existingUsers = userDao.getAll();
                Log.d("MainActivityDatabase", "现有用户数量：" + existingUsers.size());
                boolean isAdminExists = false;
                for(User user:existingUsers){
                    if (user.role.equals("ADMINISTRATOR")) {
                        isAdminExists = true;
                        // 如果管理员用户已存在，则不再创建
                    }
                }

                if (!isAdminExists) {
                    // 创建管理员用户
                    User adminUser = new User("admin","admin123","ADMINISTRATOR");

                    userDao.insertAll(adminUser);
                    Log.d("MainActivityDatabase", "无管理员用户，创建默认管理员用户");

                }



                // 3.查询所有用户
                List<User> users = userDao.getAll();

                // 4.在主线程显示结果

                Log.d("MainActivityDatabase", "用户数量：" + users.size());
                if (!users.isEmpty()) {
                    for (User user : users) {
                        Log.d("MainActivityDatabase", String.valueOf(user));
                    }

                } else {
                    Log.d("MainActivityDatabase", "没有用户数据");
                }

            } catch (Exception e){
                Log.e("MainActivityDatabase", "用户数据库操作失败", e);

            }
            try{
                knowledgeDao = db.knowledgeDao();
            
                // 检查Knowledge表是否为空
                List<Knowledge> existingKnowledge = knowledgeDao.getAllKnowledge();
                Log.d("MainActivityDatabase", "现有知识条目数量：" + existingKnowledge.size());
                
                if (existingKnowledge.isEmpty()) {
                    // 创建测试知识条目
                    Knowledge testKnowledge = new Knowledge(
                        "Java基础-数据类型",
                        "语言基础类型介绍：int、long、float、double、char、boolean、String",
                        "/path/to/image.jpg",
                        "Java",
                        "2024-01-01",
                        "2024-01-01"
                    );
                    
                    knowledgeDao.add(testKnowledge);
                    Log.d("MainActivityDatabase", "测试知识条目已创建");
                }
                
                // 查询所有知识条目
                List<Knowledge> allKnowledge = knowledgeDao.getAllKnowledge();
                runOnUiThread(() -> {//安卓特有UI线程，Java就用ExecutorService建newCacheThreadPool或newSingleThreadExecutor
                    Log.d("MainActivityDatabase", "知识条目数量：" + allKnowledge.size());
                    for (Knowledge k : allKnowledge) {
                        Log.d("MainActivityDatabase", String.valueOf(k));
                    }
                });
            }catch (Exception e) {
                Log.e("MainActivityDatabase", "知识条目操作失败", e);
            }
        }).start();
    }
}