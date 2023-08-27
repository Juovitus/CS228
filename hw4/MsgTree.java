package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 *  
 * @author Noah Hoss
 *
 */

public class MsgTree {
	public char payloadChar;
	public MsgTree left;
	public MsgTree right;
	private MsgTree root;
	//private MsgTree root;
	/*Can use a static char idx to the tree string for recursive
	solution, but it is not strictly necessary*/
	private static int staticCharIdx = 0;
	
	//Constructor building the tree from a string
	public MsgTree(String encodingString) {
		//set payload
		payloadChar = encodingString.charAt(staticCharIdx);
		//increment for recursion
		staticCharIdx++;
		//If payload is ^ then we create new node, otherwise its a leaf.
		if(payloadChar == '^') {
			left = new MsgTree(encodingString);
			right = new MsgTree(encodingString);
		}
	}
	
	//Constructor for a single node with null children
	public MsgTree(char payloadChar){
		this.payloadChar = payloadChar;
		left = null;
		right = null;
	}
	
	//method to print characters and their binary codes
	public static void printCodes(MsgTree root, String code){
		if(root == null) {
			//idk how we'd get here
			return;
		}else {
			//This is way longer than it needs to be because \n and \s were making issues
			//--Mainly \n
			if(root.payloadChar == '^') {
				//output left nodes first
				printCodes(root.left, code + "0");
				//then right nodes
				printCodes(root.right, code + "1");
			}else if(root.payloadChar == ' '){
				//output current payload char
				System.out.println("   " + "\\s" + "      " + code);
			}else if(root.payloadChar == '\n') {
				//output current payload char
				System.out.println("   " + "\\n" + "      " + code);
			}
			else {
				//output current payload char
				System.out.println("   " + root.payloadChar + "       " + code);
			}

		}
	}

	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
		//Get file name
		/**Current test file names are
		 * cadbard.arch
		 * constitution.arch
		 * monalisa.arch
		 * twocities.arch
		 */
		System.out.print("Please enter file name to decode: ");
		String inputFileName = scan.nextLine();
		//String inputFileName = "constitution.arch";
		
		// Get file
		File inFile = new File(inputFileName);
		// Check if it exists, if it doesn't, we throw exception.
		if (!inFile.exists()) {
			scan.close();
			throw new FileNotFoundException("Input file: " + inputFileName + " is not found.");
		} else {
			// Create scanner for file
			//Literally just used to check for next line, probably bad but IDC this is schoolwork
			Scanner scanLength = new Scanner(inFile);
			String stringOne = scanLength.nextLine();
			String binaryString = scanLength.nextLine();
			String combined = "";
			if(scanLength.hasNextLine()) {
				combined = stringOne + "\n" + binaryString + "\n";
				binaryString = scanLength.nextLine();
			}else {
				combined = stringOne;
			}
			
			//array for storage
			char [] charList = new char[combined.length()];
			for(int i = 0; i < combined.length(); i++) {
				charList[i] = combined.charAt(i);
			}
			MsgTree binTree = new MsgTree(combined);
			//Gonna use recursion to print out stuff so put this here
			System.out.println("character code");
			System.out.println("-------------------------");
			printCodes(binTree, "");
			System.out.println("\nMESSAGE: ");
			decode(binTree, binaryString);
			scan.close();
		}
	}
	
	public static void decode(MsgTree codes, String msg) {
		MsgTree currNode = codes;
		for(int i = 0; i < msg.length(); i++) {
			if(codes == null) {
				//We shouldn't get here but we gonna check for it :)
				return;
			}
			if(msg.charAt(i) == '0') {
				//go left and cut off 0
				currNode = currNode.left;
				String dz = msg.substring(1);
			}else if(msg.charAt(i) == '1') {
				//go right and cut off 1
				currNode = currNode.right;
				String dz = msg.substring(1);
			}
			if(currNode.payloadChar != '^') {
				System.out.print(currNode.payloadChar);
				currNode = codes;
			}
		}
	}
}
