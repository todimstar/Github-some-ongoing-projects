package com.liu.coursedesign.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.liu.coursedesign.model.Knowledge;

import java.util.List;

@Dao
public interface KnowledgeDao {

    @Insert
    long add(Knowledge knowledge);

    @Delete
    int delete(Knowledge knowledge);

    @Update
    int update(Knowledge knowledge);


    //查询所有知识,应该在整体列表时调用
    @Query("SELECT * FROM knowledges")
    List<Knowledge> getAllKnowledge();

    // 根据分类查询知识,分栏展示用
    @Query("SELECT * FROM knowledges WHERE category = :category")
    List<Knowledge> getKnowledgeByCategory(String category);
    /**
     * 根据分类获取数量
     * Get Count by Category
     * 
     * @param category 分类名称 - Category name
     * @return 该分类的知识数量 - Number of knowledge in this category
     */
    @Query("SELECT COUNT(*) FROM knowledges WHERE category = :category")
    int getCountByCategory(String category);

    // 获取所有分类的方法，分类栏自身用
    @Query("SELECT DISTINCT category FROM knowledges WHERE category IS NOT NULL AND category != ''")
    List<String> getAllCategories();


    // 根据标题查询知识,可能用于各详细知识页面
    @Query("SELECT * FROM knowledges WHERE title = :title")
    Knowledge getKnowledegByTitle(String title);

    // 根据ID查询知识,使用场景：编辑功能、详情页面
    @Query("SELECT * FROM knowledges WHERE id = :id")
    Knowledge getKnowledgeById(int id);

    /**
     * 获取随机知识条目 - 用于今日推荐
     * Get Random Knowledge - For Today's Recommendation
     * 
     * 原理解释：
     * - ORDER BY RANDOM() → 将查询结果随机排序
     * - LIMIT 1 → 只取第一条记录
     * - 每次调用都会返回不同的随机知识
     */
    @Query("SELECT * FROM knowledges ORDER BY RANDOM() LIMIT 1")
    Knowledge getRandomKnowledge();

    /**
     * 搜索知识 - 根据关键词模糊搜索
     * Search Knowledge - Fuzzy search by keyword
     * 
     * @param keyword 搜索关键词 - Search keyword
     * @return 匹配的知识列表 - List of matched knowledge
     * 
     * SQL解释：
     * - LIKE '%' || :keyword || '%' → 模糊匹配 (Fuzzy matching)
     * - '%' → 通配符，匹配任意字符 (Wildcard, matches any characters)
     * - '||' → SQLite的字符串连接符 (String concatenation in SQLite)
     * - OR → 逻辑或运算符 (Logical OR operator)
     * 
     * 搜索范围：标题、分类、描述
     * Search scope: title, category, description
     */
    @Query("SELECT * FROM knowledges WHERE " +
           "title LIKE '%' || :keyword || '%' OR " +
           "category LIKE '%' || :keyword || '%' OR " +
           "description LIKE '%' || :keyword || '%'")
    List<Knowledge> searchKnowledge(String keyword);

    /**
     * 获取知识总数 - 用于统计功能
     * Get Knowledge Count - For statistics
     * 
     * @return 知识条目总数 - Total number of knowledge items
     */
    @Query("SELECT COUNT(*) FROM knowledges")
    int getKnowledgeCount();
    
        
    /**
     * 获取最新添加的知识
     * Get Latest Added Knowledge
     * 
     * @param limit 返回数量限制 - Return count limit
     * @return 最新的知识列表 - List of latest knowledge
     * 
     * SQL解释：
     * - ORDER BY createTime DESC → 按创建时间降序排序 (Order by create time descending)
     * - DESC = Descending (降序) → 最新的在前
     * - ASC = Ascending (升序) → 最旧的在前
     */
    @Query("SELECT * FROM knowledges ORDER BY createTime DESC LIMIT :limit")
    List<Knowledge> getLatestKnowledge(int limit);


}
