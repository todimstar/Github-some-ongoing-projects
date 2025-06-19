package com.liu.class02;

import android.os.Bundle;
import android.text.TextUtils;  // 用于文本处理的工具类
import android.view.View;       // Android视图基类
import android.widget.Button;   // 按钮控件
import android.widget.ListView; // 列表视图控件
import android.widget.Toast;    // 提示框控件

import androidx.appcompat.app.AlertDialog; // 对话框控件
import androidx.appcompat.app.AppCompatActivity; // Activity基类

import java.util.ArrayList; // Java集合类，类似于C++的vector
import java.util.List;      // Java集合接口

/**
 * 主活动类，相当于应用的主窗口
 * 继承自AppCompatActivity，这是Android的基本活动类
 */
public class MainActivity extends AppCompatActivity {
    // 成员变量声明，类似C++的private成员
    private ListView listViewCourses;  // 课程列表视图
    private Button buttonSubmit;       // 提交按钮
    private CourseAdapter adapter;     // 自定义适配器，用于连接数据和ListView

    /**
     * 活动创建时的回调方法，类似于C++的构造函数
     * @param savedInstanceState 保存的状态信息，用于恢复活动状态
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 调用父类方法，类似C++中的基类构造函数调用
        setContentView(R.layout.activity_main); // 设置布局，R是自动生成的资源类

        // 通过ID查找视图组件，类似于在C++中通过指针获取UI元素
        listViewCourses = findViewById(R.id.listViewCourses);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // 初始化课程列表，使用ArrayList（Java的动态数组，类似C++的vector）
        List<CourseAdapter.CourseItem> courseList = new ArrayList<>();
        
        // 添加课程项，每项包含名称、描述和图标资源ID
        // android.R.drawable是Android系统提供的图标资源
        courseList.add(new CourseAdapter.CourseItem("Java程序设计", "面向对象编程基础课程", android.R.drawable.ic_menu_edit));
        courseList.add(new CourseAdapter.CourseItem("C语言程序设计", "计算机编程入门课程", android.R.drawable.ic_menu_sort_by_size));
        courseList.add(new CourseAdapter.CourseItem("数据结构", "计算机科学核心课程", android.R.drawable.ic_menu_compass));
        courseList.add(new CourseAdapter.CourseItem("通识学时课程", "综合实践课程", android.R.drawable.ic_menu_agenda));
        courseList.add(new CourseAdapter.CourseItem("数据库原理", "数据管理与存储技术", android.R.drawable.ic_menu_save));
        courseList.add(new CourseAdapter.CourseItem("操作系统", "计算机系统核心课程", android.R.drawable.ic_menu_manage));
        
        // 可以继续添加更多课程，ListView会自动支持滚动
        courseList.add(new CourseAdapter.CourseItem("计算机网络", "网络通信基础课程", android.R.drawable.ic_menu_share));
        courseList.add(new CourseAdapter.CourseItem("软件工程", "软件开发方法学", android.R.drawable.ic_menu_help));
        courseList.add(new CourseAdapter.CourseItem("人工智能", "智能系统设计基础", android.R.drawable.ic_menu_search));
        courseList.add(new CourseAdapter.CourseItem("编译原理", "程序语言处理技术", android.R.drawable.ic_menu_view));
        courseList.add(new CourseAdapter.CourseItem("计算机图形学", "图形图像处理技术", android.R.drawable.ic_menu_gallery));
        courseList.add(new CourseAdapter.CourseItem("Web开发", "网站应用开发技术", android.R.drawable.ic_menu_info_details));

        // 创建适配器并设置给ListView
        // 适配器是连接数据源和ListView的桥梁，类似于MVC模式中的Controller
        adapter = new CourseAdapter(this, courseList);
        listViewCourses.setAdapter(adapter);

        // 设置提交按钮点击事件，使用匿名内部类（类似C++的lambda表达式）
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取选中的课程列表
                List<String> selectedCourses = adapter.getSelectedCourses();
                if (selectedCourses.isEmpty()) {
                    // 如果没有选择课程，显示提示信息
                    // Toast是Android的轻量级提示框
                    Toast.makeText(MainActivity.this, "请选择至少一门课程", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 使用对话框显示选择的课程
                // AlertDialog是Android的对话框组件
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("选课成功");
                // TextUtils.join类似于C++中使用分隔符连接字符串数组
                builder.setMessage("你选择的课程：" + TextUtils.join(", ", selectedCourses));
                builder.setPositiveButton("确定", null); // 设置确定按钮，无点击事件
                builder.show(); // 显示对话框

                // 同时使用Toast显示（Toast是短暂显示的提示信息）
                String message = "你选择的课程：" + TextUtils.join(", ", selectedCourses);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}