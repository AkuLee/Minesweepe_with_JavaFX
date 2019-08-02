import java.util.Random;

public class Minesweeper {

    private Controller controller;

    protected enum State {
        PLAYING,
        VICTORY,
        DEFEAT
    }

    protected State state;
    protected int safeTiles;
    protected int[][] tiles; // -1 when unvisited. Positive int when visited and no mines.
    protected boolean[][] mines;
    protected int length, height;

    public Minesweeper(int length, int height, int nbMines, Controller controller) {
        this.length = length;
        this.height = height;
        setBoard(nbMines);
        state = State.PLAYING;
        this.controller = controller;
    }

    public void setBoard(int nbMines) {

        safeTiles = height*length - nbMines;
        tiles = new int[height][length];
        mines = new boolean[height][length];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                tiles[i][j] = -1;
                mines[i][j] = false;
            }
        }

        Random rand = new Random();
        for (int i = 0; i < nbMines; i++) {

            int bombX, bombY;

            do {

                bombX = Math.abs(rand.nextInt(length - 1));
                bombY = Math.abs(rand.nextInt(height - 1));

            } while (mines[bombY][bombX]);

            mines[bombY][bombX] = true;

        }
    }

    public boolean hasMine(int x, int y) {
        if (checkInBounds(x, y)) return mines[y][x];
        else return false;
    }

    public void countMinesAround(int x, int y) {

        int mineCounter = 0;

        for (int neighY = y - 1; neighY <= y + 1; neighY++) {
            for (int neighX = x - 1; neighX <= x + 1; neighX++) {

                if (checkInBounds(neighY, neighX) && !(neighX == x && neighY == y)) {
                    if (mines[neighY][neighX]) { // la tuile voisine est minée
                        mineCounter++;
                    }
                }

            }
        }

        tiles[y][x] = mineCounter;
        controller.setNumber(mineCounter, x, y);
        safeTiles--;

        if (mineCounter == 0) {
            countNeighMines(x, y);
        }

    }

    public void countNeighMines(int x, int y) {

        for (int neighY = y - 1; neighY <= y + 1; neighY++) {
            for (int neighX = x - 1; neighX <= x + 1; neighX++) {

                if (checkInBounds(neighX, neighY) && !(neighX == x && neighY == y)
                        && !mines[neighY][neighX] && tiles[neighY][neighX] == -1) {

                    countMinesAround(neighX, neighY);
                }

            }
        }

    }

    public boolean checkInBounds(int x, int y) {
        return (0 <= x && x < length && 0 <= y && y < height);
    }

    public String gridToString() {

        String string = "";

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                string += (mines[i][j] ? "x " : "o ");
            }

            string += "         |           ";

            for (int j = 0; j < height; j++) {
                string += (tiles[i][j] == -1 ? "-1 " : " " + tiles[i][j] + " ");
            }

            string += "\n";
        }

        return string;
    }

    public static void main(String[] args) {

        /*// args = [length, height, nbMines]
        length = Integer.parseInt(args[0]); height = Integer.parseInt(args[1]);
        //Minesweeper game = new Minesweeper(length, height, Integer.parseInt(args[2]));

        System.out.println(game.gridToString()); // todo remove

        State state = State.PLAYING;

        Scanner scanner = new Scanner(System.in);
        String s;

        while (state == State.PLAYING) {

            if (safeTiles == 0) {
                state = State.VICTORY;
                System.out.println("Victory!");
                continue;
            }

            System.out.println("Safe tiles left: " + safeTiles);

            s = scanner.nextLine(); // Inputs should follow "x y"
            String[] commands = s.split(" ");

            int x = Integer.parseInt(commands[0]), y = Integer.parseInt(commands[1]);

            if (game.hasMine(x,y)) {
                state = State.DEFEAT;
                System.out.println("Defeat :(");
                continue;
            }

            if (tiles[y][x] == -1) { // has not been visited
                game.countMinesAround(x, y);
            }

            System.out.println(game.gridToString());

        }
*/
    }
}
