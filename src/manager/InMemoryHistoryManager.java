package manager;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private LinkedHashMap<Integer, Task> history = new LinkedHashMap<>();


    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            history.remove(task.getId());
        }
        history.put(task.getId(), task);
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    @Override
    public LinkedHashMap<Integer, Task> getHistory() {
        return history; //
    }
}
