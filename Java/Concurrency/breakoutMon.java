import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class breakoutMon {
	enum Falculty {
		Empty, Art, Science, Engineering
	}

	public static class Room {
		Falculty owner;
		int count; // Count number of student in the room. 
		//Used to check if room reach empty state. 
		Set<Falculty> waiting; //used to show that a faculty is already in queue.
		Set<Integer> visited; // used to prevent student from reenter constantly.
		Queue<Falculty> falcWaitList; //Queue to keep track of which faculty is first.

		public Room() {
			this.owner = Falculty.Empty;
			this.count = 0;
			falcWaitList = new LinkedList<>();
			waiting = new HashSet<>();
			visited = new HashSet<>();
		}

		public synchronized boolean enter(int id, Falculty falculty) {
			// Force non faculty member and people who left during this session to wait
			while ((owner != Falculty.Empty && owner != falculty) || visited.contains(id)) {
				if (!waiting.contains(falculty)) { // if not on wait list 
					waiting.add(falculty); // add it to queue
					falcWaitList.add(falculty); // indicate who is already on the list.
				}
				try {
					wait(); // wait until owner leaves
					// In the event that the wrong faculty got notified,
					// The while loop will catch it and it will continue waiting. 
				} catch (InterruptedException e) {}
			}
			if (owner == Falculty.Empty) {
				owner = falculty; // Switch ownership and update status
				System.out.println(String.format("Ownership change to: %s", falculty.name()));
			}
			// Student who leave can't reenter again less other faculty had their turn.
			visited.add(id); // add to the visited set. 
			count++;
			return true;
		}

		public synchronized void leave() {
			count--;
			if (count == 0) { //last one in the room
				visited.clear(); // clear the visited because ownership will change.
				if (!falcWaitList.isEmpty()) {
					Falculty next = falcWaitList.remove(); // get next faculty
					waiting.remove(next); //remove from the set
					this.owner = next; //set next owner
					notifyAll(); // notify all waiting thread.
				}else {
					this.owner = Falculty.Empty;
				}
				//indicate state change.
				System.out.println(String.format("Ownership change to: %s", owner.name()));
			}
		}
	}

	public static class Student implements Runnable {
		int id, k, w;
		long startTime, duration;
		Falculty falculty;
		Random rand;
		Room room;

		public Student(int id, int k, int w, int n, long start,Room room, Falculty falculty) {
			this.id = id;
			this.k = k;
			this.w = w;
			this.duration = n*1000;
			this.startTime = start;
			this.room = room;
			this.falculty = falculty;
			this.rand = new Random();
		}

		public void run() {
			long time = System.currentTimeMillis();
			while(time - startTime < duration) {
				sleepFor(k*(1+rand.nextInt(10)));
				room.enter(id, falculty);
				sleepFor(w*(1+rand.nextInt(10)));
				room.leave();
				time = System.currentTimeMillis();
			}
		}
		
		private void sleepFor(int duration) {
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {}
		}

	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Wrong number of arguments. Example Usage: ./breakoutMon n k w");
			return;
		}
		try {
			// parsing input
			int n = Integer.parseInt(args[0]); // run time
			int k = Integer.parseInt(args[1]); // try delay time
			int w = Integer.parseInt(args[2]); // stay delay time
			long startTime = System.currentTimeMillis(); //get start time for reference.
			System.out.println("Staring breakoutMon. Inputs:");
			System.out.println(String.format("n: %d, k: %d, w: %d", n,k,w));
			Room room = new Room(); //initialize room
			Thread[] threads = initThreads(n, k, w, startTime, room); //initialize threads.
			for(Thread thread : threads)thread.start(); //start all threads
			for(Thread thread : threads)thread.join(); //wait until all thread finish
			System.out.println("All thread finished.");
		} catch (Exception e) {
			System.out.println("ERROR " + e);
			e.printStackTrace();
		}
		System.out.println("Program Terminating.");
	}
	
	public static Thread[] initThreads(int n, int k, int w, long start, Room room) {
		Thread[] threads = new Thread[12];
		for(int i = 0 ; i < 12 ; i++) {
			Falculty falc = Falculty.Art;
			if(i>=4)falc = Falculty.Science;
			if(i>=8)falc = Falculty.Engineering;
			Student student = new Student(i, k, w, n, start, room, falc);
			threads[i] = new Thread(student);
		}
		return threads;
	}
}
