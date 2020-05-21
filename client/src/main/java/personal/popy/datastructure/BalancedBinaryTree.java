package personal.popy.datastructure;

import java.util.LinkedList;
import static personal.popy.datastructure.TreeNode.height;

/**
 * 平衡二叉树，又叫AVL树，它是一棵空树或它的左右两个子树的高度差的绝对值不超过1，并且左右两个子树都是一棵平衡二叉树
 * 参考教程：https://blog.csdn.net/javazejian/article/details/53892797
 */
public class BalancedBinaryTree<T extends Comparable<T>> implements Tree<T> {

    TreeNode<T> root;

    @Override
    public void insert(T data) {
        if (root == null) {
            root = new TreeNode(data);
            return;
        }
        root = insert(root, data);
    }

    private static TreeNode insert(TreeNode node, Comparable data) {
        if (node == null) {
            return new TreeNode<>(data);
        }
        int compareResult = data.compareTo(node.data);
        if (compareResult > 0) {//data bigger
            node.right = insert(node.right, data);
            if (height(node.right) - height(node.left) == 2) {//相差2需要旋转
                node = data.compareTo(node.right.data) > 0 ? //检查插入的左右分支
                        node.RR() //右右
                        : node.RL(); //右左
            }

        } else if (compareResult < 0) {
            node.left = insert(node.left, data);
            if (height(node.left) - height(node.right) == 2) {
                node = data.compareTo(node.left.data) < 0 ?
                        node.LL()//左左
                        : node.LR(); // 左右
            }
        }
        node.reSetmaxHeight();
        return node;
    }

    //左左旋转
    //将做分支换成主分枝返回，并且将自己变成左分支的右分支
    private TreeNode<T> LLRotation(TreeNode<T> node) {
        TreeNode<T> left = node.left;
        node.left = left.right;
        left.right = node;
        node.reSetmaxHeight();
        left.reSetmaxHeight();
        return left;
    }

    //右右旋转
    //将右分支变成左分支，并且将自己变成右分支的左分支
    private TreeNode<T> RRRotation(TreeNode<T> node) {
        TreeNode<T> right = node.right;
        node.right = right.left;
        right.left = node;
        node.reSetmaxHeight();
        right.reSetmaxHeight();
        return right;
    }

    //先对左节点进行右旋转，再对本节点进行左旋转
    private TreeNode<T> LRRotation(TreeNode<T> node) {
        node.left = RRRotation(node.left);
        return LLRotation(node);
    }

    //先对右节点进行左旋转，再对本节点进行右旋转
    private TreeNode<T> RLRotation(TreeNode<T> node) {
        node.right = LLRotation(node.right);
        return RRRotation(node);
    }


    @Override
    public boolean delete(T data) {
        return false;
    }

    @Override
    public boolean contains(T data) {
        return root != null && root.find(data) != null;
    }

    @Override
    public T max() {
        if (root == null)
            return null;
        return root.max();
    }

    @Override
    public T min() {
        if (root == null)
            return null;
        return root.min();
    }

    @Override
    public void InorderTraversal() {

    }

    @Override
    public void PreorderTraversal() {

    }

    public void levelTraverse() {
        if (root == null) {
            return;
        }
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        TreeNode lastRight = root;
        TreeNode right = null;
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.print(node.data + "  ");
            if (node.left != null) {
                queue.offer(right = node.left);

            }
            if (node.right != null) {
                queue.offer(right = node.right);
            }
            if (node == lastRight) {
                lastRight = right;
                System.out.println();
            }
        }
        System.out.println();
    }


}
