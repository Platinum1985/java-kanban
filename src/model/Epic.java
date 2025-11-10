package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    public List<Integer> subTaskIds = new ArrayList<>();
    private LocalDateTime endTime;
    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    public Epic(String name,  Status status, String description,String localeDateTime,Long duration) {
        super(name, status,description,localeDateTime,duration);
    }

    public Epic(int id, TaskType type, String name, Status status, String description, String localDateTime, Long duration) {
        super(id,type,name,status,description,localDateTime,duration);
    }

    public void addSubTaskId(int subTaskId) {
        subTaskIds.add(subTaskId);
    }


    @Override
    public String toString() {
        return super.getId() + "," +super. getTaskType() + "," + super.getName() + "," +super.getSt()+","+super.getDescription()+",";
    }

    public List<Integer> getSubTaskIds() {

        return subTaskIds;
    }

}
