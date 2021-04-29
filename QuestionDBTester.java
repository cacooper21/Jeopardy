/**
 * QuestionDBTester
 * Author: Christian Duncan
 * CSC340 - Spring 2021
 * A simple test to verify and demonstrate the QuestionDB class
 **/
import java.util.Collections;
import java.io.FileNotFoundException;

public class QuestionDBTester {
    public static void main(String[] args) throws FileNotFoundException {
        QuestionDB qdb = new QuestionDB("triviaQuestions.txt");
        qdb.shuffle();
        Question q;
        while ((q = qdb.nextQuestion()) != null) {
            System.out.println("Question: " + q.question);
            String answer = q.choices.get(0);  // Store the answer as we are about to shuffle the choices.
            Collections.shuffle(q.choices);  // Shuffle the choices
            for (int i = 0; i < q.choices.size(); i++) {
                System.out.println("  (" + (char) ('A' + i) + ") " + q.choices.get(i));
            }
            System.out.println("  Answer: " + answer);
        }
    }
}
