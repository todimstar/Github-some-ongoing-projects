package com.liu.coursedesign.ui.activities;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.liu.coursedesign.Dao.KnowledgeDao;
import com.liu.coursedesign.R;
import com.liu.coursedesign.database.AppDatabase;
import com.liu.coursedesign.model.Knowledge;
import com.liu.coursedesign.ui.adapters.AdminAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 管理员内容管理页面
 * 对应布局文件: activity_admin_manage.xml
 */
public class AdminManageActivity extends AppCompatActivity implements AdminAdapter.OnItemActionListener {
    
    // UI组件
    private Toolbar toolbar;
    private EditText etSearch;
    private FloatingActionButton fabAdd;
    private RecyclerView recyclerView;
    
    private AdminAdapter adminAdapter;    // 页面适配器
    private List<Knowledge> knowledgeList;// 知识列表
    private KnowledgeDao knowledgeDao;

    private ExecutorService executorService;
    private volatile boolean isLoading = false;// 防止重复操作的标志，volatile确保线程安全
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage);
        
        initViews();
        initBusinessLogic();

        setupRecyclerViewWithEmptyData(); // 先用空数据设置RecyclerView
        loadKnowledgeDataAsync();         // 然后异步加载真实数据

        setupClickListeners();
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etSearch = findViewById(R.id.etSearch);
        fabAdd = findViewById(R.id.fabAdd);
        recyclerView = findViewById(R.id.recyclerView);
        
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    private void initBusinessLogic() {
        AppDatabase db = AppDatabase.getDatabase(this);
        knowledgeDao = db.knowledgeDao();

        // 注册单线程池
        executorService = Executors.newFixedThreadPool(1);
    }
        
    /**
     * 现代方案：用空数据初始化RecyclerView
     * - 作用：先创建UI骨架，再填充真实数据
     * - 优势：避免初始化顺序问题，提供更好的用户体验
     */
    private void setupRecyclerViewWithEmptyData() {
        // 初始化空列表 - Initialize Empty List
        knowledgeList = new ArrayList<>();

        // 搞个适配器
        adminAdapter = new AdminAdapter(knowledgeList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adminAdapter);

        Log.d("AdminManage", "RecyclerView初始化完成，使用空数据");
    }

    /**
     * ✅ 现代方案：异步加载数据并更新UI
     * - Callback Pattern = 回调模式，数据加载完成后更新UI
     * - Thread Safety = 线程安全，确保UI操作在主线程执行
     */
    private void loadKnowledgeDataAsync() {
        if (isLoading) return;
        isLoading = true;
        
        // 显示加载状态 
        showLoadingState();
        
        executorService.execute(() -> {
            try {
                // 后台线程：数据库操作 
                List<Knowledge> loadedData = knowledgeDao.getAllKnowledge();
                
                runOnUiThread(() -> {
                    onDataLoadComplete(loadedData);
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    onDataLoadError(e);
                });
            }
        });
    }

    /**
     * ✅ 数据加载完成回调
     */
    private void onDataLoadComplete(List<Knowledge> loadedData) {
        // 更新内部数据 
        this.knowledgeList.clear();
        this.knowledgeList.addAll(loadedData);
        
        // 通知适配器数据变化 
        if (adminAdapter != null) {
            adminAdapter.notifyDataSetChanged();
        }
        
        // 隐藏加载状态
        hideLoadingState();
        isLoading = false;
        
        Log.d("AdminManage", "数据加载完成，共" + loadedData.size() + "条记录");
        
        // 显示成功提示
        if (loadedData.isEmpty()) {
            Toast.makeText(this, "暂无知识条目，点击➕添加内容", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "加载了 " + loadedData.size() + " 条知识", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ✅ 数据加载错误回调
     * Data Load Error Callback
     */
    private void onDataLoadError(Exception error) {
        hideLoadingState();
        isLoading = false;
        
        Log.e("AdminManage", "数据加载失败", error);
        Toast.makeText(this, "数据加载失败：" + error.getMessage(), Toast.LENGTH_LONG).show();
        
        // 提供重试选项 - Provide Retry Option
        showRetryDialog();
    }

    /**
     * ✅ 显示加载状态
     * Show Loading State
     */
    private void showLoadingState() {
        // 可以添加进度条或加载动画
        // ProgressBar, Shimmer效果等
        if (fabAdd != null) fabAdd.setEnabled(false);
        if (etSearch != null) etSearch.setEnabled(false);
    }
    
    /**
     * ✅ 隐藏加载状态
     * Hide Loading State
     */
    private void hideLoadingState() {
        if (fabAdd != null) fabAdd.setEnabled(true);
        if (etSearch != null) etSearch.setEnabled(true);
    }
    
    /**
     * ✅ 显示重试对话框
     * Show Retry Dialog
     */
    private void showRetryDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("加载失败")
            .setMessage("数据加载失败，是否重试？")
            .setPositiveButton("重试", (dialog, which) -> {
                loadKnowledgeDataAsync();
            })
            .setNegativeButton("取消", null)
            .show();
    }
    
    private void setupClickListeners() {
        // 添加按钮
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(AdminManageActivity.this, EditKnowledgeActivity.class);
            intent.putExtra("mode", "add");
            startActivity(intent);
        });
        
        // 搜索功能
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String keyword = etSearch.getText().toString().trim();
            if (!keyword.isEmpty()) {
                searchKnowledge(keyword);
            }
            return true;
        });
        
        // Toolbar返回按钮
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
     private void searchKnowledge(String keyword) {
        if (isLoading) {
            Toast.makeText(this, "正在加载中，请稍候...", Toast.LENGTH_SHORT).show();
            return;
        }
        
        isLoading = true;
        showLoadingState();
        
        executorService.execute(() -> {
            try {
                List<Knowledge> searchResults = knowledgeDao.searchKnowledge(keyword);
                
                runOnUiThread(() -> {
                    // 更新数据 - Update Data
                    this.knowledgeList.clear();
                    this.knowledgeList.addAll(searchResults);
                    
                    // 通知适配器 - Notify Adapter
                    if (adminAdapter != null) {
                        adminAdapter.notifyDataSetChanged();
                    }
                    
                    hideLoadingState();
                    isLoading = false;
                    
                    // 更新标题显示搜索结果 - Update Title to Show Search Results
                    if (toolbar != null) {
                        toolbar.setTitle("搜索结果: " + keyword);
                    }
                    
                    Toast.makeText(this, "找到 " + searchResults.size() + " 条结果", 
                        Toast.LENGTH_SHORT).show();
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    onDataLoadError(e);
                });
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // 页面恢复时重新加载数据 - Reload data when page resumes
        if (!isLoading) {
            loadKnowledgeDataAsync();
        }
    }
    
    // 实现AdminAdapter的回调接口
    @Override
    public void onEditClick(Knowledge knowledge) {
        Intent intent = new Intent(AdminManageActivity.this, EditKnowledgeActivity.class);
        intent.putExtra("mode", "edit");
        intent.putExtra("knowledge_id", knowledge.getId());
        startActivity(intent);
    }
    
    @Override
    public void onDeleteClick(Knowledge knowledge) {
        if (isLoading) {
            Toast.makeText(this, "正在处理中，请稍候...", Toast.LENGTH_SHORT).show();
            return;
        }
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("确认删除")
            .setMessage("确定要删除《" + knowledge.getTitle() + "》吗？")
            .setPositiveButton("删除", (dialog, which) -> {
                deleteKnowledge(knowledge);
            })
            .setNegativeButton("取消", null)
            .show();
    }
    
    /**
     * ✅ 专门的删除方法
     * Dedicated Delete Method
     */
    private void deleteKnowledge(Knowledge knowledge) {
        isLoading = true;
        showLoadingState();
        
        executorService.execute(() -> {
            try {
                // 后台线程删除 - Background Thread Delete
                knowledgeDao.delete(knowledge);
                
                // 重新加载数据 - Reload Data
                List<Knowledge> updatedData = knowledgeDao.getAllKnowledge();
                
                runOnUiThread(() -> {
                    // 更新数据 - Update Data
                    this.knowledgeList.clear();
                    this.knowledgeList.addAll(updatedData);
                    
                    // 通知适配器 - Notify Adapter
                    if (adminAdapter != null) {
                        adminAdapter.notifyDataSetChanged();
                    }
                    
                    hideLoadingState();
                    isLoading = false;
                    
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    onDataLoadError(e);
                });
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
