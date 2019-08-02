package Minesweeper_w_Canvas;

import Minesweeper_w_Canvas.ControllerCanvas;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MinesweeperCanvasView extends Application {

    private VBox intro; // Scene
    private Stage primaryStage;
    private ControllerCanvas controllerCanvas;

    private GraphicsContext graphic;
    private Pane root;

    private int length, height, nbMines;

    private int imgPx = 2;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage; // Copy primaryStage for global use in this class.
        makeIntroScene(); // Initialise intro scene.

        // --- Initialise screen ---
        primaryStage.setScene(new Scene(intro));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Minesweeper");
        primaryStage.show();
    }

    /**
     * Function that makes the intro scene where you choose the settings for the game. Scene is then set to private
     * attribute intro of VBox class.
     */
    private void makeIntroScene() {

        // --- Text ---
        Text first = new Text("Length: "),
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
            length = (int) lengthText.getValue();
            height = (int) heightText.getValue();
            makeCanvas();

            nbMines = (int) nbMinesText.getValue();
            controllerCanvas = new ControllerCanvas(this, length, height, nbMines);
            primaryStage.setScene(new Scene(root));
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
    public void makeCanvas() {

        int sHeight = imgPx*height* Images.images[0].length, sLength = imgPx*length* Images.images[0].length;

        // length
        if (imgPx*length* Images.images[0][0].length > 800) {
            sLength = 800;
            imgPx *= (int) (imgPx * (800 / (length* Images.images[0][0].length)));
        }

        // height
        if (imgPx*height* Images.images[0][0].length > 700) {
            sHeight = 700;
            imgPx = (int) (imgPx * (700 / (height* Images.images[0][0].length)));
        }

        System.out.println("height:" + sHeight + " length:" + sLength);

        Canvas canvas = new Canvas(sLength, sHeight);
        graphic = canvas.getGraphicsContext2D();

        int[][] image = Images.images[11];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < length; x++) {

                for (int i = 0; i < image.length; i++) {
                    for (int j = 0; j < image.length; j++) {

                        graphic.setFill(getColor(image[i][j]));

                        graphic.fillRect(imgPx * (j + x*image.length), imgPx * (i + y*image.length),
                                         imgPx, imgPx);

                    }
                }

            }
        }

        root = new Pane(canvas);
        root.setOnMouseReleased(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            int tileX = (int) (mouseX/(imgPx * Images.images[0].length));
            int tileY = (int) (mouseY/(imgPx * Images.images[0].length));

            System.out.println("x:" + tileX + " y:" + tileY);

            controllerCanvas.send(tileX, tileY);
        });
    }

    /**
     * Function that sets a text to a button
     * @param idx the index of the image
     * @param x the x-value of the tile
     * @param y the y-value of the tile
     */
    public void setTileView(int idx, int x, int y) {

        int[][] image = Images.images[idx];
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image.length; j++) {

                graphic.setFill(getColor(image[i][j]));

                graphic.fillRect(imgPx * (j + x*image.length), imgPx * (i + y*image.length),
                        imgPx, imgPx);

            }
        }

    }

    public Color getColor(int color) {
        return Color.rgb(Images.colormap[color][0], Images.colormap[color][1], Images.colormap[color][2]);
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
            makeCanvas();
            controllerCanvas = new ControllerCanvas(this, length, height, nbMines);
            primaryStage.setScene(new Scene(root));
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

        root.setOpacity(0.40);

        StackPane stack = new StackPane(root, again);
        stack.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(stack)); // Set new scene.*/
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
            makeCanvas();
            controllerCanvas = new ControllerCanvas(this, length, height, nbMines);
            primaryStage.setScene(new Scene(root));
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

        root.setOpacity(0.40);

        StackPane stack = new StackPane(root, again);
        stack.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(stack)); // Set new scene.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
