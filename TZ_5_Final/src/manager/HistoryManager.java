package manager;

import model.Task;

import java.util.ArrayList;
import java.util.Collection;

public interface HistoryManager {
    public  void add(Task task);
    public Collection<Task> getHistory();
}
