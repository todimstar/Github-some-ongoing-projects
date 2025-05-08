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
// ... import ...
import android.widget.TextView; // 导入 TextView

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TextView mainTitleTextView; // 添加主标题 TextView 引用
    private SciencePagerAdapter pagerAdapter;
    private List<ScienceContent> scienceContentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTitleTextView = findViewById(R.id.main_title); // 找到主标题 TextView
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        // 设置主标题文本
        mainTitleTextView.setText(R.string.main_title_text); // 使用 strings.xml 中的资源

        prepareScienceData();

        pagerAdapter = new SciencePagerAdapter(this, scienceContentList);
        viewPager.setAdapter(pagerAdapter);

        if (tabLayout != null) {
            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> tab.setText(pagerAdapter.getPageTitle(position))
            ).attach();
        }
        // ... WindowInsets handling ...
    }

    private void prepareScienceData() {
        scienceContentList = new ArrayList<>();
        // 使用字符串资源 ID
        // 将 placeholder_image 替换为相应的图片资源
        scienceContentList.add(new ScienceContent(R.drawable.tanglangxia, "孔雀螳螂虾", R.string.java_basics_desc));
        scienceContentList.add(new ScienceContent(R.drawable.h, "宽吻海豚", R.string.oop_desc));
        scienceContentList.add(new ScienceContent(R.drawable.haidan, "岩纹海胆", R.string.java_core_lib_desc));
        scienceContentList.add(new ScienceContent(R.drawable.jiazhou, "加州海狮", R.string.database_basics_desc));
        scienceContentList.add(new ScienceContent(R.drawable.shayu, "灰色礁鲨", R.string.web_basics_desc));
        scienceContentList.add(new ScienceContent(R.drawable.hongyu, "魟鱼", R.string.servlet_jsp_desc));
        scienceContentList.add(new ScienceContent(R.drawable.changmao, "长毛鱼", R.string.frameworks_desc));
        scienceContentList.add(new ScienceContent(R.drawable.xiaochou, "小丑鱼", R.string.build_tools_vcs_desc));
//        scienceContentList.add(new ScienceContent(android.R.drawable.ic_dialog_info, "Java 基础", R.string.java_basics_desc));
//        scienceContentList.add(new ScienceContent(android.R.drawable.ic_menu_sort_by_size, "面向对象 (OOP)", R.string.oop_desc));
//        scienceContentList.add(new ScienceContent(android.R.drawable.ic_menu_manage, "Java 核心库", R.string.java_core_lib_desc));
//        scienceContentList.add(new ScienceContent(android.R.drawable.ic_menu_agenda, "数据库基础", R.string.database_basics_desc));
//        scienceContentList.add(new ScienceContent(android.R.drawable.ic_dialog_map, "Web 基础", R.string.web_basics_desc));
//        scienceContentList.add(new ScienceContent(android.R.drawable.ic_menu_view, "Servlet & JSP", R.string.servlet_jsp_desc));
//        scienceContentList.add(new ScienceContent(android.R.drawable.ic_menu_share, "常用框架", R.string.frameworks_desc));
//        scienceContentList.add(new ScienceContent(android.R.drawable.stat_sys_download, "构建工具 & VCS", R.string.build_tools_vcs_desc));
        // ...
    }
}