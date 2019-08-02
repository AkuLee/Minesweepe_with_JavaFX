import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MinesweeperGridView extends Application {

    boolean firstime = true;

    private VBox intro; // Scene
    private Stage primaryStage;
    private Controller controller;
    protected GridPane grid;

    private int length, height, nbMines;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage; // Copy primaryStage for global use in this class.
        makeIntroScene(); // Initialise intro scene.

        // --- Initialise screen ---
        primaryStage.setScene(new Scene(intro));
        primaryStage.setResizable(false);
        primaryStage.setTitle("MinesweeperGridView");
        primaryStage.show();
    }

    /**
     * Function that makes the intro scene where you choose the settings for the game. Scene is then set to private
     * attribute intro of VBox class.
     */
    private void makeIntroScene() {

        // --- Text ---
        Text    first = new Text("Length: "),
                snd = new Text("Height: "),
                third = new Text("Number of mines: ");

        // --- Spinner settings --- todo change textfield to scroller
        Spinner lengthText = new Spinner(2, 25, 10),
                heightText = new Spinner(2, 25, 10),
                nbMinesText = new Spinner(1, 500, 25);

        lengthText.setPrefSize(75, 30);
        heightText.setPrefSize(75, 30);
        nbMinesText.setPrefSize(75, 30);

        // --- Submit button ---
        Button submit = new Button("Submit");
        submit.setOnAction(event -> { // On submit, recuperate all settings given by user and generate the game from it.
            height = (int) lengthText.getValue();
            length = (int) heightText.getValue();
            makeGrid();

            nbMines = (int) nbMinesText.getValue();
            controller = new Controller(this, length, height, nbMines);
            primaryStage.setScene(new Scene(grid));
        });

        // --- Align settings input vertically ---
        HBox firstLine = new HBox(first, lengthText); firstLine.setAlignment(Pos.CENTER);
        HBox sndLine = new HBox(snd, heightText); sndLine.setAlignment(Pos.CENTER);
        HBox thirdLine = new HBox(third, nbMinesText); thirdLine.setAlignment(Pos.CENTER);

        intro = new VBox(firstLine, sndLine, thirdLine, submit);
        intro.setPrefSize(300, 300);
        intro.setAlignment(Pos.CENTER);

    }

    /**
     * Function that creates a GridPane of length x height with a button in each cell and sets it to private attribute
     * grid.
     */
    public void makeGrid() {

        grid = new GridPane();

        for (int i = 0; i < height; i++) { // Y-axis
            for (int j = 0; j < length; j++) { // X-axis
                Button button = new Button(" "); // Default initial content

                button.setStyle("-fx-font-weight: bold");
                button.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
                button.setBorder(new Border(
                        new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
                ));

                button.setPrefSize(40, 40); // Preferred size.
                button.setFocusTraversable(false); // Aesthetic non traversable
                button.setId(j + "-" + i); // "x-y"

                button.setOnAction((event) -> { // Recuperate (x,y) position and send to controller for logic changes.
                    String[] pos = button.getId().split("-");
                    controller.send(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));
                });

                if (firstime) {
                    System.out.println(button.isHover());
                    firstime = false;
                }

                grid.add(button, j, i);
            }
        }

    }

    /**
     * Function that sets a text to a button
     * @param color color of text
     * @param text the string
     * @param x the x-value of the button
     * @param y the y-value of the button
     */
    public void setGridText(Color color, String text, int x, int y) {
        Button button = (Button) grid.getChildren().get(y * length + x);
        button.setTextFill(color);
        button.setText(text);
        button.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));

    }

    /**
     * Function generates the losing screen and sets the scene with private attribute primaryStage..
     */
    public void defeat() {

        Text defeat = new Text("Nice try");
        defeat.setTextAlignment(TextAlignment.CENTER);
        defeat.setStyle("-fx-font-weight: bold");

        Button tryAgain = new Button("Try again");
        tryAgain.setOnAction(event -> {
            makeGrid();
            controller = new Controller(this, length, height, nbMines);
            primaryStage.setScene(new Scene(grid));
        });
        tryAgain.setFocusTraversable(false);
        tryAgain.setStyle("-fx-font-weight: bold");

        Button changeSettings = new Button("Play with new settings");
        changeSettings.setOnAction( event -> {
            makeIntroScene();
            primaryStage.setScene(new Scene(intro));
        });
        changeSettings.setFocusTraversable(false);
        changeSettings.setStyle("-fx-font-weight: bold");

        VBox again = new VBox(defeat, tryAgain, changeSettings);
        again.setAlignment(Pos.CENTER);

        grid.setOpacity(0.40);

        StackPane stack = new StackPane(grid, again);
        stack.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(stack)); // Set new scene.
    }

    /**
     * Function generates the winning screen and sets the scene with private attribute primaryStage.
     */
    public void victory() {
        Text defeat = new Text("Well played!");
        defeat.setTextAlignment(TextAlignment.CENTER);
        defeat.setStyle("-fx-font-weight: bold");

        Button playAgain = new Button("Play again");
        playAgain.setOnAction(event -> {
            makeGrid();
            controller = new Controller(this, length, height, nbMines);
            primaryStage.setScene(new Scene(grid));
        });
        playAgain.setFocusTraversable(false);
        playAgain.setStyle("-fx-font-weight: bold");

        Button changeSettings = new Button("Play with new settings");
        changeSettings.setOnAction( event -> {
            makeIntroScene();
            primaryStage.setScene(new Scene(intro));
        });
        changeSettings.setFocusTraversable(false);
        changeSettings.setStyle("-fx-font-weight: bold");

        VBox again = new VBox(defeat, playAgain, changeSettings);
        again.setAlignment(Pos.CENTER);

        grid.setOpacity(0.40);

        StackPane stack = new StackPane(grid, again);
        stack.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(stack)); // Set new scene.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
