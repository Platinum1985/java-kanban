package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import model.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    public int idTask = 0;// Счётчик идентификаторов
    public int idEpic = 0;
    public int idSubTask = 0;
    public HashMap<Integer, Task> tasks = new HashMap(); // Список задач
    public HashMap<Integer, Epic> epics = new HashMap();
    public HashMap<Integer, SubTask> subTasks = new HashMap<>();
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
    public ArrayList<Task> getHistory() {
     return historyManager.getHistory();
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
        epics.put(idEpic, epic);
        epic.setId(idEpic);
        idEpic++;
    }

    @Override
    public void addSubTask(SubTask subTask, int epicId) {
subTask.setId(idSubTask);

            subTasks.put(idSubTask, subTask);
            Epic epic = epics.get(epicId);
            if (epic != null) {
                epic.subTaskIds.add(idSubTask);
                idSubTask++;
            }
          Epic e =  epics.get(epicId);
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
    public String getTaskByID(int id) {
/* if (history.size()<10) {
    history.add(tasks.get(id));
} else {
    history.add(tasks.get(id));
    history.remove(0);

}*/ historyManager.add(tasks.get(id));

        return tasks.get(id).toString();
    }

    @Override
    public String getEpicByID(int id) {
        /*if (history.size()<10) {
            history.add(epics.get(id));
        } else {
            history.add(epics.get(id));
            history.remove(0);
        } */
        historyManager.add(epics.get(id));
        return epics.get(id).toString();
    }

    @Override
    public String getSubTaskByID(int id) {
      /*  if (history.size()<10) {
            history.add(subTasks.get(id));
        } else {
            history.add(subTasks.get(id));
            history.remove(0);
        } */
        historyManager.add(subTasks.get(id));
        return subTasks.get(id).toString();
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
}