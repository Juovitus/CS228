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
 * This class implements the version of the quicksort algorithm presented in the lecture.   
 *
 */

public class QuickSorter extends AbstractSorter
{

	// Other private instance variables if you need ... 
	int first;
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *   
	 * @param pts   input array of integers
	 */
	public QuickSorter(Point[] pts)
	{
		super(pts);
		super.algorithm = "quicksort";
	}
		

	/**
	 * Carry out quicksort on the array points[] of the AbstractSorter class.  
	 * 
	 */
	@Override 
	public void sort()
	{
		quickSortRec(first, points.length - 1);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last)
	{
		int partitioned;
		if(first >= last) {
			return;
		}
		partitioned = partition(first, last);
		quickSortRec(first, partitioned - 1);
		quickSortRec(partitioned + 1, last);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int first, int last)
	{
        Point pivotPoint = points[last];
        int firstNum = first - 1;

        for (int i = first; i < last; i++) { // does last need to be length???
        	if (pointComparator.compare(points[i], pivotPoint) <= 0) {
                firstNum++;
                swap(firstNum, i);
            }
        }
        swap(firstNum + 1, last);
        return firstNum+1; // is this right?
	}	
	// Other private methods in case you need ...
}
