package Test;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
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
    public void isSaveValuesHistoryManager() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task = new Task("dfc", "azx", Status.NEW);
        Epic epic = new Epic("xc", "az", Status.IN_PROGRESS);
        SubTask subTask = new SubTask("rf", "aq", Status.DONE);

        taskManager.addTask(task);
        taskManager.addEpics(epic);
        taskManager.addSubTask(subTask, 1);

        taskManager.getTaskById(0);
        taskManager.getEpicById(1);
        taskManager.getSubTaskById(2);
        taskManager.getTaskById(0);
        taskManager.getSubTaskById(2);
        taskManager.getEpicById(1);

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