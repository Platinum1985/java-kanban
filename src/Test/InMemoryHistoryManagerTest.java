package Test;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import manager.TimeOverlapException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    public void isSaveValuesHistoryManager() throws TimeOverlapException {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task = new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L);
        Epic epic = new Epic("4 Епотека", Status.NEW, "Особняк на берегу моря", "2025.10.27 00:06",5l);
        SubTask subTask = new SubTask("6 вернуть", Status.NEW, "hfdk", "2025.11.27 01:03", 75L);

        taskManager.addTask(task);
        taskManager.addEpics(epic);
        taskManager.addSubTask(subTask, 2);
        assertTrue(historyManager.getHistory().isEmpty());//если задачи не просмотрены-historyManager должен быть пуст
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubTaskById(3);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(3);
        taskManager.getEpicById(2);

        ArrayList<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task);
        expectedHistory.add(subTask);
        expectedHistory.add(epic);

        int expectedArraySize = 3;
        assertEquals(taskManager.getHistory().size(), expectedArraySize);

        // Проверим каждый элемент истории
        Collection<Task> history = taskManager.getHistory();
        int i = 0;
        for (Task actualTask : history) {
            assertEquals(actualTask, expectedHistory.get(i));
            i++;
        }

    }

}