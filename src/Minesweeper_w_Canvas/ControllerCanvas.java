package Minesweeper_w_Canvas;

public class ControllerCanvas {

    private MinesweeperCanvasView view;
    private MinesweeperCanvas game;

    public ControllerCanvas(MinesweeperCanvasView view, int length, int height, int nbMines) {
        this.view = view;
        game = new MinesweeperCanvas(length, height, nbMines, this);
    }

    public void send(int x, int y) {

        if (game.state == MinesweeperCanvas.State.PLAYING) {
            if (game.hasMine(x,y)) {
                revealMines(x, y);
                game.state = MinesweeperCanvas.State.DEFEAT;
            }

            else if (game.tiles[y][x] == -1) { // has not been visited
                game.countMinesAround(x, y);
            }

            if (game.safeTiles == 0) {
                revealMines();
                game.state = MinesweeperCanvas.State.VICTORY;
            }
        }

    }

    public void setTile(int idx, int x, int y) {
        view.setTileView(idx, x, y);
    }

    public void setNumber(int nbMines, int x, int y) {
        setTile(nbMines, x, y);
    }

    public void revealMines() {

        for (int i = 0; i < game.height; i++) {
            for (int j = 0; j < game.length; j++) {

                if (game.mines[i][j]) {
                    view.setTileView(9, j, i);
                }

            }
        }

        view.victory();

    }

    public void revealMines(int x, int y) {

        for (int i = 0; i < game.height; i++) {
            for (int j = 0; j < game.length; j++) {

                if (j == x && i == y) {
                    view.setTileView(10, x, y);
                } else if (game.mines[i][j]) {
                    view.setTileView(9, j, i);
                }

            }
        }

        view.defeat();

    }
}
