package com.liu.weixinfragment;

import android.os.Bundle;
import android.view.MenuItem; // 新增导入

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull; // 新增导入
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment; // 新增导入
import androidx.fragment.app.FragmentManager; // 新增导入
import androidx.fragment.app.FragmentTransaction; // 新增导入

// 确保这些导入路径与你创建的 Fragment 类的实际路径一致
import com.liu.weixinfragment.ui.chat.ChatFragment;
import com.liu.weixinfragment.ui.contacts.ContactsFragment;
import com.liu.weixinfragment.ui.discover.DiscoverFragment;
import com.liu.weixinfragment.ui.me.MeFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView; // 新增导入
import com.google.android.material.navigation.NavigationBarView; // 新增导入

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    // Fragment 实例
    private ChatFragment chatFragment;
    private ContactsFragment contactsFragment;
    private DiscoverFragment discoverFragment;
    private MeFragment meFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // 这行可以保留，也可以根据 UI 需求调整或移除
        setContentView(R.layout.activity_main);

        // 原有的 WindowInsetsListener 代码，如果 BottomNavigationView 和 FrameLayout 占据全屏，
        // 并且你希望内容在系统栏后面，可以保留。否则，如果 BottomNavigationView 在系统栏下方，
        // 这部分可能需要调整或移除，以避免 BottomNavigationView 被遮挡或产生额外边距。
        // 为了简单起见，我们暂时注释掉它，后续可以根据 UI 效果调整。
        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        */

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 初始化 Fragments (确保在后续步骤中创建这些 Fragment 类)
        chatFragment = new ChatFragment();
        contactsFragment = new ContactsFragment();
        discoverFragment = new DiscoverFragment();
        meFragment = new MeFragment();

        // 设置默认显示的 Fragment
        if (savedInstanceState == null) {
            currentFragment = chatFragment; // 默认显示聊天 Fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, currentFragment, ChatFragment.class.getSimpleName())
                    .commit();
        } else {
            // 处理配置更改（例如屏幕旋转）后的 Fragment 恢复
            currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            // 如果需要更精细的恢复逻辑，可以保存和恢复 Fragment 的 tag 或状态
        }


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                String tag = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_chat) {
                    selectedFragment = chatFragment;
                    tag = ChatFragment.class.getSimpleName();
                } else if (itemId == R.id.nav_contacts) {
                    selectedFragment = contactsFragment;
                    tag = ContactsFragment.class.getSimpleName();
                } else if (itemId == R.id.nav_discover) {
                    selectedFragment = discoverFragment;
                    tag = DiscoverFragment.class.getSimpleName();
                } else if (itemId == R.id.nav_me) {
                    selectedFragment = meFragment;
                    tag = MeFragment.class.getSimpleName();
                }

                if (selectedFragment != null && selectedFragment != currentFragment) {
                    switchFragment(selectedFragment, tag);
                    return true;
                }
                return false;
            }
        });

        // 可选: 设置初始选中项 (如果希望启动时选中某个特定项，而不是依赖于 savedInstanceState)
        // 如果 savedInstanceState 为 null，我们已经在上面设置了默认 Fragment，
        // BottomNavigationView 应该会自动同步选中状态，如果菜单项 ID 和 Fragment 对应良好。
        // 如果需要强制选中，可以这样做：
        // if (savedInstanceState == null) {
        //     bottomNavigationView.setSelectedItemId(R.id.nav_chat);
        // }
    }

    private void switchFragment(Fragment targetFragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 尝试通过 tag 查找 Fragment，看是否已经添加过
        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);

        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        if (existingFragment == null) {
            // 如果 Fragment 不存在，则添加它
            transaction.add(R.id.fragment_container, targetFragment, tag);
        } else {
            // 如果 Fragment 已存在，则显示它
            transaction.show(targetFragment);
        }
        currentFragment = targetFragment;
        transaction.commit();
    }
}