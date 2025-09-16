package Test;

import model.Epic;
import model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void epicIDTaskEquals() {
        Epic e1 = new Epic("Имя1", "sdfg", Status.NEW);
        Epic e2 = new Epic("Name2", "fjg", Status.DONE);
        assertTrue(e1.equals(e2), "Объекты с одинаковыми ID не равны!");
    }
}

