package edu.iastate.cs228.hw1;
/**
 *  
 * @author Noah Hoss
 *
 */

import java.util.Comparator;
import java.lang.IllegalArgumentException; 

/**
 * 
 * This abstract class is extended by SelectionSort, InsertionSort, MergeSort, and QuickSort.
 * It stores the input (later the sorted) sequence. 
 *
 */
public abstract class AbstractSorter
{
	
	protected Point[] points;    // array of points operated on by a sorting algorithm. 
	                             // stores ordered points after a call to sort(). 
	
	protected String algorithm = null; // "selection sort", "insertion sort", "mergesort", or
	                                   // "quicksort". Initialized by a subclass constructor.
		 
	protected Comparator<Point> pointComparator = null;  
	
	protected int order;

	
	// Add other protected or private instance variables you may need. 
	

	protected AbstractSorter()
	{
		// No implementation needed. Provides a default super constructor to subclasses. 
		// Removable after implementing SelectionSorter, InsertionSorter, MergeSorter, and QuickSorter.
	}
	
	/**
	 * This constructor accepts an array of points as input. Copy the points into the array points[]. 
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	protected AbstractSorter(Point[] pts) throws IllegalArgumentException
	{
		if(pts == null) {
			throw new IllegalArgumentException("pts is null in AbstractSorter.");
		}
		if(pts.length == 0) {
			throw new IllegalArgumentException("pts is == 0 in AbstractSorter.");
		}
		points = pts.clone();
	}

	/**
	 * Generates a comparator on the fly that compares by x-coordinate if order == 0, by y-coordinate if order == 1. Assign the comparator to the variable pointComparator. 
	 * 
	 * @param order  0   by x-coordinate 
	 * 				 1   by y-coordinate
	 * 			    
	 * @throws IllegalArgumentException if order is less than 0 or greater than 1
	 *        
	 */
	public void setComparator(int order) throws IllegalArgumentException
	{
		if(order < 0) {
			throw new IllegalArgumentException("Order is less than 0 in setComparator.");
		}
		if(order > 1) {
			throw new IllegalArgumentException("Order is greater than 1. in setComparator.");
		}
		if(order == 0) {
			pointComparator = new Comparator<Point>(){
				@Override
				public int compare(Point p1, Point p2) {
				return p1.getX() - p2.getX();
				}
			};	
		} else if (order == 1) {
			pointComparator = new Comparator<Point>(){
				@Override
				public int compare(Point p1, Point p2) {
				return p1.getY() - p2.getY();
				}
			};

		}
		
	}
	
	/**
	 * Use the created pointComparator to conduct sorting.  
	 * 
	 * Ought to be protected. Made public for testing. 
	 */
	public abstract void sort(); 
	
	/**
	 * Obtain the point in the array points[] that has median index 
	 * 
	 * @return	median point 
	 */
	public Point getMedian()
	{
		return points[points.length/2]; 
	}
	
	/**
	 * Copys the array points[] onto the array pts[]. 
	 * 
	 * @param pts
	 */
	public void getPoints(Point[] pts)
	{
		//pts = points.clone();
		//IDK WHY CLONE DOESN'T WORK BUT IT SETS CORRECTLY THEN IS NULL? WDF
		for (int i = 0; i < points.length; i++) {
			pts[i] = points[i];
		}
	}

	/**
	 * Swaps the two elements indexed at i and j respectively in the array points[]. 
	 * 
	 * @param i
	 * @param j
	 */
	protected void swap(int i, int j)
	{
		Point[] pointsSwap = new Point[1];
		pointsSwap[0] = points[i];
		points[i] = points[j];
		points[j] = pointsSwap[0];
	}	
}
