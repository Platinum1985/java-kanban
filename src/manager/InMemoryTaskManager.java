package manager;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    public int idTask = 1;// Счётчик идентификаторов

    public void setTasks(Map<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    public void setEpics(Map<Integer, Epic> epics) {
        this.epics = epics;
    }

    public void setSubTasks(Map<Integer, SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    private Map<Integer, Task> tasks = new HashMap(); // Список задач
    private Map<Integer, Epic> epics = new HashMap();
    private Map<Integer, SubTask> subTasks = new HashMap();
    HistoryManager historyManager = Managers.getDefaultHistory();
    private Set<Task> sortedTasks = new TreeSet<>(Comparator.comparing((Task t) -> t.getStartTime())
            .thenComparing(t -> t.getId()));


    public void addTask(Task task) throws TimeOverlapException {
        for (Task existingTask : tasks.values()) {
            if (existingTask.equals(task)) {
                System.out.println("Задача с такими же данными уже существует.");
                return; // Выходим из метода, не добавляя задачу
            }
        }
        boolean hasOverlap = tasks.values().stream()
                .flatMap(existingTask -> subTasks.values().stream()
                         .map(subTask -> isTimeOverlap(existingTask, subTask)))
                .anyMatch(result -> result);
        if (hasOverlap) {
            throw new  TimeOverlapException("Интервал времени выполнения добавляемой задачи совпадает с интервалом другой задачи");
        }
        task.setId(idTask);
        task.setTaskType(TaskType.TASK);
        tasks.put(idTask, task);
        sortedTasks.add(task);
        idTask++;
    }

    @Override
    public Collection<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void addEpics(Epic epic) {
        for (Epic existingEpic : epics.values()) {
            System.out.println(epics.values());
            if (existingEpic.equals(epic)) {
                // Задача с такими же полями уже существует в HashMap
                System.out.println("Задача с такими же данными уже существует.");
                return; // Выходим из метода, не добавляя задачу
            }
        }

        epic.setId(idTask);
        System.out.println(idTask);
        epic.setTaskType(TaskType.EPIC);
        epics.put(idTask, epic);
        idTask++;
    }

    @Override
    public void addSubTask(SubTask subTask, int epicId) throws TimeOverlapException {
        boolean hasOverlap = tasks.values().stream()
                .flatMap(task -> subTasks.values().stream()
                        .map(subTaskFromMap -> isTimeOverlap(task, subTask)))
                .anyMatch(result -> result);
        if (hasOverlap) {
          throw new  TimeOverlapException("Интервал времени выполнения добавляемой задачи совпадает с интервалом другой задачи");

        }
        subTask.setId(idTask);
        subTasks.put(idTask, subTask);
        Epic epic = epics.get(epicId);
        if (epic != null) {
            subTask.setYourEpicId(epicId);
            subTask.setTaskType(TaskType.SUBTASK);
            epic.subTaskIds.add(idTask);
            sortedTasks.add(subTask);
            idTask++;
            epic.setDuration(0L);
            epic.setStartTime(
                    subTasks.values().stream()
                            .filter(subTaskFromMap -> epic.subTaskIds.contains(subTaskFromMap.getId()))
                            .map(SubTask::getStartTime)
                            .min(Comparator.naturalOrder())
                            .orElse(null) // или другое значение по умолчанию
            );
            epic.setEndTime(
                    subTasks.values().stream()
                            .filter(subTaskFromMap -> epic.subTaskIds.contains(subTaskFromMap.getId()))
                            .map(SubTask::getStartTime)
                            .max(Comparator.naturalOrder())
                            .orElse(null) // или другое значение по умолчанию
            );
            for (Integer i : epic.subTaskIds) {
                Duration epicDuration = epic.getDuration();
                epicDuration = epicDuration.plusMinutes(subTasks.get(i).getDuration().toMinutes());
                epic.setDuration(epicDuration.toMinutes());
            }
            if (!sortedTasks.contains(epic)) {
                sortedTasks.add(epic);
            }

        }
        Epic e = epics.get(epicId);
        int size = e.subTaskIds.size();
        long countProgress = e.subTaskIds.stream()
                .map(subTasks::get)
                .filter(subTaskFromMap -> subTaskFromMap.getSt().equals(Status.IN_PROGRESS) || subTaskFromMap.getSt().equals(Status.DONE))
                .count();

        long countDone = e.subTaskIds.stream()
                .map(subTasks::get)
                .filter(sbTask -> subTask.getSt().equals(Status.DONE))
                .count();

        if (countDone == size) {
            e.setSt(Status.DONE);
        } else if (countDone < size && countProgress > 0) {
            e.setSt(Status.IN_PROGRESS);
        } else {
            e.setSt(Status.NEW);
        }
    }

    public String checkStatusEpic(Epic e) {
        int size = e.subTaskIds.size();
        long countProgress = e.subTaskIds.stream()
                .map(subTasks::get)
                .filter(subTask -> subTask.getSt().equals(Status.IN_PROGRESS) || subTask.getSt().equals(Status.DONE))
                .count();

        long countDone = e.subTaskIds.stream()
                .map(subTasks::get)
                .filter(subTask -> subTask.getSt().equals(Status.DONE))
                .count();

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
        }
        return epics.get(id).toString();
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
        sortedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        for (int j : epics.get(id).subTaskIds)//удаляем подзадач эпика из HashMap
        {
            subTasks.remove(j);
        }
        sortedTasks.remove(epics.get(id));
        epics.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        for (Epic e : epics.values()) {
            e.subTaskIds.removeIf(j -> j == id);
            this.checkStatusEpic(e);
        }
        sortedTasks.remove(subTasks.get(id));
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


    public Set<Task> getPrioritizedTasks() {
        return sortedTasks;
    }

    public boolean isTimeOverlap(Task task, Task task2) {
        LocalDateTime taskStartTime = task.getStartTime();
        LocalDateTime taskEndTime = task.getEndTime();
        LocalDateTime subTaskStartTime = task2.getStartTime();
        LocalDateTime subTaskEndTime = task2.getEndTime();

        // Проверка на пересечение интервалов
        return !(taskEndTime.isBefore(subTaskStartTime) || subTaskEndTime.isBefore(taskStartTime));
    }
}