/***
 * Trivia Nite:
 * A simple game to ask some trivia questions.
 *
 * Author: Christian Duncan
 * Modified by: ...
 * CSC340 - Spring 2021
 *
 * This is the Main GUI interface to the TriviaNite game.
 ***/
import java.awt.*;        // import statements to make necessary classes available
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class TriviaNite extends JFrame {
    public static final int MAX_CHOICES = 8;   // Maximum choices allowed
    
    private GameEngine gameEngine;   // The Game Engine
    private Thread gameEngineThread; // The Thread that is running this game Engine
    
    JTextPane questionArea;          // The question area
    ButtonGroup choices;             // The list of choices (as buttons)
    int playerID;                    // Which player am I?
    Question currentQuestion = null; // The current question
    JDialog answerWindow;            // A window to show the answer and player scores (between questions)
    JTextArea answerTextArea;        // The text area hold the answer and scores
    ClientToServerConnection clientConnection;
    public static class PlayerScores {
        String name;
        String score;
        public PlayerScores(String name, String score){
            this.name = name;
            this.score = score;
        }
    }
    public TriviaNite() {
        setLocation(100, 100);
        setTitle("Trivia Nite");
        Container mainPane = getContentPane();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
        mainPane.setPreferredSize(new Dimension(1000, 700));

        // Make the space for the various components
        questionArea = new JTextPane();
        questionArea.setEditable(false);
        questionArea.setContentType("text/html");
        questionArea.setFont(new Font("Serif", Font.BOLD, 30));
        questionArea.setText("<html><center><font face=\"Serif\" size=\"30\">Please connect to the game to start playing.</font></center></html>");
        mainPane.add(questionArea);

        JPanel choicePanel = new JPanel();
        choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.Y_AXIS));
        choices = new ButtonGroup();
        Action choiceAction = new AbstractAction("Choice") {
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof JToggleButton) {
                        JToggleButton selectedButton = (JToggleButton) e.getSource();  // Which button was selected
                        // Find the choice and disable the buttons
                        int c = 0;
                        int choice = -1;
                        for (Enumeration<AbstractButton> enumeration = choices.getElements(); enumeration.hasMoreElements(); c++) {
                            JToggleButton choiceButton = (JToggleButton) enumeration.nextElement();
                            if (choiceButton == selectedButton) {
                                choice = c;
                            }
                            choiceButton.setEnabled(false);
                        }
                        assert(choice >= 0);   // If it isn't there is a coding error somewhere!
                        // Inform the Game Engine that this choice was made
                        gameEngine.makeChoice(playerID, choice);
                    } else {
                        System.out.println("ERROR: Action was not a button? " + e.getSource());
                    }
                }
            };
        
        for (int i = 0; i < MAX_CHOICES; i++) {
            JToggleButton button = new JToggleButton(choiceAction);
            button.setVisible(false);
            choices.add(button);
            choicePanel.add(button);
        }
        choicePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        choicePanel.setPreferredSize(new Dimension(400, 600));
        choicePanel.setMaximumSize(new Dimension(400, 600));
        choicePanel.setBorder(BorderFactory.createTitledBorder("Choices"));
        mainPane.add(choicePanel);

        // Setup the menubar
        setupMenuBar();

        // Setup the answer window
        setupAnswerWindow();
        
       
    }

    
    
    // Helper function to setup the menu bar
    private void setupMenuBar() {
        JMenuBar mbar = new JMenuBar();
        JMenu menu;
        JMenuItem menuItem;
        Action menuAction;
        menu = new JMenu("Game");
        menuAction = new AbstractAction("Start Game") {
                public void actionPerformed(ActionEvent event) {
                     //set up client connection
                
                    // "Connect to" the game -- in our case we will just start up a game.
                    // First get the name
                    String name = JOptionPane.showInputDialog("Please enter your name.");
                     clientConnection = new ClientToServerConnection(TriviaNite.this, name);
                     clientConnection.start();
                    //createGame();  // In a network version you would NOT use this! use in gameserver
                    
                    // "Register" the player send request to server and wait for response, handled by client to server
                    //playerID = gameEngine.addPlayer(TriviaNite.this, name);
                }
            };
        menuAction.putValue(Action.SHORT_DESCRIPTION, "Join the game");
        menuItem = new JMenuItem(menuAction);
        menu.add(menuItem);
        mbar.add(menu);
        setJMenuBar(mbar);
    }
   
    // Set up the answer window - used for the answer and the current list of players and scores
    private void setupAnswerWindow() {
        answerWindow = new JDialog(this, "Answer and Scores");
        answerTextArea = new JTextArea(10, 40);
        answerTextArea.setEditable(false);
        Font answerFont = new Font("Serif", Font.BOLD, 30);
        answerTextArea.setFont(answerFont);
        answerWindow.add(answerTextArea);
        answerWindow.setVisible(false);
        answerWindow.pack();
        answerWindow.setResizable(true);
    }
    
    // Just a helper to create a button label in a nice style -- easier to edit here to taste
    private String buttonLabel(int index, String label) {
        // return new String("<html><font face=\"Serif\" size=\"20\">(" + (char) ('A' + index) + "): " + label + "</font></html>");
        return new String("(" + (char) ('A' + index) + "): " + label + "");
    }

    /**
     * Update the current question
     *   This is invoked by the GameEngine (or in the Network version by a Client Connection that receives
     *   a message from the GameServer.)
     * @param question The new question to display
     * NOTE: You might want to synchronize the question list itself or disable the buttons somehow to avoid a race condition if
     *       the button is pressed while the question is being updated.  (Just a potential issue.)
     **/
    public synchronized void updateQuestion(Question question) {
        currentQuestion = question;  // Store it for reference!
        assert(question.choices.size() <= choices.getButtonCount());  // If not, we need more choices or the question should be reworded! 
        questionArea.setText("<html><center><font face=\"Serif\" size=\"30\">" + question.question + "</font></center></html>");
        int i = 0;
        choices.clearSelection();
        for (Enumeration<AbstractButton> e = choices.getElements(); e.hasMoreElements(); ) {
            JToggleButton choiceButton = (JToggleButton) e.nextElement();
            if (i < question.choices.size()) {
                String choice = question.choices.get(i);
                choiceButton.setText(buttonLabel(i, choice));
                choiceButton.setVisible(true);
                choiceButton.setSelected(false);
                choiceButton.setEnabled(true);
            } else {
                choiceButton.setVisible(false);
            }
            i++;
        }

        // And hide the answer window
        answerWindow.setVisible(false);
    }

    /**
     * Post the answer to the current question
     *   This is invoked by the GameEngine (or in the Network version by a Client Connection that receives
     *   a message from the GameServer.)
     * @param answer Which choice was the real answer
     * @param players The list of all players (so one can see their current scores)
     * For simplicity we will just make it a message dialog box.  Nothing fancy!
     **/
    public synchronized void postAnswer(int answer, ArrayList<PlayerScores> players) {
        StringBuilder message = new StringBuilder();
        if (currentQuestion != null) {
            // Could be that the answer is posted for previous question - so ignore that part then.
            message.append("Answer: (" + (char) ('A' + answer) + ") ");
            message.append(currentQuestion.choices.get(answer));
            message.append("\n\n");
        }
        message.append("Scores:\n=======\n");
        for (PlayerScores p: players) {
            message.append(p.name + ": " + p.score + "\n");
        }
        answerTextArea.setText(message.toString());
        answerWindow.pack();
        answerWindow.setVisible(true);
    }
    
    /**
     * The main entry point that sets up the window and basic functionality
     */
    public static void main(String[] args) {
        TriviaNite frame = new TriviaNite();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
