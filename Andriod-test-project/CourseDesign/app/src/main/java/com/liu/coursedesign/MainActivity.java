package com.liu.coursedesign;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.liu.coursedesign.Dao.UserDao;
import com.liu.coursedesign.database.AppDatabase;
import com.liu.coursedesign.model.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        //     Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        //     v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        //     return insets;
        // });

        // 初始化数据库
        initDatabase();

        // 测试数据库操作
        testDatabase();
        
    }

    private void initDatabase() {
        // 初始化数据库
        db = AppDatabase.getDatabase(this);
        
        // 获取UserDao实例
        userDao = db.userDao();
    }

    private void testDatabase(){
        new Thread(() -> {
            try{
                // 先检查表是否为空，如果为空则创建管理员用户
                List<User> existingUsers = userDao.getAll();
                Log.d("Database", "现有用户数量：" + existingUsers.size());
                
                if (existingUsers.isEmpty()) {
                    // 创建管理员用户
                    User adminUser = new User("admin","admin123","ADMIN"); // 如果你的User类有role字段

                    
                    userDao.insertAll(adminUser);
                    Log.d("Database", "管理员用户已创建");

                    // 创建测试用户
                    User testUser = new User("testUser", "testPassword", "USER");

                    try {
                        userDao.insertAll(testUser);
                    }catch (Exception e){
                        Log.d("DataBase","TestUser插入有问题\n"+e);
                    }
                    Log.d("Database", "测试用户已创建");

                }



                // 3.查询所有用户
                List<User> users = userDao.getAll();

                // 4.在主线程显示结果
                runOnUiThread(() -> {   //安卓特有UI线程，Java就用ExecutorService建newCacheThreadPool或newSingleThreadExecutor
                    Log.d("Database", "用户数量：" + users.size());
                    if (!users.isEmpty()) {
                        for (User user : users) {
                            Log.d("Database", "用户：" + user.username + " 角色：" + user.role + " 密码：" + user.password + " ID：" + user.uid);
                        }
                        
                    } else {
                        Log.d("Database", "没有用户数据");
                    }
                });
            } catch (Exception e){
                Log.e("Database", "数据库操作失败", e);

            }
        }).start();
    }
}