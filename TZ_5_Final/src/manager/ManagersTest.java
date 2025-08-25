package manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @Test
    public void testGetDefaultTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "TaskManager должен быть проинициализирован");
        // Дополнительно можно проверить, что taskManager имеет ожидаемые свойства или методы
    }

    @Test
    public void testGetDefaultHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "HistoryManager должен быть проинициализирован");
        // Дополнительно можно проверить, что historyManager имеет ожидаемые свойства или методы
    }
}