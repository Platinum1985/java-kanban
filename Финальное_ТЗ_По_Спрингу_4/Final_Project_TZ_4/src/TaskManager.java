import java.util.HashMap;

public class TaskManager {
    int idTask = 0;// Счётчик идентификаторов
    int idEpic = 0;
    int idSubTask = 0;
    HashMap<Integer, Task> tasks = new HashMap(); // Список задач
    HashMap<Integer, Epic> epics = new HashMap();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();


    void addTask(int i, Task task) {
        tasks.put(i, task);
        idTask++;

    }

    void addEpics(Integer i, Epic epic) {
        epics.put(i, epic);
        idEpic++;
    }

    void addSubTask(int subTaskId, SubTask subTask, int epicId) {
        subTasks.put(subTaskId, subTask);
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.addSubTaskId(subTaskId);
        }
        idSubTask++;
    }

    String checkStatusEpic(Epic e) { //получение статуса эпика
        int size = e.subTaskIds.size();
        int countProgress = 0;
        int countDone = 0;
        for (int i : e.subTaskIds) {
            if (subTasks.get(i).st.equals(Status.IN_PROGRESS) ||
                    subTasks.get(i).st.equals(Status.DONE)) {
                countProgress++;
            }
            if (subTasks.get(i).st.equals(Status.DONE)) {
                countDone++;
            }
        }
        if (countDone == size) {
            e.st = Status.DONE;
        } else if (countDone < size && countProgress > 0) {
            e.st = Status.IN_PROGRESS;
        } else {
            e.st=Status.NEW;
        }
return e.st.toString();
    }

    void clearTask(HashMap<Integer, Task> hashMap) {
        hashMap.clear();
    }

    void clearEpics() {
        epics.clear();
        subTasks.clear(); //если эпиков нет, то и подзадачи ненужны

    }

    void clearSubTasks() {
        for (Epic e : epics.values())
            e.subTaskIds.clear();
        subTasks.clear();

    }

    String printAllTasks() {
        String result = "";
        for (Task t : tasks.values()) {
            result += t.toString() + "\n";
        }
        return result;

    }

    String printAllEpics() {
        String result = "";
        for (Epic e : epics.values()) {
            result += e.toString() + "\n";
        }
        return result;

    }

    String printAllSubTasks() {
        String result = "";
        for (SubTask st : subTasks.values()) {
            result += st.toString() + "\n";
        }
        return result;
    }

    String getTaskByID(int id) {
        return tasks.get(id).toString();
    }

    String getEpicByID(int id) {
        return epics.get(id).toString();
    }

    String getSubTaskByID(int id) {
        return subTasks.get(id).toString();
    }

    void updateTask(Task newTask, int id) {
        tasks.put(id, newTask);

    }

    void updateEpic(Epic newEpic, int id) {
        epics.put(id, newEpic);

    }

    void updateSubTask(SubTask newST, int id) {
        subTasks.put(id, newST);
        for (Epic e : epics.values()) {
            for(int j:e.subTaskIds) {
                if(j==id)
                    this.checkStatusEpic(e);
            }

        }


    }

    void deleteTask(int id) {
        tasks.remove(id);
    }

    void deleteEpic(int id) {
        for (int j:epics.get(id).subTaskIds)//удаляем подзадач эпика из HashMap
        {
            subTasks.remove(j);
        }
        epics.remove(id);
    }
    void deleteSubTask(int id) {
        for (Epic e : epics.values()) {
            e.subTaskIds.removeIf(j -> j == id);
            this.checkStatusEpic(e);
        }
        subTasks.remove(id);
    }
}