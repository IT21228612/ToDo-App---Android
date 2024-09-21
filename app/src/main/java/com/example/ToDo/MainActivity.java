package com.example.ToDo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.ToDo.Adapters.ToDoAdapter;
import com.example.ToDo.Model.ToDoModel;
import com.example.ToDo.Utils.DatabaseHandler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private DatabaseHandler db;

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab , deleteChecked;
    private Spinner sort;

    private List<ToDoModel> taskList;

    private static final int EDIT_TASK_REQUEST_CODE = 1; // Define a request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db, MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        // Pass both Context and ToDoAdapter to RecyclerItemTouchHelper constructor
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(this, tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = findViewById(R.id.fab);
        deleteChecked = findViewById(R.id.deleteChecked);
        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });


        deleteChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCheckedTasks();
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);

    }

    private void deleteCheckedTasks() {
        // Get all tasks from the adapter
        List<ToDoModel> allTasks = tasksAdapter.getTasks();

        // Check if there are any checked tasks
        boolean hasCheckedTasks = false;
        for (ToDoModel task : allTasks) {
            if (task.isStatus()) {
                hasCheckedTasks = true;
                break;
            }
        }

        // Show confirmation dialog only if there are checked tasks
        if (hasCheckedTasks) {
            // Create and show the confirmation dialog
            new AlertDialog.Builder(this)
                    .setTitle("Delete Checked Tasks")
                    .setMessage("Are you sure you want to delete all checked tasks?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Delete checked tasks if user confirms
                            performDeleteCheckedTasks();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            // Show a message if there are no checked tasks
            Toast.makeText(MainActivity.this, "No checked tasks to delete", Toast.LENGTH_SHORT).show();
        }
    }

    private void performDeleteCheckedTasks() {
        // Get all tasks from the adapter
        List<ToDoModel> allTasks = tasksAdapter.getTasks();

        // Iterate through all tasks and delete checked tasks from the database
        for (ToDoModel task : allTasks) {
            if (task.isStatus()) {
                db.deleteTask(task.getId());
            }
        }

        // Refresh the task list in the adapter after deletion
        taskList = db.getAllTasks();
        tasksAdapter.setTasks(taskList);
    }
}
