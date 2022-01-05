package controller;

import util.Answer;

/**
 * Provides an abstract class with one method for all different controller
 * classes to implement. It is an abstract class instead of an interface, since
 * some common code will be re-factored into this class in the future.
 */
public abstract class BaseController {
	/**
	 * returns the answer that matches the question from the user
	 * 
	 * @param question the user's question
	 * @return Answer the answer to the user's question and its related information
	 */
	public abstract Answer getInfo(String question);
}
