package model;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subTaskIds=new ArrayList<>();


    public Epic(String name, String description, Status st, int id) {
        super(name, description, st, id);
    }

    public void addSubTaskId(int subTaskId) {

        subTaskIds.add(subTaskId);
    }


    @Override
    public String toString() {
        return "model.Epic{" +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", st=" + this.getSt() +
                ", id=" + this.getId() +
                ", subTaskIds=" + subTaskIds +
                '}';
    }
}
