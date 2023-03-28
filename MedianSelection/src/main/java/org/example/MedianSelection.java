package org.example;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Compute the median of a stream of Double
 */
public class MedianSelection {

    /**
     * Declare minHeap, maxHeap
     */
    private Queue<Double> minHeap, maxHeap;

    /**
     * Constructor for MedianSelection class
     * Construct a new MedianSelection object with an empty min heap and max heap.
     */
    public MedianSelection() {
        minHeap = new PriorityQueue<Double>();
        maxHeap = new PriorityQueue<Double>(Comparator.reverseOrder());
    }

    /**
     * Return the minimum heap containing the values
     * @return the minimum heap
     */
    public Queue<Double> getMinHeap() {
        return minHeap;
    }

    /**
     * Returns the maximum heap containing the values
     * @return the maximum heap
     */
    public Queue<Double> getMaxHeap() {
        return maxHeap;
    }

    /**
     * Add a new number to the heap
     * @param num The number to be added to the heap
     */
    public void add(double num) {
        // If the value of the element is less than or equal to the top element of the max heap, put it in the max heap
        if (!minHeap.isEmpty() && num < minHeap.peek()) {
            maxHeap.offer(num);
        } else { //If the value of the element is greater than the top element of the max heap, put it in the min heap
            minHeap.offer(num);
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
            minHeap.offer(maxHeap.poll());
        }
        while (minHeap.size() > maxHeap.size() + 1) {
            // Move the root element of the min heap to the max heap
            maxHeap.offer(minHeap.poll());
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
            median = (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
        return median;
    }
}
