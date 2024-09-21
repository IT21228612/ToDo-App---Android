package com.example.ToDo.Utils;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ToDo.Adapters.ToDoAdapter;
import com.example.ToDo.Model.ToDoModel;
import com.example.ToDo.R;
import com.example.ToDo.Utils.DatabaseHandler;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editName, editDescription, editDeadline;
    private Spinner editPriority;
    private Button editSaveButton;

    private DatabaseHandler db;
    private ToDoModel task;

    private List<ToDoModel> taskList;

    private ToDoAdapter tasksAdapter;
    private RecyclerView tasksRecyclerView;

    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Initialize views
        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        editDeadline = findViewById(R.id.editDeadline);
        editPriority = findViewById(R.id.editPriority);
        editSaveButton = findViewById(R.id.editSaveButton);

        // Initialize DatabaseHandler
        db = new DatabaseHandler(this);
        db.openDatabase();

        // Retrieve the ToDoModel object from the intent extras
        task = (ToDoModel) getIntent().getSerializableExtra("EDIT_TASK");

        // Populate the Spinner with priority options
        List<String> priorityOptions = Arrays.asList(getResources().getStringArray(R.array.priority_options));
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorityOptions);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editPriority.setAdapter(priorityAdapter);

        // Populate UI fields with the data from the ToDoModel object
        if (task != null) {
            editName.setText(task.getName());
            editDescription.setText(task.getDescription());
            editDeadline.setText(task.getDeadline());

            // Set the selected priority in the Spinner
            int priorityIndex = getIndexForPriority(task.getPriority());
            if (priorityIndex != -1) {
                editPriority.setSelection(priorityIndex);
            }
        }

        // Set OnClickListener for the Save button
        editSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the task with the new data
                updateTask();
            }
        });

        // Set OnClickListener for the deadline EditText
        editDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void updateTask() {
        // Get the new values from the UI fields
        String name = editName.getText().toString();
        String description = editDescription.getText().toString();
        String deadline = editDeadline.getText().toString();
        String priority = editPriority.getSelectedItem().toString(); // Get the selected priority from the Spinner

        // Check if db is initialized and not null
        if (db != null) {
            // Log the updated task details
            Log.d("UpdateTask-db not null", "Updated task details: Name: " + name + ", Description: " + description + ", Deadline: " + deadline + ", Priority: " + priority);

            // Update the task in the database
            db.updateTask(task.getId(), name, description, deadline, priority);
        } else {
            Log.e("UpdateTask", "Database is not initialized or is null");
        }

        // Refresh the tasks list
        refreshTaskList();

        // Finish the activity
        finish();
    }

    // Helper method to refresh the tasks list
    private void refreshTaskList() {
        // Initialize tasksRecyclerView if it's not already initialized
        if (tasksRecyclerView == null) {
            tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        }

        // Initialize tasksAdapter
        if (tasksAdapter == null) {
            tasksAdapter = new ToDoAdapter(db, EditTaskActivity.this);
            tasksRecyclerView.setAdapter(tasksAdapter);
        }

        // Get the updated task list from the database
        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        // Update the adapter with the new task list
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }

    // Helper method to show the date picker dialog
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Update the EditText with the selected date
                        editDeadline.setText(selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    // Helper method to get the index of the priority in the Spinner
    private int getIndexForPriority(String priority) {
        String[] priorityOptions = getResources().getStringArray(R.array.priority_options);
        for (int i = 0; i < priorityOptions.length; i++) {
            if (priorityOptions[i].equals(priority)) {
                return i;
            }
        }
        return -1;
    }
}
