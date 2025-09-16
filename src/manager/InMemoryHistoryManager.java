package manager;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private LinkedHashMap<Integer, Task> history = new LinkedHashMap<>();


    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            history.remove(task.getId());
        }
        history.put(task.getId(), task);
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    @Override
    public LinkedHashMap<Integer, Task> getHistory() {
        return history;
    }
}


/*
package manager;

import model.Task;

public class InMemoryHistoryManager implements HistoryManager {
    private DoublyLinkedList history = new DoublyLinkedList();

    @Override
    public void add(Task task) {
        if (history.contains(task.getId())) {
            history.remove(task.getId());
        }
        history.add(task.getId(), task);
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    @Override
    public DoublyLinkedList getHistory() {
        return history;
    }
}

Также необходимо дополнить DoublyLinkedList методами contains и remove, которые будут искать и удалять элементы по ключу. Вот пример того, как это можно реализовать:

class DoublyLinkedList {
    Node head;
    Node tail;

    public DoublyLinkedList() {
        head = null;
        tail = null;
    }

    // Добавление узла в конец списка
    public void add(Integer key, Task task) {
        Node newNode = new Node(key, task);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    // Проверка наличия узла по ключу
    public boolean contains(Integer key) {
        Node current = head;
        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Удаление узла по ключу
    public void remove(Integer key) {
        Node current = head;
        while (current != null) {
            if (current.key.equals(key)) {
                if (current == head) {
                    head = current.next;
                } else {
                    current.prev.next = current.next;
                }
                if (current == tail) {
                    tail = current.prev;
                } else {
                    current.next.prev = current.prev;
                }
                break;
            }
            current = current.next;
        }
    }
}
 */