package com.liu.tabuiwithviewpager2;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2; // 导入 ViewPager2
import com.google.android.material.tabs.TabLayout; // 导入 TabLayout
import com.google.android.material.tabs.TabLayoutMediator; // 导入 TabLayoutMediator
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout; // 可选
    private SciencePagerAdapter pagerAdapter;
    private List<ScienceContent> scienceContentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // EdgeToEdge 可能与 ViewPager2/TabLayout 配合需要额外调整 padding
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout); // 可选

        // 准备科普数据 (你需要替换成真实的图片资源和文字)
        prepareScienceData();

        pagerAdapter = new SciencePagerAdapter(this, scienceContentList);
        viewPager.setAdapter(pagerAdapter);

        // 可选: 将 TabLayout 与 ViewPager2 关联
        if (tabLayout != null) {
            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> tab.setText(pagerAdapter.getPageTitle(position))
            ).attach();
        }


        // 处理 WindowInsets (如果未使用 EdgeToEdge, 可能不需要)
        // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        //     Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        //     // 注意: 这里可能需要调整 padding 应用到 ViewPager 或其父布局，而不是根布局 main
        //     v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        //     return insets;
        // });
    }

    private void prepareScienceData() {
        scienceContentList = new ArrayList<>();
        // 添加你的科普内容
        // 确保 R.drawable.image1, R.drawable.image2 等图片资源存在于你的 drawable 目录下
        scienceContentList.add(new ScienceContent(R.drawable.placeholder_image, "主题一", "这是关于主题一的详细科普介绍..."));
        scienceContentList.add(new ScienceContent(R.drawable.placeholder_image, "主题二", "这是关于主题二的详细科普介绍..."));
        scienceContentList.add(new ScienceContent(R.drawable.placeholder_image, "主题三", "这是关于主题三的详细科普介绍..."));
        // ... 添加更多内容
    }
}