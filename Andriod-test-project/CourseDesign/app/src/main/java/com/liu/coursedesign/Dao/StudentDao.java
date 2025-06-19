package com.liu.coursedesign.Dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.liu.coursedesign.model.Student;

import java.util.List;

@Dao
public interface StudentDao {
    @Insert
    void add(Student student);

    @Delete
    int delete(Student student);

    @Query("SELECT * FROM students")
    List<Student> getAllStudents();

    @Query("SELECT * FROM students WHERE name = :studentName")
    Student findByUsername(String studentName);

}
