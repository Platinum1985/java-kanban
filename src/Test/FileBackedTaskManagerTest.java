package Test;

import manager.FileBackedTaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("temp", ".csv");
        manager = new FileBackedTaskManager(tempFile.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    void testSaveAndLoadEmptyFile() throws IOException {
        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(loadedManager.printAllTasks().isEmpty());
    }

    @Test
    void testSaveMultipleTasks() throws IOException {
        manager.addTask(new Task("Задача 1", "Описание задачи 1", Status.NEW));
        manager.addTask(new Task("Задача 2", "Описание задачи 2", Status.IN_PROGRESS));
        manager.addTask(new Task("Задача 3", "Описание задачи 3", Status.DONE));

        manager.save();
    }

    @Test
    void testLoadMultipleTasks() throws IOException {
        manager.addTask(new Task("Задача 1", "Описание задачи 1", Status.NEW));
        manager.addTask(new Task("Задача 2", "Описание задачи 2", Status.IN_PROGRESS));
        manager.addTask(new Task("Задача 3", "Описание задачи 3", Status.DONE));

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(loadedManager.printAllTasks().contains("Задача 1"));
        assertTrue(loadedManager.printAllTasks().contains("Задача 2"));
        assertTrue(loadedManager.printAllTasks().contains("Задача 3"));
    }
}