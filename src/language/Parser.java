package language;

import java.util.ArrayList;

/**
 * This interface provides a method for all the Parsers to implement
 */
public interface Parser {
	/**
	 * This method parses the user's question into a list of key words.
	 * 
	 * @param sentence The user's question
	 * @return a list of key words parsed from the user's question
	 */
	ArrayList<String> parseIntent(String sentence);
}
