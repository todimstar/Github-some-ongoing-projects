package com.liu.coursedesign.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.liu.coursedesign.model.Knowledge;

import java.util.List;

@Dao
public interface KnowledgeDao {

    @Insert
    void add(Knowledge knowledge);

    @Delete
    int delete(Knowledge knowledge);


    //查询所有知识,应该在整体列表时调用
    @Query("SELECT * FROM knowledges")
    List<Knowledge> getAllKnowledge();

    // 根据分类查询知识,分栏用
    @Query("SELECT * FROM knowledges WHERE category = :category")
    List<Knowledge> getKnowledgeByCategory(String category);

    // 根据标题查询知识,可能用于各详细知识页面
    @Query("SELECT * FROM knowledges WHERE title = :title")
    Knowledge getKnowledegByTitle(String title);

    // 根据ID查询知识,不知道能用于哪里
    @Query("SELECT * FROM knowledges WHERE id = :id")
    Knowledge getKnowledgeById(int id);


}
