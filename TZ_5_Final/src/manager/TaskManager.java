package manager;

import model.Epic;
import model.SubTask;
import model.Task;

public interface TaskManager {





    void addEpics(Epic epic);

    void addSubTask(SubTask subTask, int epicId);

    String printAllTasks();

    String printAllEpics();

    String printAllSubTasks();

    String getTaskByID(int id);

    String getEpicByID(int id);

    String getSubTaskByID(int id);

    void updateTask(Task newTask, int id);

    void updateEpic(Epic newEpic, int id);

    void updateSubTask(SubTask newST, int id);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubTask(int id);
}
