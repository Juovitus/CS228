package edu.iastate.cs228.hw3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

/**
 *  
 * @author Noah Hoss
 *
 */

//Honestly, I could've done the error handling way better and it took up WAY too much space code-wise. IK that :(

/**
 * Takes input file named input.txt containing infix expressions on seperate
 * lines. File is located in default directory Doesn't implement file choosing
 * functionality.
 * 
 * Outputs to output.txt in same directory as input.txt Each line in output
 * contains postfix conversion of the corresponding line in input file.
 * 
 * @param args
 **/
public class Infix2Postfix {

	// Variables for ease of use.
	private static String outputFileName = ("output.txt");
	private static String inputFileName = ("input.txt");

	public static void main(String[] args) throws IOException {
		// Get file
		File inFile = new File(inputFileName);
		// Check if it exists, if it doesn't, we throw exception.
		if (!inFile.exists()) {
			throw new FileNotFoundException("Input file not found.");
		} else {
			// Create scanner
			Scanner readFile = new Scanner(inFile);
			// Start adding to stack if applicable?
			addToStack(readFile);
			// Close readFile.
			readFile.close();
		}
	}

	public static void addToStack(Scanner readFile) throws IOException {

		// Setup string for output ease.
		String outputString = "";
		// Var to check if output file exists after creation
		File file = new File(outputFileName);
		// We should also create the output file here prolly(Overwrite existing)
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
		if (!file.exists()) {
			writer.close();
			throw new IOException("File doesn't exist for some reason.");
		}

		// Let's check if input file has next
		while (readFile.hasNextLine()) {
			//Variables to check for expression errors easily.
			int operatorCheck = 0;
			int operandCheck = 0;
			double numParenthesis = 0;
			//Set output string
			outputString = "";
			// Setup stack of characters
			Stack<String> operatorStack = new Stack<String>();
			// Split on spaces
			String[] splitString = readFile.nextLine().split(" ");
			int splitLength = splitString.length - 1;
			String lastS = "";
			Boolean noSubExpression = false;
			Boolean tooManyOperators = false;
			Boolean tooManyOperands = false;

			// setup loop to go through each string in splitString
			for(String s : splitString) {
				if(precedenceOfOperators(s) >= 1) {
					operatorCheck--;
					operandCheck--;
				}
				if(operatorCheck > 1) {
					break;
				}
				//If current s is a value we just add it to string basically(Add a space for..spacing)
				if(precedenceOfOperators(s) == 0 && !s.equalsIgnoreCase("(") && !s.equalsIgnoreCase(")")) {
					//This is a VALUE NOT AN OPERATOR
					operatorCheck++;
					operandCheck++;
					outputString += s + " ";
					//If S is equal to an opening parenthesis then we push to stack
				}else if(s.equalsIgnoreCase("(")) {
					numParenthesis++;
					operatorStack.push(s);
					//if S is equal to a closing parenthesis then pop stack
				}else if(s.equalsIgnoreCase(")")) {
					//Catch no subexpression
					if(lastS.equalsIgnoreCase("(")) {
						lastS = s;
						noSubExpression = true;
						break;
					}
					if(precedenceOfOperators(lastS) > 0) {
						tooManyOperators = true;
						break;
					}
					numParenthesis--;
					while(!operatorStack.empty()) {
						String f = operatorStack.pop();
						if(f.equalsIgnoreCase("(")) {
							break;
						}else {
							outputString += f + " ";
						}
					}
				}else {
					if(operatorCheck < 0 || lastS.equalsIgnoreCase("(")) {
						tooManyOperators = true;
						lastS = s;
						break;
					}
					if(operandCheck >= 1) {
						tooManyOperands = true;
						break;
					}
					while(!operatorStack.empty()) {
						String sPeek = operatorStack.peek();
						if(sPeek.equalsIgnoreCase("(") || precedenceOfOperators(s) > precedenceOfOperators(sPeek)){
							break;
						}else {
							outputString += operatorStack.pop() + " ";
							//Too many operators left in stack
							if(!operatorStack.isEmpty()) {
								while(!operatorStack.isEmpty()) {
									//tooManyOps = true;
									//Pop so we know what operator is the error on
									lastS = operatorStack.pop();
									//break out of loop
								}
								break;
							}
						}
					}
					if(precedenceOfOperators(splitString[splitLength]) > 0) {
						tooManyOperators = true;
					}
					operatorStack.push(s);
				}
				lastS = s;
			}
			//Push operators to end of string(all of them dumby, not just one.)
			while(!operatorStack.isEmpty()) {
				operatorCheck++;
				operandCheck--;
				outputString+= operatorStack.pop() + " ";
			}
			if(operandCheck > 1) {
				tooManyOperands = true;
			}
			if(operatorCheck > 1) {
				
			}
			//Error handling
			while(true) {
				//Check for too many operators
				if(tooManyOperators) {
					outputString = "Error: too many operators (" + lastS + ")";
					break;
				}
				//Check for too many operands
				if(tooManyOperands) {
					outputString = "Error: too many operands (" + lastS + ")";
					break;
				}
				//Let's check for no subExpression error
				if(noSubExpression) {
					outputString = "Error: no subexpression detected ()";
					break;
				}
				//Let's check for parenthesis errors
				if(numParenthesis != 0) {
					if(numParenthesis / 2 <= 0) {
						outputString = "Error: No opening parenthesis detected.";
						break;
					}else {
						outputString = "Error: No closing parenthesis detected.";
						break;
					}
				}
				break;
			}
			//Write current line to file
			writeStringToFile(outputString, writer);
			//If statement so there isn't blank space on end of file
			if (readFile.hasNextLine()) {
				writer.newLine();
			}
		}
		readFile.close();
		writer.close();
	}

	/**
	 * Takes input string and writes it to file.
	 * 
	 * @param outputString - contains outputString from addToStack to add to file.
	 * @param writer       - file to write to
	 * @throws IOException
	 **/
	private static void writeStringToFile(String outputString, BufferedWriter writer) throws IOException {
		writer.write(outputString);
	}

	/**
	 * Takes input of charAtI and determines precedence of it based on operator.
	 * Precedence examples is coming from lecture A-20210405 2021-1
	 * 
	 * @param charAtI - should be an operator to determine precedence of.
	 **/
	private static int precedenceOfOperators(String charAtI) {
		// If's to check and set precedence. (Afterwards I realized I could easily use a
		// switch here lol.)
		// PEMDAS, so highest priority gets...set to highest precedence(besides
		// parenthesis as we handled those already)?
		if (charAtI.equalsIgnoreCase("^")) {
			// highest precedence
			return 3;
		} else if (charAtI.equalsIgnoreCase("*") || charAtI.equalsIgnoreCase("/") || charAtI.equalsIgnoreCase("%")) {
			// middle precedence - I believe modulo belongs here as its basically / 2
			return 2;
		} else if (charAtI.equalsIgnoreCase("+") || charAtI.equalsIgnoreCase("-")) {
			// lowest precedence
			return 1;
		}
		// If it's none of these it's not an operator to be considered here.
		return 0;
	}
}