package com.liu.coursedesign.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.liu.coursedesign.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    long insertAll(User user);// insert注解方法返回值只能void或long,long[],List<Long>

    @Insert
    long[] insertAll(User... users);

    @Delete
    int deleteAll(User[] users);// 返回值只能void或int(影响的行数)

    @Update
    int update(User user);// 返回值只能void或int(影响的行数)

    // 所有Room的注解方法传入参数都得是实体类或实体类的集合
    // 展示用户列表
    @Query("SELECT * FROM users")
    List<User> getAll();

    // 根据ID查找用户
    @Query("SELECT * FROM users WHERE id = :id")
    User findById(int id);

    // 通过名字找用户，找回密码的时候吧因为有专门的login验证了
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findUsersByUsername(String username);

    // 直接检查用户名和密码，返回用户，可用于明文密码匹配,加盐需要先处理再调用
    @Query("SELECT * FROM users WHERE username = :username AND password = :passward LIMIT 1")
    User login(String username, String passward);

    // 检查用户名是否存在，注册时检查使用
    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int checkUsernameExists(String username);

    /**
     * 获取用户总数
     * Get Total User Count
     * 
     * @return 用户总数 (Total number of users)
     * 
     * SQL解释：
     * - SELECT COUNT(*) = 选择计数所有记录 (Select count of all records)
     * - FROM users = 从users表 (From users table)
     */
    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();


    /**
     * 更新用户密码
     * Update User Password
     * 
     * 原理解释：
     * - @Query注解用于自定义SQL查询 (Custom SQL query annotation)
     * - UPDATE = 更新操作 (Update operation)
     * - SET = 设置字段值 (Set field value)
     * - WHERE = 条件筛选 (Condition filtering)
     * - :userId和:newPassword是参数占位符 (Parameter placeholders)
     * 
     * 方法参数说明：
     * - userId: 用户ID，用于定位要更新的用户记录
     * - newPassword: 新密码字符串
     * 
     * 返回值：影响的行数，通常为1表示成功更新一条记录
     */
    @Query("UPDATE users SET password = :newPassword WHERE id = :userId")
    int updatePassword(int userId, String newPassword);

}
