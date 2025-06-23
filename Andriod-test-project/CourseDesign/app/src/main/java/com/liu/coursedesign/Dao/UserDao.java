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
    void insertAll(User... users);// insert注解方法返回值只能void或long,long[],List<Long>

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


}
