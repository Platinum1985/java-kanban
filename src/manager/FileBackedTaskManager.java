package manager;

import model.*;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager{
    private String filePath;


    public FileBackedTaskManager(String filePath) {
        this.filePath = filePath; }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            String title = "id,type,name,status,description,epic";
            bw.write(title);
            bw.newLine(); // Добавляем новую строку после заголовка

            Map<Integer, Task> allTasks = new HashMap<>();
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
                    bw.write(subTask.getId() + "," + subTask.getTaskType() + "," + subTask.getName() + "," + subTask.getSt() + "," + subTask.getDescription() + "," + subTask.getYourEpicId());
                    bw.newLine();
                } else{
                    bw.write(task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getSt() + "," + task.getDescription() + ",");
                    bw.newLine(); // Добавляем новую строку после каждой записи
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager("C:\\Users\\1\\Desktop\\AllTasks.csv");

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
                TaskType type =TaskType.valueOf(parts[1]);
                String name = parts[2];
                Status status =Status.valueOf(parts[3]);
                String description = parts[4];
                Integer epicId = parts.length > 5 && !parts[5].isEmpty() ? Integer.parseInt(parts[5]) : null;

                switch (type) {
                    case TaskType.TASK:
                        manager.getTasks().put(id, new Task(id,type, name, status, description));
                        break;
                    case TaskType.EPIC:
                        manager.getEpics().put(id, new Epic(id, type, name, status, description));
                        break;
                    case TaskType.SUBTASK:
                        SubTask subTask = new SubTask(id,type, name, status, description,epicId);
                        subTask.setYourEpicId(epicId);
                        manager.getSubTasks().put(id, subTask);
                        break;
                }

            }
        }

        return manager;
    }

    @Override
    public void addTask(Task task) {
       /* for (Task existingTask : super.getTasks().values()) {
            if (existingTask.equals(task)) {
                // Задача с такими же полями уже существует в HashMap
                System.out.println("Задача с такими же данными уже существует.");
                return; // Выходим из метода, не добавляя задачу
            }
        }
        super.getTasks().put(idTask, task);
        task.setId(idTask);
        task.setTaskType(TaskType.TASK);
        idTask++;*/
        super.addTask(task);
        save();
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
    public void addSubTask(SubTask subTask, int epicId) {
     super.addSubTask(subTask,epicId);
     save();
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
     return   super.getTaskById(id);
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






}
