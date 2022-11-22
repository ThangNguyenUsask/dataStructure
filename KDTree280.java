/*
name: Thang Nguyen
nsid: dun329
student number:11275930
course number:CMPT280
*/


package lib280.tree;
import lib280.base.NDPoint280;
import lib280.list.LinkedList280;

public class KDTree280 {
    private KDNode280 rootTree;
    private int dimension;

    public KDTree280(int dimension) {
        rootTree = null;
        this.dimension = dimension;
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

        I t = array[index_1];  // swap 2 elements with given index
        array[index_1] = array[index_2];
        array[index_2] = t;

    }


    /**
     * This method is about finding the median of a given array with its dimension
     *
     * @param pointArray the array of comparable elements
     * @param left       offset of start of subarray for which we want the median element
     * @param right      offset of end of subarray for which we want the median element
     * @param d          the dimension
     * @param j          we want to find the element that belongs at array index j
     */
    private void jSmallest(NDPoint280[] pointArray, int left, int right, int d, int j) {

        // Partition the subarray using the last element , list [ right ] , as a pivot .
        // The index of the pivot after partitioning is returned .
        if (right > left) {
            int pivotIndex = partition(pointArray, left, right, d);

            // If the position j is smaller than the pivot index ,
            // recursively look for the j-th the smallest element between left , and pivotIndex -1 range
            if (j < pivotIndex) {
                jSmallest(pointArray, left, pivotIndex - 1, d, j);
            }
            // If the position j is larger than the pivot index ,
            // recursively look for the j-th the smallest element between pivotIndex +1 and right z range
            else if (j > pivotIndex) {
                jSmallest(pointArray, pivotIndex + 1, right, d, j);
            }
        }
    }


    /**
     * This method is about finding  the offset at which the pivot element ended up
     *
     * @param pointArray the array of comparable elements
     * @param left       offset of start of subarray for which we want the median element
     * @param right      offset of end of subarray for which we want the median element
     * @param d          the dimension
     * @return the offset at which the pivot element ended up
     */
    private int partition(NDPoint280[] pointArray, int left, int right, int d) {

        double pivot = pointArray[right].idx(d); //picking the last elements as the pivot
        int swapOffSet = left;

        for (int i = left; i < right; i++) {        // loop through all elements, if and element smaller than the pivot,
            // we swap item at position i and offset position
            if (pointArray[i].idx(d) <= pivot) {
                swap(pointArray, swapOffSet, i);
                swapOffSet = swapOffSet + 1;
            }
        }

        swap(pointArray, swapOffSet, right);        //swap the right and offset position
        return swapOffSet; // return the offset where the pivot ended up
    }


    /**
     * This method is about forming a k-d tree
     *
     * @param pointArray the array of comparable elements
     * @param left       offset of start of subarray for which we want the median element
     * @param right      offset of end of subarray for which we want the median element
     * @param depth      the depth of the add node
     * @return the subtree
     */
    private KDNode280 kdTree(NDPoint280[] pointArray, int left, int right, int depth) {
        if (right < left) {
            return null;
        } else {
            // Select axis based on depth so that axis cycles through all valid values
            int k = this.dimension;
            int d = depth % k;
            int medianOffset = (right - left) / 2 + left;
            // added the dimension d parameter to jSmallest
            jSmallest(pointArray, left, right, d, medianOffset);  // Put the median element in the correct position
            // Create node and construct subtrees
            KDNode280 node = new KDNode280(pointArray[medianOffset]);
            // insert point when it is smaller than the median in a recursively way
            node.leftNode = kdTree(pointArray, left, medianOffset - 1, depth + 1);
            // insert point when it is bigger than the median in a recursively way
            node.rightNode = kdTree(pointArray, medianOffset + 1, right, depth + 1);
            return node;
        }

    }


    /**
     * This method is about forming a k-d tree
     *
     * @param pointArray the array of comparable elements
     */
    public void insert(NDPoint280[] pointArray) {
        this.rootTree = kdTree(pointArray, 0, pointArray.length - 1, 0);
    }


    /**
     * This method is about searching node exist in a given range
     *
     * @param node     the current node
     * @param lowBound lower bound off the search range
     * @param upBound  upper bound off the search range
     * @param depth    the depth where curNode place in
     */
    public LinkedList280<NDPoint280> rangeSearch(KDNode280 node, NDPoint280 lowBound, NDPoint280 upBound, int depth) {

        // calculate axis in given dimension
        int axCheck = depth % this.dimension;
        if (node == null) {
            return null;
        }

        // we initial let the node between range lower and upper
        NDPoint280 data = (NDPoint280) node.item();

        // in - range elements are in left subtree
        if (data.compareByDim(axCheck, lowBound) <= 0) {
            return rangeSearch(node.leftNode(), lowBound, upBound, depth + 1);
        }
        // in - range elements are in right subtree
        else if (data.compareByDim(axCheck, upBound) >= 0) {
            return rangeSearch(node.rightNode(), lowBound, upBound, depth + 1);
        }
        else {
            // in - range elements could exist in both subtrees .
            LinkedList280<NDPoint280> L = rangeSearch(node.leftNode(), lowBound, upBound, depth + 1);
            LinkedList280<NDPoint280> R = rangeSearch(node.rightNode(), lowBound, upBound, depth + 1);
            // case when both coordinates of T.rootItem () are in range
            if (data.compareByDim(axCheck, lowBound) >= 0 && data.compareByDim(axCheck, upBound) <= 0) {
                return setUnion(L, R, this.rootTree);
            } else {
                return setUnion(L, R);
            }
        }
    }
    // I still confuse about pseudoCode of this function from text book
    // I have no idea why setUnion has 2 and 3 parameters at the same time
    // And I can not find a suitable type of parameter for lowBound and upBound in both function
    // ,so it keeps conflicting each others


    /**
     * This method returning a list of elements in a given range
     *
     * @param lowBound lower bound off the search range
     * @param upBound  upper bound off the search range
     * @return a list of elements in a given range
     */
    public LinkedList280<NDPoint280> setUnion(NDPoint280 lowBound, NDPoint280 upBound) {

        //unfinished
        int dep = this.dimension;
        KDNode280 nodeInit = this.rootTree;
        return new LinkedList280<>();
    }


    /**
     * Form a string representation that includes level numbers.
     * Analysis: Time = O(n), where n = number of items in the (sub)lib280.tree
     *
     * @param node current node
     * @param i    the level for the root of this (sub)lib280.tree
     */
    protected String toStringByLevel(KDNode280 node, int i) {

        // this method is completely taken from LinkedSimpleTree280
        // with some small modification
        StringBuffer blanks = new StringBuffer((i - 1) * 5);
        for (int j = 0; j < i - 1; j++)
            blanks.append("     ");

        String result = "";
        if (node != null && (node.leftNode() != null || node.rightNode() != null))
            result += toStringByLevel(node.rightNode(), i + 1);

        result += "\n" + blanks + i + ": ";
        if (node == null)
            result += "-";
        else {
            result += node.toString();
            if (node.leftNode() != null || node.rightNode() != null)
                result += toStringByLevel(node.leftNode(), i + 1);
        }
        return result;

    }


    /**
     * Represent the tree by level
     */
    public String toString() {
        return toStringByLevel(this.rootTree, 1);
    }


    public static void main(String[] args) {

        // initialize all given node for this 2d tree
        NDPoint280[] nd2 = new NDPoint280[8];
        nd2[0] = new NDPoint280(new double[]{3,8});
        nd2[1] = new NDPoint280(new double[]{5,9});
        nd2[2] = new NDPoint280(new double[]{6,1});
        nd2[3] = new NDPoint280(new double[]{4,7});
        nd2[4] = new NDPoint280(new double[]{2,13});
        nd2[5] = new NDPoint280(new double[]{2,11});
        nd2[6] = new NDPoint280(new double[]{1,6});
        nd2[7] = new NDPoint280(new double[]{9,12});


        System.out.println("Input 2D points:\n");
        // print all given node for this 2d tree
        int i = 0;
        do {
            System.out.println(nd2[i]);
            i = i + 1;
        } while (i < 8);
        //present 2d tree in console
        System.out.println("\nThe 2D lib280.tree built from these points is: ");
        KDTree280 kd2 = new KDTree280(2);
        kd2.insert(nd2);      // inserting all node to the tree
        System.out.println(kd2);


        // initialize all given node for this 4d tree
        NDPoint280[] nd3 = new NDPoint280[9];
        nd3[0] = new NDPoint280(new double[]{3,9,0,3});
        nd3[1] = new NDPoint280(new double[]{17,3,5,9});
        nd3[2] = new NDPoint280(new double[]{4,11,14,1});
        nd3[3] = new NDPoint280(new double[]{7,9,6,4});
        nd3[4] = new NDPoint280(new double[]{3,5,4,6});
        nd3[5] = new NDPoint280(new double[]{18,2,4,8});
        nd3[6] = new NDPoint280(new double[]{4,9,2,15});
        nd3[7] = new NDPoint280(new double[]{11,6,17,2});
        nd3[8] = new NDPoint280(new double[]{12,7,16,11});

        System.out.println("\nInput 4D points:\n");
        // print all given node for this 4d tree
        int x = 0;
        do {
            System.out.println(nd3[x]);
            x = x + 1;
        } while (x < 9);
        //present 3d tree in console
        System.out.println("\nThe 4D lib280.tree built from these points is: ");
        // display kd tree from the given nodes
        KDTree280 kd4 = new KDTree280(4);
        kd4.insert(nd3);         // inserting all node to the tree
        System.out.println(kd4);


        // First test for range search
        NDPoint280 stL = new NDPoint280(new double[]{0, 1, 0, 0});
        NDPoint280 stU = new NDPoint280(new double[]{5, 16, 13, 17});

        LinkedList280<NDPoint280> rangeElements = kd4.setUnion(stL, stU);
        System.out.println("Looking for points between " + stL + " and " + stU + ".");

        //print all elements in the given range if it's exist and print a notice message otherwise
        if (!rangeElements.isEmpty()) {
            System.out.println("Found: ");
            rangeElements.goFirst();
            //print all elements of rangeElements through a loop
            do {
                System.out.println(rangeElements.item());
                rangeElements.goForth();
            }
            while (rangeElements.itemExists());
        } else {
            System.out.println("There are no elements can be found in the given range");
        }


        // second test for range search
        NDPoint280 ndL = new NDPoint280(new double[]{0, 1, 0, 0});
        NDPoint280 ndU = new NDPoint280(new double[]{8, 12, 14, 12});

        rangeElements = kd4.setUnion(ndL, ndU);
        System.out.println("Looking for points between " + ndL + " and " + ndU + ".");

        //print all elements in the given range if it's exist and print a notice message otherwise
        if (!rangeElements.isEmpty()) {
            System.out.println("Found: ");
            rangeElements.goFirst();
            //print all elements of rangeElements through a loop
            do {
                System.out.println(rangeElements.item());
                rangeElements.goForth();
            }
            while (rangeElements.itemExists());
        } else {
            System.out.println("There are no elements can be found in the given range");
        }


        // third test for range search
        NDPoint280 rdL = new NDPoint280(new double[]{0, 1, 0, 0});
        NDPoint280 rdU = new NDPoint280(new double[]{15, 7, 10, 4});

        rangeElements = kd4.setUnion(rdL, rdU);
        System.out.println("Looking for points between " + rdL + " and " + rdU + ".");

        //print all elements in the given range if it's exist and print a notice message otherwise
        if (!rangeElements.isEmpty()) {
            System.out.println("Found: ");
            rangeElements.goFirst();
            //print all elements of rangeElements through a loop
            do {
                System.out.println(rangeElements.item());
                rangeElements.goForth();
            }
            while (rangeElements.itemExists());
        } else {
            System.out.println("There are no elements can be found in the given range");
        }
    }
}

