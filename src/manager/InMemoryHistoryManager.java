
package manager;

import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.List;

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
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node current = history.head;
        ;
        while (current != null) {
            historyList.add(current.getValue());
            current = current.getNext();
        }
        return historyList;
    }

//Также необходимо дополнить DoublyLinkedList методами contains и remove, которые будут искать и удалять элементы по ключу. Вот пример того, как это можно реализовать:

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
                tail.setNext(newNode);
                newNode.setPrev(tail);
                tail = newNode;
            }
        }

        // Проверка наличия узла по ключу
        public boolean contains(Integer key) {
            Node current = head;
            while (current != null) {
                if (current.getKey() == (key)) {
                    return true;
                }
                current = current.getNext();
            }
            return false;
        }

        // Удаление узла по ключу
        public void remove(Integer key) {
            Node current = head;
            while (current != null) {
                if (current.getKey() == (key)) {
                    if (current == head) {
                        head = current.getNext();
                    } else {
                        current.getPrev().setNext(current.getNext());
                    }
                    if (current == tail) {
                        tail = current.getPrev();
                    } else {
                        current.getNext().setPrev(current.getPrev());
                    }
                    break;
                }
                current = current.getNext();
            }
        }
    }
}
