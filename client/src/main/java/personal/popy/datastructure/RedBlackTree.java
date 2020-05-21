package personal.popy.datastructure;

/**
 * @see https://www.cnblogs.com/ysocean/p/8004211.html
 */
public class RedBlackTree<T extends Comparable<T>> implements Tree<T> {
    RBNode<T> root;


    /**
     * 与平衡二叉树不同，这里的左旋转是指向左旋转
     * 左旋的顶端节(x)点必须要有右子节点(y)。
     * 左旋做了三件事：
     * 1. 将y的左子节点赋给x的右子节点,并将x赋给y左子节点的父节点(y左子节点非空时)
     * 2. 将x的父节点p(非空时)赋给y的父节点，同时更新p的子节点为y(左或右)
     * 3. 将y的左子节点设为x，将x的父节点设为y
     */
    /* 左旋
     *         N            R
     *        / \          / \
     *       L   R  ===>  N   T1
     *          / \      / \
     *         T  T1    L   T
     */
    private void leftRotate(RBNode<T> N) {
        RBNode<T> R = N.right;
        if (R == null) return;
        N.setRigthChildAndUpdateParent(R.left);
        replaceParent(N, R);
        R.setLeftChildAndUpdateParent(N);
    }

    /* 右旋
     *         N            L
     *        / \          / \
     *       L   R  ===>  T1  N
     *      / \              / \
     *     T1  T            T   R
     */
    private void rightRotate(RBNode<T> N) {
        RBNode<T> L = N.left;
        if(L == null) return;
        N.setLeftChildAndUpdateParent(L.right);
        replaceParent(N, L);
        L.setRigthChildAndUpdateParent(N);
    }


    private void replaceParent(RBNode<T> x, RBNode<T> y){
        y.parent = x.parent;
        if(x.parent == null) root = y;
        else{
            if(x.parent.left == x){
                x.parent.left = y;
            }else{
                x.parent.right = y;
            }
        }
    }

    @Override
    public void insert(T data) {

    }

    @Override
    public boolean delete(T data) {
        return false;
    }

    @Override
    public boolean contains(T data) {
        return false;
    }

    @Override
    public T max() {
        return null;
    }

    @Override
    public T min() {
        return null;
    }

    @Override
    public void InorderTraversal() {

    }

    @Override
    public void PreorderTraversal() {

    }
}
