package language;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class looks at the user's question to find key words while ignoring any
 * stop words.
 */
public class StopWordsParser implements Parser {
	private ArrayList<String> swlist;

	/**
	 * The constructor reads in the list of stop words from the txt file. Also,
	 * every word is converted to lowercase for consistency.
	 */
	public StopWordsParser() {
		File file = new File("stopwords.txt");
		Scanner s;
		try {
			s = new Scanner(file);
			swlist = new ArrayList<String>();
			while (s.hasNext()) {
				String line = s.next();
				swlist.add(line.toLowerCase());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method parses the user's question into individual key words by using a
	 * regular expression to ignore any non-alphabetical characters; it also ignores
	 * stop words (read from the stopwords.txt).
	 * 
	 * @param sentence The user's question.
	 * @return a list of key words parsed from the user's question
	 */
	@Override
	public ArrayList<String> parseIntent(String sentence) {
		ArrayList<String> list = new ArrayList<String>();
		String clean = sentence.replaceAll("[\\[\\.,:;'^?!-+/*_=&%$#@~|}{\"<>\\]\\(\\)]", " ");
		String[] words = clean.toLowerCase().split("\\s+");
		for (String word : words) {
			if (word.equals("-"))
				continue;
			if (!swlist.contains(word))
				list.add(word);
		}
		/*
		 * if (list.isEmpty()) list.add("sorry");
		 */
		return list;
	}
}
