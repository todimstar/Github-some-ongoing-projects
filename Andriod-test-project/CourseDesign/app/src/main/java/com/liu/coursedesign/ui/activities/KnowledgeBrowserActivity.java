package com.liu.coursedesign.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.liu.coursedesign.Dao.KnowledgeDao;
import com.liu.coursedesign.R;
import com.liu.coursedesign.database.AppDatabase;
import com.liu.coursedesign.model.Knowledge;
import com.liu.coursedesign.ui.adapters.KnowledgeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 知识浏览页面Activity
 * 对应布局文件: fragment_knowledge_browser.xml
 */
public class KnowledgeBrowserActivity extends AppCompatActivity {

    // 线程池管理 - Thread Pool Management
    private ExecutorService executorService;
    
    // 数据加载状态追踪 - Data Loading State Tracking
    private boolean isDataLoaded = false;
    
    private Toolbar toolbar;
    private TabLayout tabLayoutCategories;
    private ViewPager2 viewPagerKnowledge;
    private TextView tvPageIndicator;
    private LinearProgressIndicator progressIndicator;
    private MaterialButton btnPrevious, btnNext;
    private FloatingActionButton fabSearch;
    
    private KnowledgeAdapter knowledgeAdapter;
    private List<Knowledge> knowledgeList;
    private KnowledgeDao knowledgeDao;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_knowledge_browser);
        
        // 初始化线程池 - Initialize Thread Pool
        initExecutorService();

        initViews();
        initBusinessLogic();

        // 设置空适配器以防RecyclerView报告没有适配器
        setupEmptyViewPager();

        // 显示加载指示器 - Show Loading Indicator
        showLoadingState();
        // 异步加载数据 - Asynchronously Load Data
        loadKnowledgeDataAsync();
    }

    /**
     * 设置空的ViewPager适配器
     * Setup Empty ViewPager Adapter
     * 
     * 原理解释：
     * - 作用：防止ViewPager2内部RecyclerView报告"No adapter attached"警告
     * - 时机：在数据加载之前立即设置
     * - 好处：提供更好的用户体验，避免警告信息
     */
    private void setupEmptyViewPager() {
        // 创建空的知识列表 - Create Empty Knowledge List
        List<Knowledge> emptyList = new ArrayList<Knowledge>();
        
        // 设置空适配器 - Set Empty Adapter
        knowledgeAdapter = new KnowledgeAdapter(emptyList);
        viewPagerKnowledge.setAdapter(knowledgeAdapter);
        
        Log.d("KnowledgeBrowser", "空适配器设置完成 - Empty Adapter Setup Complete");
    }
    
    /**
     * 初始化线程池
     * Initialize Thread Pool
     * 
     * 原理解释：
     * - newFixedThreadPool(1) = 创建固定大小为1的线程池
     * - 作用：复用线程，避免频繁创建销毁的开销
     * - 优势：生命周期可控，避免内存泄漏
     */
    private void initExecutorService() {
        executorService = Executors.newFixedThreadPool(1);
    }

    /**
     * 异步加载知识数据 (修复版本)
     * Asynchronously Load Knowledge Data (Fixed Version)
     * 
     * 原理解释：
     * - Asynchronous = Async (异步) + chronous (时间相关) = 异步操作
     * - Callback = Call (调用) + back (返回) = 回调机制
     * - 作用：在后台线程加载数据，在主线程更新UI
     */
    private void loadKnowledgeDataAsync() {
        executorService.execute(() ->{
            try {
                Log.d("KnowledgeBrowser", "开始加载数据 - Starting Data Loading");

                // 后台线程：数据库操作 - Background Thread: Database Operation
                List<Knowledge> loadedData = knowledgeDao.getAllKnowledge();

                Log.d("KnowledgeBrowser", "数据加载完成，共" + loadedData.size() + "条记录");
                for (Knowledge k : loadedData)Log.d("KnowledgeBrowser", String.valueOf(k));
                // 切换到主线程更新UI - Switch to Main Thread to Update UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onDataLoadComplete(loadedData);
                    }
                });

            } catch (Exception e) {
                Log.e("KnowledgeBrowser", "数据加载失败 - Data Loading Failed", e);

                // 错误处理也要在主线程 - Error Handling Also on Main Thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onDataLoadError(e);
                    }
                });
            }
        });
    }

    /**
     * 数据加载完成回调 (主线程执行)
     * Data Load Complete Callback (Execute on Main Thread)
     * 
     * 原理解释：
     * - Callback = 回调函数，异步操作完成后调用
     * - 作用：更新UI，设置适配器，刷新界面
     * - 时机：必须在主线程执行，确保UI操作安全
     */
    private void onDataLoadComplete(List<Knowledge> loadedData) {
        // 更新数据 - Update Data
        this.knowledgeList = loadedData;
        this.isDataLoaded = true;
        
        // 隐藏加载状态 - Hide Loading State
        hideLoadingState();
        
        if (loadedData != null && !loadedData.isEmpty()) {
            // ✅ 更新适配器数据而不是重新创建 - Update Adapter Data Instead of Recreating
            updateViewPagerData(loadedData);
            
            Log.d("KnowledgeBrowser", "UI更新完成 - UI Update Complete");
            Toast.makeText(this, "加载了 " + loadedData.size() + " 条知识点", Toast.LENGTH_SHORT).show();
            
        } else {
            // 显示空数据状态 - Show Empty Data State
            showEmptyDataState();
        }
    }

    /**
     * 更新ViewPager数据
     * Update ViewPager Data
     * 
     * 原理解释：
     * - 作用：更新已存在的适配器数据，避免重新创建适配器
     * - 优势：性能更好，动画更流畅
     * - 方法：使用适配器的updateData方法
     */
    private void updateViewPagerData(List<Knowledge> newData) {
        if (knowledgeAdapter != null) {
            // 更新适配器数据 - Update Adapter Data
            knowledgeAdapter.updateData(newData);
            
            // 设置页面变化监听器（如果还没设置）- Set Page Change Listener (if not set yet)
            if (viewPagerKnowledge.getAdapter() != null) {
                setupPageChangeListener();
            }
            
            // 初始化页面指示器 - Initialize Page Indicator
            updatePageIndicator(0);
            updateNavigationButtons(0);
            
        } else {
            // 备用方案：重新设置适配器 - Fallback: Reset Adapter
            setupViewPager();
            Log.d("KnowledgeBrowser", "knowledgeAdapter为空,用备用方案setupViewPager()重设适配器");
        }
    }

    /**
     * 设置页面变化监听器
     * Setup Page Change Listener
     * 
     * 原理解释：
     * - OnPageChangeCallback = On (在) + Page (页面) + Change (变化) + Callback (回调) = 页面变化回调
     * - 作用：监听用户滑动页面的事件
     * - 用途：更新页面指示器、导航按钮状态
     */
    private void setupPageChangeListener() {
        viewPagerKnowledge.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Log.d("KnowledgeBrowser", "页面切换到: " + position);
                
                currentPosition = position;
                updatePageIndicator(position);
                updateNavigationButtons(position);
                
                // 可以在这里添加页面切换的其他逻辑
                // Additional page change logic can be added here
                onPageChanged(position);
            }
            
            /**
             * 页面开始滑动时调用
             * Called when page starts scrolling
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                
                // 根据滑动状态执行不同操作 - Execute different operations based on scroll state
                switch (state) {
                    case ViewPager2.SCROLL_STATE_IDLE:
                        // 滑动停止 - Scrolling stopped
                        Log.d("KnowledgeBrowser", "页面滑动停止");
                        break;
                    case ViewPager2.SCROLL_STATE_DRAGGING:
                        // 用户开始拖拽 - User starts dragging
                        Log.d("KnowledgeBrowser", "用户开始拖拽页面");
                        break;
                    case ViewPager2.SCROLL_STATE_SETTLING:
                        // 页面自动滑动到目标位置 - Page automatically scrolls to target position
                        Log.d("KnowledgeBrowser", "页面自动滑动中");
                        break;
                }
            }
        });
    }

    /**
     * 页面变化时的额外处理
     * Additional Processing When Page Changes
     * 
     * 原理解释：
     * - 作用：在页面切换时执行自定义逻辑
     * - 用途：预加载下一页数据、更新统计信息等
     */
    private void onPageChanged(int position) {
        // 获取当前显示的知识点 - Get Currently Displayed Knowledge
        if (knowledgeList != null && position < knowledgeList.size()) {
            Knowledge currentKnowledge = knowledgeList.get(position);
            
            // 更新标题栏 - Update Title Bar
            if (toolbar != null) {
                toolbar.setTitle(currentKnowledge.getTitle());
            }
            
            // 记录浏览历史（如果需要）- Record Browsing History (if needed)
            recordViewHistory(currentKnowledge);
            
            Log.d("KnowledgeBrowser", "当前浏览: " + currentKnowledge.getTitle());
        }
    }
    
    /**
     * 记录浏览历史
     * Record Browsing History
     * 
     * 原理解释：
     * - Browsing History = Browsing (浏览) + History (历史) = 浏览历史
     * - 作用：跟踪用户查看了哪些知识点
     * - 用途：推荐系统、学习进度跟踪
     */
    private void recordViewHistory(Knowledge knowledge) {
        // 在后台线程记录历史 - Record history in background thread
        if (executorService != null) {
            executorService.execute(() -> {
                try {
                    // 这里可以添加历史记录到数据库的逻辑
                    // Logic to add history record to database can be added here
                    Log.d("KnowledgeBrowser", "记录浏览历史: " + knowledge.getTitle());
                    
                    // 例如：增加浏览次数 - For example: increase view count
                    // knowledgeDao.incrementViewCount(knowledge.getId());
                    
                } catch (Exception e) {
                    Log.e("KnowledgeBrowser", "记录浏览历史失败", e);
                }
            });
        }
    }

    /**
     * 数据加载错误回调
     * Data Load Error Callback
     */
    private void onDataLoadError(Exception error) {
        hideLoadingState();
        showErrorState(error.getMessage());
        
        // 提供重试机制 - Provide Retry Mechanism
        showRetryOption();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tabLayoutCategories = findViewById(R.id.tabLayoutCategories);
        viewPagerKnowledge = findViewById(R.id.viewPagerKnowledge);
        tvPageIndicator = findViewById(R.id.tvPageIndicator);
        progressIndicator = findViewById(R.id.progressIndicator);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        fabSearch = findViewById(R.id.fabSearch);
        
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 设置监听器
        setupClickListeners();
        // 设置分类
        setupTabLayout();
    }
    
    private void initBusinessLogic() {
        AppDatabase db = AppDatabase.getDatabase(this);
        knowledgeDao = db.knowledgeDao();
    }
    
    /**
     * 显示加载状态
     * Show Loading State
     * 
     * 原理解释：
     * - Loading State = Loading (加载中) + State (状态) = 加载状态
     * - 作用：给用户反馈，告知数据正在加载
     * - UI组件：ProgressBar, Loading文字, 禁用按钮等
     */
    private void showLoadingState() {
        if (progressIndicator != null) {
            progressIndicator.setVisibility(View.VISIBLE);
        }
        
        // 禁用导航按钮 - Disable Navigation Buttons
        if (btnPrevious != null) btnPrevious.setEnabled(false);
        if (btnNext != null) btnNext.setEnabled(false);
        if (fabSearch != null) fabSearch.setEnabled(false);
        
        // 显示加载提示 - Show Loading Hint
        if (tvPageIndicator != null) {
            tvPageIndicator.setText("正在加载知识点...");
        }
    }
    
    /**
     * 隐藏加载状态
     * Hide Loading State
     */
    private void hideLoadingState() {
        if (progressIndicator != null) {
            progressIndicator.setVisibility(View.GONE);
        }
        
        // 重新启用按钮 - Re-enable Buttons
        if (btnPrevious != null) btnPrevious.setEnabled(true);
        if (btnNext != null) btnNext.setEnabled(true);
        if (fabSearch != null) fabSearch.setEnabled(true);
    }
    
    /**
     * 显示空数据状态
     * Show Empty Data State
     */
    private void showEmptyDataState() {
        if (tvPageIndicator != null) {
            tvPageIndicator.setText("暂无知识点数据");
        }
        
        Toast.makeText(this, "没有找到知识点数据，请检查数据库", Toast.LENGTH_LONG).show();
    }
    
    /**
     * 显示错误状态
     * Show Error State
     */
    private void showErrorState(String errorMessage) {
        if (tvPageIndicator != null) {
            tvPageIndicator.setText("加载失败");
        }
        
        Toast.makeText(this, "加载失败: " + errorMessage, Toast.LENGTH_LONG).show();
    }
    
    /**
     * 显示重试选项
     * Show Retry Option
     */
    private void showRetryOption() {
        new AlertDialog.Builder(this)
            .setTitle("加载失败")
            .setMessage("数据加载失败，是否重试？")
            .setPositiveButton("重试", (dialog, which) -> {
                showLoadingState();
                loadKnowledgeDataAsync();
            })
            .setNegativeButton("取消", (dialog, which) -> {
                finish(); // 关闭Activity
            })
            .show();
    }
    
    /**
     * 修复后的setupViewPager方法
     * Fixed setupViewPager Method
     */
    private void setupViewPager() {
        // 确保数据已加载 - Ensure Data is Loaded
        if (knowledgeList == null || knowledgeList.isEmpty()) {
            Log.w("KnowledgeBrowser", "尝试设置ViewPager但数据为空 - Trying to setup ViewPager but data is empty");
            return;
        }
        
        // 创建适配器并设置给ViewPager2 - Create Adapter and Set to ViewPager2
        knowledgeAdapter = new KnowledgeAdapter(knowledgeList);
        viewPagerKnowledge.setAdapter(knowledgeAdapter);
        
        // 设置页面变化监听器 - Set Page Change Listener
        viewPagerKnowledge.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                updatePageIndicator(position);
                updateNavigationButtons(position);
            }
        });
        
        // 初始化页面指示器 - Initialize Page Indicator
        updatePageIndicator(0);
        updateNavigationButtons(0);
        
        Log.d("KnowledgeBrowser", "ViewPager设置完成 - ViewPager Setup Complete");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // 清理线程池 - Clean up Thread Pool
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                // 等待现有任务完成 - Wait for Existing Tasks to Complete
                if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    executorService.shutdownNow(); // 强制关闭 - Force Shutdown
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }
    }
    
    
    //old
    private void setupTabLayout() {
        // 添加分类标签
        tabLayoutCategories.addTab(tabLayoutCategories.newTab().setText("全部"));
        tabLayoutCategories.addTab(tabLayoutCategories.newTab().setText("基础概念"));
        tabLayoutCategories.addTab(tabLayoutCategories.newTab().setText("高级概念"));
        tabLayoutCategories.addTab(tabLayoutCategories.newTab().setText("实践应用"));
        
        // 设置标签选择监听器
        tabLayoutCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterKnowledgeByCategory(tab.getText().toString());
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    
    private void setupClickListeners() {
        // 上一页按钮
        btnPrevious.setOnClickListener(v -> {
            if (currentPosition > 0) {
                viewPagerKnowledge.setCurrentItem(currentPosition - 1, true);
                Log.d("KnowledgeBrowser", "切换到上一页: " + (currentPosition - 1));
            }
        });
        
        // 下一页按钮
        btnNext.setOnClickListener(v -> {
            if (currentPosition < knowledgeList.size() - 1) {
                viewPagerKnowledge.setCurrentItem(currentPosition + 1, true);
                Log.d("KnowledgeBrowser", "切换到下一页: " + (currentPosition + 1));
            }
        });
        
        // 搜索按钮
        fabSearch.setOnClickListener(v -> {
            // 显示搜索对话框或跳转到搜索页面
            showSearchDialog();
        });
        
        // Toolbar返回按钮
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    /**
     * 更新页面指示器
     */
    private void updatePageIndicator(int position) {
        int total = knowledgeList.size();
        if (total > 0) {
            tvPageIndicator.setText(String.format("%d / %d", position + 1, total));
            
            // 更新进度条
            float progress = (float) (position + 1) / total * 100;
            progressIndicator.setProgress((int) progress, true);
        }
    }
    
    /**
     * 更新导航按钮状态
     */
    private void updateNavigationButtons(int position) {
        btnPrevious.setEnabled(position > 0);
        btnNext.setEnabled(position < knowledgeList.size() - 1);
    }
    
    /**
     * 根据分类筛选知识
     */
    private void filterKnowledgeByCategory(String category) {
        if ("全部".equals(category)) {
            executorService.execute(() -> {
                knowledgeList = knowledgeDao.getAllKnowledge();
            });
        } else {
            executorService.execute(() -> {
                knowledgeList = knowledgeDao.getKnowledgeByCategory(category);
            });
        }
        
        // 更新适配器
        knowledgeAdapter.updateData(knowledgeList);
        
        // 重置到第一页
        viewPagerKnowledge.setCurrentItem(0, false);
        updatePageIndicator(0);
        updateNavigationButtons(0);
    }
    
    private void checkShowRecommendation() {
        boolean showRecommendation = getIntent().getBooleanExtra("show_recommendation", false);
        if (showRecommendation) {
            // 显示推荐内容，可以跳转到特定页面或高亮显示
            toolbar.setTitle("今日推荐");
        }
    }
    
    private void showSearchDialog() {
        // 简单的搜索对话框实现
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("搜索知识");
        
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("请输入关键词");
        builder.setView(input);
        Log.d("KnowledgeBrowser", "已显示搜索对话框");
        
        builder.setPositiveButton("搜索", (dialog, which) -> {
            String keyword = input.getText().toString().trim();
            if (!keyword.isEmpty()) {
                searchKnowledge(keyword);
            }
        });
        
        builder.setNegativeButton("取消", null);
        builder.show();
    }
    
    private void searchKnowledge(String keyword) {
        if (executorService != null) {
            // // 显示搜索加载状态 - Show Search Loading State
            // showSearchLoadingState();
            
            executorService.execute(() -> {
                try {
                    // 后台线程：执行搜索操作
                    List<Knowledge> searchResults = knowledgeDao.searchKnowledge(keyword);
                    
                    runOnUiThread(() -> {
                        if (!searchResults.isEmpty()) {
                            knowledgeList = searchResults;
                            knowledgeAdapter.updateData(knowledgeList);
                            viewPagerKnowledge.setCurrentItem(0, false);
                            updatePageIndicator(0);
                            updateNavigationButtons(0);
                            toolbar.setTitle("搜索结果: " + keyword);
                            
                        }else {
                            android.widget.Toast.makeText(this, "未找到相关内容", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    });
                    
                } catch (Exception e) {
                    Log.e("KnowledgeBrowser", "搜索失败 - Search Failed", e);
                    
                    // 错误处理也要在主线程 
                    runOnUiThread(() -> {
                        Toast.makeText(KnowledgeBrowserActivity.this, 
                            "搜索失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            });
        }
         
    }
}
