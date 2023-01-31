import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

	public List<List<String>> tokenList;
	public List<String> nextToken;
	private String output;

	public Parser(List<List<String>> tokenList, String file) {
		this.tokenList = tokenList;
		output = file;
	}

	public String getNextToken() {
		return tokenList.get(0).get(0);
	}

	// PARSER VALUE
	public String getNextTokenType() {
		return tokenList.get(0).get(1);
	}

	public Node statement() throws Exception {
		Node root = baseStatement();
		if (tokenList.size() == 0) {
			return root;
		}
		while (getNextToken().equals(";")) {
			consume();
			root = new Node (";", "SYMBOL", root, baseStatement());
			if (tokenList.size() == 0) {
				break;
			}
		}
		return root;
	}
	
	public Node baseStatement() throws Exception {
		if (getNextTokenType().equals("IDENTIFIER")) {
			Node root = assignment();
			return root;
		}
		else if(getNextToken().equals("if")) {
			Node root = ifStatement();
			return root;
		}
		else if(getNextToken().equals("while")) {
			Node root = whileStatement();
			return root;
		}
		else if(getNextToken().equals("skip")) {
			consume();
			Node root = new Node ("skip", "IDENTIFIER");
			return root;
		}
		else throw new Exception("It Is Not In Grammar!");
		 
	}

	public Node assignment() throws Exception {
		String temp = getNextToken();
		consume();
		if (getNextToken().equals(":=")) {
			consume();
			Node root = new Node(":=", "SYMBOL", new Node(temp, "IDENTIFIER"), parseExpression());
			return root;
		} else
			throw new Exception("Error, Expected :=");
	}

	public Node ifStatement() throws Exception {
		consume();
		Node root = new Node("if", "Statememnt", parseExpression(), null);
		if (getNextToken().equals("then")) {
			consume();
		} else
			throw new Exception("Error, Expected then");
		root.middle = statement();
		if (getNextToken().equals("else")) {
			consume();
		}
		else 
			throw new Exception("Error, Expected else");
		root.right = statement();
		if (getNextToken().equals("endif")) {
			consume();
		}
		else 
			throw new Exception("Error, Expected endif");
		return root;
	}
	
	public Node whileStatement() throws Exception {
		consume();
		Node root = new Node ("while", "LOOP", parseExpression(), null);
		if (getNextToken().equals("do")) {
			consume();
		} else throw new Exception ("Error, Expected do");
		root.right = statement();
		if (getNextToken().equals("endwhile")) {
			consume();
		}
		else throw new Exception ("Error, Expected endwhile");
		return root;
	
	}
	
	// PARSER TYPE
	public Node parseExpression() throws Exception {
		Node root = parseTerm();
		if (tokenList.size() == 0) {
			return root;
		}

		while (getNextToken().equals("+")) {
			consume();
			root = new Node("+", "SYMBOL", root, parseTerm());
			if (tokenList.size() == 0) {
				break;
			}
		}
		return root;
	}

	// PARSE EXPRESSION CHECKS FOR +
	public Node parseTerm() throws Exception {

		Node root = parseFactor();
		if (tokenList.size() == 0) {
			return root;
		}

		while (getNextToken().equals("-")) {
			consume();
			root = new Node("-", "SYMBOL", root, parseFactor());
			if (tokenList.size() == 0) {
				break;
			}
		}
		return root;
	}

	// PARSE TERM CHECKS FOR -
	public Node parseFactor() throws Exception {
		Node root = parsePiece();
		if (tokenList.size() == 0) {
			return root;
		}

		while (getNextToken().equals("/")) {
			consume();
			root = new Node("/", "SYMBOL", root, parsePiece());
			if (tokenList.size() == 0) {
				break;
			}
		}
		return root;
	}

	// PARSE FACTOR CHECKS FOR /
	public Node parsePiece() throws Exception {
		Node root = parseElement();
		if (tokenList.size() == 0) {
			return root;
		}

		while (getNextToken().equals("*")) {
			consume();
			root = new Node("*", "SYMBOL", root, parseElement());
			if (tokenList.size() == 0) {
				break;
			}
		}
		return root;
	}

	// PARSE PIECE CHECKS FOR *
	public Node parseElement() throws Exception {
		if (getNextToken().equals("(")) {
			consume();
			Node root = parseExpression();
			if (tokenList.size() == 0) {
				// CHECKS FOR OPENING (

				FileWriter writer = new FileWriter(output, true);
				writer.write("No closing paranthesis found!");
				writer.close();
				throw new Exception("NO CLOSING PARANTHESIS");
			}
			if (getNextToken().equals(")")) {
				consume();
				return root;
			} else {
				FileWriter writer = new FileWriter(output, true);
				writer.write("No closing paranthesis found!");
				writer.close();
				throw new Exception("NO CLOSING PARANTHESIS");
			}
		} else if (getNextTokenType().equals("NUMBER")) {
			Node root = new Node(getNextToken(), getNextTokenType());
			consume();
			return root;
		} else if (getNextTokenType().equals("IDENTIFIER")) {
			Node root = new Node(getNextToken(), getNextTokenType());
			consume();
			return root;
		} else {
			FileWriter writer = new FileWriter(output, true);
		writer.write("Not In The Grammar!");
		writer.close();
		throw new Exception("Not In The Grammar");
		}
		
		
	}

	// PARSE ELEMENT GOING INTO NUMBER/IDENTIFIER/EXCEPTION HANDLING
	// CHECKS FOR CLOSING ) AND NUMBERS OR IDENTIFIERS
	// THROWS EXCEPTION IF NO CLOSING ) IS THERE
	public void consume() {
		tokenList.remove(0);
	}
	// FUNCTION TO CONSUME TOKEN AND GO TO FOLLOWING
}
