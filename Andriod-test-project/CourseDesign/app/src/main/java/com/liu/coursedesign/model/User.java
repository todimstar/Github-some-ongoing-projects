package com.liu.coursedesign.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.liu.coursedesign.Dao.UserRoles;

import java.util.Objects;


@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)    // 主键自增
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "role")
    public String role; // 角色：管理员(ADMINISTRATOR)、普通用户(USER)等

    public User(){}

    // 到时候加密再改password传入方式
    // 构造函数重载(Constructor Overloading) - 带有默认角色的构造函数
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = UserRoles.USER; // 默认角色为普通用户
    }
    
    // 完整参数的构造函数 - 可以指定角色
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString(){
        // 返回用户信息的字符串表示 - String Representation(字符串表示)
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}'; // 注意：不包括密码，密码不应该在toString中暴露
    }

    // 重写equals方法 - 用于对象比较
    // Override(重写) - 子类重新定义父类的方法
    // Object - Java中所有类的父类
    @Override
    public boolean equals(Object obj) {
        // 检查是否是同一个对象的引用 - Reference(引用)比较
        if (obj == this)return true;
        
        // 检查是否为null或者不是同一个类型
        // getClass - 更严格检查类型是不是完全一致
        if (obj == null || getClass() != obj.getClass())return false;
        
        
        User user = (User) obj;
        
        // 比较id字段是否相等
        if(this.id == 0 || user.id == 0) {
            // 如果id为0，比较其他字段不包括密码
            return Objects.equals(username, user.username) &&   //得保证username能作为唯一标识符
                    Objects.equals(role, user.role);
        }
        return this.id == user.id;
    }

    // 重写hashCode方法 - 与equals配套使用
    // Hash Code(哈希码) - 对象的数字标识符
    @Override
    public int hashCode() {
        // Objects.hash(xx) - 生成基于xx字段的哈希码
        // 瞬态对象处理 - 当id为0时，使用其他字段生成哈希码
        // Transient Object(瞬态对象) - 尚未持久化到数据库的对象 -> 自己new出来的User都是瞬态对象，没有id；从数据库里查询出来的User有id，才是持久化对象
        if (this.id == 0) {
            // 对于新创建的对象，使用用户名和密码生成哈希码
            // 避免所有新对象都有相同的哈希码(0)
            return Objects.hash(this.username,this.role);
        }
        return Objects.hash(this.id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}



