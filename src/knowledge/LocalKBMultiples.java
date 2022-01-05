package knowledge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import util.Answer;

/**
 * This class encapsulates all the questions and answers known to the InfoBot.
 */
public class LocalKBMultiples implements KnowledgeBase {
	Scanner s;
	File file;
	ArrayList<String> knowledge;

	/**
	 * This constructor reads in a csv file which contains all the questions and
	 * corresponding answers for the InfoBot.
	 */
	public LocalKBMultiples() {
		file = new File("kb.csv");
		try {
			s = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		knowledge = new ArrayList<String>();
		while (s.hasNextLine()) {
			String line = s.nextLine();
			int index = line.indexOf("?"); // quality check KB
			if (index != -1) {
				knowledge.add(line.toLowerCase());
			}
		}
	}

	/**
	 * This method checks if there is a match for the key words from the user's
	 * question. If there is a match, an answer is returned. If there is no match,
	 * it will tell the user that there is no answer found. Please be aware that
	 * there may be multiple matched answers returned to the user.
	 * 
	 * @param intents contains the keywords from the user's question.
	 * @return a list of the answers for the user
	 */
	public ArrayList<Answer> getAnswers(ArrayList<String> intents) {
		ArrayList<Answer> answers = new ArrayList<Answer>();
		String noAnswer = "Sorry, we don't have an answer for your question. Please try another question.";
		Answer a = new Answer();
		a.setAnswer(noAnswer);
		a.setRank(1);
		Answer answer = null;
		// if (intents.size() == 1 && intents.get(0).equals("sorry")) {
		if (intents.size() == 0) {
			answers.add(a);
			return answers;
		}
		// }
		for (String kk : knowledge) {
			String q = kk.substring(0, kk.indexOf("?")); // only compare the question not the answer
			boolean first = true;
			boolean found = false;
			for (String i : intents) {
				if (q.indexOf(i) == -1) {
					continue;
				} else {
					found = true;
					if (first) {
						answer = new Answer();
						answer.setRank(1);
						first = false;
					} else {
						answer.setRank(answer.getRank() + 1);
					}
				}
			}
			if (found) {
				answer.setAnswer(kk.substring(kk.indexOf("?") + 2));
				answer.setQuestion(q);
				answers.add(answer);
			}
		}
		if (answers.isEmpty()) {
			answers.add(a);
		}
		return answers;
	}
}
