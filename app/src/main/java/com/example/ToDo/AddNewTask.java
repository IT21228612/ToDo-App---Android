package com.example.ToDo;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ToDo.Model.Task;
import com.example.ToDo.Utils.TaskDatabase;
import com.example.ToDo.Utils.TaskViewModel;

import java.util.List;

public class AddNewTask extends AppCompatActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText priorityEditText;
    private EditText deadlineEditText;
    private Button saveButton;

    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);

        // Initialize the TaskDatabase instance
        TaskDatabase.getInstance(this);

        // Initialize ViewModel
        // Initialize ViewModel using application context
        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TaskViewModel.class);




        // Find views from the layout
        nameEditText = findViewById(R.id.name);
        descriptionEditText = findViewById(R.id.description);
        priorityEditText = findViewById(R.id.priority);
        deadlineEditText = findViewById(R.id.deadline);
        saveButton = findViewById(R.id.saveButton);

        // Set up click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    private void saveTask() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        int priority = Integer.parseInt(priorityEditText.getText().toString().trim());
        long deadline = Long.parseLong(deadlineEditText.getText().toString().trim());

        // Create a new Task object
        Task newTask = new Task(name, description, priority, deadline, false);

        // Save the new task using the ViewModel
        taskViewModel.insertTask(newTask);

        // Log a message indicating that the task has been saved
        Log.d("AddNewTask", "Task savedddd: Title - " + newTask.getName() + ", Description - " + newTask.getDescription() + ", Deadline - " + newTask.getDeadline());

        // Post a delayed Runnable to execute after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After the delay, retrieve tasks synchronously
                List<Task> tasks = taskViewModel.getAllTasksSync();

                // Check if tasks is not null
                if (tasks != null) {
                    for (Task task : tasks) {
                        // Log each task
                        Log.d("AddNewTask", "Task retrieved: Title - " + task.getName() + ", Description - " + task.getDescription() + ", Deadline - " + task.getDeadline());
                    }
                } else {
                    Log.d("AddNewTask", "No tasks found in the database");
                }
            }
        }, 5000); // 5000 milliseconds = 5 seconds

        // Finish the activity to go back to MainActivity
        finish();
    }
}
