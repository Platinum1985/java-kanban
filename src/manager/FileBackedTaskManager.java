package manager;

import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private String filePath;
    private Map<Integer, Task> allTasks = new HashMap<>();

    private FileBackedTaskManager(String filePath) {
        this.filePath = filePath;
    }

    public static FileBackedTaskManager create(String filePath) {
        return new FileBackedTaskManager(filePath);
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            String title = "id,type,name,status,description,epic,startTime,duration";
            bw.write(title);
            bw.newLine(); // Добавляем новую строку после заголовка

            for (Map.Entry<Integer, Task> entry : super.getTasks().entrySet()) {
                allTasks.put(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<Integer, Epic> entry : super.getEpics().entrySet()) {
                allTasks.put(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<Integer, SubTask> entry : super.getSubTasks().entrySet()) {
                allTasks.put(entry.getKey(), entry.getValue());
            }

            // Сохраняем данные каждой задачи в файл
            for (Task task : allTasks.values()) {
                if (task instanceof SubTask) {
                    SubTask subTask = (SubTask) task;
                    bw.write(subTask.getId() + "," + subTask.getTaskType() + "," + subTask.getName() + "," + subTask.getSt() + "," + subTask.getDescription() + "," + subTask.getYourEpicId() + "," + subTask.getStartTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")) + "," + subTask.getDuration().toMinutes());
                    bw.newLine();
                } else {
                    bw.write(task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getSt() + "," + task.getDescription() + "," + "," + task.getStartTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")) + "," + task.getDuration().toMinutes() + ",");
                    bw.newLine(); // Добавляем новую строку после каждой записи
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи данных в файл: " + e.getMessage());//исправил
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());//исправил

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true; // Флаг для первой строки

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Пропускаем первую строку
                    continue;
                }

                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                TaskType type = TaskType.valueOf(parts[1]);
                String name = parts[2];
                Status status = Status.valueOf(parts[3]);
                String description = parts[4];
                Integer epicId = parts.length > 5 && !parts[5].isEmpty() ? Integer.parseInt(parts[5]) : null;
                String localDateTime = parts[6];
                Long duration = Long.parseLong(parts[7]);

                switch (type) {
                    case TaskType.TASK:
                        manager.getTasks().put(id, new Task(id, type, name, status, description, localDateTime, duration));
                        break;
                    case TaskType.EPIC:
                        manager.getEpics().put(id, new Epic(id, type, name, status, description, localDateTime, duration));
                        break;
                    case TaskType.SUBTASK:
                        SubTask subTask = new SubTask(id, type, name, status, description, epicId, localDateTime, duration);
                        subTask.setYourEpicId(epicId);
                        manager.getSubTasks().put(id, subTask);
                        break;
                }
            }
        }

        return manager;
    }

    @Override
    public void addTask(Task task) throws TimeOverlapException {
        if (hasTimeOverlap(task)) {
            throw new TimeOverlapException("Время подзадачи пересекается с уже добавленной задачей");
        } else {
            super.addTask(task);
            save();
        }
    }

    @Override
    public Collection<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void addEpics(Epic epic) {
        super.addEpics(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask, int epicId) throws TimeOverlapException {
        if (hasTimeOverlap(subTask)) {
            throw new TimeOverlapException("Время подзадачи пересекается с уже добавленной задачей");
        } else {
            super.addSubTask(subTask, epicId);
            save();
        }
    }

    @Override
    public String checkStatusEpic(Epic e) {
        return super.checkStatusEpic(e);

    }

    @Override
    public void clearTask(HashMap<Integer, Task> hashMap) {
        super.clearTask(hashMap);
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    @Override
    public String printAllTasks() {
        return super.printAllTasks();
    }

    @Override
    public String printAllEpics() {
        return super.printAllEpics();
    }

    @Override
    public String printAllSubTasks() {
        return super.printAllSubTasks();
    }

    @Override
    public String getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public String getEpicById(int id) {
        return super.getEpicById(id);
    }

    @Override
    public String getSubTaskById(int id) {
        return super.getSubTaskById(id);
    }

    @Override
    public void updateTask(Task newTask, int id) {
        super.updateTask(newTask, id);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic, int id) {
        super.updateEpic(newEpic, id);
        save();
    }

    @Override
    public void updateSubTask(SubTask newST, int id) {
        super.updateSubTask(newST, id);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public Set getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }
    private boolean hasTimeOverlap(Task task) {
        LocalDateTime startTime = task.getStartTime();
        Duration duration = task.getDuration();
        LocalDateTime endTime = startTime.plus(duration);

        return allTasks.values().stream()
                .anyMatch(existingTask -> {
                    LocalDateTime existingStartTime = existingTask.getStartTime();
                    Duration existingDuration = existingTask.getDuration();
                    LocalDateTime existingEndTime = existingStartTime.plus(existingDuration);

                    return startTime.isBefore(existingEndTime) && endTime.isAfter(existingStartTime);
                });
    }
}
