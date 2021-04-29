/***
 * Question
 * A single question in our game and the choices
 *
 * Author: Christian Duncan
 * Modified by: ...
 * CSC340 - Spring 2021
 ***/
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Question {
    String question;  // The question
    List<String> choices;  // The various choices (first choice IS the answer)

    /**
     * Main constructor
     * @param q The question to ask
     * @param choices An array of choices to present (done as a variable-length argument)
     **/
    public Question(String q, String... choices) {
        this.question = q;
        this.choices = Arrays.asList(choices);
    }

    /**
     * Main constructor
     * @param q The question to ask
     * @param choices An array of choices to present
     **/
    public Question(String q, List<String> choices) {
        this.question = q;
        this.choices = choices;
    }
    
    /**
     * Copy Constructor
     * @param copy The Question to copy
     **/
    public Question(Question copy) {
        this.question = copy.question;  // Note: References the SAME strings (since Strings are immutable)
        this.choices = new ArrayList<>(copy.choices.size());
        for (String c: copy.choices) {
            this.choices.add(c);
        }
    }
}
