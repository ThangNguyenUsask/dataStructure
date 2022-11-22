/*
name: Thang Nguyen
nsid: dun329
student number:11275930
course number:CMPT280
*/


package lib280.tree;
public class KDNode280<I extends Comparable<? super I>> extends BinaryNode280<I> {

    public KDNode280(I x){
        super(x);
    }

    /**
     * return the left node.
     */
    public KDNode280<I> leftNode(){
        return (KDNode280<I>) this.leftNode;
    }
    /**
     * return the right node.
     */
    public KDNode280<I> rightNode(){
        return (KDNode280<I>) this.rightNode;
    }
    /**
     * return information lies within the node.
     */
    public I item()
    {
        return (I) this.item;
    }
    /**
     * Returns a string display result
     */
    public String toString() {
        return this.item.toString();
    }

}