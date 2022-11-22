/*
name: Thang Nguyen
nsid: dun329
student number:11275930
course number:CMPT280
*/

package lib280.tree;
public class AVLTreeNode280<I extends Comparable<? super I>> extends BinaryNode280<I> {

    protected int leftHeight;

    protected int rightHeight;
    
    public AVLTreeNode280(I x) {
        super(x);
        leftHeight = 0;
        rightHeight = 0;
    }

    public int getLeftHeight() {
        return leftHeight;
    }

    public int getRightHeight() {
        return rightHeight;
    }

    public void setLeftHeight(int ltHeight) {
        this.leftHeight = ltHeight;
    }

    public void setRightHeight(int rtHeight) {
        this.rightHeight = rtHeight;
    }

    public AVLTreeNode280<I> leftNode() {
        return (AVLTreeNode280<I>)super.leftNode();
    }

    public AVLTreeNode280<I> rightNode() {
        return (AVLTreeNode280<I>) super.rightNode();
    }

}