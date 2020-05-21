package personal.popy.datastructure;

class TreeNode<T extends Comparable> {
    T data;
    int height;
    TreeNode<T> left;
    TreeNode<T> right;

    TreeNode(T data) {
        this.data = data;
    }

    void reSetmaxHeight(){
        int leftHeight = height(left);
        int rightHeight = height(right);
        height =  Math.max(leftHeight, rightHeight) + 1;
    }

    static int height(TreeNode node){
        return node == null ? -1 : node.height;
    }

    TreeNode<T> find(T data){
        TreeNode node = this;
        while(node != null){
            int result = data.compareTo(node.data);
            if(result > 0){
                node = node.right;
            }
            else if(result < 0){
                node = node.left;
            }else break;
        }
        return node;
    }

    T max(){
        TreeNode<T> node = this;
        while (node.right != null) {
            node = node.right;
        }
        return node.data;
    }

    T min(){
        TreeNode<T> node = this;
        while (node.left != null) {
            node = node.left;
        }
        return node.data;
    }

    TreeNode<T> LL(){
        TreeNode<T> left = this.left;
        this.left = left.right;
        left.right = this;
        reSetmaxHeight();
        left.reSetmaxHeight();
        return left;
    }

    TreeNode<T> RR(){
        TreeNode<T> right = this.right;
        this.right = right.left;
        right.left = this;
        reSetmaxHeight();
        right.reSetmaxHeight();
        return right;
    }

    TreeNode<T> LR(){
        this.left = this.left.RR();
        return this.LL();
    }

    TreeNode<T> RL(){
        this.right = this.right.LL();
        return this.RR();
    }
}
