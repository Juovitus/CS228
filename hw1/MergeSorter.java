package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 *  
 * @author Noah Hoss
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	// Other private instance variables if you need ... 
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		super(pts);
		super.algorithm = ("mergesort");
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		mergeSortRec(this.points);
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts)
	{
		int ptsLength = pts.length;
		int midPoint = ptsLength/2;
		if(ptsLength <= 1) {
			//0 or 1 length Arrays are already sorted
			return;
		}
		//create and populate higher half
		Point[] high = new Point[ptsLength-midPoint];
		int highArrLocation = 0;
		for(int i = midPoint; i < ptsLength; i++) {
			high[highArrLocation] = pts[i];
			highArrLocation++;
		}
		//create and populate lower half
		Point[] low = new Point[midPoint];
		for(int i = 0; i < midPoint; i++) {
			low[i] = pts[i];
		}
		
		mergeSortRec(high);
		mergeSortRec(low);
		//create location for merged array
		Point[] merged = new Point[pts.length];
		//merge it
		merged = mergeArrays(low,high);
		//set pts to merged
		for(int i = 0; i < merged.length; i++) {
			pts[i] = merged[i];
		}
	}

	/**
	 * Merging
	 * @param low the lower section to be merged
	 * @param high the higher section to be merged
	 */
	private Point[] mergeArrays(Point[] low, Point[] high) {
		int lowLength = low.length;
		int highLength = high.length;
		int lowCompare = 0;
		int highCompare = 0;
		int sortedLocation = 0;
		//Create temp array to sort values into ad return.
		Point[] sorted = new Point[lowLength + highLength];
		
		while(lowCompare < lowLength && highCompare < highLength) {
			if(pointComparator.compare(low[lowCompare], high[highCompare]) <= 0 ) {
				sorted[sortedLocation] = low[lowCompare];
				sortedLocation++;
				lowCompare++;
			}else {
				sorted[sortedLocation] = high[highCompare];
				sortedLocation++;
				highCompare++;
			}
		}

		if(lowCompare >= lowLength) {
			for(int i = highCompare; i < highLength; i++) {
				sorted[sortedLocation] = high[i];
				sortedLocation++;
			}
		}else {
			for(int i = lowCompare; i < lowLength; i++) {
				sorted[sortedLocation] = low[i];
				sortedLocation++;
			}
		}
		//Finally, return the sorted array MYGOD
		return sorted;
	}
	
	// Other private methods in case you need ...

}
