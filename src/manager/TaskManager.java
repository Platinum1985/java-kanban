package manager;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.Collection;

public interface TaskManager {

    Collection<Task> getHistory();

    void addTask(Task task);

    void addEpics(Epic epic);

    void addSubTask(SubTask subTask, int epicId);

    String printAllTasks();

    String printAllEpics();

    String printAllSubTasks();

    String getTaskById(int id);

    String getEpicById(int id);

    String getSubTaskById(int id);

    void updateTask(Task newTask, int id);

    void updateEpic(Epic newEpic, int id);

    void updateSubTask(SubTask newST, int id);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubTask(int id);
}
