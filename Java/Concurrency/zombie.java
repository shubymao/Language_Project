
public class zombie {

	static class Simulation {
		volatile int zombieCount;
		volatile boolean flag;
		int killCount;
		double killChance, zombieChance;

		Simulation(double killChance, double zombieChance) {
			this.zombieCount = 0;
			this.flag = true;
			this.killChance = killChance;
			this.zombieChance = zombieChance;
		}

		public synchronized boolean controlDoor() {
			if(flag && Math.random() < zombieChance) {
				zombieCount++;
				return true;
			}
			return false;
		}

		public synchronized boolean killZombie() {
			if(zombieCount > 0 && Math.random() < killChance) {
				zombieCount--;
				return true;
			}
			return false;
		}
		
		public synchronized int checkCount() {
			return this.zombieCount;
		}
		
		public synchronized boolean getFlag() {
			return this.flag;
		}
		
		public synchronized void setFlag(boolean flag) {
			this.flag = flag;
		}
	}

	static class Controller implements Runnable {
		Simulation sim;
		long startTime, simTime;
		int id, counter;
		public Controller(Simulation sim,int id, long startTime, long simTime) {
			this.sim = sim;
			this.startTime = startTime;
			this.simTime = simTime;
			this.id = id;
			this.counter = 0;
		}

		public void run() {
			System.out.println("Controller Thread Starting");
			while (System.currentTimeMillis() - startTime < simTime) {
				if(sim.controlDoor())counter++;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
			}
			String summary = sf("Controller %d finished and have let in: %d zombies.",id, counter);
			System.out.println(summary);
		}
	}

	static class Warrior implements Runnable {
		Simulation sim;
		long startTime, simTime, killCount;
		int n, minuteKillCount;
		public Warrior(Simulation sim, int n, long startTime, long simTime) {
			this.n = n;
			this.sim = sim;
			this.startTime = startTime;
			this.simTime = simTime;
			this.killCount = 0;
		}

		public void run() {
			System.out.println("Warrior Thread Starting");
			long lastCheckedTime = System.currentTimeMillis();
			while ( System.currentTimeMillis() - startTime < simTime) {
				boolean kill = sim.killZombie();
				if(kill) {
					minuteKillCount++;
					killCount++;
				}
				if(System.currentTimeMillis() - lastCheckedTime > 1000) {
					int count = sim.checkCount();
					boolean flag = sim.getFlag();
					if(count < n/2)sim.setFlag(true);
					else if(count > n)sim.setFlag(false);
					lastCheckedTime = System.currentTimeMillis();
					String format = "Zombie Count: %d, Door: %s, Killed %d zombie per second.";
					String summary = sf(format, count, flag?"Open":"Close" , minuteKillCount);
					System.out.println(summary);
					minuteKillCount = 0;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
			}
			System.out.println("Warrior Thread Finishing");
			String format = "Total Kill: %d, Remaining: %d, Avg Kill per second: %f, Time Elapsed: %d second";
			long duration = (System.currentTimeMillis() - startTime)/1000;
			String summary = sf(format, killCount, sim.checkCount(), (double)killCount/duration, duration);
			System.out.println(summary);
		}
	}

	public static void main(String[] args) {
		// Input check
		if (args.length != 2) {
			System.out.println("Wrong number of arguements provided. Ex. zombie k n");
		}
		//Initializing and parsing constants
		int k = tryParse(args[0]);
		int n = tryParse(args[1]);
		long startTime = System.currentTimeMillis();
		long duration = 90000; //1.5 minute
		
		//Initialize the monitor class
		Simulation sim = new Simulation(0.4,0.1);
		
		//Initialize all thread;
		System.out.println("Initializing Treads");
		Thread[] doorControllers = new Thread[k];
		for (int i = 0; i < k; i++) {
			Controller controller = new Controller(sim, i, startTime, duration);
			doorControllers[i] = new Thread(controller);
		}
		Warrior warrior = new Warrior(sim, n, startTime, duration);
		Thread warriorThread = new Thread(warrior);
		
		System.out.println("Starting threads");
		warriorThread.start();
		for (Thread th: doorControllers)th.start();
	}

	public static String sf(String format, Object... args) {
		String res = String.format(format,args);
		return res;
	}
	
	public static int tryParse(String args) {
		int res = -1;
		try {
			res = Integer.parseInt(args);
		} catch (NumberFormatException e) {
			System.out.println("Input provided is not integer. Please provide integer k and n value.");
			throw e;
		}
		return res;
	}

}
