package com.example.ToDo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ToDo.MainActivity;
import com.example.ToDo.Model.ToDoModel;
import com.example.ToDo.R;
import com.example.ToDo.Utils.DatabaseHandler;
import com.example.ToDo.Utils.EditTaskActivity;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private DatabaseHandler db;
    private MainActivity activity;

    private Context context; // Add context variable

    public ToDoAdapter(DatabaseHandler db, Context context) { // Pass context to the adapter
        this.db = db;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final ToDoModel item = todoList.get(position);
        holder.taskNameTextView.setText(item.getName());
        holder.taskDescriptionTextView.setText(item.getDescription());
        holder.taskPriorityTextView.setText(item.getPriority());
        holder.taskDeadlineTextView.setText(item.getDeadline());
        holder.taskCheckbox.setChecked(item.isStatus());


        // Apply or remove strikethrough and change text color based on the checked status
        if (item.isStatus()) {
            holder.taskNameTextView.setPaintFlags(holder.taskNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.taskDescriptionTextView.setPaintFlags(holder.taskDescriptionTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.taskPriorityTextView.setPaintFlags(holder.taskPriorityTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.taskDeadlineTextView.setPaintFlags(holder.taskDeadlineTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.taskNameTextView.setTextColor(Color.GRAY);
            holder.taskDescriptionTextView.setTextColor(Color.GRAY);
            holder.taskPriorityTextView.setTextColor(Color.GRAY);
            holder.taskDeadlineTextView.setTextColor(Color.GRAY);
        } else {
            holder.taskNameTextView.setPaintFlags(holder.taskNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.taskDescriptionTextView.setPaintFlags(holder.taskDescriptionTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.taskPriorityTextView.setPaintFlags(holder.taskPriorityTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.taskDeadlineTextView.setPaintFlags(holder.taskDeadlineTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.taskNameTextView.setTextColor(Color.BLACK);
            holder.taskDescriptionTextView.setTextColor(Color.BLACK);
            holder.taskPriorityTextView.setTextColor(Color.BLACK);
            holder.taskDeadlineTextView.setTextColor(Color.BLACK);
        }

        holder.taskCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToDoModel item = todoList.get(holder.getAdapterPosition());

                // Handle checkbox state changes
                handleCheckboxStateChange(holder, holder.getAdapterPosition(), isChecked);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic to edit an item
                editItem(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ToDoModel> todoList) {
        List<ToDoModel> checkedTasks = new ArrayList<>();
        this.todoList = new ArrayList<>(todoList);

        // Separate checked tasks from the original list
        for (int i = 0; i < this.todoList.size(); i++) {
            ToDoModel task = this.todoList.get(i);
            if (task.isStatus()) {
                checkedTasks.add(task);
                this.todoList.remove(i);
                i--; // Decrement i to avoid skipping elements after removal
            }
        }

        // Add the new checked tasks to the bottom of the original list
        this.todoList.addAll(checkedTasks);



        notifyDataSetChanged();
    }


    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId()); // Delete the item from the database
        todoList.remove(position); // Remove the item from the list
        notifyItemRemoved(position); // Notify the adapter that the item is removed at the given position
        notifyItemRangeChanged(position, todoList.size()); // Notify the adapter that the data set has changed after the item removal
    }


    public void editItem(int position) {
        // Get the ToDoModel object at the specified position
        ToDoModel item = todoList.get(position);

        // Create an intent to start EditTaskActivity
        Intent intent = new Intent(context, EditTaskActivity.class);

        // Put the ToDoModel object as an extra in the intent
        intent.putExtra("EDIT_TASK", item);

        // Start EditTaskActivity
        context.startActivity(intent);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskCheckbox;
        TextView taskNameTextView, taskDescriptionTextView, taskPriorityTextView, taskDeadlineTextView;

        ViewHolder(View view) {
            super(view);
            taskCheckbox = view.findViewById(R.id.taskCheckbox);
            taskNameTextView = view.findViewById(R.id.taskNameTextView);
            taskDescriptionTextView = view.findViewById(R.id.taskDescriptionTextView);
            taskPriorityTextView = view.findViewById(R.id.taskPriorityTextView);
            taskDeadlineTextView = view.findViewById(R.id.taskDeadlineTextView);
        }
    }


    // Method to handle checkbox state changes and update the dataset
    private void handleCheckboxStateChange(final ViewHolder holder, final int position, final boolean isChecked) {
        holder.itemView.post(new Runnable() {
            @Override
            public void run() {
                ToDoModel item = todoList.get(position);
                item.setStatus(isChecked);

                // Update the status in the database
                db.updateStatus(item.getId(), isChecked ? 1 : 0);

                if (isChecked) {
                    // Move the checked task to the bottom of the list
                    todoList.remove(holder.getAdapterPosition());
                    todoList.add(item);
                    notifyItemMoved(holder.getAdapterPosition(), todoList.size() - 1);
                } else {
                    // Move the unchecked task to the top of the list
                    todoList.remove(holder.getAdapterPosition());
                    todoList.add(0, item);
                    notifyItemMoved(holder.getAdapterPosition(), 0);
                }

                // Apply or remove strikethrough based on the checked status
                if (isChecked) {
                    holder.taskNameTextView.setPaintFlags(holder.taskNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.taskDescriptionTextView.setPaintFlags(holder.taskDescriptionTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.taskPriorityTextView.setPaintFlags(holder.taskPriorityTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.taskDeadlineTextView.setPaintFlags(holder.taskDeadlineTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.taskNameTextView.setTextColor(Color.GRAY);
                    holder.taskDescriptionTextView.setTextColor(Color.GRAY);
                    holder.taskPriorityTextView.setTextColor(Color.GRAY);
                    holder.taskDeadlineTextView.setTextColor(Color.GRAY);
                } else {
                    holder.taskNameTextView.setPaintFlags(holder.taskNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.taskDescriptionTextView.setPaintFlags(holder.taskDescriptionTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.taskPriorityTextView.setPaintFlags(holder.taskPriorityTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.taskDeadlineTextView.setPaintFlags(holder.taskDeadlineTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.taskNameTextView.setTextColor(Color.BLACK);
                    holder.taskDescriptionTextView.setTextColor(Color.BLACK);
                    holder.taskPriorityTextView.setTextColor(Color.BLACK);
                    holder.taskDeadlineTextView.setTextColor(Color.BLACK);
                }
            }
        });
    }

    // Method to get the list of tasks
    public List<ToDoModel> getTasks() {
        return todoList;
    }
}
