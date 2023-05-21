package gk646.jnet.util.datastructures;

public class Tree<T> {
    Tree<T> left;
    Tree<T> right;
    T data;

    Tree(T data) {
        this.data = data;
    }


    public void insert(T e) {
        if (data == null) {
            data = e;
            return;
        }
        if (left == null) {
            left = new Tree<>(e);
            return;
        } else if (right == null) {
            right = new Tree<>(e);
            return;
        }
    }
}
