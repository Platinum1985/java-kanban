package Test;

import manager.TaskManager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    protected abstract T createTaskManager();

    @Test
    void testAddTask() {
        // Пример теста для добавления задачи
        Task task = new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L);
        taskManager.addTask(task);
        taskManager.getTaskById(task.getId());
        Assertions.assertTrue(taskManager.getHistory().contains(task));
    }
    @Test
    void testEpicStatusAllNew() {
        Epic epic = new Epic("1 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 0L);
        taskManager.addEpics(epic);
        SubTask subTask1 = new SubTask("2 вернуть", Status.NEW, "hf1dk", "2025.11.27 01:03", 75L );
        taskManager.addSubTask(subTask1,epic.getId());
        SubTask subTask2 = new SubTask("3 xnj nj", Status.NEW, "h2fdk", "2025.11.27 01:03", 16L);
        taskManager.addSubTask(subTask2, epic.getId());
        Assertions.assertEquals(Status.NEW, epic.getSt());
    }
    @Test
    void testEpicStatusAllDone() {
        Epic epic = new Epic("1 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 0L);
        taskManager.addEpics(epic);
        SubTask subTask1 = new SubTask("2 вернуть", Status.DONE, "hf1dk", "2025.11.27 01:03", 75L );
        taskManager.addSubTask(subTask1,epic.getId());
        SubTask subTask2 = new SubTask("3 xnj nj", Status.DONE, "h2fdk", "2025.11.27 01:03", 16L);
        taskManager.addSubTask(subTask2, epic.getId());
        Assertions.assertEquals(Status.DONE, epic.getSt());
    }
    @Test
    void testEpicStatusNewDone() {
        Epic epic = new Epic("1 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 0L);
        taskManager.addEpics(epic);
        SubTask subTask1 = new SubTask("2 вернуть", Status.NEW, "hf1dk", "2025.11.27 01:03", 75L );
        taskManager.addSubTask(subTask1,epic.getId());
        SubTask subTask2 = new SubTask("3 xnj nj", Status.DONE, "h2fdk", "2025.11.27 01:03", 16L);
        taskManager.addSubTask(subTask2, epic.getId());
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getSt());
    }
    @Test
    void testEpicStatusInProgress() {
        Epic epic = new Epic("1 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 0L);
        taskManager.addEpics(epic);
        SubTask subTask1 = new SubTask("2 вернуть", Status.IN_PROGRESS, "hf1dk", "2025.11.27 01:03", 75L );
        taskManager.addSubTask(subTask1,epic.getId());
        SubTask subTask2 = new SubTask("3 xnj nj", Status.IN_PROGRESS, "h2fdk", "2025.11.27 01:03", 16L);
        taskManager.addSubTask(subTask2, epic.getId());
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getSt());
    }


    // Другие общие тесты...
}
