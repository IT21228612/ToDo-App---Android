package com.example.ToDo.Utils;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import com.example.ToDo.Model.Task;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    LiveData<Task> getTaskById(long taskId);

    // Method to fetch all tasks synchronously
    @Query("SELECT * FROM tasks")
    List<Task> getAllTasksSync();
}
