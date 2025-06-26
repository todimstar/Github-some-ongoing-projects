package com.liu.coursedesign.Dao;

/**
 * 用户角色常量类
 * 方便，不用记得admin还是amdinistrator用UserRoles一点就出来了
 */
public final class UserRoles {
    // 私有构造函数，防止实例化
    private UserRoles() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    // 定义用户角色常量
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    public static final String USER = "USER";
}
