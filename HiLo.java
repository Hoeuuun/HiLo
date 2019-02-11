import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class HiLo extends Application {
    private final int MAX_NUM = 100;
    private final String RULES = "This program allows you to play a guessing game.\n I will think of a number between" +
            " 1 and %d and you will guess until you get it.\n For each guess, I'll tell you whether the right answer is " +
            "higher or lower your guess.\nI'm thinking of the number...";

    private final String CORRECT_MSG = "%d is correct!\nClick \"yes\" to play again or \"no\" to quit";
    private final String GUESS_LOWER_MSG = "guess lower - %d is%shigh";
    private final String GUESS_HIGHER_MSG = "guess higher - %d is%slow";
    private final String INPUT_ERROR_MSG = "%s is not a number!";

    private final int WINDOW_WIDTH = 600;
    private final int WINDOW_HEIGHT = 300;

    private final Button yesButton = new Button("Yes");
    private final Button noButton = new Button("No");

    // Range to let the player know how close they are
    private final int ERROR_THRESHOLD = 15;
    private Random randomGen;
    private int randomNum;

    private Text gameRules;

    private HBox textBox;
    private TextField guessField;
    private Text guessFieldLabel;

    private Text resultText = new Text("");

    private HBox buttonBox;

    private GridPane gridPane;

    @Override
    public void start(Stage primaryStage) {
        // generate a random number between 1-100, inclusive
        randomGen = new Random();

        // initialize random number
        initGame();

        // layout
        gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: beige");
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        // display game rules
        gameRules = new Text(String.format(RULES, MAX_NUM));
        gridPane.add(gameRules, 1, 1, 2, 2);

        // prompt user to input guess in text field, pressing "enter" to submit the guess
        guessFieldLabel = new Text("Your guess? ");
        guessField = new TextField();
        textBox = new HBox(guessFieldLabel, guessField);
        textBox.setSpacing(20);
        textBox.setAlignment(Pos.CENTER);
        gridPane.add(textBox, 2, 3, 1, 1);

        yesButton.setOnAction(this::handleReplayButton);
        noButton.setOnAction(this::handleReplayButton);

        // buttons layout
        buttonBox = new HBox(yesButton, noButton);
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);
        buttonBox.setSpacing(20);
        gridPane.add(buttonBox, 2, 6, 1, 1);

        // hide buttons initially
        toggleButtons(false);

        // process user's guess
        guessField.setOnAction(this::processGuessField);

        // display the result
        gridPane.add(resultText, 2, 4, 1, 1);

        Scene scene = new Scene(gridPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("Hi-Lo Guessing Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Determines if guess is correct, too high, or too low
    private void processGuessField(ActionEvent event) {
        int userGuess;

        // Tries to parse user's input into a guessed integer
        try {
            userGuess = Integer.parseInt(guessField.getText());
        } catch (NumberFormatException e) {
            setResultText(String.format(INPUT_ERROR_MSG, guessField.getText()), Color.DARKRED);
            return;
        } finally {
            guessField.clear();
        }

        if (userGuess == randomNum) { // if user chooses correctly, ask if want to replay, show yes/no buttons
            setResultText(String.format(CORRECT_MSG, userGuess), Color.GREEN);
            guessField.setEditable(false);
            toggleButtons(true);
        }
        else if (userGuess > randomNum) {
            setResultText( String.format(GUESS_LOWER_MSG, userGuess, getAdverbForResponse(userGuess, randomNum)),
                    Color.RED);
        }
        else {
            setResultText( String.format(GUESS_HIGHER_MSG, userGuess, getAdverbForResponse(userGuess, randomNum)),
                    Color.BLUE);
        }
    }

    private void handleReplayButton(ActionEvent event) {
        if(event.getSource()==yesButton) {
            initGame();
            guessField.clear();
            setResultText("", Color.BLACK);
            toggleButtons(false);
            //allow the user text box to accept guesses again
            guessField.setEditable(true);
        }
        else if (event.getSource()==noButton) {
            Platform.exit();
            System.exit(0);
        }
    }

    // Displays buttons only when selection is allowed (status is true), i.e., when user guesses correctly
    private void toggleButtons(boolean status) {
        yesButton.setVisible(status);
        noButton.setVisible(status);
    }

    // Returns proper adverb when user's guess is too off
    private String getAdverbForResponse(int userGuess, int randomNum) {
        if (Math.abs(userGuess - randomNum) > ERROR_THRESHOLD) {
            return " too ";
        } else {
            return " ";
        }
    }

    private void setResultText(String msg, Color clr) {
        resultText.setText(msg);
        resultText.setFill(clr);
    }

    public int generateRandomNumber(int bound) {
        return randomGen.nextInt(bound) + 1;
    }

    private void initGame() {
        randomNum = generateRandomNumber(MAX_NUM);
        System.out.println("randomNum is: " + randomNum);
    }

    public static void main(String[] args) {
        launch(args);
    }
}