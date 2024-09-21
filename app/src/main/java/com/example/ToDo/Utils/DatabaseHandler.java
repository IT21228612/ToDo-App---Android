package com.example.ToDo.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ToDo.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String NAME_COLUMN = "name";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String DEADLINE_COLUMN = "deadline";
    private static final String PRIORITY_COLUMN = "priority";
    private static final String STATUS_COLUMN = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME_COLUMN + " TEXT, " + DESCRIPTION_COLUMN + " TEXT, " + DEADLINE_COLUMN + " TEXT, " +
            PRIORITY_COLUMN + " TEXT, " + STATUS_COLUMN + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(String name, String description, String deadline, String priority, boolean status){
        ContentValues cv = new ContentValues();
        cv.put(NAME_COLUMN, name);
        cv.put(DESCRIPTION_COLUMN, description);
        cv.put(DEADLINE_COLUMN, deadline);
        cv.put(PRIORITY_COLUMN, priority);
        cv.put(STATUS_COLUMN, status ? 1 : 0);
        db.insert(TODO_TABLE, null, cv);
    }


    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        int id = cur.getInt(cur.getColumnIndex(ID));
                        String name = cur.getString(cur.getColumnIndex(NAME_COLUMN));
                        String description = cur.getString(cur.getColumnIndex(DESCRIPTION_COLUMN));
                        String deadline = cur.getString(cur.getColumnIndex(DEADLINE_COLUMN));
                        String priority = cur.getString(cur.getColumnIndex(PRIORITY_COLUMN));
                        boolean status = cur.getInt(cur.getColumnIndex(STATUS_COLUMN)) == 1;

                        ToDoModel task = new ToDoModel(name, description, deadline, priority, status);
                        task.setId(id);
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            if (cur != null) {
                cur.close();
            }
        }
        return taskList;
    }


    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS_COLUMN, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String name, String description, String deadline, String priority) {
        ContentValues cv = new ContentValues();
        cv.put(NAME_COLUMN, name);
        cv.put(DESCRIPTION_COLUMN, description);
        cv.put(DEADLINE_COLUMN, deadline);
        cv.put(PRIORITY_COLUMN, priority);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
