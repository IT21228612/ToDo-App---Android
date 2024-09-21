package com.example.ToDo.Utils;

import androidx.lifecycle.LiveData;
import com.example.ToDo.Model.Task;
import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;

    public TaskRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public void insertTask(Task task) {
        taskDao.insert(task);
    }

    public void updateTask(Task task) {
        taskDao.update(task);
    }

    public void deleteTask(Task task) {
        taskDao.delete(task);
    }

    public LiveData<List<Task>> getAllTasksLiveData() {
        return taskDao.getAllTasks();
    }

    public LiveData<Task> getTaskByIdLiveData(long taskId) {
        return taskDao.getTaskById(taskId);
    }

    // Method to fetch all tasks synchronously
    public List<Task> getAllTasksSync() {
        return taskDao.getAllTasksSync();
    }
}
