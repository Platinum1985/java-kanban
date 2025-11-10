package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private String name;
    private String description; //"Описание"
    private Status st = Status.NEW;
    private LocalDateTime endTime;
    private Duration duration;
    private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");


    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Task(int id, TaskType type, String name, Status status, String description, String localDateTime, Long duration) {
        this.id = id;
        this.taskType = type;
        this.name = name;
        this.st = status;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(localDateTime, dateTimeFormatter);
    }

    public void setDuration(Long minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    private LocalDateTime startTime;
    private TaskType taskType;
    private int id = -1;


    public Duration getDuration() {
        return duration;
    }

    public Task( String name, Status status, String description,  String startTime, Long duration) {
        this.name = name;
        this.st = status;//!!!
        this.description = description;
        this.duration=Duration.ofMinutes(duration);
        this.startTime=LocalDateTime.parse(startTime, dateTimeFormatter);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
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
