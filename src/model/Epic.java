package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    public List<Integer> subTaskIds = new ArrayList<>();
    private int id = -1;


    public Epic(String name, String description, Status st) {
        super(name, description, st);
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

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }
}
