package Test;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void testIDTaskEquals() {
        Task t1 = new Task("Имя1", "sdfg", Status.NEW);
        Task t2 = new Task("Name2", "fjg", Status.DONE);
        t1.setId(0);
        t2.setId(0);
        assertTrue(t1.equals(t2), "Объекты с одинаковыми ID не равны!");
    }
}