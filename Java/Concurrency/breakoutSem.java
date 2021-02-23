import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class breakoutSem {
	enum Falculty {
		Empty, Art, Science, Engineering
	}

	public static class Room {
		Falculty owner; //indicate the current owner of room.
		int count; //Keep track of number of person in the room
		HashMap<Falculty,Integer> waitingCnt; //count the waiting # of each faculty
		HashMap<Falculty,Semaphore> semMap; //Used to release the waiting member of a faculty 
		Semaphore lock; //master lock to prevent concurrent access to data structure.
		Set<Integer> visited; // used to prevent student from reenter constantly.
		Queue<Falculty> falcWaitList; //Queue to keep track of which faculty is first.

		public Room() {
			this.owner = Falculty.Empty;
			this.count = 0;
			falcWaitList = new LinkedList<>();
			waitingCnt = new HashMap<>();
			semMap = new HashMap<>(); //initialize all to 0. 
			//i.e all must wait when other faculty are in or already been inside.
			semMap.put(Falculty.Art, new Semaphore(0,true));
			semMap.put(Falculty.Science, new Semaphore(0,true));
			semMap.put(Falculty.Engineering, new Semaphore(0,true));
			lock = new Semaphore(1,true); //master lock can only be access by one at a time
			visited = new HashSet<>();
		}

		public boolean enter(int id, Falculty falculty) {
			lock();
			// Force non faculty member and people who left during this session to wait
			if ((owner != Falculty.Empty && owner != falculty) || visited.contains(id)) {
				if (!waitingCnt.containsKey(falculty)) { //if not on wait list 
					falcWaitList.add(falculty); // add it to queue
					waitingCnt.put(falculty, 0); // indicate how many are waiting
				}
				waitingCnt.put(falculty,waitingCnt.get(falculty)+1);
				unlock(); //release the lock and wait
				acquire(semMap.get(falculty)); // wait until the owner leave and let us in.
				lock();
			}
			if (owner == Falculty.Empty) {
				owner = falculty; // Switch ownership and update status
				System.out.println(String.format("Ownership change to: %s", falculty.name()));
			}
			visited.add(id); // add to the visited set. 
			// Not allowed to enter again unless other faculty goes.
			count++;
			unlock();
			return true;
		}

		public void leave() {
			lock();
			count--;
			if (count == 0) { //last one in the room
				visited.clear(); // clear the visited because ownership will change.
				if (!falcWaitList.isEmpty()) { // someone is waiting
					Falculty next = falcWaitList.remove(); // get who is waiting
					int cnt = waitingCnt.get(next); //get how many is waiting
					waitingCnt.remove(next); //remove waiting counter
					semMap.get(next).release(cnt); // release the people waiting in that faculty
					this.owner = next; //Set next owner
				}else {
					this.owner = Falculty.Empty; //set back to empty if no one waiting.
				}
				//indicate state change.
				System.out.println(String.format("Ownership change to: %s", owner.name()));
			}
			unlock();
		}
		
		// master lock handle
		private void lock() {
			acquire(lock);
		}
		
		// unlock handle
		private void unlock() {
			lock.release();
		}
		
		//acquire the given semaphore handles
		private void acquire(Semaphore sem) {
			try {
				sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
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
			this.duration = n*1000; // convert second to ms
			this.startTime = start;
			this.room = room;
			this.falculty = falculty;
			this.rand = new Random();
		}

		//main run function of thread
		public void run() {
			long time = System.currentTimeMillis();
			while(time - startTime < duration) { // exit if time exceed n 
				sleepFor(k*(1+rand.nextInt(10))); //sleep for wait time
				room.enter(id, falculty); //enter room
				sleepFor(w*(1+rand.nextInt(10))); //sleep for stay time
				room.leave(); //leave room
				//get current time to determine time elapsed
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
			System.out.println("Staring breakoutMon. Inputs:");
			System.out.println(String.format("n: %d, k: %d, w: %d", n,k,w));
			long startTime = System.currentTimeMillis(); //get start time for reference.
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
			Falculty falc = Falculty.Art;  // [0,3] is Art
			if(i>=4)falc = Falculty.Science; // [4,7] is Science
			if(i>=8)falc = Falculty.Engineering; // [8,11] is Engineering
			Student student = new Student(i, k, w, n, start, room, falc);
			threads[i] = new Thread(student);
		}
		return threads;
	}
}
