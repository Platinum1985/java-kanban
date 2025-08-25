package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    public void testIDTaskEquals() {
        SubTask st1 = new SubTask("Имя1", "sdfg", Status.NEW);
        SubTask st2 = new SubTask("Name2", "fjg", Status.DONE);
        assertTrue(st1.equals(st2), "Объекты с одинаковыми ID не равны!");
    }
}

