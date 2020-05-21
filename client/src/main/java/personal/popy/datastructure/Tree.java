package personal.popy.datastructure;

public interface Tree<T extends Comparable<T>> {

    void insert(T data);

    boolean delete(T data);

    boolean contains(T data);

    T max();

    T min();

    //中序遍历
    void InorderTraversal();
    //前序遍历
    void PreorderTraversal();
}
