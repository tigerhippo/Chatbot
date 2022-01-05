package knowledge;

import java.util.ArrayList;

import util.Answer;

/**
 * This interface provides a method for all the knowledge base classes to
 * implement.
 */
public interface KnowledgeBase {
	/**
	 * This method gets a list of answers based upon the user's intent
	 * 
	 * @param intent The user's keywords
	 * @return a list of the answers for the user
	 */
	ArrayList<Answer> getAnswers(ArrayList<String> intent);
}
