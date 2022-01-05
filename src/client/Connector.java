package client;

import controller.BaseController;
import controller.MultiDialogueController;
import util.Answer;

/**
 * The Connector is an encapsulation of the method used to connect to the
 * backend classes of InfoBot. In the current implementation, the connector type
 * is always 'local', it means that the Bot is within the same local deployment
 * as the GUIClient. In the future, there will be other type of connectors, such
 * as using an online Web Service as the Bot, then the type will be 'remote'.
 */
public class Connector {
	protected String type;
	private BaseController controller;
	private static Connector connector;

	/**
	 * This is the creator of the connection object for its clients. Please note
	 * that there will be only one instance of the Connector to be shared (if there
	 * are multiple calls from clients). That is why getConnector method is used to
	 * get the connector instance instead of the Connector constructor. And that is
	 * is why the constructor is private.
	 * 
	 * @param type the type of Connector instance you are trying to get, currently
	 *             only 'local' is supported.
	 * @return the Connector instance
	 */
	public static Connector getConnector(String type) {
		if ("local".equalsIgnoreCase(type)) {
			if (connector != null)
				return connector;
			else {
				connector = new Connector(type);
				return connector;
			}
		} else {
			String url = "tobedetermined";
			return new Connector(url);
		}
	}

	/**
	 * Connector constructor - please note that this is private.
	 * 
	 * @param type the type of Connector you are trying to create, currently only
	 *             local type connector
	 * @see controller.MultiDialogueController
	 */
	private Connector(String type) {
		this.type = type;
		controller = new MultiDialogueController();
	}

	/**
	 * An overloaded Connector constructor - please note that this is also private.
	 * this is going to be implemented in the future.
	 * 
	 * @param type the type of Connector is to create
	 * @param url  leads to the backend controller services
	 */
	private Connector(String type, String url) {
		// will provide online version of controller here
	}

	/**
	 * The method used to get answers from the backend classes of InfoBot. Depends
	 * on the type of connector this getInfo method will connect to different type
	 * of controller classes. In the current implementation, it only supports the
	 * MultiDialogueController type.
	 * 
	 * @param question the user's question
	 * @return the answer to be displayed on the GUI interface
	 * @see controller.MultiDialogueController MultiDialogueController
	 */
	public String getInfo(String question) {
		Answer info;
		if ("local".equalsIgnoreCase(type)) {
			// connect to local controller
			info = controller.getInfo(question);
		} else {
			// for online controller
			return null; // dummy for now
		}
		return info.getAnswer();
	}
}
