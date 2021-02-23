import java.util.PriorityQueue;

class TestPriorityQueue{
    public static void main(String[] args) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((a,b)->{return a-b;});
        pq.add(13);
        pq.add(0);
        pq.add(1);
        pq.add(4);
        pq.add(50);
        pq.add(-7);
        pq.add(9);
        while(!pq.isEmpty()){
            System.out.println(pq.poll());
        }
    }

}