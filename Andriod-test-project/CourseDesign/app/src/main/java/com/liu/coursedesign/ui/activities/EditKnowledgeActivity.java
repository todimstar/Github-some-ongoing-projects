package com.liu.coursedesign.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

// Material Design导入 (Material Design Imports)
import com.google.android.material.textfield.TextInputEditText;

// ✅ Glide库导入 (Glide Library Imports)
// 注意：确保这些导入语句能正确识别
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.google.android.material.textfield.TextInputEditText;
import com.liu.coursedesign.Dao.KnowledgeDao;
import com.liu.coursedesign.R;
import com.liu.coursedesign.database.AppDatabase;
import com.liu.coursedesign.model.Knowledge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 编辑知识页面Activity
 * 对应布局文件: activity_edit_knowledge.xml
 */
public class EditKnowledgeActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
    private TextInputEditText etTitle, etCategory, etDescription;// 三个输入框
    private ImageView ivPreview;
    private Button btnSelectImage, btnRemoveImage, btnCancel, btnSave;
    
    private KnowledgeDao knowledgeDao;
    private String mode; // "add" 或 "edit"
    private Knowledge currentKnowledge;
    private String selectedImagePath; // 存储的真实文件路径
    private Uri selectedImageUri; //url处理图片

    private ExecutorService singleExecutor;

    // 现代化权限请求 
    private ActivityResultLauncher<String> imagePickerLauncher;
    private ActivityResultLauncher<String[]> permissionLauncher;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_knowledge);
        
        initViews();
        initBusinessLogic();
        initImagePickerLauncher(); // 初始化图片选择器
        setupClickListeners();
        loadKnowledgeData();

    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etTitle = findViewById(R.id.etTitle);
        etCategory = findViewById(R.id.etCategory);
        etDescription = findViewById(R.id.etDescription);
        ivPreview = findViewById(R.id.ivPreview);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnRemoveImage = findViewById(R.id.btnRemoveImage);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    private void initBusinessLogic() {
        AppDatabase db = AppDatabase.getDatabase(this);
        knowledgeDao = db.knowledgeDao();
        singleExecutor = Executors.newSingleThreadExecutor();
        
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");

        if ("edit".equals(mode)) {
            int knowledgeId = intent.getIntExtra("knowledge_id", -1);
            if (knowledgeId != -1) {
                loadKnowledgeFromDatabase(knowledgeId);//专门处理数据库搜索
            }
        }
        
        // 设置标题 
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("add".equals(mode) ? "添加知识" : "编辑知识");
        }
    }

    /**
     * ✅ 初始化现代化图片选择器 (Modern Image Picker Initialization)
     * 
     * 原理解释：
     * - ActivityResultLauncher = Activity (活动) + Result (结果) + Launcher (启动器)
     * - 作用：替代已废弃的startActivityForResult()方法
     * - 优势：类型安全、内存泄漏防护、生命周期感知
     */
    private void initImagePickerLauncher() {
        // ✅ 图片选择启动器 - Image Selection Launcher
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    handleImageSelection(result);
                }
            }
        );
        
        // ✅ 权限请求启动器 - Permission Request Launcher  
        permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            permissions -> {
                boolean allGranted = true;
                for (Boolean granted : permissions.values()) {
                    if (!granted) {
                        allGranted = false;
                        break;
                    }
                }
                
                if (allGranted) {
                    openImagePicker();
                } else {
                    Toast.makeText(this, "需要存储权限才能选择图片", Toast.LENGTH_LONG).show();
                }
            }
        );
    }
    
    /**
     * ✅ 处理图片选择结果 (Handle Image Selection Result)
     * 
     * 原理解释：
     * - 步骤1：将Content URI转换为真实文件路径
     * - 步骤2：复制图片到应用私有目录
     * - 步骤3：显示图片预览
     * - 步骤4：更新selectedImagePath变量
     */
    private void handleImageSelection(Uri imageUri) {
        try {
            // ✅ 显示加载状态 - Show Loading State
            btnSelectImage.setEnabled(false);
            btnSelectImage.setText("处理中...");
            
            // ✅ 在后台线程处理图片 - Process Image in Background Thread
            singleExecutor.execute(() -> {
                try {
                    // 步骤1：复制图片到应用私有目录 - Copy Image to Private Directory
                    String savedPath = saveImageToPrivateStorage(imageUri);
                    
                    // 步骤2：在主线程更新UI - Update UI on Main Thread
                    runOnUiThread(() -> {
                        if (savedPath != null) {
                            selectedImagePath = savedPath;
                            selectedImageUri = imageUri;
                            
                            // ✅ 使用Glide加载图片 - Load Image with Glide
                            loadImageWithGlide(savedPath);
                            
                            Toast.makeText(this, "图片选择成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "图片处理失败", Toast.LENGTH_SHORT).show();
                        }
                        
                        // 恢复按钮状态 - Restore Button State
                        btnSelectImage.setEnabled(true);
                        btnSelectImage.setText("选择图片");
                    });
                    
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "图片处理出错：" + e.getMessage(), 
                            Toast.LENGTH_LONG).show();
                        btnSelectImage.setEnabled(true);
                        btnSelectImage.setText("选择图片");
                    });
                }
            });
            
        } catch (Exception e) {
            Toast.makeText(this, "图片选择失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * ✅ 保存图片到应用私有存储 (Save Image to Private Storage)
     * 
     * 原理解释：
     * - Private Storage = Private (私有) + Storage (存储) = 应用私有存储
     * - 作用：将外部图片复制到应用控制的目录中
     * - 优势：不需要额外权限、应用卸载时自动清理
     */
    private String saveImageToPrivateStorage(Uri sourceUri) {
        try {
            // 创建私有图片目录 - Create Private Image Directory
            File imageDir = new File(getFilesDir(), "images");
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            
            // 生成唯一文件名 - Generate Unique File Name
            String fileName = "img_" + System.currentTimeMillis() + ".jpg";
            File targetFile = new File(imageDir, fileName);
            
            // 复制图片文件 - Copy Image File
            try (InputStream inputStream = getContentResolver().openInputStream(sourceUri);
                 FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                
                if (inputStream == null) {
                    return null;
                }
                
                // 逐字节复制 - Copy Byte by Byte
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                
                outputStream.flush();
                
                Log.d("EditKnowledge", "图片保存成功: " + targetFile.getAbsolutePath());
                return targetFile.getAbsolutePath();
                
            }
            
        } catch (Exception e) {
            Log.e("EditKnowledge", "保存图片失败", e);
            return null;
        }
    }
    
    /**
     * ✅ 使用Glide加载图片 (Load Image with Glide)
     * 
     * 原理解释：
     * - Glide = 谷歌推荐的图片加载库
     * - 功能：异步加载、内存缓存、自动缩放、错误处理
     * - 优势：性能好、内存安全、使用简单
     */
    private void loadImageWithGlide(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            
            if (imageFile.exists()) {
                // ✅ 使用Glide的现代化配置 (Modern Glide Configuration)
                RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.image_placeholder)     // 加载中显示
                    .error(R.drawable.image_placeholder)           // 加载失败显示
                    .centerCrop()                                  // 居中裁剪
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC); // 自动缓存策略
                
                Glide.with(this)                    // Context绑定
                    .load(imageFile)                // 加载文件
                    .apply(options)                 // 应用配置
                    .into(ivPreview);               // 显示到ImageView
                
                Log.d("EditKnowledge", "Glide加载图片成功: " + imagePath);
                
            } else {
                // ✅ 文件不存在时显示占位符
                ivPreview.setImageResource(R.drawable.image_placeholder);
                Log.w("EditKnowledge", "图片文件不存在: " + imagePath);
            }
            
        } catch (Exception e) {
            Log.e("EditKnowledge", "Glide加载图片失败", e);
            ivPreview.setImageResource(R.drawable.image_placeholder);
            
            // ✅ 降级到原生方法 (Fallback to Native Method)
            Toast.makeText(this, "Glide加载失败，使用备用方法", Toast.LENGTH_SHORT).show();
            loadImageWithoutGlide(imagePath);
        }
    }
    
    /**
     * ✅ 备用图片加载方法 - 不使用Glide (Fallback Image Loading - Without Glide)
     * 
     * 原理解释：
     * - 适用于不想引入Glide库的情况
     * - 使用BitmapFactory直接解码图片
     * - 包含内存优化和错误处理
     */
    private void loadImageWithoutGlide(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            
            if (imageFile.exists()) {
                // ✅ 优化版图片加载 - Optimized Image Loading
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true; // 先获取图片尺寸
                BitmapFactory.decodeFile(imagePath, options);
                
                // 计算缩放比例 - Calculate Sample Size
                int targetWidth = ivPreview.getWidth();
                int targetHeight = ivPreview.getHeight();
                if (targetWidth <= 0) targetWidth = 500; // 默认值
                if (targetHeight <= 0) targetHeight = 500; // 默认值
                
                options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);
                options.inJustDecodeBounds = false; // 真正解码图片
                
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                if (bitmap != null) {
                    ivPreview.setImageBitmap(bitmap);
                    Log.d("EditKnowledge", "直接加载图片成功: " + imagePath);
                } else {
                    ivPreview.setImageResource(R.drawable.image_placeholder);
                    Log.w("EditKnowledge", "图片解码失败: " + imagePath);
                }
                
            } else {
                ivPreview.setImageResource(R.drawable.image_placeholder);
                Log.w("EditKnowledge", "图片文件不存在: " + imagePath);
            }
            
        } catch (Exception e) {
            Log.e("EditKnowledge", "加载图片失败", e);
            ivPreview.setImageResource(R.drawable.image_placeholder);
        }
    }
    
    /**
     * ✅ 计算图片缩放比例 (Calculate Image Sample Size)
     * 
     * 原理解释：
     * - Sample Size = Sample (采样) + Size (大小) = 采样大小
     * - 作用：避免大图片导致OOM (Out Of Memory) 内存溢出
     * - 算法：找到合适的2的幂次方缩放比例
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            
            // 计算最大的inSampleSize值（2的幂次方）
            while ((halfHeight / inSampleSize) >= reqHeight && 
                   (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        
        return inSampleSize;
    }
    
    private void setupClickListeners() {
        btnSelectImage.setOnClickListener(v -> {
            checkPermissionAndSelectImage();
        });
        btnRemoveImage.setOnClickListener(v -> removeImage());
        btnCancel.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveKnowledge());
        
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * ✅ 检查权限并选择图片 (Check Permission and Select Image)
     * 
     * 原理解释：
     * - Android 13+ 使用分区权限系统
     * - READ_MEDIA_IMAGES 专门用于读取图片
     * - READ_EXTERNAL_STORAGE 用于Android 12及以下
     */
    private void checkPermissionAndSelectImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (API 33+) 使用新权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                
                permissionLauncher.launch(new String[]{Manifest.permission.READ_MEDIA_IMAGES});
            } else {
                openImagePicker();
            }
        } else {
            // Android 12及以下使用旧权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
                
                permissionLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            } else {
                openImagePicker();
            }
        }
    }
    
    /**
     * ✅ 打开图片选择器 (Open Image Picker)
     */
    private void openImagePicker() {
        imagePickerLauncher.launch("image/*");
    }
    
    private void removeImage() {
        selectedImagePath = null;
        selectedImageUri = null;
        ivPreview.setImageResource(R.drawable.image_placeholder);
        Toast.makeText(this, "已移除图片", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * ✅ 从数据库加载知识数据 (Load Knowledge Data from Database)
     */
    private void loadKnowledgeFromDatabase(int knowledgeId) {
        singleExecutor.execute(() -> {
            try {
                Knowledge knowledge = knowledgeDao.getKnowledgeById(knowledgeId);
                
                runOnUiThread(() -> {
                    if (knowledge != null) {
                        currentKnowledge = knowledge;
                        loadKnowledgeData();
                    } else {
                        Toast.makeText(this, "知识条目不存在", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "加载失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        });
    }
    
    /**
     * ✅ 加载知识数据到界面
     */
    private void loadKnowledgeData() {
        if ("edit".equals(mode) && currentKnowledge != null) {
            etTitle.setText(currentKnowledge.getTitle());
            etCategory.setText(currentKnowledge.getCategory());
            etDescription.setText(currentKnowledge.getDescription());
            selectedImagePath = currentKnowledge.getImagePath();
            
            // 加载图片预览
            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                // 使用图片加载库加载图片
                ivPreview.setImageResource(R.drawable.image_placeholder);
            }
        }
    }
    
    private void saveKnowledge() {
        String title = etTitle.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        
        if (title.isEmpty() || category.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // 禁用保存按钮防止重复点击
        btnSave.setEnabled(false);
        btnSave.setText("保存中...");
        
        singleExecutor.execute(() -> {
            try {
                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .format(new Date());
                
                if ("add".equals(mode)) {
                    // 添加新知识
                    Knowledge newKnowledge = new Knowledge();
                    newKnowledge.setTitle(title);
                    newKnowledge.setCategory(category);
                    newKnowledge.setDescription(description);
                    newKnowledge.setImagePath(selectedImagePath);
                    newKnowledge.setCreateTime(currentTime);
                    newKnowledge.setUpdateTime(currentTime);
                    
                    long result = knowledgeDao.add(newKnowledge);
                    
                    runOnUiThread(() -> {
                        if (result > 0) {
                            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
                            btnSave.setEnabled(true);
                            btnSave.setText("保存");
                        }
                    });
                    
                } else {
                    // 更新现有知识
                    currentKnowledge.setTitle(title);
                    currentKnowledge.setCategory(category);
                    currentKnowledge.setDescription(description);
                    currentKnowledge.setImagePath(selectedImagePath);
                    currentKnowledge.setUpdateTime(currentTime);
                    
                    int result = knowledgeDao.update(currentKnowledge);
                    
                    runOnUiThread(() -> {
                        if (result > 0) {
                            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                            btnSave.setEnabled(true);
                            btnSave.setText("保存");
                        }
                    });
                }
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "保存失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                    btnSave.setEnabled(true);
                    btnSave.setText("保存");
                });
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (singleExecutor != null && !singleExecutor.isShutdown()) {
            singleExecutor.shutdown();
        }
    }
}
