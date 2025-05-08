package com.liu.weixinfragment.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar; // 导入 Toolbar
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liu.weixinfragment.R;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private List<ChatItem> chatItemList;
    private TextView placeholderText;
    private Toolbar toolbarChat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        toolbarChat = view.findViewById(R.id.toolbar_chat);
        recyclerViewChat = view.findViewById(R.id.recycler_view_chat);
        placeholderText = view.findViewById(R.id.text_chat_placeholder);

        setupToolbar(); // 设置 Toolbar

        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext()));
        chatItemList = new ArrayList<>();
        chatAdapter = new ChatAdapter(getContext(), chatItemList);
        recyclerViewChat.setAdapter(chatAdapter);

        loadChatItems();
        updatePlaceholderVisibility();

        return view;
    }

    private void setupToolbar() {
        toolbarChat.setTitle("微信"); // 设置标题
        // 如果你的主题中 Toolbar 的标题颜色不是白色，可以在这里设置
        // toolbarChat.setTitleTextColor(getResources().getColor(android.R.color.white, null));
        toolbarChat.inflateMenu(R.menu.chat_fragment_menu); // 加载菜单
        toolbarChat.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_search) {
                Toast.makeText(getContext(), "搜索功能待实现", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.action_add) {
                Toast.makeText(getContext(), "添加功能待实现", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void loadChatItems() {
        // 添加示例聊天数据
        chatItemList.add(new ChatItem(R.drawable.placeholder_image_1, "文件传输助手", "好的，文件已收到。", "昨天"));
        chatItemList.add(new ChatItem(R.drawable.placeholder_image_2, "微信团队", "[应用通知] 欢迎使用微信！", "星期一"));
        chatItemList.add(new ChatItem(R.drawable.placeholder_image_1, "张三", "在吗？晚上一起吃饭？", "15:30"));
        chatItemList.add(new ChatItem(R.drawable.placeholder_image_2, "李四", "哈哈，太有趣了！", "14:05"));
        chatItemList.add(new ChatItem(R.drawable.placeholder_image_1, "王五", "[语音]", "13:12"));
        chatItemList.add(new ChatItem(R.drawable.placeholder_image_2, "赵六", "图片已发送，请查收。", "10:55"));
        chatItemList.add(new ChatItem(R.drawable.placeholder_image_1, "家庭群(5)", "小明：@所有人 周末聚餐通知", "09:20"));

        for (int i = 1; i <= 10; i++) {
             chatItemList.add(new ChatItem(
                i % 2 == 0 ? R.drawable.placeholder_image_2 : R.drawable.placeholder_image_1,
                "好友 " + i,
                "这是一条测试消息 " + i,
                "10:" + String.format("%02d", i*2)
        ));
        }

        chatAdapter.notifyDataSetChanged();
        updatePlaceholderVisibility();
    }

    private void updatePlaceholderVisibility() {
        if (chatItemList == null || chatItemList.isEmpty()) {
            recyclerViewChat.setVisibility(View.GONE);
            placeholderText.setVisibility(View.VISIBLE);
        } else {
            recyclerViewChat.setVisibility(View.VISIBLE);
            placeholderText.setVisibility(View.GONE);
        }
    }
}