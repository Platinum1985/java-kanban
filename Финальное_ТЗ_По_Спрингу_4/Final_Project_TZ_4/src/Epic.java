import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{
    public ArrayList<Integer> subTaskIds=new ArrayList<>();


    public Epic(String name, String description, Status st, int id) {
        super(name, description, st, id);
    }

    public void addSubTaskId(int subTaskId) {

        subTaskIds.add(subTaskId);
    }


    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", st=" + st +
                ", id=" + id +
                ", subTaskIds=" + subTaskIds +
                '}';
    }
}
