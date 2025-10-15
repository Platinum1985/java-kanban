import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
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
    public static void main(String[] args) {
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
            System.out.println(loadFile.printAllTasks());
            System.out.println(loadFile.printAllEpics());
            System.out.println(loadFile.printAllSubTasks());
            loadFile.addTask(new Task("0 Переезд", "В теплые края", Status.NEW));
            loadFile.addTask(new Task("1 Переезд", "В теплые края", Status.NEW));
            loadFile.addEpics(new Epic("2 Переезд", "В теплые края", Status.NEW));
            loadFile.addEpics(new Epic("3 Епотека", "Особняк на берегу моря", Status.NEW));
            loadFile.addEpics(new Epic("4 вернуть", "hfjdk", Status.NEW));
            /* loadFile.addSubTask(new SubTask("5 подстрока1", "описание1", Status.NEW), 4);
            loadFile.addSubTask(new SubTask("6 подстрока2", "описание2", Status.NEW), 3);
            loadFile.addTask(new Task("После 1 сохранения", "В теплые края", Status.NEW));*/
            System.out.println(loadFile.printAllTasks());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}