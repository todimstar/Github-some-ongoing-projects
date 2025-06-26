package com.liu.coursedesign.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
// 添加缺失的导入
import com.liu.coursedesign.util.SessionManager;
import com.liu.coursedesign.database.AppDatabase;
import com.liu.coursedesign.Dao.KnowledgeDao;
import com.liu.coursedesign.model.Knowledge;
import com.liu.coursedesign.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 主页面Activity - 根据用户角色显示不同界面
 * 对应布局文件: activity_main.xml
 */
public class MainActivity extends AppCompatActivity {
    
    // UI组件
    private Toolbar toolbar;
    private TextView tvUsername, tvUserRole, tvRecommendationTitle;
    private LinearLayout layoutUserFunctions, layoutAdminFunctions;
    private MaterialButton btnLogout;
    
    // 普通用户功能卡片
    private MaterialCardView cardTodayRecommendation, cardBrowseKnowledge, 
                            cardLearningProgress, cardPersonalSettings, cardChangePassword;
    
    // 管理员功能卡片
    private MaterialCardView cardAddContent, cardEditContent, cardDeleteContent,
                            cardUserManagement, cardDataStatistics, cardSystemSettings;
    
    // 业务逻辑
    private SessionManager sessionManager;
    private KnowledgeDao knowledgeDao;
    private ExecutorService executorService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 加入你的会话检查逻辑
        if(!initSessionManager()) return; // 如果未登录，跳转到登录页面
        
        initViews();
        initBusinessLogic();
        setupUserInterface();
        setupClickListeners();
        loadTodayRecommendation();

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
    
    private void initViews() {
        // 基础组件
        toolbar = findViewById(R.id.toolbar);
        tvUsername = findViewById(R.id.tvUsername);
        tvUserRole = findViewById(R.id.tvUserRole);
        tvRecommendationTitle = findViewById(R.id.tvRecommendationTitle);
        layoutUserFunctions = findViewById(R.id.layoutUserFunctions);
        layoutAdminFunctions = findViewById(R.id.layoutAdminFunctions);
        btnLogout = findViewById(R.id.btnLogout);
        
        // 普通用户功能卡片
        cardTodayRecommendation = findViewById(R.id.cardTodayRecommendation);
        cardBrowseKnowledge = findViewById(R.id.cardBrowseKnowledge);
        cardLearningProgress = findViewById(R.id.cardLearningProgress);
        cardPersonalSettings = findViewById(R.id.cardPersonalSettings);
        cardChangePassword = findViewById(R.id.cardChangePassword);
        
        // 管理员功能卡片
        cardAddContent = findViewById(R.id.cardAddContent);
        cardEditContent = findViewById(R.id.cardEditContent);
        cardDeleteContent = findViewById(R.id.cardDeleteContent);
        cardUserManagement = findViewById(R.id.cardUserManagement);
        cardDataStatistics = findViewById(R.id.cardDataStatistics);
        cardSystemSettings = findViewById(R.id.cardSystemSettings);
        
        // 设置Toolbar
        setSupportActionBar(toolbar);
    }
    
    /**
     * 初始化业务逻辑层
     */
    private void initBusinessLogic() {
        //目前应该就数据库算业务逻辑
        // 初始化数据库
        AppDatabase db = AppDatabase.getDatabase(this);

        // 获取KnowledgeDao实例
        knowledgeDao = db.knowledgeDao();

        executorService = Executors.newSingleThreadExecutor();
    }


    
    /**
     * 根据用户角色设置界面
     */
    private void setupUserInterface() {
        // 获取用户信息
        SessionManager.UserSessionInfo sessionInfo = sessionManager.getUserSessionInfo();
        String userRole = sessionManager.getUserRole();
        
        if (sessionInfo != null) {
            // 设置用户信息显示  
            tvUsername.setText(sessionInfo.username);
            
            // 根据角色显示不同界面
            if ("ADMINISTRATOR".equals(userRole)) {
                // 显示管理员界面
                layoutUserFunctions.setVisibility(View.GONE);
                layoutAdminFunctions.setVisibility(View.VISIBLE);
                tvUserRole.setText("管理员");
                
                // 可选：更改主题色调
                //toolbar.setBackgroundResource(R.drawable.admin_toolbar_gradient);
            } else {
                // 显示普通用户界面
                layoutUserFunctions.setVisibility(View.VISIBLE);
                layoutAdminFunctions.setVisibility(View.GONE);
                tvUserRole.setText("普通用户");
            }
        }
    }
    
    /**
     * 设置所有点击事件
     */
    private void setupClickListeners() {
        // 普通用户功能点击事件
        cardTodayRecommendation.setOnClickListener(v -> {
            // 跳转到知识浏览页面，显示推荐内容
            Intent intent = new Intent(MainActivity.this, KnowledgeBrowserActivity.class);
            intent.putExtra("show_recommendation", true);
            startActivity(intent);
        });
        
        cardBrowseKnowledge.setOnClickListener(v -> {
            // 跳转到知识浏览页面
            Intent intent = new Intent(MainActivity.this, KnowledgeBrowserActivity.class);
            startActivity(intent);
        });
        
        cardLearningProgress.setOnClickListener(v -> {
            // 显示学习进度（可以是对话框或新页面）
            showLearningProgressDialog();
        });
        
        cardPersonalSettings.setOnClickListener(v -> {
            // 跳转到设置页面
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
        
        cardChangePassword.setOnClickListener(v -> {
            // 跳转到设置页面的修改密码部分
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("show_change_password", true);
            startActivity(intent);
        });
        
        // 管理员功能点击事件
        cardAddContent.setOnClickListener(v -> {
            // 跳转到添加内容页面
            Intent intent = new Intent(MainActivity.this, EditKnowledgeActivity.class);
            intent.putExtra("mode", "add");
            startActivity(intent);
        });
        
        cardEditContent.setOnClickListener(v -> {
            // 跳转到内容管理页面
            Intent intent = new Intent(MainActivity.this, AdminManageActivity.class);
            startActivity(intent);
        });
        
        cardDeleteContent.setOnClickListener(v -> {
            // 跳转到内容管理页面，删除模式
            Intent intent = new Intent(MainActivity.this, AdminManageActivity.class);
            intent.putExtra("mode", "delete");
            startActivity(intent);
        });
        
        cardUserManagement.setOnClickListener(v -> {
            // 用户管理功能（可以是新页面或对话框）
            showUserManagementDialog();
        });
        
        cardDataStatistics.setOnClickListener(v -> {
            // 数据统计功能
            showDataStatisticsDialog();
        });
        
        cardSystemSettings.setOnClickListener(v -> {
            // 系统设置
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("admin_mode", true);
            startActivity(intent);
        });
        
        // 退出登录
        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }
    
    /**
     * 加载今日推荐内容
     */
    private void loadTodayRecommendation() {
        executorService.execute(() ->{
            // 从数据库获取推荐内容（可以是最新的或随机的）
            Knowledge recommendation = knowledgeDao.getRandomKnowledge();
            if (recommendation != null) {
                tvRecommendationTitle.setText(recommendation.getTitle());
            }
        });
    }
    
    private void showLearningProgressDialog() {
        // 显示学习进度对话框
        new AlertDialog.Builder(this)
            .setTitle("学习进度")
            .setMessage("您已学习了 5 个知识点\n总共 20 个知识点\n完成度：25%")
            .setPositiveButton("继续学习", (dialog, which) -> {
                Intent intent = new Intent(MainActivity.this, KnowledgeBrowserActivity.class);
                startActivity(intent);
            })
            .setNegativeButton("关闭", null)
            .show();
    }
    
    private void showUserManagementDialog() {
        new AlertDialog.Builder(this)
            .setTitle("用户管理")
            .setMessage("当前系统用户数：15\n管理员：2\n普通用户：13")
            .setPositiveButton("查看详情", null)
            .setNegativeButton("关闭", null)
            .show();
    }
    
    private void showDataStatisticsDialog() {
        new AlertDialog.Builder(this)
            .setTitle("数据统计")
            .setMessage("知识条目：20\n用户访问：156次\n今日新增：3条")
            .setPositiveButton("查看详情", null)
            .setNegativeButton("关闭", null)
            .show();
    }
    
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("确认退出")
            .setMessage("确定要退出登录吗？")
            .setPositiveButton("确定", (dialog, which) -> logout())
            .setNegativeButton("取消", null)
            .show();
    }

    /**
     * 用户退出登录
     *
     */
    public void logout() {
        // 清除会话信息
        sessionManager.clearSession();

        goToLoginActivity();
    }

    /**
     * 显示学习进度对话框
     * Show Learning Progress Dialog
     * 
     * 原理解释：
     * - SharedPreferences = Android提供的轻量级数据存储方案
     * - 适用场景：存储用户设置、简单的统计数据、偏好配置
     * - 不适用场景：大量数据、复杂查询（应该用数据库）
     */
//    private void showLearningProgressDialog() {
//        // 从SharedPreferences获取学习统计数据 (Get learning statistics from SharedPreferences)
//        SharedPreferences prefs = getSharedPreferences("learning_progress", MODE_PRIVATE);
//
//        // 获取学习统计数据 (Get learning statistics)
//        int totalViewed = prefs.getInt("total_viewed", 0);           // 总浏览数
//        int todayViewed = prefs.getInt("today_viewed", 0);           // 今日浏览数
//        int favoriteCount = prefs.getInt("favorite_count", 0);       // 收藏数（未来功能）
//
//        // 计算学习进度百分比 (Calculate learning progress percentage)
//        new Thread(() -> {
//            try {
//                int totalKnowledge = knowledgeDao.getKnowledgeCount();
//                int progressPercent = totalKnowledge > 0 ? (totalViewed * 100 / totalKnowledge) : 0;
//
//                runOnUiThread(() -> {
//                    String message = String.format(
//                        "📚 学习统计\n\n" +
//                        "总浏览：%d 条知识\n" +
//                        "今日浏览：%d 条\n" +
//                        "学习进度：%d%%\n" +
//                        "收藏数量：%d 条",
//                        totalViewed, todayViewed, progressPercent, favoriteCount
//                    );
//
//                    new AlertDialog.Builder(this)
//                        .setTitle("📈 我的学习进度")
//                        .setMessage(message)
//                        .setPositiveButton("重置统计", (dialog, which) -> {
//                            // 重置学习统计数据 (Reset learning statistics)
//                            resetLearningProgress();
//                        })
//                        .setNegativeButton("关闭", null)
//                        .show();
//                });
//
//            } catch (Exception e) {
//                Log.e("MainActivity", "获取学习进度失败", e);
//                runOnUiThread(() -> {
//                    Toast.makeText(this, "获取学习进度失败", Toast.LENGTH_SHORT).show();
//                });
//            }
//        }).start();
//    }
//
//    /**
//     * 重置学习进度
//     * Reset Learning Progress
//     */
//    private void resetLearningProgress() {
//        SharedPreferences prefs = getSharedPreferences("learning_progress", MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        // 清除所有学习统计数据 (Clear all learning statistics)
//        editor.clear();
//        editor.apply(); // apply() = 异步保存，commit() = 同步保存
//
//        Toast.makeText(this, "学习进度已重置", Toast.LENGTH_SHORT).show();
//    }
//
//    /**
//     * 更新学习进度 (供其他Activity调用)
//     * Update Learning Progress (For other Activities to call)
//     *
//     * 使用方法：在KnowledgeBrowserActivity中，每次用户浏览知识时调用
//     * Usage: Call this method in KnowledgeBrowserActivity whenever user views knowledge
//     */
//    public static void updateLearningProgress(Context context) {
//        SharedPreferences prefs = context.getSharedPreferences("learning_progress", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        // 更新总浏览数 (Update total viewed count)
//        int totalViewed = prefs.getInt("total_viewed", 0);
//        editor.putInt("total_viewed", totalViewed + 1);
//
//        // 更新今日浏览数 (Update today's viewed count)
//        String today = new java.text.SimpleDateFormat("yyyy-MM-dd",
//            java.util.Locale.getDefault()).format(new java.util.Date());
//        String lastViewDate = prefs.getString("last_view_date", "");
//
//        if (today.equals(lastViewDate)) {
//            // 同一天，增加今日浏览数 (Same day, increase today's count)
//            int todayViewed = prefs.getInt("today_viewed", 0);
//            editor.putInt("today_viewed", todayViewed + 1);
//        } else {
//            // 新的一天，重置今日浏览数 (New day, reset today's count)
//            editor.putInt("today_viewed", 1);
//            editor.putString("last_view_date", today);
//        }
//
//        editor.apply();
//    }

}
