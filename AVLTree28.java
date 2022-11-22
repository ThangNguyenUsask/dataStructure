/*
name: Thang Nguyen
nsid: dun329
student number:11275930
course number:CMPT280
*/


package lib280.tree;

import lib280.base.Searchable280;
import lib280.exception.*;

public class AVLTree28<I extends Comparable<? super I>> extends LinkedSimpleTree280<I> implements Searchable280<I> {

    protected BinaryNode280<I> cur;

    protected BinaryNode280<I> parent;

    protected boolean continueSearch = false;

    public AVLTree28() {
        this.setRootNode(null);
    }

    protected AVLTreeNode280<I> createNewNode(I item) {
        return new AVLTreeNode280<I>(item);
    }

    public AVLTreeNode280<I> rootNode() {
        return (AVLTreeNode280<I>) super.rootNode;
    }

    @Override
    public I item() throws NoCurrentItem280Exception {
        if (itemExists()) {
            return cur.item();
        } else {
            throw new NoCurrentItem280Exception("item not exist, can not finish the requirement");
        }
    }

    @Override
    public boolean itemExists() {
        return cur != null;
    }

    public boolean isEmpty() {
        return this.rootNode() == null;
    }

    public boolean isFull() {
        return false;
    }

    /**
     * Check if tree contain elements y or not
     */
    @Override
    public boolean has(I y) {
        return itemExists();
    }

    /**
     * Check if two elements x and y are equivalent
     */
    @Override
    public boolean membershipEquals(I x, I y) {
        return x.equals(y);
    }

    /**
     * Go to location contain x in the tree
     */
    @Override
    public void search(I x) {
        boolean found = false;
        while (!found && itemExists()) {
            switch (compare(x, item())) {
                case 1:
                    parent = cur;
                    cur = parent.rightNode();
                case -1:
                    parent = cur;
                    cur = parent.leftNode();
                case 0:
                    found = true;
            }
        }
    }

    /**
     * Calculate the height different between left and right subtree of node N
     */
    protected int signedImbalance(AVLTreeNode280<I> N) {
        return N.getLeftHeight() - N.getRightHeight();
    }

    /**
     * Research the search when it has been called
     */
    @Override
    public void restartSearches() {
        continueSearch = false;
    }

    /**
     * Resume the search when it has been called
     */
    @Override
    public void resumeSearches() {
        continueSearch = true;
    }

    /**
     * Make left rotation action to the part of tree at its root
     *
     * @param parent Tree root node's parent
     */
    protected void leftRotate(AVLTreeNode280<I> parent) {
        AVLTree28<I> A = this;   // the Alphabet name are taken from slide
        AVLTree28<I> C = (AVLTree28<I>) this.rootRightSubtree();
        AVLTree28<I> D = (AVLTree28<I>) this.rootLeftSubtree();

        //make A become left node of C and D become right node of A
        // heavy node (E) move up 1 level to maintain AVL property
        C.rootNode().setLeftNode(A.rootNode);
        A.rootNode().setRightNode(D.rootNode);
        this.setRootNode(C.rootNode());

    }

    /**
     * Make right rotation action to the part of tree at its root
     *
     * @param parent Tree root node's parent
     */
    protected void rightRotate(AVLTreeNode280<I> parent) {
        AVLTree28<I> A = this;      // the Alphabet name are taken from slide
        AVLTree28<I> B = (AVLTree28<I>) this.rootLeftSubtree();
        AVLTree28<I> E = (AVLTree28<I>) this.rootRightSubtree();

        //make A become right node of B and E become left node of A
        // heavy node (D) move up 1 level to maintain AVL property
        B.rootNode().setRightNode(A.rootNode);
        A.rootNode().setLeftNode(E.rootNode);
        this.setRootNode(B.rootNode());

    }

    /**
     * Make a double left rotation action to the part of tree at its root
     *
     * @param parent Tree root node's parent
     */
    protected void leftRightRotate(AVLTreeNode280<I> parent) {
        // right rotation of the right subtree of the critical node,
        // then do  a left rotation of the critical node.
        AVLTree28<I> rightSub = (AVLTree28<I>) this.rootRightSubtree();
        rightSub.rightRotate(this.rootNode());
        this.leftRotate(parent);
    }

    /**
     * Make a double right rotation action to the part of tree at its root
     *
     * @param parent Tree root node's parent
     */
    protected void rightLeftRotate(AVLTreeNode280<I> parent) {

        //left rotation of the left subtree of the critical node,
        //then do a right rotation of the critical node.
        AVLTree28<I> leftSub = (AVLTree28<I>) this.rootRightSubtree();
        leftSub.leftRotate(this.rootNode());
        this.rightRotate(parent);
    }

    /**
     * Compare 2 datas and return associated index each case
     *
     * @param data_1 first data
     * @param data_2 second data
     */
    public int compare(I data_1, I data_2) {
        if (data_1.compareTo(data_2) < 0) {
            return -1;
        } else if (data_1.compareTo(data_2) > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * calculate the height of left sub-tree when it is called and update it
     */
    public void updateLHeight() {
        this.rootNode().setLeftHeight(
                Math.max(this.rootNode().leftNode().getLeftHeight(), this.rootNode().leftNode().getRightHeight()) + 1);
    }

    /**
     * calculate the height of right sub-tree when it is called and update it
     */
    public void updateRHeight() {
        this.rootNode().setRightHeight(
                Math.max(this.rootNode().rightNode().getLeftHeight(), this.rootNode().rightNode().getRightHeight()) + 1);
    }

    /**
     * Insert an item to the given tree
     *
     * @param item The item need to be inserted.
     */
    public void insert(I item) {
        this.insert(item, null);
    }


    /**
     * Insert the item to the given tree when we know its parent
     *
     * @param item   The item need to be inserted.
     * @param parent Tree root node's parent
     */
    protected void insert(I item, AVLTreeNode280<I> parent) {
        if (this.isEmpty()) {    // if the tree is empty, new created node will be the root of the tree
            this.setRootNode(this.createNewNode(item));
        }

        AVLTreeNode280<I> checkNode = this.rootNode();

        int data_1 = compare(item, checkNode.item());
        switch (data_1) {

            case 0:
                throw new DuplicateItems280Exception("This item already exist");

            case 1:  // if the value of the node bigger than its parents, we must go right
                AVLTree28<I> rightSub = (AVLTree28<I>) this.rootRightSubtree();
                rightSub.insert(item, this.rootNode());
                updateRHeight();  //update the height of right sub-tree
                break;

            case -1:  // if the value of the node smaller than its parents, we must go left
                AVLTree28<I> leftSub = (AVLTree28<I>) this.rootLeftSubtree();
                leftSub.insert(item, this.rootNode());
                updateLHeight();   //update the height of left sub-tree
                break;

        }

        checkAVL(parent);  // check if we still satisfy AVL property when we finish the insertion
    }

    /**
     * Swap two elements position
     *
     * @param value_1 the first value
     * @param value_2 the second value
     */
    public <I extends Comparable<? super I>> void swap(AVLTreeNode280<I> value_1, AVLTreeNode280<I> value_2) {

        AVLTreeNode280<I> t = value_1;
        value_1.item = (I) value_2;
        value_2.item = (I) t;
    }

    /**
     * Delete an item from the given tree
     */
    public void deleteItem() throws NoCurrentItem280Exception {
        if (this.itemExists()) {
            I deleteItem = this.item();
            this.delete(deleteItem, null);
        }
        throw new NoCurrentItem280Exception("Cant delete item in an empty tree");
    }

    /**
     * Delete an item from the given tree when we know its parent
     *
     * @param deleteItem The item need to be deleted.
     * @param parent     Tree root node's parent
     */
    protected void delete(I deleteItem, AVLTreeNode280<I> parent) {
        if (this.isEmpty()) {
            throw new RuntimeException("Cant delete item in an empty tree");
        }

        int data_2 = compare(deleteItem, rootItem());
        switch (data_2) {

            // // if value of the item need to be deleted bigger than item, go right recursively
            case 1:
                AVLTree28<I> rightSub = (AVLTree28<I>) this.rootRightSubtree();
                rightSub.delete(deleteItem, this.rootNode());

                if (this.rootNode().rightNode() == null) { // update the height of right subtree after the deletion
                    this.rootNode().setRightHeight(0);    // if the root node does not have right child, then height = 0
                } else {
                    updateRHeight();                   // update the height of the right sub-tree
                }
                checkAVL(parent);               // check if we still satisfy AVL property after the deletion
                break;

            case -1:  // if the value of the node smaller than its parents, we must go left recursively
                AVLTree28<I> leftSub = (AVLTree28<I>) this.rootLeftSubtree();
                leftSub.delete(deleteItem, this.rootNode());

                if (this.rootNode().leftNode() == null) {    // update the height of left subtree after the deletion
                    this.rootNode().setLeftHeight(0);       // if the root node does not have left child, then height = 0
                } else {
                    updateLHeight();                    // update the height of the left sub-tree
                }
                checkAVL(parent);             // check if we still satisfy AVL property after the deletion

            case 0:

                // deletion case when a node does not have any child
                if (parent.leftNode() == null && parent.rightNode() == null) {
                    this.delete(deleteItem, this.rootNode());
                }
                // deletion case when a node does not have left child
                if (parent.leftNode() == null) {
                    swap(parent, parent.rightNode());
                    delete((I) parent.rightNode, this.rootNode());
                }
                // deletion case when a node does not have right child
                if (parent.rightNode() == null) {
                    swap(parent, parent.leftNode());
                    delete((I) parent.leftNode, this.rootNode());
                }

                // deletion case when a node have 2 child
                if (parent.leftNode() != null && parent.rightNode() != null) {
                    // I am unable to find in-order successor by coding
                    // After we find the in-order successor, we copy its content
                    // to the node that we want to delete and then delete the in-order successor
                }
                break;
        }

    }


    /**
     * Check to see if the Tree still satisfy AVL property
     *
     * @param parent Tree root node's parent
     */
    protected void checkAVL(AVLTreeNode280<I> parent) {

        int levelDifferent = this.signedImbalance(this.rootNode());

        switch (levelDifferent) { // Tree is balance if height different is -1,0,1
            case -1:
            case 0:
            case 1:
                return;

            case 2:
                if (this.signedImbalance(this.rootNode()) == 2) {    // if level different = 2, it means that AVL tree not satisfy
                    if (this.signedImbalance(this.rootNode().rightNode()) >= 0) {
                        leftRightRotate(parent);     // double left rotation
                    }
                    leftRotate(parent);  // do a left rotation when we have a heavy right sub-tree
                } else {
                    if (this.signedImbalance(this.rootNode().leftNode()) <= 0) {
                        rightLeftRotate(parent);     // double right rotation
                    }
                    rightRotate(parent);   // do a right rotation when we have a heavy right sub-tree
                }
                break;

        }
    }

    /**
     * Form a string representation that includes level numbers.
     * Analysis: Time = O(n), where n = number of items in the (sub)lib280.tree
     *
     * @param i the level for the root of this (sub)lib280.tree
     */
    protected String toStringByLevel(int i) {
        StringBuffer blanks = new StringBuffer((i - 1) * 5);
        for (int j = 0; j < i - 1; j++)
            blanks.append("     ");

        String result = new String();
        if (!isEmpty() && (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty()))
            result += rootRightSubtree().toStringByLevel(i + 1);

        result += "\n" + blanks + i + ": ";
        if (isEmpty())
            result += "-";
        else {
            result += rootItem();
            if (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty())
                result += rootLeftSubtree().toStringByLevel(i + 1);
        }
        return result;
    }

    public String toString() {
        return this.toStringByLevel(1);
    }

    public static void main(String[] args) {
        AVLTree28<Integer> K = new AVLTree28<Integer>(); // create a new tree named K
        K.insert(11);
        K.insert(13);
        K.insert(16);
        K.insert(18);
        K.insert(27);
        K.insert(32);
        K.insert(50);
        System.out.println(K);

        K.search(27);
        K.deleteItem();     // remove node that has no child
        System.out.println(K);
        K.insert(31);
        System.out.println(K);
        K.search(13);
        K.deleteItem();    // remove a node that has 2 child
        System.out.println(K);
        K.insert(20);
        System.out.println(K);
        K.search(31);
        K.deleteItem();   //remove a node that has 1 child
        System.out.println(K);
        if (K.has(37)) {
            System.out.println("Tree does not has item 37 but method still count");
        }
        if (!K.has(18)) {
            System.out.println("Tree does has item 18 but method does not count");
        }
        if (K.has(31)) {
            System.out.println("Tree does not has item 31 but method still count");
        }
    }
}
