package com.example.ToDo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ToDo.Model.Task;
import com.example.ToDo.Utils.TaskViewModel;
import com.example.ToDo.R;

public class EditTaskActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText priorityEditText;
    private EditText deadlineEditText;
    private Button saveButton;

    private TaskViewModel taskViewModel;
    private Task editedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        // Initialize ViewModel
        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TaskViewModel.class);

        // Find views from the layout
        nameEditText = findViewById(R.id.edit_name);
        descriptionEditText = findViewById(R.id.edit_description);
        priorityEditText = findViewById(R.id.edit_priority);
        deadlineEditText = findViewById(R.id.edit_deadline);
        saveButton = findViewById(R.id.saveEditButton);

        // Retrieve task details from the intent
        editedTask = getIntent().getParcelableExtra("task");
        if (editedTask != null) {
            // Populate EditText fields with task details
            nameEditText.setText(editedTask.getName());
            descriptionEditText.setText(editedTask.getDescription());
            priorityEditText.setText(String.valueOf(editedTask.getPriority()));
            deadlineEditText.setText(String.valueOf(editedTask.getDeadline()));
        }

        // Set up click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditedTask();
            }
        });
    }

    private void saveEditedTask() {
        // Retrieve edited values from EditText fields
        String editedName = nameEditText.getText().toString().trim();
        String editedDescription = descriptionEditText.getText().toString().trim();
        int editedPriority = Integer.parseInt(priorityEditText.getText().toString().trim());
        long editedDeadline = Long.parseLong(deadlineEditText.getText().toString().trim());

        // Update the edited task with new values
        editedTask.setName(editedName);
        editedTask.setDescription(editedDescription);
        editedTask.setPriority(editedPriority);
        editedTask.setDeadline(editedDeadline);

        // Update the task in the database using ViewModel
        taskViewModel.updateTask(editedTask);

        // Finish the activity
        finish();
    }
}
