package model;

public class SubTask extends Task {

    public SubTask(String name, String description, Status st) {

        super(name, description, st);
    }


    @Override
    public String toString() {
        return "model.SubTask{" +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", st=" + this.getSt() +
                ", id=" + this.getId() +
                '}';
    }
}
