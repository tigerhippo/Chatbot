package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import knowledge.KnowledgeBase;
import knowledge.LocalKBMultiples;
import language.Parser;
import language.StopWordsParser;
import util.Answer;

/**
 * This class manages the conversation between the InfoBot and a user by
 * prompting the user how and when to ask questions, and by keeping a record to
 * see if it answers the user's questions correctly. If there are multiple
 * matched answers, it will send back answers one at a time.
 */
public class MultiDialogueController extends BaseController {
	private Parser sp;
	private KnowledgeBase kb;
	private Stage conversation;

	/**
	 * The constructor initializes the Parser, KnowledgeBase, and Stage member
	 * variables.
	 */
	public MultiDialogueController() {
		this.sp = new StopWordsParser();
		this.kb = new LocalKBMultiples();
		this.conversation = new Stage();
	}

	/**
	 * This is the method used to manage the conversation between the InfoBot and a
	 * user. It sends the question asked by the user to the Parser, to get back the
	 * user's key words. The key words are then sent to the knowledge base to get a
	 * matching answer (or multiple answers) back.
	 * <p>
	 * If there are multiple answers matching the user's question, this method will
	 * return one answer at a time back to the user, then, check to see if the
	 * answer is accepted by the user or not, if yes, it stops sending the rest of
	 * the potential matching answers to the user, if no, it keeps sending the rest
	 * of the answers to the user until it exhausts all the answers.
	 * <p>
	 * It also handles many special conversational cases, such as greetings from
	 * users, empty questions, reseting the conversation, existing the program.
	 * 
	 * @param question The question the user asks
	 * @return Answer the answer to the user's question and its related information
	 */
	@Override
	public Answer getInfo(String question) {
		ArrayList<Answer> answers = conversation.answers;
		Answer answer = new Answer();
		if (question == null || question.trim().length() == 0) {
			answer.setAnswer("Please type words in your question, before you hit the return key.");
			answer.setRank(1);
			return answer;
		}
		String qq = question.trim(); // have to trim the user input here
		String q = qq.toLowerCase();
		if (conversation.userQuestion && checkGreetings(q)) {
			answer.setAnswer("Hi, I am LHS Info Bot. I am happy to answer your questions.");
			answer.setRank(1);
			return answer;
		}
		// potentially we can screen off very short words (<=3) here
		if ((!conversation.userQuestion)
				&& (q.equals("yes") || q.equals("y") || q.equals("yeah") || q.equals("correct"))) {
			answer.setAnswer("Thank you. Please ask another question. Or type Bye to end the conversation.");
			// reset everything for conversation
			conversation.reset();
			return answer;
		}
		if ((!conversation.userQuestion) && (q.equals("restart") || q.equals("reset") || q.equals("start over")
				|| q.equals("redo") || q.equals("try again"))) {
			answer.setAnswer("OK. Ready. Please type in your question.");
			// reset everything for conversation
			conversation.reset();
			return answer;
		}
		if (conversation.userQuestion) {
			ArrayList<String> intents = sp.parseIntent(q);
			answers = kb.getAnswers(intents);
			conversation.answers = answers;
			if (answers.size() > 1) {
				if (!conversation.answerReturned)
					rankAnswers(answers);
				if (conversation.currentIndex < answers.size()) {
					Answer intermediate = answers.get(conversation.currentIndex);
					conversation.currentIndex++;
					conversation.userQuestion = false;
					conversation.answerReturned = true;
					String ret = "Did you mean to ask: " + intermediate.getQuestion() + "?" + "\nHere is the answer: "
							+ intermediate.getAnswer() + "\nDoes this answer your question? Please type Yes or No"
							+ "\nOr type reset to start over";
					answer.setAnswer(ret);
				} else { // used all the answers
					answer.setAnswer(
							"Sorry, there is no answer available for your question. Please try another question.");
					// end of the conversation
					conversation.reset();
				}
			} else { // just one answer, pass through to user
				answer = answers.get(0);
			}
		} else { // don't go to the knowledge base yet, since you are doing multiple turns
			if (conversation.currentIndex < answers.size()) {
				Answer intermediate = answers.get(conversation.currentIndex);
				String ret = "Did you mean to ask: " + intermediate.getQuestion() + "?" + "\nHere is the answer: "
						+ intermediate.getAnswer() + "\nDoes this answer your question? Please type Yes or No"
						+ "\nOr type reset to start over.";
				answer.setAnswer(ret);
				conversation.currentIndex++;
			} else {
				answer.setAnswer("Sorry, there is no answer available for your question.");
				answer.setRank(1);
				// end of it, reset conversation:
				conversation.reset();
			}
		}
		return answer;
	}

	/**
	 * This method ranks the answers to the user's question based upon the number of
	 * key words matched, from the most to the least.
	 * 
	 * @param answers The list of answers
	 */
	private void rankAnswers(ArrayList<Answer> answers) {
		Collections.sort(answers);
	}

	/**
	 * This method checks if the user is trying to greet the InfoBot or not.
	 * 
	 * @param question The question asked by the user
	 * @return boolean if it is greeting from the user returns true, otherwise false
	 */
	private boolean checkGreetings(String question) {
		// find if there are greeting phrases from the user if a match is found, return
		// true, otherwise false
		Pattern p = Pattern
				.compile("^(hi|hello|howdy|good morning|good evening|good afternoon|how are you|how do you do)");
		Matcher m = p.matcher(question);
		if (m.find())
			return true;
		return false;
	}
}
