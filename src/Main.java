import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.TimeOverlapException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws TimeOverlapException {
        String pathName = "C:\\Users\\1\\Desktop\\AllTasks.csv";
        File file = new File(pathName);
        try {
            if (file.createNewFile())
                System.out.println("Файл успешно создан: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile(file);
            // Теперь вы можете использовать manager для работы с загруженными данными
            loadFile.idTask = loadFile.getTasks().size() + loadFile.getSubTasks().size() + loadFile.getEpics().size() + 1;
            System.out.println("Все Task: "+loadFile.printAllTasks());
            System.out.println("Все Epic: "+loadFile.printAllEpics());
            System.out.println("Все SubTask: "+loadFile.printAllSubTasks());
            loadFile.addTask(new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L));
            loadFile.addTask(new Task("2 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 6L));
            loadFile.addEpics(new Epic("3 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 0L));
            loadFile.addEpics(new Epic("4 Епотека", Status.NEW, "Особняк на берегу моря", "2025.10.27 00:06", 0L));
            loadFile.addEpics(new Epic("5 вернуть", Status.NEW, "hfjdk", "2025.12.27 01:03", 0L));
            loadFile.addSubTask(new SubTask("6 вернуть", Status.NEW, "hfdk", "2025.01.27 01:03", 75L), 4);
            loadFile.addSubTask(new SubTask("7 ...ть", Status.NEW, "hfgfjddk", "2025.10.29 01:03", 85L), 4);
            loadFile.addTask(new Task("8 ...ть", Status.NEW, "hfgfjd123dk", "2026.01.29 01:03", 856L));
            loadFile.addSubTask(new SubTask("9 34 ...ть", Status.NEW, "hfgfhjkjddk", "2025.10.21 01:03", 15L), 3);
            //loadFile.addTask(new Task("9 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 6L));
            System.out.println(loadFile.printAllTasks());
            System.out.println(loadFile.getPrioritizedTasks());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}