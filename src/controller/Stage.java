package controller;

import java.util.ArrayList;

import util.Answer;

/**
 * The Stage keeps track of the stages of the conversation between the InfoBot
 * and the user by telling whether the user is asking a question or the InfoBot
 * is returning an answer to the user. The currentIndex is used to keep track of
 * the index of the current answer being returned. The answers ArrayList is used
 * to keep a list of all the possible answers to a question.
 */
public class Stage {
	boolean multistage = false;
	boolean userQuestion = true;
	boolean answerReturned = false;
	int currentIndex = 0;
	ArrayList<Answer> answers = null;

	/**
	 * This method sets the conversation stage back to the beginning, so that
	 * InfoBot is ready to answer a new question.
	 */
	void reset() {
		this.multistage = false;
		this.userQuestion = true;
		this.answerReturned = false;
		this.currentIndex = 0;
		this.answers = new ArrayList<Answer>();
	}
}
