package AVLtree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class AVLTree {
    Node root;
    int size;
    @Contract(pure = true)
    AVLTree(){
        root = null;
        size = 0;
    }
    public void insert(int i){
        root = this.add(root,i);
    }
    public void inorder(){
        inorder(root);
    }
    public int find(int i){
        return find(root,i);
    }
    @Contract("null, _ -> new")
    private Node add(Node root, int i){
        if(root == null) return new Node(i);
        if(root.val < i){
            root.right = this.add(root.right,i);
        }
        else {
            root.left = this.add(root.left,i);
        }
        int left = getHeight(root.left);
        int right = getHeight(root.right);
        if(left-right >= 2){
            //left heavy case
            if(getHeight(root.left.left) > getHeight(root.left.right)){
                //left-left case. Do a right rotation in place.
                root = rightrotate(root);
            }
            else {
                //left-right case. Do a left rotation in left node and a right rotation in place.
                root.left = leftrotate(root.left);
                root = rightrotate(root);
            }
        }
        else if(right-left>=2){
            //right heavy case
            if(getHeight(root.right.right)>getHeight(root.right.left)){
                //right-right case. Do a left rotation in place.
                root = leftrotate(root);
            }
            else {
                //right-left case. Do a right rotation in right node and a left rotation in place.
                root.right = rightrotate(root.right);
                root = leftrotate(root);
            }
        }
        updateHeight(root);
        return root;
    }
    @NotNull
    private Node leftrotate(Node root){
        Node nroot = root.right;
        root.right = nroot.left;
        nroot.left = root;
        root.height = Math.max(root.left==null?-1:root.left.height,root.right==null?-1:root.right.height)+1;
        return nroot;
    }
    @NotNull
    private Node rightrotate(Node root){
        Node nroot = root.left;
        root.left = nroot.right;
        nroot.right = root;
        updateHeight(root);
        return nroot;
    }
    private void inorder(@NotNull Node root){
        if(root.left!=null)inorder(root.left);
        System.out.println(Integer.toString(root.val)+" ");
        if(root.right!=null)inorder(root.right);
    }
    @Contract(pure = true)
    private int getHeight(Node root){
        if(root == null)return -1;
        return root.height;
    }
    private int find(Node root,int i){
        if(root==null)return -1;
        if(root.val>i)return find(root.left,i);
        else if(root.val<i)return find(root.right,i);
        else return root.val;
    }
    private void updateHeight(Node root){
        root.height = Math.max(getHeight(root.left),getHeight(root.right))+1;
    }
}
