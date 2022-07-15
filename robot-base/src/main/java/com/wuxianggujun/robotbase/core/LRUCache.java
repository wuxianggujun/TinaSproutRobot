package com.wuxianggujun.robotbase.core;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<T> {
    private final int capacity;
    private int size;
    private final Map<String, Node> hashMap;

    private final DoublyLinkedList internalQueue;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.hashMap = new HashMap<>();
        this.internalQueue = new DoublyLinkedList();
        this.size = 0;
    }

    public T get(final String key) {
        Node node = hashMap.get(key);
        if (node == null) {
            return null;
        }
        internalQueue.modeNodeToFront(node);
        return hashMap.get(key).value;
    }

    public void put(final String key, final T value) {
        Node currentNode = hashMap.get(key);
        if (currentNode != null) {
            currentNode.value = value;
            internalQueue.modeNodeToFront(currentNode);
            return;
        }

        if (size == capacity) {
            String rearNodeKey = internalQueue.getRearKey();
            internalQueue.removeNodeRear();
            hashMap.remove(rearNodeKey);
            size--;
        }

        Node node = new Node(key, value);
        internalQueue.addNodeToFront(node);
        hashMap.put(key, node);
        size++;
    }

    private class Node {
        String key;
        T value;
        Node next, prev;

        public Node(final String key, final T value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.prev = null;
        }
    }

    private class DoublyLinkedList {
        private Node front, rear;

        public DoublyLinkedList() {
            front = rear = null;
        }

        private void addNodeToFront(final Node node) {
            if (rear == null) {
                front = rear = node;
                return;
            }
            node.next = front;
            front.prev = node;
            front = node;
        }

        public void modeNodeToFront(final Node node) {
            if (front == node) {
                return;
            }
            if (node == rear) {
                rear = rear.prev;
                rear.next = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
            node.prev = null;
            node.next = front;
            front.prev = node;
            front = node;

        }

        private void removeNodeRear() {
            if (rear == null) {
                return;
            }
            System.out.println("Deleting key:"+rear.key);
            
            if (front == rear){
                front =  rear = null;
            }else {
                rear = rear.prev;
                rear.next = null;
            }

        }

        public String getRearKey() {
             return rear.key;
        }
    }
}