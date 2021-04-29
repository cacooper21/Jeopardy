/***
 * QuestionDB
 * A "database" of Questions.
 *
 * Author: Christian Duncan
 * Modified by: ...
 * CSC340 - Spring 2021
 *
 * NOTE: This is not a true database.  It is just a collection of Questions.
 ***/
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class QuestionDB {
    ArrayList<Question> qList;   // The list of questions
    int index;   // The index into the questions

    /**
     * Contruct an instance of a QuestionDB by reading from a given file.
     * @throws FileNotFoundException if the file is not able to be loaded.
     **/
    public QuestionDB(String qfile) throws FileNotFoundException {
        qList = new ArrayList<>();
        Question q;

        // Load the questions in --- later
        Scanner in = new Scanner(new File(qfile));
        while (in.hasNext()) {
            String line = in.nextLine();
            if (line != null && line.length() > 0 && line.charAt(0) != '#') {
                String[] list = line.split(",[ ]*(?=([^\"]*\"[^\"]*\")*[^\"]*$)");  // split by commas but also ignore commas in "..."
                if (list.length < 2) {
                    System.err.println("Warning: Invalid question. " + line);
                    System.err.println("line[0] = " + list[0]);
                } else {
                    String question = list[0];
                    if (question.charAt(0) == '\"') {
                        // Strip the beginning and ending quotes.
                        // We don't do this for the choices - but that could be done too!
                        question = question.substring(1, question.length()-1);
                    }
                    ArrayList<String> choices = new ArrayList<>();
                    for (int i = 1; i < list.length; i++) choices.add(list[i]);
                    qList.add(new Question(question, choices));
                }
            }                          
        }                

        // Another way to create a set of questions...
        // q = new Question("What is 1+1?", "2", "1", "3", "4");
        // qList.add(q);
        
        // q = new Question("What is your favorite color?", "Yellow", "Blue", "Green", "Red");
        // qList.add(q);

        // q = new Question("What is the capital of France?", "Paris", "Europe", "London", "Marseille" );
        // qList.add(q);

        index = 0;
    }

    /**
     * Shuffle and reset the question database
     **/
    public void shuffle() {
        Collections.shuffle(qList);
        index = 0;
    }

    /**
     * Next Question
     * @return a COPY of the next Question in the (shuffled) database or null if none left
     **/
    public Question nextQuestion() {
        if (index >= qList.size()) return null;
        else {
            index++;
            return new Question(qList.get(index-1));
        }
    }
}
