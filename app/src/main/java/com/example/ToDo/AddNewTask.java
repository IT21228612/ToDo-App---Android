package com.example.ToDo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.ToDo.DialogCloseListener;
import com.example.ToDo.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.example.ToDo.Model.ToDoModel;
import com.example.ToDo.Utils.DatabaseHandler;

import java.util.Calendar;
import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskName, newTaskDescription, newTaskDeadline;
    private Button newTaskSaveButton;

    private DatabaseHandler db;
    private Context context;

    private int year, month, day;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
        context = getContext(); // Get context
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskName = Objects.requireNonNull(getView()).findViewById(R.id.name);
        newTaskDescription = getView().findViewById(R.id.description);
        newTaskDeadline = getView().findViewById(R.id.deadline);
        newTaskSaveButton = getView().findViewById(R.id.saveButton);
        final Spinner prioritySpinner = getView().findViewById(R.id.priority);

        // Get current date values
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        // Populate spinner with options from strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.priority_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        newTaskSaveButton.setEnabled(false); // Disable save button by default

        newTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable save button if the task name is not empty
                newTaskSaveButton.setEnabled(!s.toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        newTaskDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(context, deadlineDateSetListener, year, month, day);
                datePicker.show();
            }
        });

        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newTaskName.getText().toString();
                String description = newTaskDescription.getText().toString();
                String deadline = newTaskDeadline.getText().toString();
                String priority = prioritySpinner.getSelectedItem().toString(); // Assuming prioritySpinner is your Spinner instance
                boolean status = false; // Default status is false for new tasks

                db.insertTask(name, description, deadline, priority, status);
                dismiss();
            }
        });

    }

    private DatePickerDialog.OnDateSetListener deadlineDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
            // Update the EditText with the selected date
            newTaskDeadline.setText(selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }
    };

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
