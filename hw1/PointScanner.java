package edu.iastate.cs228.hw1;

/**
 * 
 * @author Noah Hoss
 *
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;

/**
 * 
 * This class sorts all the points in an array of 2D points to determine a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class PointScanner  
{
	private Point[] points; 
	
	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	                                      // the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;    
	
	private String 	outputFileName = ("outputFile.txt");
	
	protected long scanTime; 	       // execution time in nanoseconds. 
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[].
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		//Going to do seperate ifs for debugging purposes.
		if(pts == null) {
			throw new IllegalArgumentException("pts is == null in PointScanner(Point[] pts, Alrogithm algo).");
		}
		if(pts.length == 0) {
			throw new IllegalArgumentException("pts.length is == 0 in PointScanner(Point[] pts, Alrogithm algo).");
		}
		
    	points = new Point[pts.length];
        for (int i = 0; i < pts.length; i++) {
            points[i] = new Point(pts[i].getX(), pts[i].getY());
        }
		sortingAlgorithm = algo;
	}

	/**
	 * This constructor reads points from a file. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException
	{
		File file = new File(inputFileName);
		Scanner readFile = new Scanner(file);
		if(!file.exists()) {
			readFile.close();
			throw new FileNotFoundException("File not found with name: " + inputFileName + " in PointScanner.");
		}
		//Create placeholder Array
		ArrayList<Point> arr = new ArrayList<Point>();
		//Forgot to set the algorithm, oops.
		sortingAlgorithm = algo;
		ArrayList<Integer> xy = new ArrayList<Integer>();
		//Chuck values into arraylist for errorhandling purposes
		while(readFile.hasNextInt()) {
			xy.add(readFile.nextInt());
		}
		//If there is uneven number of ints, throw input mismatch.
		if(xy.size() % 2 != 0) {
			readFile.close();
			throw new InputMismatchException("File with name: " + inputFileName + " in PointScanner has an odd number of integers.");
		}else {
			for(int i = 0; i < xy.size()-1; i++) {
				arr.add(new Point(xy.get(i), xy.get(i+1)));
				i++;
			}
			//close
			readFile.close();
			points = new Point[arr.size()];
			for(int i = 0; i < points.length; i++) {
				points[i] = new Point((Point) arr.get((i)));
			}
		}
	}

	/**
	 * Carry out two rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates.     
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting.       
	 * @param algo
	 * @return
	 */
	public void scan()
	{
		AbstractSorter aSorter = null; 
		// create an object to be referenced by aSorter according to sortingAlgorithm. for each of the two 
		// rounds of sorting, have aSorter do the following: 
		// 
		//     a) call setComparator() with an argument 0 or 1. 
		//
		//     b) call sort(). 		
		// 
		//     c) use a new Point object to store the coordinates of the Median Coordinate Point
		//
		//     d) set the medianCoordinatePoint reference to the object with the correct coordinates.
		//
		//     e) sum up the times spent on the two sorting rounds and set the instance variable scanTime. 
		
		 switch (sortingAlgorithm) {
         case SelectionSort:
             aSorter = new SelectionSorter(points);
             break;
         case InsertionSort:
             aSorter = new InsertionSorter(points);
             break;
         case QuickSort:
             aSorter = new QuickSorter(points);
             break;
         case MergeSort:
             aSorter = new MergeSorter(points);
             break;
     }
		
		long sortStartTime = System.nanoTime();
		int x = 0;
		int y = 0;
		for(int i = 0; i < 2; i++) {
			
			aSorter.setComparator(i);
			aSorter.sort();
			
			if(i == 0) {
				x = aSorter.getMedian().getX();
			}else {
				y = aSorter.getMedian().getY();
				medianCoordinatePoint = new Point(x,y);
			}
		}
		//Dude IDK If this bit doesn't work well then...Yeah :)!
		this.scanTime = System.nanoTime() - sortStartTime;
	}
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description. 
	 */
	public String stats()
	{
		String s = String.format("%-18s %-6d %-8d", sortingAlgorithm.toString(), points.length, scanTime);
		return s;
	}
	
	/**
	 * Write MCP after a call to scan(),  in the format "MCP: (x, y)"   The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */
	@Override
	public String toString()
	{
		String s = ("MCP: (" + medianCoordinatePoint.getX() + ", " + medianCoordinatePoint.getY() + ")");
		return s; 
	}

	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException
	{
		try {
			System.out.println(this.outputFileName);
			PrintWriter output = new PrintWriter(this.outputFileName);
			output.println(this.toString());
			output.close();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("File with name: " + outputFileName + " not found in writeMCPToFile");
		}
	}		
}