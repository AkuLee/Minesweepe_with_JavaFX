package Minesweeper_w_gridpane;

import javafx.scene.paint.Color;

public class ControllerGrid {

    private MinesweeperGridView view;
    private MinesweeperGrid game;

    public ControllerGrid(MinesweeperGridView view, int length, int height, int nbMines) {
        this.view = view;
        game = new MinesweeperGrid(length, height, nbMines, this);
    }

    public void send(int x, int y) {
        System.out.println(x + " " + y);

        if (game.state == MinesweeperGrid.State.PLAYING) {
            if (game.hasMine(x,y)) {
                revealMines(x, y);
                game.state = MinesweeperGrid.State.DEFEAT;
            }

            else if (game.tiles[y][x] == -1) { // has not been visited
                game.countMinesAround(x, y);
            }

            if (game.safeTiles == 0) {
                revealMines();
                game.state = MinesweeperGrid.State.VICTORY;
            }
        }

    }

    public void setTile(Color color, String text, int x, int y) {
        view.setGridText(color, text, x, y);
    }

    public void setNumber(int nbMines, int x, int y) {
        switch(nbMines) {
            case 0: setTile(Color.DIMGREY, Integer.toString(nbMines), x, y);break;
            case 1: setTile(Color.BLUE, Integer.toString(nbMines), x, y); break;
            case 2: setTile(Color.GREEN, Integer.toString(nbMines), x, y); break;
            case 3: setTile(Color.RED, Integer.toString(nbMines), x, y); break;
            case 4: setTile(Color.DARKBLUE, Integer.toString(nbMines), x, y); break;
            case 5: setTile(Color.DARKRED, Integer.toString(nbMines), x, y); break;
            case 6: setTile(Color.DARKTURQUOISE, Integer.toString(nbMines), x, y); break;
            case 7: setTile(Color.PURPLE, Integer.toString(nbMines), x, y); break;
            case 8: setTile(Color.DARKSLATEGREY, Integer.toString(nbMines), x, y); break;
        }
    }

    public void revealMines() {

        for (int i = 0; i < game.height; i++) {
            for (int j = 0; j < game.length; j++) {

                if (game.mines[i][j]) {
                    view.setGridText(Color.BLACK, "x", j, i);
                }

            }
        }

        view.victory();

    }

    public void revealMines(int x, int y) {

        for (int i = 0; i < game.height; i++) {
            for (int j = 0; j < game.length; j++) {

                if (j == x && i == y) {
                    view.setGridText(Color.INDIANRED, "x", x, y);
                } else if (game.mines[i][j]) {
                    view.setGridText(Color.BLACK, "x", j, i);
                }

            }
        }

        view.defeat();

    }
}
