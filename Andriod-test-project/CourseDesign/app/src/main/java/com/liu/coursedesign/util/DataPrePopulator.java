package com.liu.coursedesign.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.liu.coursedesign.Dao.KnowledgeDao;
import com.liu.coursedesign.R;
import com.liu.coursedesign.model.Knowledge;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 数据预填充管理器 (Data Pre-population Manager)
 * 
 * 原理解释：
 * - Pre-population = Pre (预先) + population (填充) = 预先填充
 * - 作用：在应用首次启动时自动创建示例知识内容
 * - 时机：检查数据库是否为空，如果为空则填充初始数据
 * - 好处：新用户安装后立即看到丰富内容，不会看到空白页面
 */
public class DataPrePopulator {
    
    private static final String TAG = "DataPrePopulator";
    private static final String PREFS_NAME = "app_initialization";
    private static final String KEY_DATA_POPULATED = "data_populated";
    
    private Context context;
    private KnowledgeDao knowledgeDao;
    private ExecutorService executorService;

    //默认图片路径
    private static final String IMAGE_PATH_JAVA = "android.resource://%s/" + R.drawable.img_java;
    private static final String IMAGE_PATH_PYTHON = "android.resource://%s/" + R.drawable.img_python;
    private static final String IMAGE_PATH_C = "android.resource://%s/" + R.drawable.img_c;
    private static final String IMAGE_PATH_DEFAULT = "android.resource://%s/" + R.drawable.img_default;
    
    /**
     * 构造函数 (Constructor)
     * 
     * 原理解释：
     * - Constructor = Construct (构造) + or (者) = 构造器
     * - 作用：初始化对象时设置必要的依赖项
     * - 参数：Context用于访问SharedPreferences，KnowledgeDao用于数据库操作
     */
    public DataPrePopulator(Context context, KnowledgeDao knowledgeDao) {
        this.context = context;
        this.knowledgeDao = knowledgeDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }
    
    /**
     * 检查并填充初始数据 (Check and Populate Initial Data)
     * 
     * 原理解释：
     * - 步骤1：检查是否已经填充过数据
     * - 步骤2：检查数据库是否为空
     * - 步骤3：如果需要，则填充示例数据
     * - 步骤4：标记填充完成状态
     */
    public void checkAndPopulateData() {
        executorService.execute(() -> {
            try {
                // 检查是否已填充过数据 - Check if data has been populated
                SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                boolean isDataPopulated = prefs.getBoolean(KEY_DATA_POPULATED, false);
                
                if (isDataPopulated) {
                    Log.d(TAG, "数据已填充过，跳过初始化 - Data already populated, skipping initialization");
                    return;
                }
                
                // 检查数据库是否为空 - Check if database is empty
                List<Knowledge> existingKnowledge = knowledgeDao.getAllKnowledge();
                if (existingKnowledge != null && !existingKnowledge.isEmpty()) {
                    Log.d(TAG, "数据库已有数据，跳过填充 - Database has existing data, skipping population");
                    markDataAsPopulated();
                    return;
                }
                
                Log.d(TAG, "开始填充初始知识数据 - Starting initial knowledge data population");
                
                // 填充示例数据 - Populate sample data
                populateSampleKnowledge();
                
                // 标记数据已填充 - Mark data as populated
                markDataAsPopulated();
                
                Log.d(TAG, "初始数据填充完成 - Initial data population completed");
                
            } catch (Exception e) {
                Log.e(TAG, "数据填充过程出错 - Error during data population", e);
            }
        });
    }
    
    /**
     * 填充示例知识数据 (Populate Sample Knowledge Data)
     * 
     * 原理解释：
     * - Sample Data = Sample (示例) + Data (数据) = 示例数据
     * - 作用：创建各种编程概念的知识卡片
     * - 分类：Java基础、Python入门、C语言基础、编程概念等
     */
    private void populateSampleKnowledge() {
        List<Knowledge> sampleKnowledgeList = createSampleKnowledgeList();
        
        // 批量插入数据 - Batch insert data
        for (Knowledge knowledge : sampleKnowledgeList) {
            try {
                long result = knowledgeDao.add(knowledge);
                if (result > 0) {
                    Log.d(TAG, "成功添加知识: " + knowledge.getTitle());
                } else {
                    Log.w(TAG, "添加知识失败: " + knowledge.getTitle());
                }
            } catch (Exception e) {
                Log.e(TAG, "添加知识时出错: " + knowledge.getTitle(), e);
            }
        }
    }
    
    /**
     * 创建示例知识列表 (Create Sample Knowledge List)
     * 
     * 原理解释：
     * - 作用：定义各种编程学习路线的知识点
     * - 结构：每个知识点包含标题、描述、分类、时间等信息
     * - 教学性：内容面向初学者，循序渐进
     */
    private List<Knowledge> createSampleKnowledgeList() {
        List<Knowledge> knowledgeList = new ArrayList<>();
        String currentTime = getCurrentTime();
        
        // Java学习路线知识点 - Java Learning Path Knowledge Points
        knowledgeList.addAll(createJavaKnowledge(currentTime));
        
        // Python学习路线知识点 - Python Learning Path Knowledge Points
        knowledgeList.addAll(createPythonKnowledge(currentTime));
        
        // C语言学习路线知识点 - C Language Learning Path Knowledge Points
        knowledgeList.addAll(createCKnowledge(currentTime));
        
        // 编程基础概念知识点 - Programming Basic Concepts Knowledge Points
        knowledgeList.addAll(createProgrammingConcepts(currentTime));

        getImagePathForCategory(knowledgeList);

        return knowledgeList;
    }

    // 遍历填充知识点的图片路径
    private void getImagePathForCategory(List<Knowledge> knowledgeList) {
        String imagePathFormat;
        try {
            for (Knowledge k : knowledgeList) {
                if (k.category.contains("Java")) {
                    imagePathFormat = IMAGE_PATH_JAVA;
                } else if (k.category.contains("Python")) {
                    imagePathFormat = IMAGE_PATH_PYTHON;
                } else if (k.category.contains("C语言")) {
                    imagePathFormat = IMAGE_PATH_C;
                } else {
                    imagePathFormat = IMAGE_PATH_DEFAULT;
                }
                k.imagePath = String.format(imagePathFormat, context.getPackageName());
            }
            // 最后，用真实的包名填充占位符并返回
        }catch(Exception e){
            Log.e("DataPrePopulator","添加图片路径有问题",e);
        }
        
    }
    
    /**
     * 创建Java学习路线知识点 (Create Java Learning Path Knowledge)
     * 
     * 原理解释：
     * - Learning Path = Learning (学习) + Path (路径) = 学习路线
     * - 作用：为Java初学者提供系统化的学习内容
     * - 顺序：从基础语法到面向对象，再到高级特性
     */
    private List<Knowledge> createJavaKnowledge(String currentTime) {
        List<Knowledge> javaKnowledge = new ArrayList<>();
        
        // Java基础语法 - Java Basic Syntax
        javaKnowledge.add(new Knowledge(
            "Java HelloWorld程序",
            "Java程序的入门第一步！HelloWorld程序展示了Java的基本结构。\n\n" +
            "代码示例：\n" +
            "public class HelloWorld {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"Hello, World!\");\n" +
            "    }\n" +
            "}\n\n" +
            "关键概念：\n" +
            "• public：公共访问修饰符\n" +
            "• class：定义类的关键字\n" +
            "• main方法：程序入口点\n" +
            "• System.out.println：输出语句",
            null, // 暂时不设置图片路径
            "Java基础",
            currentTime,
            currentTime
        ));
        
        // Java变量和数据类型 - Java Variables and Data Types
        javaKnowledge.add(new Knowledge(
            "Java变量和数据类型",
            "变量是存储数据的容器，Java有多种数据类型。\n\n" +
            "基本数据类型：\n" +
            "• int：整数类型，如 int age = 25;\n" +
            "• double：小数类型，如 double price = 99.99;\n" +
            "• boolean：布尔类型，如 boolean isStudent = true;\n" +
            "• char：字符类型，如 char grade = 'A';\n" +
            "• String：字符串类型，如 String name = \"张三\";\n\n" +
            "变量命名规则：\n" +
            "• 必须以字母、下划线或$开头\n" +
            "• 不能使用Java关键字\n" +
            "• 建议使用驼峰命名法",
            null,
            "Java基础",
            currentTime,
            currentTime
        ));
        
        // Java条件语句 - Java Conditional Statements
        javaKnowledge.add(new Knowledge(
            "Java条件语句 (if-else)",
            "条件语句用于根据不同条件执行不同的代码块。\n\n" +
            "基本语法：\n" +
            "if (条件) {\n" +
            "    // 条件为真时执行\n" +
            "} else if (其他条件) {\n" +
            "    // 其他条件为真时执行\n" +
            "} else {\n" +
            "    // 所有条件都为假时执行\n" +
            "}\n\n" +
            "实例：\n" +
            "int score = 85;\n" +
            "if (score >= 90) {\n" +
            "    System.out.println(\"优秀\");\n" +
            "} else if (score >= 80) {\n" +
            "    System.out.println(\"良好\");\n" +
            "} else {\n" +
            "    System.out.println(\"需要努力\");\n" +
            "}",
            null,
            "Java基础",
            currentTime,
            currentTime
        ));
        
        // Java循环语句 - Java Loop Statements
        javaKnowledge.add(new Knowledge(
            "Java循环语句详解",
            "循环用于重复执行代码块，Java提供三种主要循环。\n\n" +
            "1. for循环 - 已知循环次数：\n" +
            "for (int i = 0; i < 5; i++) {\n" +
            "    System.out.println(\"第\" + (i+1) + \"次循环\");\n" +
            "}\n\n" +
            "2. while循环 - 条件循环：\n" +
            "int count = 0;\n" +
            "while (count < 3) {\n" +
            "    System.out.println(\"计数：\" + count);\n" +
            "    count++;\n" +
            "}\n\n" +
            "3. do-while循环 - 至少执行一次：\n" +
            "int num = 0;\n" +
            "do {\n" +
            "    System.out.println(\"数字：\" + num);\n" +
            "    num++;\n" +
            "} while (num < 2);",
            null,
            "Java基础",
            currentTime,
            currentTime
        ));
        
        // Java面向对象 - Java Object-Oriented Programming
        javaKnowledge.add(new Knowledge(
            "Java面向对象编程基础",
            "面向对象是Java的核心特性，包含类、对象、封装、继承、多态。\n\n" +
            "类和对象：\n" +
            "public class Student {\n" +
            "    private String name;    // 私有属性\n" +
            "    private int age;\n" +
            "    \n" +
            "    // 构造方法\n" +
            "    public Student(String name, int age) {\n" +
            "        this.name = name;\n" +
            "        this.age = age;\n" +
            "    }\n" +
            "    \n" +
            "    // getter和setter方法\n" +
            "    public String getName() { return name; }\n" +
            "    public void setName(String name) { this.name = name; }\n" +
            "}\n\n" +
            "使用对象：\n" +
            "Student student = new Student(\"小明\", 20);",
            null,
            "Java进阶",
            currentTime,
            currentTime
        ));
        
        return javaKnowledge;
    }
    
    /**
     * 创建Python学习路线知识点 (Create Python Learning Path Knowledge)
     * 
     * 原理解释：
     * - 作用：为Python初学者提供入门指导
     * - 特点：Python语法简洁，适合编程入门
     * - 应用：数据分析、人工智能、Web开发等领域
     */
    private List<Knowledge> createPythonKnowledge(String currentTime) {
        List<Knowledge> pythonKnowledge = new ArrayList<>();
        
        // Python基础语法 - Python Basic Syntax
        pythonKnowledge.add(new Knowledge(
            "Python入门：Hello World",
            "Python是一门简洁优雅的编程语言，入门非常简单！\n\n" +
            "第一个Python程序：\n" +
            "print(\"Hello, World!\")\n" +
            "print(\"你好，Python！\")\n\n" +
            "Python的优势：\n" +
            "• 语法简洁清晰\n" +
            "• 学习曲线平缓\n" +
            "• 功能强大的标准库\n" +
            "• 活跃的社区支持\n" +
            "• 广泛的应用领域\n\n" +
            "应用领域：\n" +
            "• Web开发 (Django, Flask)\n" +
            "• 数据科学 (pandas, numpy)\n" +
            "• 人工智能 (TensorFlow, PyTorch)\n" +
            "• 自动化脚本",
            null,
            "Python入门",
            currentTime,
            currentTime
        ));
        
        // Python变量和数据类型 - Python Variables and Data Types
        pythonKnowledge.add(new Knowledge(
            "Python变量和数据类型",
            "Python的变量使用非常灵活，不需要声明类型。\n\n" +
            "基本数据类型：\n" +
            "# 整数\n" +
            "age = 25\n" +
            "# 浮点数\n" +
            "price = 99.99\n" +
            "# 字符串\n" +
            "name = \"小红\"\n" +
            "# 布尔值\n" +
            "is_student = True\n" +
            "# 列表\n" +
            "fruits = [\"苹果\", \"香蕉\", \"橙子\"]\n" +
            "# 字典\n" +
            "person = {\"姓名\": \"张三\", \"年龄\": 30}\n\n" +
            "Python特色：\n" +
            "• 动态类型：变量类型自动推断\n" +
            "• 强类型：类型安全\n" +
            "• 丰富的内置数据结构",
            null,
            "Python入门",
            currentTime,
            currentTime
        ));
        
        // Python函数 - Python Functions
        pythonKnowledge.add(new Knowledge(
            "Python函数定义和使用",
            "函数是组织代码的重要方式，Python函数定义简洁明了。\n\n" +
            "函数定义语法：\n" +
            "def greet(name, age=18):\n" +
            "    \"\"\"问候函数 - 文档字符串\"\"\"\n" +
            "    return f\"你好，{name}！你今年{age}岁。\"\n\n" +
            "# 调用函数\n" +
            "message1 = greet(\"小明\")\n" +
            "message2 = greet(\"小红\", 20)\n" +
            "print(message1)  # 你好，小明！你今年18岁。\n" +
            "print(message2)  # 你好，小红！你今年20岁。\n\n" +
            "函数特性：\n" +
            "• 默认参数：age=18\n" +
            "• 关键字参数：greet(age=25, name=\"李四\")\n" +
            "• 可变参数：*args, **kwargs\n" +
            "• 返回值：return语句",
            null,
            "Python进阶",
            currentTime,
            currentTime
        ));
        
        return pythonKnowledge;
    }
    
    /**
     * 创建C语言学习路线知识点 (Create C Language Learning Path Knowledge)
     * 
     * 原理解释：
     * - 作用：为C语言初学者提供基础指导
     * - 特点：C语言是系统编程的基础，接近底层
     * - 重要性：理解计算机工作原理的重要工具
     */
    private List<Knowledge> createCKnowledge(String currentTime) {
        List<Knowledge> cKnowledge = new ArrayList<>();
        
        // C语言基础 - C Language Basics
        cKnowledge.add(new Knowledge(
            "C语言程序结构",
            "C语言是系统编程的基础语言，了解程序结构很重要。\n\n" +
            "基本程序结构：\n" +
            "#include <stdio.h>  // 预处理指令\n\n" +
            "int main() {        // 主函数\n" +
            "    printf(\"Hello, World!\\n\");\n" +
            "    return 0;       // 返回状态码\n" +
            "}\n\n" +
            "程序组成部分：\n" +
            "• 预处理指令：#include包含头文件\n" +
            "• 主函数：程序执行入口点\n" +
            "• 变量声明：数据存储\n" +
            "• 函数调用：执行操作\n" +
            "• 返回语句：程序结束\n\n" +
            "编译过程：\n" +
            "源代码(.c) → 预处理 → 编译 → 链接 → 可执行文件",
            null,
            "C语言基础",
            currentTime,
            currentTime
        ));
        
        // C语言指针 - C Language Pointers
        cKnowledge.add(new Knowledge(
            "C语言指针概念",
            "指针是C语言的精髓，也是最具挑战性的概念。\n\n" +
            "指针基础：\n" +
            "int num = 42;      // 普通变量\n" +
            "int *ptr = &num;   // 指针变量，存储num的地址\n\n" +
            "printf(\"num的值: %d\\n\", num);        // 输出：42\n" +
            "printf(\"num的地址: %p\\n\", &num);     // 输出：地址\n" +
            "printf(\"ptr的值: %p\\n\", ptr);       // 输出：地址\n" +
            "printf(\"ptr指向的值: %d\\n\", *ptr);   // 输出：42\n\n" +
            "关键概念：\n" +
            "• &：取地址操作符\n" +
            "• *：解引用操作符\n" +
            "• 指针变量存储内存地址\n" +
            "• 通过指针可以间接访问数据\n\n" +
            "应用场景：\n" +
            "• 动态内存分配\n" +
            "• 函数参数传递\n" +
            "• 数据结构实现",
            null,
            "C语言进阶",
            currentTime,
            currentTime
        ));
        
        return cKnowledge;
    }
    
    /**
     * 创建编程基础概念知识点 (Create Programming Basic Concepts Knowledge)
     * 
     * 原理解释：
     * - 作用：提供跨语言的通用编程概念
     * - 重要性：这些概念适用于所有编程语言
     * - 目标：建立扎实的编程思维基础
     */
    private List<Knowledge> createProgrammingConcepts(String currentTime) {
        List<Knowledge> concepts = new ArrayList<>();
        
        // 算法基础 - Algorithm Basics
        concepts.add(new Knowledge(
            "算法和数据结构入门",
            "算法是解决问题的步骤，数据结构是组织数据的方式。\n\n" +
            "常见算法类型：\n" +
            "• 排序算法：冒泡排序、快速排序、归并排序\n" +
            "• 搜索算法：线性搜索、二分搜索\n" +
            "• 递归算法：分治思想的体现\n\n" +
            "基本数据结构：\n" +
            "• 数组：连续存储的相同类型数据\n" +
            "• 链表：通过指针连接的节点序列\n" +
            "• 栈：后进先出(LIFO)的数据结构\n" +
            "• 队列：先进先出(FIFO)的数据结构\n" +
            "• 树：层次化的数据结构\n" +
            "• 图：节点和边的集合\n\n" +
            "学习建议：\n" +
            "• 理解概念比记忆代码更重要\n" +
            "• 多做练习，培养算法思维\n" +
            "• 分析时间和空间复杂度",
            null,
            "编程概念",
            currentTime,
            currentTime
        ));
        
        // 软件工程概念 - Software Engineering Concepts
        concepts.add(new Knowledge(
            "软件开发生命周期",
            "了解软件是如何从想法变成产品的完整过程。\n\n" +
            "开发阶段：\n" +
            "1. 需求分析：理解用户需要什么\n" +
            "2. 系统设计：规划软件架构\n" +
            "3. 编码实现：编写程序代码\n" +
            "4. 测试验证：确保功能正确\n" +
            "5. 部署上线：发布给用户使用\n" +
            "6. 维护更新：修复问题和添加功能\n\n" +
            "开发方法论：\n" +
            "• 瀑布模型：按顺序进行各个阶段\n" +
            "• 敏捷开发：快速迭代，适应变化\n" +
            "• DevOps：开发与运维的协作\n\n" +
            "版本控制：\n" +
            "• Git：分布式版本控制系统\n" +
            "• 分支管理：并行开发不同功能\n" +
            "• 代码审查：提高代码质量",
            null,
            "编程概念",
            currentTime,
            currentTime
        ));
        
        // 数据库基础 - Database Basics
        concepts.add(new Knowledge(
            "数据库基础概念",
            "数据库是现代应用程序的重要组成部分。\n\n" +
            "关系型数据库：\n" +
            "• 表(Table)：存储数据的二维结构\n" +
            "• 行(Row)：表中的一条记录\n" +
            "• 列(Column)：表中的一个字段\n" +
            "• 主键(Primary Key)：唯一标识记录\n" +
            "• 外键(Foreign Key)：建立表之间的关系\n\n" +
            "SQL基础语句：\n" +
            "-- 查询数据\n" +
            "SELECT name, age FROM students WHERE age > 18;\n\n" +
            "-- 插入数据\n" +
            "INSERT INTO students (name, age) VALUES ('张三', 20);\n\n" +
            "-- 更新数据\n" +
            "UPDATE students SET age = 21 WHERE name = '张三';\n\n" +
            "-- 删除数据\n" +
            "DELETE FROM students WHERE age < 18;\n\n" +
            "数据库设计原则：\n" +
            "• 规范化：减少数据冗余\n" +
            "• 完整性：保证数据准确性\n" +
            "• 性能优化：建立适当索引",
            null,
            "编程概念",
            currentTime,
            currentTime
        ));
        
        return concepts;
    }
    
    /**
     * 获取当前时间字符串 (Get Current Time String)
     * 
     * 原理解释：
     * - SimpleDateFormat = Simple (简单) + Date (日期) + Format (格式) = 简单日期格式化
     * - 作用：将Date对象转换为指定格式的字符串
     * - 格式：yyyy-MM-dd HH:mm 表示年-月-日 时:分
     */
    private String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(new Date());
    }
    
    /**
     * 标记数据已填充 (Mark Data as Populated)
     * 
     * 原理解释：
     * - SharedPreferences = Shared (共享) + Preferences (偏好设置) = 共享偏好设置
     * - 作用：记录应用状态，避免重复填充数据
     * - 持久化：数据保存在设备上，应用重启后仍然有效
     */
    private void markDataAsPopulated() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
             .putBoolean(KEY_DATA_POPULATED, true)
             .apply(); // apply() = 异步保存，commit() = 同步保存
        
        Log.d(TAG, "已标记数据填充完成 - Marked data population as completed");
    }
    
    /**
     * 清理资源 (Clean up resources)
     * 
     * 原理解释：
     * - Resource Management = Resource (资源) + Management (管理) = 资源管理
     * - 作用：释放线程池资源，避免内存泄漏
     * - 时机：在Activity或Application销毁时调用
     */
    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            Log.d(TAG, "数据预填充器已清理 - DataPrePopulator cleaned up");
        }
    }
    
    /**
     * 重置数据填充状态 (Reset Data Population Status)
     * 
     * 原理解释：
     * - 作用：清除填充标记，下次启动时重新填充数据
     * - 用途：调试时或需要重新初始化数据时使用
     * - 注意：生产环境中谨慎使用
     */
    public void resetPopulationStatus() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
             .putBoolean(KEY_DATA_POPULATED, false)
             .apply();
        
        Log.d(TAG, "已重置数据填充状态 - Reset data population status");
    }
}