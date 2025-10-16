package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    public SubTask(String name, Status status, String description,  String localDateTime, Long duration) {
        super(name, status,description,localDateTime,duration);


    }

    public SubTask(int id, TaskType type, String name, Status status, String description, Integer epicId, String localDateTime, Long duration) {
        super(id,type,name,status,description,localDateTime,duration);
        this.yourEpicId=epicId;
    }

    public void setYourEpicId(int yourEpicId) {
        this.yourEpicId = yourEpicId;
    }

    int yourEpicId=-1;

   /* public SubTask(String name, String description, Status st) {

        super(name, description, st);
    }*/


    @Override
    public String toString() {
        return super.getId() + "," + super.getTaskType() + "," + super.getName() + "," +super.getSt()+","+super.getDescription()+","+this.getYourEpicId();
    }

    public int getYourEpicId() {
        return yourEpicId;
    }
}
