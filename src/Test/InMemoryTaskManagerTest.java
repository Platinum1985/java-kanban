package Test;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.TimeOverlapException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
    }

     @Test
    void testEpicCannotAddSelfAsSubtask() {
        TaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("3 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 0L);
        // Пытаемся добавить Epic в качестве подзадачи в себя и проверяем, что выбрасывается исключение
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            manager.addSubTask(epic, epic.getId());
        });
    }

    @Test
    void testSubTaskCannotAddSelfAsSubtask() {
        SubTask subTask1 = new SubTask("9 34 ...ть", Status.NEW, "hfgfhjkjddk", "2025.10.21 01:03", 15L);
        SubTask subTask2 = new SubTask("3 ...ть", Status.NEW, "dk", "2025.10.25 01:03", 15L);
        assertThrowsExactly(NullPointerException.class, () -> {
            manager.addSubTask(subTask2, subTask1.getId());
        });
    }

    @Test
    public void islookingATaskById() throws TimeOverlapException {
        Task task = new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L);
        Epic epic = new Epic("5 вернуть", Status.NEW, "hfjdk", "2025.12.27 01:03", 0L);
        SubTask subTask = new SubTask("9 34 ...ть", Status.NEW, "hfgfhjkjddk", "2025.10.21 01:03", 15L);
        // Добавляем задачи в менеджер
        manager.addTask(task);
        manager.addEpics(epic);
        manager.addSubTask(subTask, epic.getId()); // Пример связи подзадачи с эпиком
        // Получаем задачи по ID
        String taskById = manager.getTaskById(1);
        String epicById = manager.getEpicById(2);
        String subTaskById = manager.getSubTaskById(3);
        // Проверяем, что задачи были добавлены и получены корректно
        assertEquals(task.toString(), taskById);
        assertEquals(epic.toString(), epicById);
        assertEquals(subTask.toString(), subTaskById);
    }

    @Test
    void testUniqueIds() throws TimeOverlapException {
        Task task1 = new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L);
        Task task2 = new Task("2 Переезд", Status.NEW, "В теплые края", "2025.11.28 00:03", 200L);
        // Добавляем задачи с уникальными id
        manager.addTask(task1);
        manager.addTask(task2);
        // Проверяем, что id задач уникальны
        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    void testTaskImmutabilityAfterAdding() throws TimeOverlapException {
        Task task = new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L);
        Epic epic = new Epic("5 вернуть", Status.NEW, "hfjdk", "2025.12.27 01:03", 0L);
        SubTask subTask = new SubTask("7 ...ть", Status.NEW, "hfgfjddk", "2025.10.29 01:03", 85L);
        // Сохраняем исходное состояние задачи
        String initialNameEpic = epic.getName();
        String initialDescriptionEpic = epic.getDescription();
        Status initialStatusEpic = epic.getSt();

        String initialName = task.getName();
        String initialDescription = task.getDescription();
        Status initialStatus = task.getSt();

        String initialNameSubTask = subTask.getName();
        String initialDescriptionSubTask = subTask.getDescription();
        Status initialStatusSubTask = subTask.getSt();
        // Добавляем задачу в менеджер
        manager.addTask(task);

        // Получаем задачу из менеджера
        Task addedTask = ((InMemoryTaskManager) manager).getTasks().get(1);

        // Проверяем, что поля задачи остались неизменными (кроме идентификатора)
        assertEquals(initialName, addedTask.getName());
        assertEquals(initialDescription, addedTask.getDescription());
        assertEquals(initialStatus, addedTask.getSt());

        // Добавляем задачу в менеджер
        manager.addEpics(epic);

        // Получаем задачу из менеджера
        Epic addedEpic = ((InMemoryTaskManager) manager).getEpics().get(2);

        // Проверяем, что поля задачи остались неизменными (кроме идентификатора)
        assertEquals(initialNameEpic, addedEpic.getName());
        assertEquals(initialDescriptionEpic, addedEpic.getDescription());
        assertEquals(initialStatusEpic, addedEpic.getSt());

        manager.addSubTask(subTask, 2);

        // Получаем задачу из менеджера
        SubTask addedSubTask = ((InMemoryTaskManager) manager).getSubTasks().get(3);

        assertEquals(initialNameSubTask, addedSubTask.getName());
        assertEquals(initialDescriptionSubTask, addedSubTask.getDescription());
        assertEquals(initialStatusSubTask, addedSubTask.getSt());//
    }

    @Test
    void testTimeOverlapTask() throws TimeOverlapException {
        // Создаем задачи с различными временными интервалами
        Task task1 = new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 10L);
        Task task2 = new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:00", 1L);
        Task task3 = new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:12", 600L);
        manager.addTask(task1);
        manager.addTask(task2);
        // Проверяем пересечение интервалов
        Assertions.assertTrue(((InMemoryTaskManager) manager).isTimeOverlap(task1, task2)); // Интервалы пересекаются
        manager.addTask(task3);
        Assertions.assertFalse(((InMemoryTaskManager) manager).isTimeOverlap(task1, task3));  // Интервалы не пересекаются
    }
    @Test
    void testTimeOverlapAll() {
        // Создаем задачи с различными временными интервалами
        Task task1 = new Task("Task", Status.NEW, "В теплые края", "2000.01.01 00:01", 10L);
        SubTask subTask1 = new SubTask("Subtask1", Status.NEW, "В теплые края", "2000.01.01 00:10", 1L);
        SubTask subTask2 = new SubTask("Subtask2", Status.NEW, "В теплые края", "2000.01.01 00:12", 1L);

        // Проверяем пересечение интервалов
        Assertions.assertFalse(((InMemoryTaskManager) manager).isTimeOverlap(task1, subTask2)); // Интервалы не пересекаются
        Assertions.assertTrue(((InMemoryTaskManager) manager).isTimeOverlap(task1, subTask1));  // Интервалы пересекаются
    }


}