package Test;

import manager.FileBackedTaskManager;
import manager.TimeOverlapException;
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
        manager = FileBackedTaskManager.create(tempFile.getAbsolutePath());
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
    void testSaveMultipleTasks() throws IOException, TimeOverlapException {
        manager.addTask(new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L));
        manager.addTask(new Task("2 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 6L));
        manager.addTask(new Task("8 ...ть", Status.NEW, "hfgfjd123dk", "2026.01.29 01:03", 856L));

        manager.save();
    }

    @Test
    void testLoadMultipleTasks() throws IOException, TimeOverlapException {
        manager.addTask(new Task("Задача 1", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L));
        manager.addTask(new Task("Задача 2", Status.NEW, "В теплые края", "2025.12.27 00:03", 6L));
        manager.addTask(new Task("Задача 3", Status.NEW, "hfgfjd123dk", "2026.01.29 01:03", 856L));

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(loadedManager.printAllTasks().contains("Задача 1"));
        assertTrue(loadedManager.printAllTasks().contains("Задача 2"));
        assertTrue(loadedManager.printAllTasks().contains("Задача 3"));
    }
}