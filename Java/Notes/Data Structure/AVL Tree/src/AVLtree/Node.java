package AVLtree;

public class Node {
    Node left;
    Node right;
    int val;
    int height;
    Node(int value){
        this.val = value;
        height = 0;
        left = null;
        right = null;
    }
}
