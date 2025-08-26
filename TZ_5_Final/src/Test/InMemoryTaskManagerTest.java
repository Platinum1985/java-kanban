package Test;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    void testEpicCannotAddSelfAsSubtask() {
        TaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("Название", "Описание", Status.NEW);
        // Пытаемся добавить Epic в качестве подзадачи в себя и проверяем, что выбрасывается исключение
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            manager.addSubTask(epic, epic.getId());
        });
    }

    @Test
    void testSubTaskCannotAddSelfAsSubtask() {
        TaskManager manager = new InMemoryTaskManager();
        SubTask subTask1 = new SubTask("Название", "Описание", Status.NEW);
        SubTask subTask2 = new SubTask("Название2", "Описание2", Status.NEW);
        assertThrowsExactly(NullPointerException.class, () -> {
            manager.addSubTask(subTask2, subTask1.getId());
        });
    }

    @Test
    public void islookingATaskById() {
        TaskManager manager = new InMemoryTaskManager();
        // Создаем задачи
        Task task = new Task("TName", "T D", Status.NEW);
        Epic epic = new Epic("EName", "E D", Status.NEW);
        SubTask subTask = new SubTask("S N", "S D", Status.NEW);
        // Добавляем задачи в менеджер
        manager.addTask(task);
        manager.addEpics(epic);
        manager.addSubTask(subTask, 0); // Пример связи подзадачи с эпиком
        // Получаем задачи по ID
        String taskById = manager.getTaskById(0);
        String epicById = manager.getEpicById(0);
        String subTaskById = manager.getSubTaskById(0);
        // Проверяем, что задачи были добавлены и получены корректно
        assertEquals(task.toString(), taskById);
        assertEquals(epic.toString(), epicById);
        assertEquals(subTask.toString(), subTaskById);
    }

    @Test
    void testUniqueIds() {
        TaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("Название задачи 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Название задачи 2", "Описание задачи 2", Status.NEW);
        // Добавляем задачи с уникальными id
        manager.addTask(task1);
        manager.addTask(task2);
        // Проверяем, что id задач уникальны
        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    void testTaskImmutabilityAfterAdding() {
        TaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Название задачи", "Описание задачи", Status.NEW);
        Epic epic = new Epic("vb ", "dfg", Status.IN_PROGRESS);
        SubTask subTask = new SubTask("edc", "rtg,", Status.DONE);
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
        Task addedTask = ((InMemoryTaskManager) manager).getTasks().get(0);

        // Проверяем, что поля задачи остались неизменными (кроме идентификатора)
        assertEquals(initialName, addedTask.getName());
        assertEquals(initialDescription, addedTask.getDescription());
        assertEquals(initialStatus, addedTask.getSt());

        // Добавляем задачу в менеджер
        manager.addEpics(epic);

        // Получаем задачу из менеджера
        Epic addedEpic = ((InMemoryTaskManager) manager).getEpics().get(0);

        // Проверяем, что поля задачи остались неизменными (кроме идентификатора)
        assertEquals(initialNameEpic, addedEpic.getName());
        assertEquals(initialDescriptionEpic, addedEpic.getDescription());
        assertEquals(initialStatusEpic, addedEpic.getSt());

        manager.addSubTask(subTask, 0);

        // Получаем задачу из менеджера
        SubTask addedSubTask = ((InMemoryTaskManager) manager).getSubTasks().get(0);

        assertEquals(initialNameSubTask, addedSubTask.getName());
        assertEquals(initialDescriptionSubTask, addedSubTask.getDescription());
        assertEquals(initialStatusSubTask, addedSubTask.getSt());
    }


}