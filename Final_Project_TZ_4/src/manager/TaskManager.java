package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import model.Status;

import java.util.HashMap;

public class TaskManager {
    public int idTask = 0;// Счётчик идентификаторов
    public int idEpic = 0;
    public int idSubTask = 0;
    public HashMap<Integer, Task> tasks = new HashMap(); // Список задач
    public HashMap<Integer, Epic> epics = new HashMap();
    public HashMap<Integer, SubTask> subTasks = new HashMap<>();


    public void addTask(int i, Task task) {
        tasks.put(i, task);
        idTask++;

    }

    public void addEpics(Integer i, Epic epic) {
        epics.put(i, epic);
        idEpic++;
    }

    public void addSubTask(int subTaskId, SubTask subTask, int epicId) {
        subTasks.put(subTaskId, subTask);
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.addSubTaskId(subTaskId);
        }
        idSubTask++;
    }

    public String checkStatusEpic(Epic e) { //получение статуса эпика
        int size = e.subTaskIds.size();
        int countProgress = 0;
        int countDone = 0;
        for (int i : e.subTaskIds) {
            if (subTasks.get(i).getSt().equals(Status.IN_PROGRESS) ||
                    subTasks.get(i).getSt().equals(Status.DONE)) {
                countProgress++;
            }
            if (subTasks.get(i).getSt().equals(Status.DONE)) {
                countDone++;
            }
        }
        if (countDone == size) {
            e.setSt(Status.DONE);
        } else if (countDone < size && countProgress > 0) {
            e.setSt(Status.IN_PROGRESS);
        } else {
            e.setSt(Status.NEW);
        }
        return e.getSt().toString();
    }

    public void clearTask(HashMap<Integer, Task> hashMap) {
        hashMap.clear();
    }

    public void clearEpics() {
        epics.clear();
        subTasks.clear(); //если эпиков нет, то и подзадачи ненужны

    }

    void clearSubTasks() {
        for (Epic e : epics.values())
            e.subTaskIds.clear();
        subTasks.clear();

    }

    public String printAllTasks() {
        String result = "";
        for (Task t : tasks.values()) {
            result += t.toString() + "\n";
        }
        return result;

    }

    public String printAllEpics() {
        String result = "";
        for (Epic e : epics.values()) {
            result += e.toString() + "\n";
        }
        return result;

    }

    public String printAllSubTasks() {
        String result = "";
        for (SubTask st : subTasks.values()) {
            result += st.toString() + "\n";
        }
        return result;
    }

    public String getTaskByID(int id) {
        return tasks.get(id).toString();
    }

    public String getEpicByID(int id) {
        return epics.get(id).toString();
    }

    public String getSubTaskByID(int id) {
        return subTasks.get(id).toString();
    }

    public void updateTask(Task newTask, int id) {
        tasks.put(id, newTask);

    }

    public void updateEpic(Epic newEpic, int id) {
        epics.put(id, newEpic);

    }

    public void updateSubTask(SubTask newST, int id) {
        subTasks.put(id, newST);
        for (Epic e : epics.values()) {
            for (int j : e.subTaskIds) {
                if (j == id)
                    this.checkStatusEpic(e);
            }

        }


    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        for (int j : epics.get(id).subTaskIds)//удаляем подзадач эпика из HashMap
        {
            subTasks.remove(j);
        }
        epics.remove(id);
    }

    public void deleteSubTask(int id) {
        for (Epic e : epics.values()) {
            e.subTaskIds.removeIf(j -> j == id);
            this.checkStatusEpic(e);
        }
        subTasks.remove(id);
    }
}