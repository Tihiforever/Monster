package io.github.some_example_name;

public class PriorityQue {

    private final int[] queue;
    private final int[] priority;
    private int size;

    public PriorityQue(int capacity) {
        queue = new int[capacity];
        priority = new int[capacity];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == queue.length;
    }

    public int length() {
        return size;
    }

    // ---------- ADD (uses Bubble Sort) ----------
    public void push(int value, int pri) {
        if (isFull()) {
            System.out.println("Queue is full");
            return;
        }

        queue[size] = value;
        priority[size] = pri;
        size++;

        // Bubble sort by priority
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (priority[j] > priority[j + 1]) {
                    swap(j, j + 1);
                }
            }
        }
    }

    // ---------- REMOVE ----------
    public int pop() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return -1;
        }

        int value = queue[0];

        // Shift left
        for (int i = 0; i < size - 1; i++) {
            queue[i] = queue[i + 1];
            priority[i] = priority[i + 1];
        }

        size--;
        return value;
    }

    // ---------- PRINT ----------
    public void print() {
        for (int i = 0; i < size; i++) {
            System.out.print("(" + queue[i] + ", p=" + priority[i] + ") ");
        }
        System.out.println();
    }

    // ---------- SWAP ----------
    private void swap(int i, int j) {
        int tempVal = queue[i];
        queue[i] = queue[j];
        queue[j] = tempVal;

        int tempPri = priority[i];
        priority[i] = priority[j];
        priority[j] = tempPri;
    }
}
