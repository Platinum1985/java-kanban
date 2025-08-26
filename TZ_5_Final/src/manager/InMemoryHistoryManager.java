package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private List<Task> history=new ArrayList<>();
    int MAX_SIZE=10;


    @Override
    public void add(Task task) {
        if(history.size()==MAX_SIZE) {
            history.remove(0);
        }
            history.add(task);

    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
