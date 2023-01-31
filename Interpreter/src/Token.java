import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;
import java.util.*;
import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Token {

	public String value;
	public String output;
	// FileWriter ^
	FileWriter writer;
	// public String type;
	String IDENTIFIER = "^([a-z]|[A-Z])([a-z]|[A-Z]|[0-9])*";
	String NUMBER = "^([0-9]+)";
	String SYMBOL = "^\\+|\\-|\\*|/|\\(|\\)|\\:=|\\;";
	String KEYWORD = "if|then|else|endif|while|do|endwhile|skip";
	// STRING SYMBOLS KEYWORD ADDED ^

	Pattern ID = Pattern.compile(IDENTIFIER);

	Pattern NUMBER1 = Pattern.compile(NUMBER);

	Pattern SYMBOL1 = Pattern.compile(SYMBOL);

	Pattern KEYWORD1 = Pattern.compile(KEYWORD);

	// CREATING PATTERN OBJECTS KEY WORD ADDED ^
	// TAB ADDED ^^

	public Token(String word, String file) {
		value = word;
		output = file;
	}

	public int getLength() {
		return value.length();
	}

	public boolean matchingRules(List<List<String>> tokenList) {
		boolean ID = matchID(tokenList);
		boolean NUM = matchNUM(tokenList);
		boolean SYM = matchSYM(tokenList);
		if (ID || NUM || SYM) {
			return true;
		} else {
			return false;
		}
	}
	// CHECK STRING METHODS ^

	public boolean matchID(List<List<String>> tokenList) {
		Matcher iden = ID.matcher(value);
		Matcher keyword = KEYWORD1.matcher(value);
		Boolean id = iden.find();
		Boolean key = keyword.find();
		if (id && key) {
			try {
				writer = new FileWriter(output, true);
				if (iden.group().length() > keyword.group().length()) {
					List<String> temp = new ArrayList<String>();
					temp.add(iden.group());
					temp.add("IDENIFIER");
					tokenList.add(temp);
					System.out.println(iden.group() + ": IDENTIFIER");
					writer.write(iden.group() + ": IDENTIFIER\n");
					value = value.substring(iden.end());
					writer.close();
					return true;
				} else {
					List<String> temp = new ArrayList<String>();
					temp.add(iden.group());
					temp.add("IDENIFIER");
					tokenList.add(temp);
					System.out.println(keyword.group() + ": KEYWORD");
					writer.write(keyword.group() + ": KEYWORD\n");
					value = value.substring(keyword.end());
					writer.close();
					return true;
				}

			} catch (IOException e) {
				System.out.println("Error for writer");
			}
		} else if (id) {
			try {
				writer = new FileWriter(output, true);

				List<String> temp = new ArrayList<String>();
				temp.add(iden.group());
				temp.add("IDENTIFIER");
				tokenList.add(temp);

				System.out.println(iden.group() + ": IDENTIFIER");
				writer.write(iden.group() + ": IDENTIFIER\n");
				value = value.substring(iden.end());
				writer.close();
				return true;

			} catch (IOException e) {
				System.out.println("Error for writer");
			}
		}

		return false;
	}
	// IDENTIFIER METHOD ^
	// ADDED EXTENSION FOR KEYWORD FINDER. TESTS FOR BOTH AND SEPARATES FOLLOWING
	// MATCH
	// UTILIZES PRINCIPAL OF LONGEST SUBSTRING

	public boolean matchNUM(List<List<String>> tokenList) {
		Matcher number = NUMBER1.matcher(value);
		{

			if (number.find()) {
				try {
					List<String> temp = new ArrayList<String>();
					temp.add(number.group());
					temp.add("NUMBER");
					tokenList.add(temp);
					System.out.println(number.group() + ": NUMBER");
					writer = new FileWriter(output, true);
					writer.write(number.group() + ": NUMBER\n");
					writer.close();
				} catch (IOException e) {
					System.out.println("Error for writer");
				}
				value = value.substring(number.end());
				return true;
			}
			return false;
		}
		// NUMBER METHOD ^
	}

	public boolean matchSYM(List<List<String>> tokenList) {
		Matcher symbol = SYMBOL1.matcher(value);
		{

			if (symbol.find()) {
				try {
					List<String> temp = new ArrayList<String>();
					temp.add(symbol.group());
					temp.add("SYMBOL");
					tokenList.add(temp);
					System.out.println(symbol.group() + ": SYMBOL");
					writer = new FileWriter(output, true);
					writer.write(symbol.group() + ": SYMBOL\n");
					writer.close();
				} catch (IOException e) {
					System.out.println("Error for writer");
				}
				value = value.substring(symbol.end());
				return true;
			}
			return false;
		}
		// SYMBOL METHOD ^
	}

} // ARRAYLISTS ADDED IN MATCHERS TO IMPLEMENT PARSING
