
/*
name: Thang Nguyen
nsid: dun329
student number:11275930
course number:CMPT280
*/


package lib280.tree;

import lib280.base.Dispenser280;
import lib280.exception.*;

public class ArrayedHeap280<I extends Comparable<? super I>> extends ArrayedBinaryTree280<I> implements Dispenser280<I> {

    /**
     * Constructor
     *
     * @param cap Maximum number of elements that can store in the heap.
     */

    public ArrayedHeap280(int cap) {
        super(cap);
        items = (I[]) new Comparable[capacity + 1];
    }

    /**
     * Function that swap 2 elements
     *
     * @param array   the array that contain first and second index
     * @param index_1 first index
     * @param index_2 second index
     */
    public static <I> void swap(I[] array, int index_1, int index_2) {
        if (index_1 < 0 || index_2 < 0) {
            throw new RuntimeException("Array unable to obtain elements with that index");
        }

        I t = array[index_1];
        array[index_1] = array[index_2];
        array[index_2] = t;

    }


    /**
     * Insert a new item into the heap.
     *
     * @param x The item to be inserted.
     * @throws ContainerFull280Exception if the heap is full.
     */

    public void insert(I x) throws ContainerFull280Exception {
        if (!this.isFull()) {
            this.items[this.count + 1] = x;  // add 1 more index to the heap and add the new element to the last index
            this.count = this.count + 1;
        } else {
            throw new ContainerFull280Exception("Cannot add item to a heap that is full.");
        }
        this.currentNode = 1; // move the cursor to the start of the heap

        if (this.count == 1){
            return;
        }

        int a = count;
        int compare = items[a].compareTo(items[findParent(a)]);  //compare item with its parent

        do {
            swap(items, a, findParent(a)); // swap item at index a and its parent
            a = findParent(a); // move that node to its parent to check 1 more time
        }
        while (a > 1 && compare > 0);
        // if item in index n bigger than it parent, we need to swap
        // both elements until it smaller than its parent to maintain heap property

    }


    /**
     * Delete the item that store at the top of the heap.
     *
     * @throws ContainerEmpty280Exception if the heap is empty.
     */


    public void deleteItem() throws ContainerEmpty280Exception {
        if (this.isEmpty()) {
            throw new ContainerEmpty280Exception("Cannot delete when the heap is empty.");
        }
        // First we swap the node at the top of the heap with the last elements
        // Then we remove the elements store in last index by decrement count by 1
        if (this.count > 1) {
            swap(items, 1, count);
        }
        this.count = this.count - 1;

        if (this.count == 0) {   // if we remove all the elements in the heaps, we finish
            this.currentNode = 0;
            return;
        }

        int n = 1;

        while (findRightChild(n) <= count) {
            int child = findRightChild(n);    // Choose the left child.
            int compare_LR = items[child].compareTo(items[findLeftChild(n)]);

            if (compare_LR < 0) {    // We must choose the bigger value between left and right tree to swap with its parent
                child = child - 1;
            }
            int compare_PC = items[child].compareTo(items[n]);
            if (compare_PC > 0) {   //if child bigger than its parent then we swap
                swap(items, n, child);
                n = child;         // keep checking when the node in its child position
            }
        }
    }

    /**
     * Helper for the regression test.  Verifies the heap property for all nodes.
     */
    private boolean hasHeapProperty() {
        for (int i = 1; i <= count; i++) {
            if (findRightChild(i) <= count) {  // if i Has two children...
                // ... and i is smaller than either of them, , then the heap property is violated.
                if (items[i].compareTo(items[findRightChild(i)]) < 0) return false;
                if (items[i].compareTo(items[findLeftChild(i)]) < 0) return false;
            } else if (findLeftChild(i) <= count) {  // if n has one child...
                // ... and i is smaller than it, then the heap property is violated.
                if (items[i].compareTo(items[findLeftChild(i)]) < 0) return false;
            } else break;  // Neither child exists.  So we're done.
        }
        return true;
    }

    /**
     * Regression test
     */
    public static void main(String[] args) {

        ArrayedHeap280<Integer> H = new ArrayedHeap280<Integer>(10);

        // Empty heap should have the heap property.
        if (!H.hasHeapProperty()) System.out.println("Does not have heap property.");

        // Insert items 1 through 10, checking after each insertion that
        // the heap property is retained, and that the top of the heap is correctly i.
        for (int i = 1; i <= 10; i++) {
            H.insert(i);
            if (H.item() != i) System.out.println("Expected current item to be " + i + ", got " + H.item());
            if (!H.hasHeapProperty()) System.out.println("Does not have heap property.");
        }

        // Remove the elements 10 through 1 from the heap, chekcing
        // after each deletion that the heap property is retained and that
        // the correct item is at the top of the heap.
        for (int i = 10; i >= 1; i--) {
            // Remove the element i.
            H.deleteItem();
            // If we've removed item 1, the heap should be empty.
            if (i == 1) {
                if (!H.isEmpty()) System.out.println("Expected the heap to be empty, but it wasn't.");
            } else {
                // Otherwise, the item left at the top of the heap should be equal to i-1.
                if (H.item() != i - 1) System.out.println("Expected current item to be " + i + ", got " + H.item());
                if (!H.hasHeapProperty()) System.out.println("Does not have heap property.");
            }
        }

        System.out.println("Regression Test Complete.");
    }

}
