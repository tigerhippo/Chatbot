package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

/**
 * GUIClient is the starting point of the whole InfoBot code base. It provides a
 * Java Swing based Graphical User Interface for users to enter their text to
 * interact with the Bot.
 * <p>
 * This class also reads in a help.txt file to provide users with instructions
 * on how to use the InfoBot. It takes the user's question and use an instance
 * of Connector to call the backend dialogue controller class. The controller
 * class will coordinate the conversation with the user.
 * <p>
 * This class implement Runnable so that it can spawn off a new thread in stead
 * of blocking the main thread of Java Virtual Machine.
 */
public class GUIClient implements Runnable {
	public static JTextArea display;
	public static JTextField userInput;
	private BufferedWriter bw;
	private StringBuffer sb;
	Connector connector;

	/**
	 * GUIClient Class constructor.
	 * 
	 * @param type   the type of connector, in current implementation, it is always
	 *               'local'.
	 * @param writer a writer that is pointing to a local log file to keep a log of
	 *               the conversation between user and the InfoBot.
	 */
	public GUIClient(String type, BufferedWriter writer) {
		connector = Connector.getConnector(type);
		bw = writer;
		sb = new StringBuffer();
	}

	/**
	 * The main entrance of this program
	 * 
	 * @param args the list of command line parameters - currently none will be
	 *             passed
	 * @exception Exception it potentially throws IOExceptions
	 */
	public static void main(String[] args) throws Exception {
		File helpFile = new File("help.txt");
		File file = new File("log");
		file.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		GUIClient client = new GUIClient("local", bw);
		client.readInHelp(helpFile);
		client.start();
	}

	/**
	 * The method that spawns a new thread and starts that thread.
	 */
	public void start() {
		Thread runner = new Thread(this);
		runner.start();
	}

	/**
	 * when the new thread is running, this method is called and in turn it sets the
	 * complete GUI layout.
	 */
	public void run() {
		setFrame();
	}

	/**
	 * Sets up the Frame and couple of Panes for the complete GUI set up. The Layout
	 * type is Box - first a top and down arrangement, then within the bottom Pane,
	 * there are several components arranged in a horizontal fashion (still using
	 * Box Layout).
	 */
	public void setFrame() {
		// frame for the whole GUI
		JFrame mainScreen = new JFrame("LHS InfoBot");
		mainScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainScreen.setSize(650, 350);
		mainScreen.setLocationRelativeTo(null);
		// Panel for displaying Bot vs. User conversation texts
		JPanel displayTextPanel = new JPanel();
		displayTextPanel.setLayout(new BoxLayout(displayTextPanel, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel("Your conversation with LHS InfoBot:");
		label.setLabelFor(displayTextPanel);
		displayTextPanel.add(label);
		displayTextPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		final JTextArea display = new JTextArea(25, 50);
		// need to change JTextArea's behavior to make it non editable and also always
		// update the position of the caret (so that the user can always see the most
		// current line of text
		display.setEditable(false);
		DefaultCaret caret = (DefaultCaret) display.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scroll = new JScrollPane(display);
		displayTextPanel.add(scroll);
		displayTextPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// Panel for the lower part of the GUI: a label, a text field and a help button
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));
		inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		inputPanel.add(Box.createHorizontalGlue());
		JLabel enterText = new JLabel("Enter enter text here: ", JLabel.LEFT);
		userInput = new JTextField(50);
		Font font = new Font("Courier", Font.BOLD, 16);
		userInput.setFont(font);
		inputPanel.add(enterText);
		inputPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		inputPanel.add(userInput);
		JButton helpButton = new JButton("Help");
		helpButton.setBounds(600, 100, 60, 20);
		inputPanel.add(Box.createRigidArea(new Dimension(30, 0)));
		inputPanel.add(helpButton);
		// add event action to the user input text field
		userInput.addKeyListener(new KeyListener() {
			/**
			 * When the user hits keyboard, this method will be called. But, only when the
			 * user hits the enter key, it will in turn call the displayText method.
			 */
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					displayText();
				}
			}

			/**
			 * This method shows the text of the user's question, it also gets the answer
			 * for user's question via a call to the connector, and shows the answer
			 * together with the user's question all in the big text area.
			 */
			private void displayText() {
				String input = userInput.getText();
				display.append("User: " + input + "\n");
				try {
					bw.write("User: " + input);
					bw.newLine();
					bw.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (input != null) {
					String in = input.toLowerCase();
					if (in.equals("bye") || in.equals("see you") || in.equals("goodbye") || in.equals("exit")
							|| in.equals("seeya")) {
						try {
							bw.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.exit(0);
					}
				}
				userInput.setText(null);
				//
				String info = connector.getInfo(input);
				// String[] answers = info.getInfo();
				String output = info;
				display.append("Bot: " + output + "\n");
				try {
					bw.write("Bot: " + output);
					bw.newLine();
					bw.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		/**
		 * Add an action listener to the help button, so that when a user click the
		 * button the actionPerformed method will be called to display user instructions
		 * to the user by popping up a dialog box.
		 */
		helpButton.addActionListener(new ActionListener() {
			/**
			 * Pop up a dialog box to display user instructions when a user clicks the help
			 * button.
			 * 
			 * @param evt the ActionEvent it receives when the user clicks the button.
			 */
			public void actionPerformed(ActionEvent evt) {
				final JTextArea textArea = new JTextArea();
				// textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
				Font font = new Font("Courier", Font.PLAIN, 12);
				textArea.setFont(font);
				textArea.setEditable(false);
				textArea.setText(sb.toString());
				JScrollPane scrollPane = new JScrollPane(textArea);
				scrollPane.setPreferredSize(new Dimension(700, 500));
				JOptionPane.showMessageDialog(mainScreen, scrollPane, "LHS Info Bot Instructions",
						JOptionPane.PLAIN_MESSAGE, null);
			}
		});
		// putting every piece together in this frame to display:
		Container contentPane = mainScreen.getContentPane();
		contentPane.add(displayTextPanel, BorderLayout.CENTER);
		contentPane.add(inputPanel, BorderLayout.PAGE_END);
		mainScreen.pack();
		mainScreen.setVisible(true);
	}

	/**
	 * Read in user instructions from a help.txt file and save the content of the
	 * file in a global string buffer
	 * 
	 * @see #sb sb
	 * @param file the file object of the help.txt file to be read by this method.
	 */
	private void readInHelp(File file) {
		Scanner s;
		try {
			s = new Scanner(file);
			while (s.hasNextLine()) {
				String line = s.nextLine();
				sb.append(line).append("\n");
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}