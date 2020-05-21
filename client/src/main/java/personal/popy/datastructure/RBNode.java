package personal.popy.datastructure;

final class RBNode<T extends Comparable<T>> {
    public final static boolean RED = false;
    public final static boolean BLACK = true;
    T data;
    RBNode parent;
    RBNode left;
    RBNode right;
    boolean clr;

    void setRigthChildAndUpdateParent(RBNode<T> node){
        right = node;
        if(node!=null)node.parent = this;
    }

    void setLeftChildAndUpdateParent(RBNode<T> node){
        left = node;
        if(node!=null)node.parent = this;
    }
}
