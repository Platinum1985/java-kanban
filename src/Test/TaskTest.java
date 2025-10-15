package Test;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void testIDTaskEquals() {
        Task t1 = new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L);
        Task t2 = new Task("2 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 6L);
        t1.setId(0);
        t2.setId(0);
        assertTrue(t1.equals(t2), "Объекты с одинаковыми ID не равны!");
    }
}