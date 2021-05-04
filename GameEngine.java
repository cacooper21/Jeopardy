/***
 * Game Engine
 * The engine behind the Trivia Nite game
 *
 * Author: Christian Duncan
 * Modified by: ...
 * CSC340 - Spring 2021
 *
 * This controls the main game.
 ***/
import java.util.Collections;
import java.util.ArrayList;
import java.io.FileNotFoundException;

public class GameEngine implements Runnable {
    static final long DEFAULT_TIME_PER_QUESTION = 5000;  // 5 seconds by default
    
    QuestionDB qdb;          // The collection of Questions
    long timePerQuestion;    // The time per question (in Milliseconds)
    boolean done;            // Can be used to end the game
    long questionStartTime;  // The time the most recent question was given
    Question currentQuestion;  // What is the current question
    int currentAnswer;       // What is the answer to this question
    
    ArrayList<Player> players;  // The collection of Players
    
    /**
     * Constructor for the Game Engine
     * @param qdbFile The file containing the database of questions.
     **/
    public GameEngine(String qdbFile) throws FileNotFoundException {
        qdb = new QuestionDB(qdbFile);
        timePerQuestion = DEFAULT_TIME_PER_QUESTION;
        done = false;  // Can trigger to get the engine to stop
        players = new ArrayList<>();
    }

    /**
     * The main starting point - so this can be hosted by a Thread
     **/
    public void run() {
        qdb.shuffle();  // Shuffle up the questions

        try {
            Thread.sleep(timePerQuestion);  // Sleep a few seconds before the first question is asked.
        } catch (InterruptedException ignored) { }
        
        while (!done) {
            doOneRound();
        }
    }

    // Perform a single round of the question
    private void doOneRound() {
        currentQuestion = qdb.nextQuestion();
        if (currentQuestion == null) {
            // We went through them all.
            // Options:
            //    1) End the game -- done = true
            //    2) End a round -- report the winners and start again (though that would be boring with same questions)
            //    3) Just reshuffle -- even more boring but this is the option I'll take.
            qdb.shuffle();
            currentQuestion = qdb.nextQuestion();
        }
        String cAnsString = currentQuestion.choices.get(0);  // Store the answer as we are about to shuffle the choices.
        Collections.shuffle(currentQuestion.choices);          // Shuffle the choices
        currentAnswer = currentQuestion.choices.indexOf(cAnsString);
        
        // Inform the GameClient of the new question
        informGameClientsOfQuestion(currentQuestion);
        
        // Sleep for the desired time limit
        questionStartTime = System.currentTimeMillis();  // Mark the start of this question (for scoring points)
        try {
            Thread.sleep(timePerQuestion);
        } catch (InterruptedException ignored) {
            // Argh, woken up... but continuing on as normal.
        }

        // Inform the GameClient of the answer (and the players' stats)
        informGameClientsOfAnswer(currentAnswer);

        // Sleep again for a short second before starting the next question
        try {
            Thread.sleep(timePerQuestion);
        } catch (InterruptedException ignored) {
            // Argh, woken up... but continuing on as normal.
        }
    }

    // Inform the players of the next question
    private synchronized void informGameClientsOfQuestion(Question currentQuestion) {
        for (Player p: players) {
            p.client.updateQuestion(currentQuestion);
        }
    }
    
    // Inform the players of the answer (and all the other players' scores)
    private synchronized void informGameClientsOfAnswer(int currentAnswer) {
        for (Player p: players) {
            p.client.postAnswer(currentAnswer, players);
        }
    }
    
    /**
     * Add a player to the game
     * @param name The name of the player
     * @returns The index ID of this player
     **/
    public int addPlayer(TriviaNite client, String name) {
        players.add(new Player(client, name));
        return players.size()-1;
    }

    /**
     * Player has made a choice
     * @param playerID Which player...
     * @param choice which choice...
     **/
    public void makeChoice(int playerID, int choice) { //should this e done y server?
        if (choice == currentAnswer) {
            // CORRECT!  Score is the time difference between posting and selecting in 10th of seconds decreasing.
            long timeTaken = System.currentTimeMillis() - questionStartTime;
            long timeRemaining = timePerQuestion - timeTaken;
            Player p = players.get(playerID);
            if (timeRemaining > 0 && p != null) {
                // There was time remaining and this is a valid player
                p.updateScore((long) Math.round(timeRemaining* 0.01));  // 10 points per second remaining
            }
        }
    }
}
