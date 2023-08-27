package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.util.Random;

public class Test {

	public static void main(String arg[]) throws FileNotFoundException {
		int[] testArr = new int[] { 0, 0, -3, -9, 0, -10, 8, 4, 3, 3, -6, 3, -2, 1, 10, 5, -7, -10, 5, -2, 7, 3, 10, 5,
				-7, -10, 0, 8, -1, -6, -10, 0, 5, 5 };
		Point[] pointArr = generateArr(testArr);
		Point[] resultArr = new Point[testArr.length / 2];
		Random rand = new Random();
		Point[] pointArr2 = CompareSorters.generateRandomPoints(20, rand);
		Point[] resultArr2 = new Point[pointArr2.length];

		System.out.println("---Sorter Tests---");
		System.out.println("Selection Sort Test");
		System.out.print("Initial: ");
		printArr(pointArr);
		AbstractSorter sorter = new SelectionSorter(pointArr);
		sorter.setComparator(0);
		sorter.sort();
		sorter.getPoints(resultArr);
		System.out.print("X sorted: ");
		printArr(resultArr);
		sorter.setComparator(1);
		sorter.sort();
		sorter.getPoints(resultArr);
		System.out.print("Y sorted: ");
		printArr(resultArr);
		System.out.print("Initial: ");
		printArr(pointArr2);
		sorter = new SelectionSorter(pointArr2);
		sorter.setComparator(0);
		sorter.sort();
		sorter.getPoints(resultArr2);
		System.out.print("X sorted: ");
		printArr(resultArr2);
		sorter.setComparator(1);
		sorter.sort();
		sorter.getPoints(resultArr2);
		System.out.print("Y sorted: ");
		printArr(resultArr2);
		System.out.println("");

		System.out.println("Insertion Sort Test");
		System.out.print("Initial: ");
		printArr(pointArr);
		sorter = new InsertionSorter(pointArr);
		sorter.setComparator(0);
		sorter.sort();
		sorter.getPoints(resultArr);
		System.out.print("X sorted: ");
		printArr(resultArr);
		sorter.setComparator(1);
		sorter.sort();
		sorter.getPoints(resultArr);
		System.out.print("Y sorted: ");
		printArr(resultArr);
		System.out.print("Initial: ");
		printArr(pointArr2);
		sorter = new InsertionSorter(pointArr2);
		sorter.setComparator(0);
		sorter.sort();
		sorter.getPoints(resultArr2);
		System.out.print("X sorted: ");
		printArr(resultArr2);
		sorter.setComparator(1);
		sorter.sort();
		sorter.getPoints(resultArr2);
		System.out.print("Y sorted: ");
		printArr(resultArr2);
		System.out.println("");

		System.out.println("Merge Sort Test");
		System.out.print("Initial: ");
		printArr(pointArr);
		sorter = new MergeSorter(pointArr);
		sorter.setComparator(0);
		sorter.sort();
		sorter.getPoints(resultArr);
		System.out.print("X sorted: ");
		printArr(resultArr);
		sorter.setComparator(1);
		sorter.sort();
		sorter.getPoints(resultArr);
		System.out.print("Y sorted: ");
		printArr(resultArr);
		System.out.print("Initial: ");
		printArr(pointArr2);
		sorter = new MergeSorter(pointArr2);
		sorter.setComparator(0);
		sorter.sort();
		sorter.getPoints(resultArr2);
		System.out.print("X sorted: ");
		printArr(resultArr2);
		sorter.setComparator(1);
		sorter.sort();
		sorter.getPoints(resultArr2);
		System.out.print("Y sorted: ");
		printArr(resultArr2);
		System.out.println("");
		
		System.out.println("Quick Sort Test");
		System.out.print("Initial: ");
		printArr(pointArr);
		sorter = new QuickSorter(pointArr);
		sorter.setComparator(0);
		sorter.sort();
		sorter.getPoints(resultArr);
		System.out.print("X sorted: ");
		printArr(resultArr);
		sorter.setComparator(1);
		sorter.sort();
		sorter.getPoints(resultArr);
		System.out.print("Y sorted: ");
		printArr(resultArr);
		System.out.print("Initial: ");
		printArr(pointArr2);
		sorter = new QuickSorter(pointArr2);
		sorter.setComparator(0);
		sorter.sort();
		sorter.getPoints(resultArr2);
		System.out.print("X sorted: ");
		printArr(resultArr2);
		sorter.setComparator(1);
		sorter.sort();
		sorter.getPoints(resultArr2);
		System.out.print("Y sorted: ");
		printArr(resultArr2);
		System.out.println("");
		
		System.out.println("---Point Scanner Tests---");
		System.out.println("Selection Sort Scanner Test");
		PointScanner testScanner = new PointScanner(pointArr, Algorithm.SelectionSort);
		testScanner.scan();
		System.out.println(testScanner.toString());
		System.out.println(testScanner.stats());
		testScanner.writeMCPToFile();
		testScanner = new PointScanner(pointArr2, Algorithm.SelectionSort);
		testScanner.scan();
		System.out.println(testScanner.toString());
		System.out.println(testScanner.stats());
		testScanner.writeMCPToFile();
		System.out.println("");
		
		System.out.println("Insertion Sort Scanner Test");
		testScanner = new PointScanner(pointArr, Algorithm.InsertionSort);
		testScanner.scan();
		System.out.println(testScanner.toString());
		System.out.println(testScanner.stats());
		testScanner.writeMCPToFile();
		testScanner = new PointScanner(pointArr2, Algorithm.InsertionSort);
		testScanner.scan();
		System.out.println(testScanner.toString());
		System.out.println(testScanner.stats());
		testScanner.writeMCPToFile();
		System.out.println("");
		
		System.out.println("Merge Sort Scanner Test");
		testScanner = new PointScanner(pointArr, Algorithm.MergeSort);
		testScanner.scan();
		System.out.println(testScanner.toString());
		System.out.println(testScanner.stats());
		testScanner.writeMCPToFile();
		testScanner = new PointScanner(pointArr2, Algorithm.MergeSort);
		testScanner.scan();
		System.out.println(testScanner.toString());
		System.out.println(testScanner.stats());
		testScanner.writeMCPToFile();
		System.out.println("");
		
		System.out.println("Quick Sort Scanner Test");
		testScanner = new PointScanner(pointArr, Algorithm.QuickSort);
		testScanner.scan();
		System.out.println(testScanner.toString());
		System.out.println(testScanner.stats());
		testScanner.writeMCPToFile();
		testScanner = new PointScanner(pointArr2, Algorithm.QuickSort);
		testScanner.scan();
		System.out.println(testScanner.toString());
		System.out.println(testScanner.stats());
		testScanner.writeMCPToFile();
		System.out.println("");
	}

	public static Point[] generateArr(int[] arr) {
		Point[] newArr = new Point[arr.length / 2];
		for (int i = 0; i < arr.length / 2; i++) {
			newArr[i] = new Point(arr[i * 2], arr[(i * 2) + 1]);
		}
		return newArr;
	}

	public static void printArr(Point[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (i == 0) {
				System.out.print("[" + arr[i].toString());
				if (arr.length == 1) {
					System.out.println("]");
				}
			} else if (i == arr.length - 1) {
				System.out.println(", " + arr[i].toString() + "]");
			} else {
				System.out.print(", " + arr[i].toString());
			}
		}
	}

	public static void printArr(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (i == 0) {
				System.out.print("[" + arr[i]);
				if (arr.length == 1) {
					System.out.println("]");
				}
			} 
			else if (i == arr.length - 1) {
				System.out.println(", " + arr[i] + "]");
			} 
			else {
				System.out.print(", " + arr[i]);
			}
		}
	}

}
