package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * An implementation of a priority queue using a list-based heap.
 */
public class HeapPriorityQueue{

    /**
     * Declare data
     */
    List<Double> data;

    /**
     * Declare comparator
     */
    private Comparator<Double> comparator;

    /**
     * Create an empty priority queue based on the natural ordering of its keys
     */
    public HeapPriorityQueue() {
        this(null);
    }

    /**
     * Create an empty priority queue using the given comparator to order keys
     * @param comparator it is used to order the elements in this priority queue
     */
    public HeapPriorityQueue(Comparator<Double> comparator) {
        data = new ArrayList<Double>();
        this.comparator = comparator;
    }

    /**
     * Add a new element in the priority queue
     * @param num The number to be added to the priority queue
     **/
    public void add(double num) {
        data.add(num);
        upHeapify(data.size() - 1);
    }

    /**
     * Retrieve and remove the head of this priority queue
     * @return the removed element
     **/
    public double remove() {
        if (data.size() == 0) {
            throw new IllegalArgumentException();
        }
        swap(0, data.size() - 1);
        double tmp = data.remove(data.size() - 1);
        downHeapify(0);
        return tmp;
    }

    /**
     * Compare element at the given index with its parent and swaps them, till the heap is balanced
     * @param i the index of target element
     **/
    private void upHeapify(int i) {
        if (i == 0) return;
        int parent = (i - 1) / 2;
        if (compare(data.get(i), data.get(parent)) < 0) {
            swap(i, parent);
            upHeapify(parent);
        }
    }

    /**
     * Compare element at the given index with its children and swaps them, till the heap is balanced.
     * @param i the index of the target element
     **/
    private void downHeapify(int i) {
        int target  = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < data.size() && compare(data.get(left), data.get(target)) < 0) {
            target = left;
        }
        if (right < data.size() && compare(data.get(right), data.get(target)) < 0) {
            target = right;
        }
        if (target != i) {
            swap(target, i);
            downHeapify(i);
        }
    }

    /**
     * Swap the element at indices i and j of the list.
     * @param i the index of the element that wants to swap
     * @param j the index of the element that wants to swap
     */
    private void swap(int i, int j) {
        Double tmp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, tmp);
    }

    /**
     * Retrieve the head of this priority queue
     * @return the head element
     **/
    public double peek() {
        if (data.size() == 0) {
            throw new IllegalArgumentException();
        }
        return data.get(0);
    }

    /**
     * Return the number of items in the priority queue
     * @return the number of items
     **/
    public int size() {
        return data.size();
    }

    /**
     * Check if the priority queue is empty
     * @return the boolean
     **/
    public boolean isEmpty() {
        return data.size() == 0;
    }

    /**
     * compare two element according to key
     * @param a the element that wants to compare
     * @param b the element that wants to compare
     * @return number: 0 if the element is equal to the other one, < 0 if the element is less than the other one, > 0 if the element is greater than the other one
     * */
    private int compare(Double a, Double b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return a.compareTo(b);
    }
}
