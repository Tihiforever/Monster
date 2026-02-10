package io.github.some_example_name;

public class Stack {
    private final int[] data;
    private int pointer;

    // constructor
    public Stack(int size) {
        data = new int[size];
        pointer = -1;
    }

    // push an item onto the stack
    public void push(int value) {
        if (pointer == data.length - 1) {
            System.out.println("Stack overflow");
            return;
        }
        pointer++;
        data[pointer] = value;
    }

    // pop an item from the stack
    public int pop() {
        if (pointer == -1) {
            System.out.println("Stack underflow");
            return -1;
        }
        int value = data[pointer];
        pointer--;
        return value;
    }

    // look at the top item without removing it
    public int peek() {
        if (pointer == -1) {
            System.out.println("Stack is empty");
            return -1;
        }
        return data[pointer];
    }

    // check if stack is empty
    public boolean isEmpty() {
        return pointer == -1;
    }

    public int size(){
        return pointer;
    }
}

