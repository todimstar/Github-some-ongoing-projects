package com.liu.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // <-- 添加 Toolbar 导入
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView textViewWelcome;
    private TextView textViewContentDisplay;
    private Button buttonDial, buttonSms, buttonBrowser;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // 如果您不使用ConstraintLayout的main ID进行insets处理，可以注释或移除
        setContentView(R.layout.activity_main);

        // 设置 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar); // <-- 获取 Toolbar
        setSupportActionBar(toolbar); // <-- 将 Toolbar 设置为 ActionBar

        // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        //     Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        //     v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        //     return insets;
        // }); // 这段代码是针对特定根布局ID "main" 的，我们的新布局是RelativeLayout，所以注释掉

        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewContentDisplay = findViewById(R.id.textViewContentDisplay);
        buttonDial = findViewById(R.id.buttonDial);
        buttonSms = findViewById(R.id.buttonSms);
        buttonBrowser = findViewById(R.id.buttonBrowser);

        // 获取从LoginActivity传递过来的用户名
        String username = getIntent().getStringExtra("USERNAME_EXTRA");
        if (username != null && !username.isEmpty()) {
            textViewWelcome.setText(String.format(getString(R.string.welcome_message), username));
        } else {
            textViewWelcome.setText(String.format(getString(R.string.welcome_message), "Guest")); // 默认访客
        }

        // 底部导航按钮点击事件
        buttonDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                // dialIntent.setData(Uri.parse("tel:10086")); // 可以预设号码
                startActivity(dialIntent);
            }
        });

        buttonSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.setData(Uri.parse("smsto:")); // 打开短信应用
                // smsIntent.putExtra("sms_body", "你好"); // 可以预设短信内容
                // smsIntent.setData(Uri.parse("smsto:10086")); // 可以预设收信人
                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    Toast.makeText(MainActivity.this, R.string.sms_not_found, Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
                startActivity(browserIntent);
            }
        });
    }

    // 创建选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // 处理选项菜单点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_switch_content1) {
            textViewContentDisplay.setText("这是内容1的详细信息。");
            Toast.makeText(this, R.string.toast_content1_switched, Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_switch_content2) {
            textViewContentDisplay.setText("这是内容2的详细信息，通过菜单切换。");
            Toast.makeText(this, R.string.toast_content2_switched, Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_open_new_page) {
            Toast.makeText(this, R.string.toast_opening_new_page, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, NewPageActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_open_camera) {
            Toast.makeText(this, R.string.toast_opening_camera, Toast.LENGTH_SHORT).show();
            openCamera();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // 已有权限，直接打开相机
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } else {
                 Toast.makeText(this, R.string.camera_not_found, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予
                openCamera(); // 再次尝试打开相机
            } else {
                // 权限被拒绝
                Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // 处理相机返回的结果，例如显示拍摄的照片
            // Bundle extras = data.getExtras();
            // Bitmap imageBitmap = (Bitmap) extras.get("data"); // 这通常返回缩略图
            // imageView.setImageBitmap(imageBitmap); // 如果有ImageView来显示
            Toast.makeText(this, R.string.photo_taken, Toast.LENGTH_SHORT).show();
        }
    }
}