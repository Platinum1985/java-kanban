package Test;

import model.Status;
import model.SubTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    public void testIDTaskEquals() {
        SubTask st1 = new SubTask("6 вернуть", Status.NEW, "hfdk", "2025.11.27 01:03", 75L);
        SubTask st2 = new SubTask("7 ...ть", Status.IN_PROGRESS, "hfgfjddk", "2025.10.29 01:03", 85L);
        st1.setId(2);
        st2.setId(2);
        assertTrue(st1.equals(st2), "Объекты с одинаковыми ID не равны!");
    }
}

