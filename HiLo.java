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

public class HiLo extends Application {
    private final int MAX_NUM = 100;

    private int randomNum;

    private final int WINDOW_WIDTH = 600;
    private final int WINDOW_HEIGHT = 300;

    private Text gameRules;

    private HBox textBox;
    private TextField guessField;
    private Text guessFieldLabel;

    private Text resultText = new Text("");

    private HBox buttonBox;
    private final Button yesButton = new Button("Yes");
    private final Button noButton = new Button("No");

    private GridPane gridPane;

    @Override
    public void start(Stage primaryStage) {
        // layout
        gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: beige");
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        // display game rules
        gameRules = new Text("This program allows you to play a guessing game.\n" +
                "I will think of a number between 1 and " + MAX_NUM +
                " and will allow you to guess until you get it.\n" +
                "For each guess, I will tell you whether the right answer is " +
                "higher or lower than your guess.\n" +
                "I'm thinking of a number...\n");
        gridPane.add(gameRules, 1, 1, 2, 2);

        // generate a random number between 1-100, inclusive
        randomNum = (int)(Math.random() * MAX_NUM + 1);
        System.out.println("randomNum is: " + randomNum);

        // prompt user to input guess in text field, pressing "enter" to submit the guess
        guessFieldLabel = new Text("Your guess? ");
        guessField = new TextField();
        textBox = new HBox(guessFieldLabel, guessField);
        textBox.setSpacing(20);
        textBox.setAlignment(Pos.CENTER);
        gridPane.add(textBox, 2, 3, 1, 1);

        // buttons
        yesButton.setOnAction(this::handleYesButton);
        noButton.setOnAction(this::handleNoButton);

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
        int userGuess = Integer.parseInt(guessField.getText());

        if (userGuess == randomNum) { // if user chooses correctly, ask if want to replay, show yes/no buttons
            resultText.setText(userGuess + " is correct!\nClick \"yes\" to play again or \"no\" to quit");
            resultText.setFill(Color.GREEN);
            toggleButtons(true);

        }
        else if (userGuess > randomNum) {
            resultText.setText("lower - " + userGuess + " is too high");
            resultText.setFill(Color.BLUE);
        }
        else {
            resultText.setText("higher - " + userGuess + " is too low");
            resultText.setFill(Color.RED);
        }

        guessField.clear();
    }

    // Resets the game when the "yes" button is clicked and hides buttons
    private void handleYesButton(ActionEvent event) {
        guessField.clear();
        toggleButtons(false);
    }

    // Quits the game when the "no" button is clicked
    private void handleNoButton(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    // Displays buttons only when selection is allowed (status is true), i.e., when user guesses correctly
    private void toggleButtons(boolean status) {
        yesButton.setVisible(status);
        noButton.setVisible(status);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
