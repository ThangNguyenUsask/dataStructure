/*
name: Thang Nguyen
nsid: dun329
student number:11275930
course number:CMPT280
*/


package lib280.tree;
import lib280.exception.ContainerEmpty280Exception;
public class IterableArrayedHeap280<I extends Comparable<? super I>> extends
		ArrayedHeap280<I> {
	/**
	 * Create an iterable heap with a given capacity.
	 * @param cap The maximum number of elements that can be in the heap.
	 */
	public IterableArrayedHeap280(int cap) {
		super(cap);
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

	// TODO
// Add iterator() and deleteAtPosition() methods here.

	/**
	 * delete item that is contained in current cursor position
	 * @param pos cursor position.
	 */
	public void deleteAtPosition(ArrayedBinaryTreeIterator280<I> pos){
		if(this.isEmpty())
			throw new ContainerEmpty280Exception("Cannot delete an item from an empty heap.");

		// First we swap the node where cursor point to with the last elements
		// Then we remove the elements store in last index by decrement count by 1
		if( this.count > 1 ) {
			swap(items, pos.currentNode, count);
		}
		this.count = this.count - 1;

		if( this.count == 0) {   // if we remove all the elements in the heaps, we finish
			this.currentNode = 0;
		}

		int n = pos.currentNode;

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
	 * returns a new ArrayedBinaryTreeIterator280 object for the tree
	 */
	public ArrayedBinaryTreeIterator280<I> iterator(){
		return new ArrayedBinaryTreeIterator280<>(this);
	}
}
