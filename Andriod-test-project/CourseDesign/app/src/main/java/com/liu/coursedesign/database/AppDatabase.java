package com.liu.coursedesign.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.liu.coursedesign.Dao.KnowledgeDao;
import com.liu.coursedesign.Dao.UserDao;
import com.liu.coursedesign.model.Knowledge;
import com.liu.coursedesign.model.User;

@Database(
    entities = {User.class, Knowledge.class},
    version = 5,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    // 各个Dao定义抽象方法，提供其他地方返回各个Dao实例的方法
    public abstract UserDao userDao();
    public abstract KnowledgeDao knowledgeDao();
    
    // static 静态配合volatile,确保整个应用只有一个数据库实例(instance)
    private static volatile AppDatabase INSTANCE;

    // 给外界获取数据库实例以操作student表
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {  //上锁保证线程安全
                if (INSTANCE == null) { //双重检查
                    //解释一下这行代码：Room.databaseBuilder()是Room提供的一个方法，用于创建数据库实例。
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),//getApplicationContext()获取应用上下文，避免内存泄漏
                            AppDatabase.class, 
                            "knowledege_app_database"
                        ).fallbackToDestructiveMigration() // 如果版本号不匹配，则销毁重建数据库
                        .addCallback(sRoomDatabaseCallback) //设定回调调用函数
                        .build();
                }
            }
        }
        return INSTANCE;
    }
    
    // 回调展示数据库创建状态
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("Database", "数据库首次创建完成");
        }
    };
}