package edu.iastate.cs228.hw1;

/**
 *  
 * @author Noah Hoss
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner; 
import java.util.Random; 


public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException
	{		
		// 
		// Conducts multiple rounds of comparison of four sorting algorithms.  Within each round, 
		// set up scanning as follows: 
		// 
		//    a) If asked to scan random points, calls generateRandomPoints() to initialize an array 
		//       of random points. 
		// 
		//    b) Reassigns to the array scanners[] (declared below) the references to four new 
		//       PointScanner objects, which are created using four different values  
		//       of the Algorithm type:  SelectionSort, InsertionSort, MergeSort and QuickSort. 
		// 
		// 	
		// For each input of points, do the following. 
		// 
		//     a) Initialize the array scanners[].  
		//
		//     b) Iterate through the array scanners[], and have every scanner call the scan() 
		//        method in the PointScanner class.  
		//
		//     c) After all four scans are done for the input, print out the statistics table from
		//		  section 2.
		//
		// A sample scenario is given in Section 2 of the project description. 
		PointScanner[] scanners = new PointScanner[4]; 
		Scanner scan = new Scanner(System.in);

		System.out.println("Performances of Four Sorting Algorithms in Point Scanning");
		System.out.println("keys:  1 (random integers)  2 (file input)  3 (exit)");
		
		Random rand = new Random();
		int key = 1;
		int trial = 1;
		while(key == 1 || key == 2) {
			
			System.out.print("Trial " + trial + ": ");
			key = scan.nextInt();
			if(key != 1 && key != 2) {
				System.exit(0);
			}
			if(key == 1) {
				System.out.print("Enter number of random points: ");
				int numRandomPoints = scan.nextInt();
				Point[] randomPoints = generateRandomPoints(numRandomPoints, rand);
				
				scanners[0] = new PointScanner(randomPoints, Algorithm.SelectionSort);
				scanners[1] = new PointScanner(randomPoints, Algorithm.InsertionSort);
				scanners[2] = new PointScanner(randomPoints, Algorithm.MergeSort);
				scanners[3] = new PointScanner(randomPoints, Algorithm.QuickSort);
			}else {
				System.out.print("Points from a file\nFile name: ");
				String fileName = scan.next();
				scanners[0] = new PointScanner(fileName, Algorithm.SelectionSort);
				scanners[1] = new PointScanner(fileName, Algorithm.InsertionSort);
				scanners[2] = new PointScanner(fileName, Algorithm.MergeSort);
				scanners[3] = new PointScanner(fileName, Algorithm.QuickSort);
			}
			System.out.println("");
			System.out.printf("%-12s %-6s %-5s \n", "algorithm", "size", "time(ns)");
			System.out.println("----------------------------------");
			for(int i = 0; i < scanners.length; i++) {
				scanners[i].scan();
				System.out.println(scanners[i].stats());
				
			}
			System.out.println("----------------------------------\n");
			trial++;
		}
		scan.close();
	}
	
	
	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] × [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{ 
		if(numPts < 1) {
			throw new IllegalArgumentException("Number of pts in generateRandomPoints is less than 1.");
		}
		Point[] p = new Point[numPts];
		int x,y;
		for(int i = 0; i < numPts; i++) {
			x = rand.nextInt(101)-50;
			y = rand.nextInt(101)-50;
			Point point = new Point(x,y);
			p[i] = point;
		}
		return p; 
	}
	
}
