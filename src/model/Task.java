package model;

import java.util.Objects;

public class Task {
    private String name;

    public void setDescription(String description) {
        this.description = description;
    }

    private String description; //"Описание"
    private Status st = Status.NEW;

    public Task(int id, TaskType type, String name, Status status, String description) {
        this.id = id;
        this.taskType = type;
        this.name = name;
        this.st = status;//!!!
        this.description = description;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    private TaskType taskType;
    private int id = -1;

    public void setId(int id) {
        this.id = id;
    }

    public Task(String name, String description, Status st) {
        this.name = name;
        this.description = description;
        this.st = st;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return this.id + "," + this.taskType + "," + this.name + "," + this.st + "," + this.description + ",";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getSt() {
        return st;
    }


    public void setSt(Status st) {
        this.st = st;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setName(String name) {
        this.name = name;
    }
}
