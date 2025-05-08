package com.liu.weixinfragment.ui.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // 导入 TextView

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liu.weixinfragment.R;
import java.util.ArrayList; // 导入 ArrayList
import java.util.List;      // 导入 List

public class DiscoverFragment extends Fragment {

    private RecyclerView recyclerView;
    private DiscoverAdapter adapter;
    private List<DiscoveryItem> itemList;
    private TextView placeholderText; // 添加占位符 TextView

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_discover);
        placeholderText = view.findViewById(R.id.text_discover_placeholder); // 初始化占位符

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 初始化数据和适配器
        itemList = new ArrayList<>();
        adapter = new DiscoverAdapter(getContext(), itemList);
        recyclerView.setAdapter(adapter);

        loadItems(); // 加载数据的方法

        // 根据列表是否有数据来控制占位符的显隐
        updatePlaceholderVisibility();

        return view;
    }

    private void loadItems() {
        // 在这里添加图文列表的数据
        // 使用我们之前创建的 placeholder drawables
        itemList.add(new DiscoveryItem(R.drawable.placeholder_image_1, "朋友圈"));
        itemList.add(new DiscoveryItem(R.drawable.placeholder_image_2, "视频号"));
        itemList.add(new DiscoveryItem(R.drawable.placeholder_image_1, "扫一扫"));
        itemList.add(new DiscoveryItem(R.drawable.placeholder_image_2, "摇一摇"));
        itemList.add(new DiscoveryItem(R.drawable.placeholder_image_1, "看一看"));
        itemList.add(new DiscoveryItem(R.drawable.placeholder_image_2, "搜一搜"));
        itemList.add(new DiscoveryItem(R.drawable.placeholder_image_1, "直播"));
        itemList.add(new DiscoveryItem(R.drawable.placeholder_image_2, "附近"));
        itemList.add(new DiscoveryItem(R.drawable.placeholder_image_1, "购物"));
        itemList.add(new DiscoveryItem(R.drawable.placeholder_image_2, "游戏"));
        itemList.add(new DiscoveryItem(R.drawable.placeholder_image_1, "小程序"));


        // 模拟更多数据
        for (int i = 1; i <= 5; i++) {
            if (i % 2 == 0) {
                itemList.add(new DiscoveryItem(R.drawable.placeholder_image_2, "示例项目 " + i));
            } else {
                itemList.add(new DiscoveryItem(R.drawable.placeholder_image_1, "示例项目 " + i));
            }
        }

        adapter.notifyDataSetChanged(); // 通知适配器数据已更改
        updatePlaceholderVisibility(); // 更新占位符的可见性
    }

    private void updatePlaceholderVisibility() {
        if (itemList == null || itemList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            placeholderText.setVisibility(View.VISIBLE);
            placeholderText.setText(R.string.hello_discover_fragment); // 或者 "列表为空"
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            placeholderText.setVisibility(View.GONE);
        }
    }
}