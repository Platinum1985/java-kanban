package Test;

import model.Epic;
import model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void epicIDTaskEquals() {
        Epic e1 = new Epic("Имя1", Status.NEW, "В теплые края", "2025.12.27 00:03", 0L);
        Epic e2 = new Epic("Name2", Status.NEW, "В теплые края", "2025.12.27 00:03", 0L);
        e1.setId(1);
        e2.setId(1);
        assertTrue(e1.equals(e2), "Объекты с одинаковыми ID не равны!");
    }
}

