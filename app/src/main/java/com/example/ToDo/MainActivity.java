package com.example.ToDo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ToDo.Model.Task;
import com.example.ToDo.Utils.TaskAdapter;
import com.example.ToDo.Utils.TaskDao;
import com.example.ToDo.Utils.TaskDatabase;
import com.example.ToDo.Utils.TaskRepository;
import com.example.ToDo.Utils.TaskViewModel;
import com.example.ToDo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnItemClickListener {

    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;

    // In your MainActivity


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialize taskAdapter before setting the click listener
        taskAdapter = new TaskAdapter(this);

        taskAdapter.setOnItemClickListener(this);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        RecyclerView tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this);
        tasksRecyclerView.setAdapter(taskAdapter);


        // Create an instance of TaskDao
        TaskDao taskDao = TaskDatabase.getInstance(this).taskDao();


        // Create an instance of TaskRepository with TaskDao
        TaskRepository taskRepository = new TaskRepository(taskDao);

        // Pass the taskRepository to the TaskViewModel constructor
        taskViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(TaskViewModel.class);
        taskViewModel.init(taskRepository);



        // Observe changes in task list
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                taskAdapter.setTasks(tasks);
            }
        });

        Log.d("jeewa", "jeewantha");





        // Find FloatingActionButton and set OnClickListener
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddNewTask activity using Intent
                Intent intent = new Intent(MainActivity.this, AddNewTask.class);
                startActivity(intent);
            }
        });

        RecyclerItemTouchHelper itemTouchHelper = new RecyclerItemTouchHelper(taskAdapter, taskViewModel, this);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(tasksRecyclerView);
    }

    @Override
    public void onItemClick(int position) {
        // Get the task at the clicked position
        Task clickedTask = taskAdapter.getTaskAtPosition(position);

        // Start the edit activity and pass the task details
        Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
        intent.putExtra("task", clickedTask);
        startActivity(intent);
    }

}
