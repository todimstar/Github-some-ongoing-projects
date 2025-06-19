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
    void insertAll(User... users);

    @Delete
    int deleteAll(User[] users);

    @Update
    int update(User user);

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findUsersByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username AND password = :passward LIMIT 1")
    User login(String username, String passward);

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int checkUsernameExists(String username);


}
