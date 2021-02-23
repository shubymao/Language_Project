import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class checkers {
	//Four diagonal direction. Used for determining random next move.
	final static int[][] DIRS = {{1,1},{1,-1},{-1,1},{-1,-1}};
	
	//class indicate the square.
	static class Square {
		int player; // indicate the current player id on the square.
		
		Square(){
			this.player = -1;
		}
		
		/**
		 * Update the square player by inserting the player id.
		 * @param player
		 */
		public void occupy(int player) {
			this.player = player;
		}
		

		/**
		 * Return the current player id at the grid.
		 * @return player.
		 */
		public int getPlayer() {
			return player;
		}
		
		/**
		 * Clear the player at the square
		 * @return
		 */
		public void free() {
			this.player = -1;
		}
	}

	static class Player implements Runnable {
		boolean alive, capture; //flag to indicate states
		int n, k, id, x, y, nx, ny, tx, ty;
		Square[][] board;
		Random rand;
		ArrayList<int[]> dirs;
		
		Player(int n, int k, int id, Square[][] board) {
			this.alive = false;
			this.n = n;
			this.k = k;
			this.id = id;
			this.board = board;
			this.rand = new Random();
			this.dirs = new ArrayList<>();
			for(int[] dir : DIRS) {
				//clone the direction matrix for direction finding
				dirs.add(new int[] {dir[0],dir[1]});
			}
		}
		
		/**
		 * Returns a list of coordinate sorted by x value.
		 * Used to setup synchronize ordering. Returns null when out of bound.
		 * @param dir
		 * @return coordinates to lock sorted by x value 
		 */
		ArrayList<int[]> getSquarePath(int[] dir) {
			//Two spot from current (use for move)
			nx = x+dir[0];
			ny = y+dir[1];
			//The spot after next (if applicable, used in case of capture)
			tx = nx+dir[0];
			ty = ny+dir[1];
			if(nx >= 8 || nx < 0 || ny >= 8 || ny < 0)return null; //can't move out of bound
			ArrayList<int[]> paths = new ArrayList<>();
			paths.add(new int[]{x,y}); //current spot
			paths.add(new int[]{nx,ny}); //next spot
			//if third square in bound, add it to the lock list in case capture is performed.
			if(tx < 8 && tx >= 0 && ty < 8 && ty >= 0)paths.add(new int[]{tx,ty}); 
			Collections.sort(paths,(a,b)->a[0]-b[0]); // sort by x in ascending order
			return paths;
		}
		
		public void spawn(boolean sleep) {
			if(sleep) {
				try {
					Thread.sleep(k*(rand.nextInt(3)+2));//sleep (2-4)*k ms
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while(!alive) {
				x = rand.nextInt(8);
				y = rand.nextInt(8);
				if((x+y)%2 == 1)continue; //retry if spawn at white color grid
				synchronized (board[x][y]) { //Acquire lock on coordinate object
					if(board[x][y].getPlayer() == -1) { //isEmpty
						board[x][y].occupy(this.id); //returns returns true if successful
						alive = true;
					}
				}
			}
			String msg = String.format("T%d: respawning At (%d,%d)", id,x,y);
			System.out.println(msg);
		}
		
		private boolean makeMove() {
			Collections.shuffle(dirs); //randomize direction
			for(int[] dir : dirs) {
				//Acquire the lock from left to right (ascending x) to prevent 
				//dependency cycle, prevent 4th requirement of deadlock.
				ArrayList<int[]> cords = getSquarePath(dir);
				if(cords == null) continue; //out of bound
				boolean res = false;
				//try perform move
				if(cords.size() == 3)res = centerMove(cords,dir); 
				else res = borderMove(cords, dir);
				if(res)return true; // if move success, return.
				if(!alive) { // if player id no longer at last existing grid
					// player got catured.
					System.out.println(String.format("T%d: captured", id));
					break;
				}
			}
			if(!alive)spawn(true);
			return false;
		}
		
		/**
		 * Two Synchronized move function. Not considering capture because direction
		 * will lead to out of bound.
		 * @param p coordinate list to lock sorted by x value
		 * @param dir
		 * @return
		 */
		private boolean borderMove(ArrayList<int[]> p, int[] dir) {
			synchronized (board[p.get(0)[0]][p.get(0)[1]]) { // lock the 2 square
				synchronized (board[p.get(1)[0]][p.get(1)[1]]) {
					if(board[x][y].getPlayer() == this.id) { // check if last known location still (alive) contain this player id
						if(board[nx][ny].getPlayer()==-1) { // check if next spot is free
							board[x][y].free(); // perform the move.
							x = nx;
							y = ny;
							board[x][y].occupy(this.id); 
							System.out.println(String.format("T%d: moves to (%d,%d)",id,x,y));
							return true;
						}
					}else alive = false; //player got captured
				}
			}
			return false;
		}
		
		/**
		 * 3 synchronized move function. Capture is considered.
		 * @param p coordinate list to lock sorted by x value
		 * @param dir
		 * @return
		 */
		private boolean centerMove(ArrayList<int[]> p, int[] dir) {
			synchronized (board[p.get(0)[0]][p.get(0)[1]]) { // lock the 3 square
				synchronized (board[p.get(1)[0]][p.get(1)[1]]) {
					synchronized (board[p.get(2)[0]][p.get(2)[1]]) {
						if(board[x][y].getPlayer() == this.id) { // check if last known location still (alive) contain this player id
							Square next = board[nx][ny];
							Square afterNext = board[tx][ty];
							if(next.getPlayer()==-1) { // check if next is free
								board[x][y].free(); // perform move
								x = nx;
								y = ny;
								next.occupy(this.id);
								System.out.println(String.format("T%d: moves to (%d,%d)",id,x,y));
								return true;
							}else if(afterNext.getPlayer() == -1){ //otherwise check if capture-able.
								board[x][y].free(); //capture
								x = tx;
								y = ty;
								next.free(); //remove next from square
								afterNext.occupy(this.id);
								System.out.println(String.format("T%d: captures (%d,%d)",id,nx,ny));
								return true;
							}
						}else alive = false; //player got captured
					}
				}
			}
			return false;
		}
		
		private void cleanUp() {
			while(alive) {
				synchronized (board[x][y]) { //Acquire lock on object
					if(board[x][y].getPlayer() == this.id) { //isEmpty
						board[x][y].free(); //returns returns true if successful
						alive = false;
					}else alive = false;  //got captured
				}
			}
		}
		
		public void run() {
			for (int i = 0; i < this.n;) { //make n move
				boolean success = makeMove();
				if(success)i++; //increment only move is successful
				try {
					Thread.sleep(k);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			cleanUp();
		}
		
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Wrong number of arguments. Example Usage: ./checkers t k n");
			return;
		}
		try {
			// parsing input
			int t = Integer.parseInt(args[0]); // # of thread
			int k = Integer.parseInt(args[1]); // sleep time
			int n = Integer.parseInt(args[2]); // # of move
			if(t > 31 || t < 2) {
				System.out.println("Number of thread must be between [2,32).");
				return;
			}
			// initializing
			System.out.println("Initailizing 8 x 8 Board");
			Square[][] board = initBoard();
			System.out.println(String.format("Initializing %d Threads",t));
			Thread[] playerThreads = initThreads(t, k, n, board);
			System.out.println("Staring Game.");
			startGame(playerThreads); //start game and wait until finishes
		} catch (Exception e) {
			System.out.println("ERROR " + e);
			e.printStackTrace();
		}
		System.out.println("Program Shutting Down.");
	}
	
	/**
	 * Initializes the players and return a list of player thread
	 * @param t
	 * @param k
	 * @param n
	 * @param board
	 * @return playerThreads
	 */
	public static Thread[] initThreads(int t, int k, int n, Square[][] board) {
		Player[] players = new Player[t];
		Thread[] playerThreads = new Thread[t];
		System.out.println(String.format("Delay: %d, # of move: %d",k,n));
		for (int i = 0; i < t; i++) {
			players[i] = new Player(n, k, i, board);
			players[i].spawn(false);
			playerThreads[i] = new Thread(players[i]);
		}
		return playerThreads;
	}
	
	/**
	 * Starts the game and wait until all player finishes.
	 * @param playerThreads
	 * @throws InterruptedException
	 */
	public static void startGame(Thread[] playerThreads) throws InterruptedException {
		for (Thread playerThread : playerThreads) {
			playerThread.start(); // start all threads
		}
		for (Thread playerThread : playerThreads) {
			playerThread.join(); // wait until all thread finished
		}
	}

	/**
	 * Initializes the 8x8 board and return the board.
	 * @return board
	 */
	public static Square[][] initBoard() {
		Square[][] board = new Square[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = new Square();
			}
		}
		return board;
	}

}