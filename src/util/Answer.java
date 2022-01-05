package util;

/**
 * This class holds the answer to the user's question; the question field holds
 * the question from the csv file that corresponds to the answer for the user's
 * question, and the question's rank based upon how many key words the answer
 * matches.
 * <p>
 * It implements a Comparable interface, so that multiple answers in a list can
 * be sorted based on their ranks.
 */
public class Answer implements Comparable<Answer> {
	private String answer;
	private String question;
	private int rank;

	/**
	 * Constructor - setting the rank to zero to start with
	 */
	public Answer() {
		rank = 0; // initial no rank
	}

	/**
	 * Returns the answer field
	 * 
	 * @return the answer field for this Answer object
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * Sets the answer for this Answer object
	 * 
	 * @param answer set the answer for this object
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * Returns the rank
	 * 
	 * @return the rank for this answer
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Sets the rank
	 * 
	 * @param rank set the rank for this answer
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * Sets the question
	 * 
	 * @param question corresponding to the answer (based on the knowledge base)
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * Returns the question
	 * 
	 * @return the question corresponding to the answer (based on the knowledge
	 *         base)
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Implement the compareTo method for the Comparable interface. It defines that
	 * it is 'smaller' when its rank is bigger, so that in sorting, the defaulting
	 * sorting order can be used (ascending).
	 * 
	 * @see java.util.Collections#sort
	 * @param another another Answer object to compare
	 * @return +1 greater, -1 smaller, 0 equal
	 */
	@Override
	public int compareTo(Answer another) {
		if (this.rank == another.rank)
			return 0;
		if (this.rank < another.rank) // make sure
			return 1;
		else
			return -1;
	}
}
