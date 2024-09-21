package com.example.ToDo.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;
    private int priority;
    private long deadline;
    private boolean status;

    // Constructor
    public Task(String name, String description, int priority, long deadline, boolean status) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
    }

    // Getters and setters for id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // Getters and setters for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters and setters for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getters and setters for priority
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    // Getters and setters for deadline
    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    // Getters and setters for status
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // Parcelable implementation
    protected Task(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        priority = in.readInt();
        deadline = in.readLong();
        status = in.readByte() != 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(priority);
        dest.writeLong(deadline);
        dest.writeByte((byte) (status ? 1 : 0));
    }
}
