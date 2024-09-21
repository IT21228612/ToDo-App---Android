package com.example.ToDo.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ToDo.Model.Task;
import com.example.ToDo.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    public TaskAdapter(Context context) {
        this.context = context;
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    // Method to handle editing a task item
    public void editItem(int position) {
        // Implement your logic to handle editing here
        // For example, you can open an edit activity with the details of the task

        // Check if the listener is set
        if (listener != null) {
            // Call onItemClick method of the listener
            listener.onItemClick(position);
        }
    }


    // Method to retrieve a Task object at a given position
    public Task getTaskAtPosition(int position) {
        if (tasks != null && position >= 0 && position < tasks.size()) {
            return tasks.get(position);
        }
        return null;
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskNameTextView.setText(task.getName());
        holder.taskDescriptionTextView.setText(task.getDescription());
        holder.taskPriorityTextView.setText(String.valueOf(task.getPriority()));
        holder.taskDeadlineTextView.setText(dateFormat.format(task.getDeadline()));
        holder.taskCheckbox.setChecked(task.isStatus());
    }

    @Override
    public int getItemCount() {
        if (tasks != null) {
            return tasks.size();
        } else {
            return 0;
        }
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView taskNameTextView;
        public TextView taskDescriptionTextView;
        public TextView taskPriorityTextView;
        public TextView taskDeadlineTextView;
        public CheckBox taskCheckbox;



        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
            taskDescriptionTextView = itemView.findViewById(R.id.taskDescriptionTextView);
            taskPriorityTextView = itemView.findViewById(R.id.taskPriorityTextView);
            taskDeadlineTextView = itemView.findViewById(R.id.taskDeadlineTextView);
            taskCheckbox = itemView.findViewById(R.id.taskCheckbox);

            // Set click listener for the item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
