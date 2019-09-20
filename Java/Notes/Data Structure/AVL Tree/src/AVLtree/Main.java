package AVLtree;

public class Main {

    public static void main(String[] args) {
	// write your code here
        int n = 4000000;
        AVLTree tree = new AVLTree();
        long starttime = System.currentTimeMillis();
        for(int i = 0 ; i < n ;i++){
            tree.insert(i*10+(int)(10000000*Math.random()));
//          tree.insert(i);
        }
        for(int i=0;i<1000000;i++){
            tree.find((int)(10000000*Math.random()));
        }
        System.out.println("Node inserted: " + n +" Nodes");
        System.out.println("Tree Height is: "+ tree.root.height);
        System.out.println("Time to insert the nodes: "+(System.currentTimeMillis()-starttime)+" ms");
    }
}
