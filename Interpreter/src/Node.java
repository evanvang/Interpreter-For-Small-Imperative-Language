import java.util.ArrayList;
import java.util.List;

public class Node {

	public Node left;
	public Node right;
	public String value;
	public String type;
	public Node middle; 
	
	public Node(String value, String type, Node left, Node right) {

		this.left = left;
		this.right = right;
		this.value = value;
		this.type = type;
		this.middle = null;
	}
	public Node(String value, String type, Node left, Node middle, Node right) {

		this.left = left;
		this.right = right;
		this.value = value;
		this.type = type;
		this.middle = middle;
	}

	public Node(String value, String type) {

		this.left = null;
		this.right = null;
		this.value = value;
		this.type = type;

	}
	// NODE CLASS VARIABLES ADDED TO REFERENCE PARSER
}
