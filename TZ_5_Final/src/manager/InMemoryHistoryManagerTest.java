package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    public void isSaveValuesHistoryManager() {
        InMemoryTaskManager taskManager=new InMemoryTaskManager();
        Task task=new Task("dfc","azx", Status.NEW);
        Epic epic=new Epic("xc","az",Status.IN_PROGRESS);
        SubTask subTask=new SubTask("rf","aq",Status.DONE);
        taskManager.addTask(task);
        taskManager.addEpics(epic);
        taskManager.addSubTask(subTask,0);
        taskManager.getTaskByID(0);
        taskManager.getEpicByID(0);
        taskManager.getSubTaskByID(0);
        int expectedArraySize=3;
        assertEquals(taskManager.getHistory().size(),expectedArraySize);
        assertEquals(taskManager.getTaskByID(0),task.toString());
        assertEquals(taskManager.getEpicByID(0),epic.toString());
        assertEquals(taskManager.getSubTaskByID(0),subTask.toString());
    }

}