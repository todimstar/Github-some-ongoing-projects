package com.liu.class02;

import android.content.Context;           // 上下文，提供应用环境信息
import android.view.LayoutInflater;       // 用于加载XML布局文件
import android.view.View;                 // 视图基类
import android.view.ViewGroup;            // 视图组基类
import android.widget.BaseAdapter;        // 适配器基类
import android.widget.CheckBox;           // 复选框控件
import android.widget.CompoundButton;     // 复合按钮基类（如CheckBox）
import android.widget.ImageView;          // 图像视图控件
import android.widget.TextView;           // 文本视图控件

import java.util.ArrayList;               // 动态数组，类似C++的vector
import java.util.HashSet;                 // HashSet是基于哈希表实现的Set集合，类似C++的unordered_set
                                         // 特点：1.不允许重复元素 2.无序存储 3.查找/添加/删除时间复杂度O(1)
                                         // 原理：通过哈希函数将元素映射到内部数组的不同位置，处理冲突采用链地址法、
import java.util.List;                    // 列表接口
import java.util.Set;                     // 集合接口

/**
 * 课程适配器类，用于连接数据源和ListView
 * 继承自BaseAdapter，这是Android的基本适配器类
 */
public class CourseAdapter extends BaseAdapter {
    private List<CourseItem> courses;          // 课程数据列表
    private Set<Integer> selectedPositions;    // 已选中项的位置集合
    private Context context;                   // 上下文对象，用于访问应用资源

    /**
     * 课程项内部类，用于存储单个课程的信息
     * 在Java中，内部类可以是static的，类似C++的嵌套类
     */
    public static class CourseItem {
        String name;           // 课程名称
        String description;    // 课程描述
        int imageResId;        // 图片资源ID

        /**
         * 构造函数，初始化课程项
         * @param name 课程名称
         * @param description 课程描述
         * @param imageResId 图片资源ID
         */
        public CourseItem(String name, String description, int imageResId) {
            this.name = name;                 // this关键字类似C++中的this指针
            this.description = description;
            this.imageResId = imageResId;
        }
    }

    /**
     * 构造函数，初始化适配器
     * @param context 上下文对象
     * @param courses 课程列表
     */
    public CourseAdapter(Context context, List<CourseItem> courses) {
        this.context = context;
        this.courses = courses;
        this.selectedPositions = new HashSet<>();  // 初始化为空集合
    }

    /**
     * 获取列表项数量
     * @return 列表项数量
     */
    @Override
    public int getCount() {
        return courses.size();  // 返回列表大小，类似C++中的vector.size()
    }

    /**
     * 获取指定位置的列表项
     * @param position 位置索引
     * @return 列表项对象
     */
    @Override
    public Object getItem(int position) {
        return courses.get(position);  // 获取指定位置的项，类似C++中的vector[i]
    }

    /**
     * 获取指定位置项的ID
     * @param position 位置索引
     * @return 项ID
     */
    @Override
    public long getItemId(int position) {
        return position;  // 这里简单地使用位置作为ID
    }

    /**
     * 获取指定位置的视图
     * 这是适配器最核心的方法，用于创建或复用列表项视图
     * @param position 位置索引
     * @param convertView 可复用的视图
     * @param parent 父视图组
     * @return 配置好的视图
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;  // 视图持有者，用于缓存视图组件引用，提高性能

        // 如果没有可复用的视图，则创建新视图
        if (convertView == null) {
            // 从XML布局文件加载视图
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_course, parent, false);
            
            // 创建ViewHolder并查找视图组件
            holder = new ViewHolder();
            holder.imageViewCourse = convertView.findViewById(R.id.imageViewCourse);
            holder.textViewCourseName = convertView.findViewById(R.id.textViewCourseName);
            holder.textViewCourseDesc = convertView.findViewById(R.id.textViewCourseDesc);
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            
            // 将ViewHolder与视图关联，类似于C++中的setUserData
            convertView.setTag(holder);
        } else {
            // 复用已有视图，获取关联的ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        // 获取当前位置的课程项
        CourseItem item = courses.get(position);
        
        // 设置视图内容
        holder.textViewCourseName.setText(item.name);
        holder.textViewCourseDesc.setText(item.description);
        holder.imageViewCourse.setImageResource(item.imageResId);

        // 避免CheckBox状态错乱（由于视图复用可能导致状态混乱）
        holder.checkBox.setOnCheckedChangeListener(null);
        // 根据选中集合设置CheckBox状态
        holder.checkBox.setChecked(selectedPositions.contains(position));

        // 设置整行的点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 切换选中状态
                boolean isSelected = selectedPositions.contains(position);
                if (isSelected) {
                    selectedPositions.remove(position);  // 如果已选中，则移除
                } else {
                    selectedPositions.add(position);     // 如果未选中，则添加
                }
                notifyDataSetChanged();  // 通知适配器数据已变化，刷新视图
            }
        });

        // 设置图片的点击事件
        holder.imageViewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 切换选中状态，与整行点击逻辑相同
                boolean isSelected = selectedPositions.contains(position);
                if (isSelected) {
                    selectedPositions.remove(position);
                } else {
                    selectedPositions.add(position);
                }
                notifyDataSetChanged();
            }
        });

        // 设置CheckBox的状态变化监听器
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 根据CheckBox状态更新选中集合
                if (isChecked) {
                    selectedPositions.add(position);
                } else {
                    selectedPositions.remove(position);
                }
                // 这里不需要notifyDataSetChanged，因为CheckBox自己会更新状态
            }
        });

        return convertView;  // 返回配置好的视图
    }

    /**
     * 视图持有者类，用于缓存视图组件引用
     * 这是一种优化技术，避免频繁调用findViewById
     * 类似于C++中的结构体
     */
    private static class ViewHolder {
        ImageView imageViewCourse;     // 课程图片
        TextView textViewCourseName;   // 课程名称
        TextView textViewCourseDesc;   // 课程描述
        CheckBox checkBox;             // 选择复选框
    }

    /**
     * 获取所有选中的课程名称
     * @return 选中课程名称列表
     */
    public List<String> getSelectedCourses() {
        List<String> selectedCourses = new ArrayList<>();
        // 遍历所有选中位置
        for (int position : selectedPositions) {
            // 确保位置有效（防止数组越界）
            if (position < courses.size()) {
                // 添加课程名称到结果列表
                selectedCourses.add(courses.get(position).name);
            }
        }
        return selectedCourses;
    }
}