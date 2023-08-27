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
 * This class implements insertion sort.   
 *
 */

public class InsertionSorter extends AbstractSorter 
{
	// Other private instance variables if you need ... 
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts  
	 */
	public InsertionSorter(Point[] pts) 
	{
		super(pts);
		super.algorithm = "insertion sort";
	}	

	
	/** 
	 * Perform insertion sort on the array points[] of the parent class AbstractSorter.  
	 */
	@Override 
	public void sort()
	{
		int pointsLength = points.length;
		
		for(int i = 1; i < pointsLength; i++) {
			Point p = points[i];
			int comparePoint = i - 1;
			while((comparePoint > -1) && (pointComparator.compare(points[comparePoint], p) > 0)) {
				points[comparePoint + 1] = points[comparePoint];
				comparePoint--;
			}
			points[comparePoint + 1] = p;
		}
	}		
}
