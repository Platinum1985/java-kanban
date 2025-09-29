package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    //private String name="В этом причина??";
   // private String description; //"Описание"
   // private Status st = Status.NEW;
    public List<Integer> subTaskIds = new ArrayList<>();
   // private int id = -1;
   // private  TaskType taskType ;

    public Epic(String name, String description, Status st) {

        super(name, description, st);
    }

    public Epic(int id,TaskType type, String name, Status status, String description) {
        super(id,type,name, status,description);
       /* this.setId(id);
        this.setTaskType(TaskType.valueOf(type));
        this.setName(name);
        this.setSt(Status.valueOf(status));
        this.setDescription(description); */


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
