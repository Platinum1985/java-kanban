package model;

public class SubTask extends Task {
    public SubTask(int id,TaskType type, String name, Status status, String description,int yourEpicId) {
        super(id,type,name,status,description);
        this.setYourEpicId(yourEpicId);

    }

    public void setYourEpicId(int yourEpicId) {
        this.yourEpicId = yourEpicId;
    }

    int yourEpicId=-1;

    public SubTask(String name, String description, Status st) {

        super(name, description, st);
    }


    @Override
    public String toString() {
        return super.getId() + "," + super.getTaskType() + "," + super.getName() + "," +super.getSt()+","+super.getDescription()+","+this.getYourEpicId();
    }

    public int getYourEpicId() {
        return yourEpicId;
    }
}
