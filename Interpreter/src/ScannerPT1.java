import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;
import java.util.*;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

//EVAN VANG & CAITLIN TAN
//PHASE 1.2 SCANNER FOR Limp
//ADDITIONAL EXTENSION ADDED FOR KEYWORDS
//BUILT AND TESTED USING ECLIPSE IDE
//PARSING ADDED NOW FULLY FUNCTIONAL
//EVALUATOR 3.1 ADDED

public class ScannerPT1 {

	public static void Scanner(String[] tokens, String output, List<List<String>> arrayListOfTokens) {

		for (int i = 0; i < tokens.length; i++) {
			Token thing = new Token(tokens[i], output);
			while (thing.getLength() > 0) {
				if (thing.matchingRules(arrayListOfTokens) == false) {
					System.out.println("ERROR READING");
					try {
						FileWriter writer = new FileWriter(output, true);
						writer.write("ERROR READING \"" + thing.value + "\"\n");
						writer.close();
					} catch (IOException e) {
						System.out.println("Error for writer");
					}
					thing.value = "";
				}
			}
		}
	}

	// SCANNER FUNCTION
	public static String printTree(Node node, int height) {
		String tree = "";
		tree += node.value + " : " + node.type + "\n";
		tree += "\t\t";

		if (node.left != null) {
			for (int i = 0; i < height; i++) {
				tree += "\t\t";
			}
			tree += printTree(node.left, height + 1);

		}
		if (node.middle != null) {
			for (int i = 0; i < height; i++) {
				tree += "\t\t";
			}
			tree += printTree(node.middle, height + 1);

		}
		if (node.right != null) {
			for (int i = 0; i < height; i++) {
				tree += "\t\t";
			}
			tree += printTree(node.right, height + 1);
		}
		return tree;
	}
	// STRUCTURE OF PRINTING AST

	public static void evaluator(Node tree, Stack<Node> stk) throws Exception {
		if (tree == null) {
			return;
		}
		stk.push(tree);
		checkEvaluator(stk);
		evaluator(tree.left, stk);
		evaluator(tree.middle, stk);
		evaluator(tree.right, stk);
	}
	// CREATING EVALUATOR AND STACK

	public static void checkEvaluator(Stack<Node> stk) throws Exception {

		int size = stk.size();

		if (size < 3) {
			return;
		} else if ((stk.get(size - 1).type.equals("NUMBER") || stk.get(size - 1).type.equals("IDENTIFIER"))
				&& stk.get(size - 2).type.equals("NUMBER") || stk.get(size - 2).type.equals("IDENTIFIER")) {
			if (stk.get(size - 3).type.equals("SYMBOL")) {
				String temp = stk.get(size - 3).value;
				calculateEvaluator(stk, temp);
				checkEvaluator(stk);
			}
		}

	}
	// CHECKS SIZE OF STACK

	public static void calculateEvaluator(Stack<Node> stk, String operation) throws Exception {
		int elem1, elem2;
		elem1 = 0;
		elem2 = 0;

		if (stk.get(stk.size() - 1 - 1).type.equals("NUMBER")) {
			elem1 = Integer.parseInt(stk.get(stk.size() - 1).value);
		}
		if (stk.get(stk.size() - 2).type.equals("NUMBER")) {
			elem2 = Integer.parseInt(stk.get(stk.size() - 2).value);
		}
		if (operation.equals("+")) {
			Node temp = new Node(Integer.toString(elem1 + elem2), "NUMBER");
			popStack(stk);
			stk.set(stk.size() - 1, temp);
		}
		// CHECKS FOR + SYMBOL
		else if (operation.equals("-")) {
			if (elem2 - elem1 < 0) {
				Node temp = new Node("0", "NUMBER");
				popStack(stk);
				stk.set(stk.size() - 1, temp);
			} else {
				Node temp = new Node(Integer.toString(elem2 - elem1), "NUMBER");
				popStack(stk);
				stk.set(stk.size() - 1, temp);
			}
		}
		// CHECKS FOR - SYMBOL
		else if (operation.equals("*")) {
			Node temp = new Node(Integer.toString(elem1 * elem2), "NUMBER");
			popStack(stk);
			stk.set(stk.size() - 1, temp);
		}
		// CHECKS FOR * SYMBOL
		else if (operation.equals("/")) {

			if (elem1 == 0) {
				throw new Exception("Cannot Divide By 0!");
			}

			int quotient = elem2 / elem1;
			Math.round(quotient);

			if (quotient < 0) {
				quotient = 0;
			} else if (elem1 == 0) {
				throw new Exception("Cannot Divide By 0!");
			}

			Node temp = new Node(Integer.toString(quotient), "NUMBER");
			popStack(stk);
			stk.set(stk.size() - 1, temp);

		}
		// CHECKS FOR / SYMBOL
	}
	// HANDLES CALCULATIONS OF EVALUATOR

	public static void popStack(Stack<Node> stk) {
		stk.pop();
		stk.pop();
	}
	// POPS STACK IN EVALUATOR

	public static void main(String[] args) throws Exception {
		// UNCOMMENT SRC INPUT/SCANNER WHEN TESTING. SWITCH BACK TO ARGS [0] and [1] FOR
		// SUBMISSION
		List<List<String>> arrayListOfTokens = new ArrayList<List<String>>();

		// File input = new File("src/test.txt");
		 File input = new File(args[0]);

		// CHANGE TO "SRC/OUTPUT.TXT" WHEN TESTING. ARGS[0] FOR SUBMITTING ^

		try {
			Scanner myInput = new Scanner(input);
			while (myInput.hasNextLine()) {
				String data = myInput.nextLine();
				System.out.println("Line: " + data);
				String[] tokens = data.split("\\s+", 0);

				// Scanner(tokens, "src/output.txt", arrayListOfTokens);
				 Scanner(tokens, args[1], arrayListOfTokens);

				// CHANGE TO "SRC/OUTPUT.TXT" WHEN TESTING. ARGS[1] FOR SUBMITTING ^

				// PARSE AND PRINT TOKENS INTO AST
				// SPLITS INPUT INTO TOKENS BY WHITESPACE
			}
			myInput.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error, No File");
			// NO TEXT FILE FOUND ERROR
			// SCANNER CLASS MAINTAINED
		}

		 Parser parse = new Parser(arrayListOfTokens, args[1]);
		// Parser parse = new Parser(arrayListOfTokens, "src/output.txt");

		// CHANGE TO "SRC/OUTPUT.TXT" WHEN TESTING. ARGS[1] FOR SUBMITTING ^

		Node root = parse.parseExpression();
		String tree = printTree(root, 0);
		Stack<Node> stk = new Stack<>();
		// CALL PARSING FUNCTIONS FOR TOKENS
		try {

			FileWriter writer = new FileWriter(args[1], true);
			// FileWriter writer = new FileWriter("src/output.txt", true);

			// CHANGE TO "SRC/OUTPUT.TXT" WHEN TESTING. ARGS[1] FOR SUBMITTING ^
			System.out.println("\n" + "AST: \n" + tree);
			evaluator(root, stk);
			System.out.println("Output: " + stk.get(0).value);
			writer.write("\n" + "AST: \n" + tree);
			writer.write("\n" + "Output: " + stk.get(0).value);
			writer.close();
		} catch (IOException e) {
			System.out.println("Error for writer");
		}
		// FILEWRITER TO PARSE AND EVALUATOR OUTPUT INTO OUTPUT.TXT
	}
}
