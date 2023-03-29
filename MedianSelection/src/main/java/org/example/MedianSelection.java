package org.example;

import java.util.Comparator;

/**
 * Compute the median of a stream of Double
 */
public class MedianSelection{

    /**
     * Declare minHeap, maxHeap
     */
    private HeapPriorityQueue minHeap, maxHeap;

    /**
     * Constructor for MedianSelection class
     * Construct a new MedianSelection object with an empty min heap and max heap.
     */
    public MedianSelection() {
        minHeap = new HeapPriorityQueue();
        maxHeap = new HeapPriorityQueue(Comparator.reverseOrder());
    }

    /**
     * Return the minimum heap containing the values
     * @return the minimum heap
     */
    public HeapPriorityQueue getMinHeap() {
        return minHeap;
    }

    /**
     * Returns the maximum heap containing the values
     * @return the maximum heap
     */
    public HeapPriorityQueue getMaxHeap() {
        return maxHeap;
    }

    /**
     * Add a new number to the heap
     * @param num The number to be added to the heap
     */
    public void add(double num) {
        // If the value of the element is less than or equal to the top element of the max heap, put it in the max heap
        if (!minHeap.isEmpty() && num < minHeap.peek()) {
            maxHeap.add(num);
        } else { //If the value of the element is greater than the top element of the max heap, put it in the min heap
            minHeap.add(num);
        }
        balance();
    }

    /**
     * Balance the size of the min and max heaps by comparing the number of numbers in the heaps
     */
    public void balance() {
        // Check the number of elements in the max heap and min heap, and if the difference between their number of elements is greater than 1, balancing operation is needed
        while (maxHeap.size() > minHeap.size() + 1) {
            // Move the root element of the max heap to the min heap
            minHeap.add(maxHeap.remove());
        }
        while (minHeap.size() > maxHeap.size() + 1) {
            // Move the root element of the min heap to the max heap
            maxHeap.add(minHeap.remove());
        }
    }

    /**
     * Get the median of all the numbers obtained from the data stream
     * @return The median of the numbers currently
     */
    public double getMedian() {
        double median;
        if (minHeap.size() < maxHeap.size()) {
            median = maxHeap.peek();
        } else if (minHeap.size() > maxHeap.size()) {
            median = minHeap.peek();
        } else {
            median = (maxHeap.peek() + minHeap.peek()) / 2;
        }
        return median;
    }
}
