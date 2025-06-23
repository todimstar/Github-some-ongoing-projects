package com.liu.coursedesign.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 会话管理器 (Session Manager)
 * 用于管理用户登录状态和用户信息
 */
public class SessionManager {
    
    // SharedPreferences文件名 (SharedPreferences file name)
    private static final String PREF_NAME = "user_session";
    
    // 存储键名常量 (Storage key constants)
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";    // 是否已登录
    private static final String KEY_USERNAME = "username";            // 用户名
    private static final String KEY_USER_ID = "user_id";             // 用户ID
    private static final String KEY_USER_ROLE = "user_role";         // 用户角色
    
    private SharedPreferences preferences;  // SharedPreferences实例,这个才是存入手机存储的文件，其他Manager都是访问的它
    private SharedPreferences.Editor editor; // 编辑器，用于写入数据
    private Context context; // 上下文，用于访问系统服务
    
    /**
     * 构造函数 (Constructor)
     * @param context 上下文对象
     */
    public SessionManager(Context context) {
        this.context = context;
        
        // 获取SharedPreferences实例 (Get SharedPreferences instance)
        // MODE_PRIVATE = 私有模式，只有本应用可以访问
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        
        // 获取编辑器 (Get editor for writing data)
        editor = preferences.edit();
    }
    
    /**
     * 保存用户登录信息 (Save user login information)
     * @param userId 用户ID
     * @param username 用户名
     * @param userRole 用户角色
     */
    public void saveUserSession(int userId, String username, String userRole) {
        // editor.putXxx() = 存储不同类型的数据
        editor.putBoolean(KEY_IS_LOGGED_IN, true);     // 标记为已登录
        editor.putInt(KEY_USER_ID, userId);            // 存储用户ID
        editor.putString(KEY_USERNAME, username);       // 存储用户名
        editor.putString(KEY_USER_ROLE, userRole);     // 存储用户角色
        
        // commit() = 立即同步写入磁盘 (Immediately write to disk)
        // apply() = 异步写入，推荐使用 (Asynchronous write, recommended)
        editor.commit(); // 提交更改
    }
    
    /**
     * 检查用户是否已登录 (Check if user is logged in)
     * @return boolean 登录状态
     */
    public boolean isLoggedIn() {
        // getBoolean(key, defaultValue) = 获取布尔值，如果不存在返回默认值
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    /**
     * 获取当前用户名 (Get current username)
     * @return String 用户名，未登录返回null
     */
    public String getUsername() {
        // getString(key, defaultValue) = 获取字符串，如果不存在返回默认值
        return preferences.getString(KEY_USERNAME, null);
    }
    
    /**
     * 获取当前用户ID (Get current user ID)
     * @return int 用户ID，未登录返回-1
     */
    public int getUserId() {
        // getInt(key, defaultValue) = 获取整数，如果不存在返回默认值
        return preferences.getInt(KEY_USER_ID, -1);
    }
    
    /**
     * 获取当前用户角色 (Get current user role)
     * @return String 用户角色，未登录返回null
     */
    public String getUserRole() {
        return preferences.getString(KEY_USER_ROLE, null);
    }
    
    /**
     * 清除用户会话 (Clear user session) - 退出登录时调用
     */
    public void clearSession() {
        // clear() = 清除所有数据 (Clear all data)
        editor.clear();
        editor.commit(); // 提交更改
    }
    
    /**
     * 获取用户详细信息 (Get user details)
     * @return UserSessionInfo 用户会话信息对象
     */
    public UserSessionInfo getUserSessionInfo() {
        if (!isLoggedIn()) {
            return null; // 未登录返回null
        }
        
        return new UserSessionInfo(
            getUserId(),
            getUsername(), 
            getUserRole()
        );
    }
    
    /**
     * 用户会话信息数据类 (User session information data class)
     */
    public static class UserSessionInfo {
        public final int id;        // 用户ID
        public final String username;   // 用户名
        public final String role;   // 用户角色
        
        public UserSessionInfo(int userId, String username, String userRole) {
            this.id = userId;
            this.username = username;
            this.role = userRole;
        }
    }
}