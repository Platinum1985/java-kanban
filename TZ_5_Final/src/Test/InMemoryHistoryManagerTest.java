package Test;

import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    public void isSaveValuesHistoryManager() {
        TaskManager taskManager = Managers.getDefault();
        Task task=new Task("dfc","azx", Status.NEW);
        Epic epic=new Epic("xc","az",Status.IN_PROGRESS);
        SubTask subTask=new SubTask("rf","aq",Status.DONE);
        taskManager.addTask(task);
        taskManager.addEpics(epic);
        taskManager.addSubTask(subTask,0);
        taskManager.getTaskById(0);
        taskManager.getEpicById(0);
        taskManager.getSubTaskById(0);
        int expectedArraySize=3;
        assertEquals(taskManager.getHistory().size(),expectedArraySize);
        assertEquals(taskManager.getTaskById(0),task.toString());
        assertEquals(taskManager.getEpicById(0),epic.toString());
        assertEquals(taskManager.getSubTaskById(0),subTask.toString());
    }

}