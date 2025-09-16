package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    public int idTask = 0;// Счётчик идентификаторов
    private Map<Integer, Task> tasks = new HashMap(); // Список задач
    private Map<Integer, Epic> epics = new HashMap();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    // public ArrayList<Task> history=new ArrayList<>();
    HistoryManager historyManager = Managers.getDefaultHistory();


    public void addTask(Task task) {
        for (Task existingTask : tasks.values()) {
            if (existingTask.equals(task)) {
                // Задача с такими же полями уже существует в HashMap
                System.out.println("Задача с такими же данными уже существует.");
                return; // Выходим из метода, не добавляя задачу
            }
        }
        tasks.put(idTask, task);
        task.setId(idTask);
        idTask++;
    }

    @Override
    public Collection<Task> getHistory() {
        return historyManager.getHistory().values();
    }

    @Override
    public void addEpics(Epic epic) {
        for (Epic existingEpic : epics.values()) {
            if (existingEpic.equals(epic)) {
                // Задача с такими же полями уже существует в HashMap
                System.out.println("Задача с такими же данными уже существует.");
                return; // Выходим из метода, не добавляя задачу
            }
        }
        epics.put(idTask, epic);
        epic.setId(idTask);
        idTask++;
    }

    @Override
    public void addSubTask(SubTask subTask, int epicId) {
        subTask.setId(idTask);

        subTasks.put(idTask, subTask);
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.subTaskIds.add(idTask);
            idTask++;
        }
        Epic e = epics.get(epicId);
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

    @Override
    public String printAllTasks() {
        String result = "";
        for (Task t : tasks.values()) {
            result += t.toString() + "\n";
        }
        return result;

    }

    @Override
    public String printAllEpics() {
        String result = "";
        for (Epic e : epics.values()) {
            result += e.toString() + "\n";
        }
        return result;

    }

    @Override
    public String printAllSubTasks() {
        String result = "";
        for (SubTask st : subTasks.values()) {
            result += st.toString() + "\n";
        }
        return result;
    }

    @Override
    public String getTaskById(int id) {
        if (tasks.get(id) == null) {
            return "Задачи с таким id нет";
        } else {
            historyManager.add(tasks.get(id));
            return tasks.get(id).toString();
        }
    }

    @Override
    public String getEpicById(int id) {
        if (epics.get(id) == null) {
            return "Эпика с таким id нет";
        } else {
            historyManager.add(epics.get(id));
            return epics.get(id).toString();
        }
    }

    @Override
    public String getSubTaskById(int id) {
        if (subTasks.get(id) == null) {
            return "Подзадачи с таким id нет";
        } else {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id).toString();
        }
    }

    @Override
    public void updateTask(Task newTask, int id) {
        tasks.put(id, newTask);

    }

    @Override
    public void updateEpic(Epic newEpic, int id) {
        epics.put(id, newEpic);

    }

    @Override
    public void updateSubTask(SubTask newST, int id) {
        subTasks.put(id, newST);
        for (Epic e : epics.values()) {
            for (int j : e.subTaskIds) {
                if (j == id)
                    this.checkStatusEpic(e);
            }

        }


    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        for (int j : epics.get(id).subTaskIds)//удаляем подзадач эпика из HashMap
        {
            subTasks.remove(j);
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        for (Epic e : epics.values()) {
            e.subTaskIds.removeIf(j -> j == id);
            this.checkStatusEpic(e);
        }
        subTasks.remove(id);
    }

    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }


    public Map<Integer, Epic> getEpics() {
        return epics;
    }


    public Map<Integer, Task> getTasks() {
        return tasks;
    }

}