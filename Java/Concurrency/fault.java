import java.awt.image.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.imageio.*;
import javax.management.monitor.Monitor;

public class fault {
	
    static class Worker implements Runnable{
    	int[][] grid;
    	int w, h, k, id;
    	Random rand;
    	
    	public Worker(int[][] grid,int w, int h, int k, int id) {
			this.grid = grid;
    		this.k = k;
			this.w = w;
			this.h = h;
			this.id = id;
			this.rand = new Random();
		}
    	
    	private int[][] getLine(){
    		int[][] res = new int[2][2];
    		int side = rand.nextInt(4);
    		res[0] = getPoint(side);
    		int sSide = rand.nextInt(4);
    		while(sSide==side)sSide = rand.nextInt(4);
    		res[1] = getPoint(sSide);
    		return res;
    	}
    	
    	private int[] getPoint(int side) {
    		int[] pt = {0,0};
    		pt[0] = this.rand.nextInt(this.w); 
    		pt[1] = this.rand.nextInt(this.h);
    		if(side == 0) pt[1] = 0;
    		else if(side == 1) pt[1] = this.h-1;
    		else if(side == 2) pt[0] = 0;
    		else pt[0] = this.w-1;
    		return pt;
    	}
    	
    	private boolean afterLine(int[][] v, int[] pt) {
    		int delta = (v[1][0]- v[0][0])*(pt[1] - v[0][1]); 
    		delta -= (pt[0] - v[0][0])*(v[1][1] - v[0][1]);
    		return delta < 0;
    	}
    	
    	private void paint(int[][] vector, boolean rev, int incr) {
    		for(int i = 0 ; i < w ; i++) {
    			for(int j = 0 ; j < h ; j++) {
    				int r = rev ? w-i-1:i;
    				int c = rev ? h-j-1:j;
    				int[] pt = {r,c};
    				boolean rightBottom = afterLine(vector,pt);
    				if(rev&&rightBottom || !rev&&!rightBottom) {
    					this.grid[r][c]+=incr;
    				}
    			}
    		}
    	}
    	
    	public void run() {
    		for(int i = 0 ; i < this.k ; i++) {
    			int[][] vector = getLine();
    			boolean rev = this.rand.nextBoolean();
    			int thickness = this.rand.nextInt(10);
    			paint(vector,rev,thickness);
    		}
    	}
    	
    	
    }
    
	// Parameters
    public static int width;
    public static int height;
    public static int t;
    public static int k;
    private static final String INPUT_ERROR = "Invalid number of argument. Ex. java fault w h t k";
    private static long startTime;
    
    public static void main(String[] args) {
        try {
        	parse(args);
			int[][][] mats = new int[t][width][height];
    		Thread[] workers = initWorker(mats, t);
        	initStats();
        	for(Thread worker : workers)worker.start();
        	for(Thread worker : workers)worker.join();
        	merge(mats);
        	logStats();
        	createImage(mats[0]);
        } catch (Exception e) {
            System.out.println("ERROR " +e);
            e.printStackTrace();
        }
    }
    
    private static void merge(int[][][] mats) {
    	for(int i = 1; i < t ; i++) {
    		for(int j = 0 ; j < width ; j++) {
    			for(int k = 0 ; k < height ; k++) {
    				mats[0][j][k] += mats[i][j][k];
    			}
    		}
    	}
    }
    
    private static Thread[] initWorker(int[][][] mats, int n) {
    	Thread[] workers = new Thread[n];
    	int load  = k / t;
    	for(int i = 0 ; i < n ; i++) {
    		Worker worker = new Worker(mats[i],width,height,load,i);
    		workers[i] = new Thread(worker);
    	}
    	return workers;
    }
    
    private static void createImage(int[][] grid) throws IOException {
        BufferedImage outputimage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        int[] minMax = findMinMax(grid);
        System.out.println("Creating Image");
        for(int x = 0 ; x < width ; x++) {
        	for(int y = 0; y < height ; y++) {
        		int c = rgb(minMax[0],minMax[1],grid[x][y]);
        		outputimage.setRGB(x, y, c);
        	}
        }
        File outputfile = new File("outputimage.png");
        ImageIO.write(outputimage, "png", outputfile);
        System.out.println("Image Saved to outputimage.png");
    }
    
    private static void parse(String[] args) {
    	if(args.length!=4) {
    		throw new IllegalArgumentException(INPUT_ERROR);
    	}
    	width = Integer.parseInt(args[0]);
    	height = Integer.parseInt(args[1]);
    	t = Integer.parseInt(args[2]);
    	k = Integer.parseInt(args[3]);
    }
    
    private static void initStats() {
    	String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
    	startTime = System.currentTimeMillis();
    	System.out.println( "Initializing Fault Line Program" );
    	System.out.println( "Start Time: " + time );
    }
    
    private static void logStats() {
    	System.out.println("All thread finished");
    	long duration = System.currentTimeMillis() - startTime;
    	String message = "Time Elapsed: " + duration + " milliseconds \n";
    	String format = "Run Setting: w: %d, h: %d, t: %d, k: %d \n";
    	String report = String.format(format, width, height , t, k);
    	System.out.print(message);
    	try {
    		File f = new File("fault-log.txt");
    		if(!f.exists())f.createNewFile();
    		Path path = Paths.get("fault-log.txt");
    	    Files.write(path, report.getBytes(), StandardOpenOption.APPEND);
    	    Files.write(path, message.getBytes(), StandardOpenOption.APPEND);
    	}catch (IOException e) {
    	    System.out.println("Fail to write to log.");
    	}
    }
    
    private static int[] findMinMax(int[][] grid) {
    	int[] res = {Integer.MIN_VALUE, Integer.MAX_VALUE};
    	for(int i = 0; i < grid.length ; i++) {
    		for(int j = 0 ; j < grid[0].length ; j++) {
    			res[0] = Math.max(res[0], grid[i][j]);
    			res[1] = Math.min(res[1], grid[i][j]);
    		}
    	}
    	return res;
    }
    
    private static int rgb(int max, int min , int val) {
    	double ratio = 2.0 * ((double)val-min) / ((double)max - min);
    	int b = (int) Math.max(0,Math.round(255.0*(1-ratio)));
    	int r = (int) Math.max(0,Math.round(255.0*(ratio-1)));
    	int g = 255 - b - r;
    	int p = b | (g<<8) | (r << 16) | (255<<24);
    	return p;
    }
}
