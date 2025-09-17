package model;

public class Node {
    private int key;
    private Task value;
    private Node prev;
    private Node next;

    public Node(int key, Task value) {
        this.key = key;
        this.value = value;
        prev = null;
        next = null;
    }

    public int getKey() {
        return key;
    }

    public Task getValue() {
        return value;
    }

    public Node getPrev() {
        return prev;
    }

    public Node getNext() {
        return next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
