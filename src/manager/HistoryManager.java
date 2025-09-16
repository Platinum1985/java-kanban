package manager;

import model.Task;

import java.util.Collection;
import java.util.LinkedHashMap;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    LinkedHashMap<Integer, Task> getHistory();
}
