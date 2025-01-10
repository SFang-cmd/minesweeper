package org.cis1200.minesweeper;

/**
 * CIS 1200 HW09 - Minesweeper
 * By: Sean Fang (CO 2027)
 * Game structure inspired by game sample, TicTacToe:
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 * (c) University of Pennsylvania
 */

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * This class deals with the primary logic behind the Minesweeper game.
 * It contains many helper methods, classes, and data structures that
 * allow the game to run. Because the logic is self-contained in this class,
 * you can play Minesweeper using tihs class.
 * The graphics are separately dealt with within GameBoard
 */
public class Minesweeper {
    // Variables to track the game state
    private boolean gameOver;
    private boolean gameWon;
    private int remainingFlags;
    private int remainingTiles;

    // Started new game boolean
    private boolean newGame;

    // Track the info in each tile
    Box[][] map;

    // Random number generator
    Random rand = new Random();

    // Variables to store bomb generation
    int bGenX;
    int bGenY;

    // If bomb is revealed, store its location
    int bombX = 0;
    int bombY = 0;

    // Store the size of the minesweeper grid
    int height;
    int width;

    // Store the number of bombs that should be made given the minesweeper grid size
    private int numBombs;

    /**
     * Constructor sets up game state.
     */
    public Minesweeper(int height, int width, int numBombs) {
        reset(height, width, numBombs);
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset(int h, int w, int b) {
        height = h;
        width = w;
        numBombs = b;
        remainingFlags = b;
        remainingTiles = (w * h) - b;

        bGenX = rand.nextInt(width);
        bGenY = rand.nextInt(height);

        map = new Box[height][width];
        gameOver = false;
        gameWon = false;

        newGame = true;
    }

    /**
     * Makes a test game that is only 3x3 large. For JUnit Tests.
     */
    public void make3x3CentralMineTestGame() {
        reset(3, 3, 1);
        bGenX = 1;
        bGenY = 1;
        generateGame(3, 3, 1);

        newGame = false;
    }

    /**
     * Makes a test game that is only 5x5 large. For JUnit Tests.
     */
    public void make5x5CentralLine5MinesTestGame() {
        reset(5, 5, 5);
        bGenX = 2;
        bGenY = 2;
        map[0][2] = new BombBox(bGenX, bGenY);
        map[1][2] = new BombBox(bGenX, bGenY);
        map[2][2] = new BombBox(bGenX, bGenY);
        map[3][2] = new BombBox(bGenX, bGenY);
        map[4][2] = new BombBox(bGenX, bGenY);
        generateGame(5, 5, 0);

        newGame = false;
    }

    /**
     * Makes a test game that has the mine in the bottom right corner
     */
    public void make3x3CornerMineTestGame() {
        reset(3, 3, 1);
        bGenX = 2;
        bGenY = 2;
        generateGame(3, 3, 1);

        newGame = false;
    }

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed. If it's a new game, the turn will
     * generate a new map
     *
     * @param c column to play in
     * @param r row to play in
     */
    public void playTurn(int c, int r) {

        if (newGame) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    try {
                        map[r + j - 1][c + i - 1] = new NumberBox(c + i - 1, r + j - 1);
                    } catch (RuntimeException e) {
                        System.out.println("Out of bounds");
                    }
                }
            }
            generateGame(height, width, numBombs);
            reveal(c, r);
            newGame = false;
            System.out.println("Game Generated");
        } else {
            if (!map[r][c].isFlagged()) {
                reveal(c, r);
            }
            System.out.println("Game Square revealed");
        }

        if (remainingTiles == 0) {
            gameWon = true;
        }

        if (map[r][c].getShownVal() == 9) {
            System.out.println("Bomb hit");
            bombX = c;
            bombY = r;
            gameOver = true;
        }
        System.out.println("Turn finished");
    }

    /**
     * Creates a new minesweeper map, where the surrounding 3x3 grid is guaranteed
     * to be empty squares, which means that the surrounding 5x5 grid does not
     * contain
     * a single bomb.
     * 
     * @param height   number of vertical tiles
     * @param width    number of horizontal tiles
     * @param numBombs number of desired bombs
     */
    public void generateGame(int height, int width, int numBombs) {
        for (int r = 0; r < numBombs; r++) {
            while ((map[bGenY][bGenX] != null) ||
                    (touchesOpen(getAroundValues(bGenX, bGenY)))) {
                bGenX = rand.nextInt(width);
                bGenY = rand.nextInt(height);
            }
            map[bGenY][bGenX] = new BombBox(bGenX, bGenY);
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map[j][i] == null) {
                    map[j][i] = new NumberBox(i, j);
                    map[j][i].setVal(getNumBombs(i, j));
                }
            }
        }
    }

    /**
     * reveal is a recursive method that reveals the current box,
     * then if the box is an empty one, will reveal the 3-by-3 box
     * surrounding the box. If the surrounding box is a number,
     * the reveal recursion will stop (thus being a base case).
     * 
     * @param c column in box map
     * @param r row in box map
     */
    public void reveal(int c, int r) {
        if (!map[r][c].isRevealed() && !map[r][c].isFlagged()) {
            remainingTiles--;
            map[r][c].reveal();

            int startX;
            int endX;
            int startY;
            int endY;

            if (map[r][c].getVal() == 0) {
                if (c == 0) {
                    startX = 1;
                    endX = 3;
                } else if (c == map[0].length - 1) {
                    startX = 0;
                    endX = 2;
                } else {
                    startX = 0;
                    endX = 3;
                }
                if (r == 0) {
                    startY = 1;
                    endY = 3;
                } else if (r == map.length - 1) {
                    startY = 0;
                    endY = 2;
                } else {
                    startY = 0;
                    endY = 3;
                }
                for (int i = startX; i < endX; i++) {
                    for (int j = startY; j < endY; j++) {
                        if (!map[r + j - 1][c + i - 1].isRevealed()) {
                            reveal(c + i - 1, r + j - 1);
                        }
                    }
                }
                if (remainingTiles == 0) {
                    gameWon = true;
                }
            }
        }
    }

    /**
     * revealSurroundings calls reveal on the 3-by-3 (or less if on an edge)
     * so that the middle mouse-click functionality can work.
     * 
     * @param c column of tile who has surroundings revealed
     * @param r row of tile who has surroundings revealed
     */
    public void revealSurroundings(int c, int r) {
        int startX;
        int endX;
        int startY;
        int endY;
        if (map[r][c].isRevealed() && getNumFlags(c, r) == getNumBombs(c, r)) {
            if (c == 0) {
                startX = 1;
                endX = 3;
            } else if (c == map[0].length - 1) {
                startX = 0;
                endX = 2;
            } else {
                startX = 0;
                endX = 3;
            }
            if (r == 0) {
                startY = 1;
                endY = 3;
            } else if (r == map.length - 1) {
                startY = 0;
                endY = 2;
            } else {
                startY = 0;
                endY = 3;
            }
            for (int i = startX; i < endX; i++) {
                for (int j = startY; j < endY; j++) {
                    if (!map[r + j - 1][c + i - 1].isFlagged()) {
                        reveal(c + i - 1, r + j - 1);
                        if (map[r + j - 1][c + i - 1].getVal() == 9) {
                            System.out.println("Bomb hit");
                            bombX = c;
                            bombY = r;
                            gameOver = true;
                        }
                    }
                }
            }
            if (remainingTiles == 0) {
                gameWon = true;
            }
        }
    }

    /**
     * Method takes in a box coordinate and returns its surrounding objects
     * 
     * @param c column of tile
     * @param r row of tile
     * @return 3x3 map of the surrounding boxes
     */
    public Box[][] getAroundValues(int c, int r) {
        int startX;
        int endX;
        int startY;
        int endY;

        Box[][] grid = new Box[3][3];

        if (c <= 0) {
            startX = 1;
            endX = 3;
        } else if (c >= map[0].length - 1) {
            startX = 0;
            endX = 2;
        } else {
            startX = 0;
            endX = 3;
        }
        if (r <= 0) {
            startY = 1;
            endY = 3;
        } else if (r >= map.length - 1) {
            startY = 0;
            endY = 2;
        } else {
            startY = 0;
            endY = 3;
        }

        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                grid[i][j] = map[r + j - 1][c + i - 1];
            }
        }
        return grid;
    }

    /**
     * Method uses the box coordinates to return the number of bombs in the
     * surrounding boxes
     * 
     * @param c column of box
     * @param r row of box
     * @return number of bombs surrounding a box
     */
    public int getNumBombs(int c, int r) {
        int totalBombs = 0;
        for (Box[] row : getAroundValues(c, r)) {
            for (Box elem : row) {
                if (elem != null)
                    totalBombs += elem.getVal() == 9 ? 1 : 0;
            }
        }
        return totalBombs;
    }

    /**
     * Similar to getNumBombs, returns the number of flags around a specific box
     * 
     * @param c column of box
     * @param r row of box
     * @return number of flags placed around a box
     */
    public int getNumFlags(int c, int r) {
        int totalBombs = 0;
        for (Box[] row : getAroundValues(c, r)) {
            for (Box elem : row) {
                if (elem != null)
                    totalBombs += elem.getShownVal() >= 10 ? 1 : 0;
            }
        }
        return totalBombs;
    }

    /**
     * Used in game generation; checks to see if a bomb box has any empty
     * boxes next to it (since this cannot happen, as it would need to be
     * a numbered box)
     * Ex. ____________
     * |___|_B_|_1_|
     * The left box cannot exist of the center box is a bomb, since
     * the right box is the minimum number that needs to exist.
     * 
     * @param b the 3x3 grid that surrounds the bomb box.
     * @return whether or not there is an empty square next to the bomb box
     */
    public boolean touchesOpen(Box[][] b) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                try {
                    if (b[j][i].getVal() == 0) {
                        return true;
                    }
                } catch (RuntimeException ignored) {

                }
            }
        }
        return false;
    }

    /**
     * Flags the tile for the game
     * 
     * @param c column of tile
     * @param r row of tile
     */
    public void flagTile(int c, int r) {
        if (!map[r][c].isRevealed()) {
            map[r][c].flag();
            if (map[r][c].isFlagged()) {
                remainingFlags--;
            } else {
                remainingFlags++;
            }
        }
    }

    public int getRemainingFlags() {
        return remainingFlags;
    }

    public int getRemainingTiles() {
        return remainingTiles;
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     *
     * @return true if game is over or false if game is not over
     */
    public boolean gameOver() {
        return gameOver;
    }

    public boolean gameWon() {
        return gameWon;
    }

    /**
     * printGameState prints the current game state
     * for debugging and/or simulating a play of the game
     * from terminal
     */
    public void printGameState() {
        System.out.println("\n\nNew Turn: \n");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map[i][j] != null) {
                    int val = map[i][j].getShownVal();
                    String empty;
                    if (val >= 10) {
                        empty = " ";
                    } else if (val >= 0) {
                        empty = "  ";
                    } else if (val >= -9) {
                        empty = " ";
                    } else if (val == -10) {
                        empty = "";
                    } else {
                        empty = "";
                    }
                    System.out.print(empty + val);
                }
                System.out.print(", ");
            }
            System.out.println();
        }
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return gets the raw value (numbers 0-8 or bomb (9)) of the cell
     */
    public int getCell(int c, int r) {
        return map[r][c].getVal();
    }

    /**
     * getCellShown gets the value of a given cell, accounting for
     * 
     * @param c
     * @param r
     * @return
     */
    public int getCellShown(int c, int r) {
        try {
            return map[r][c].getShownVal();
        } catch (RuntimeException e) {
            return -1;
        }
    }

    private File getSaveDirectory() {
        String userHome = System.getProperty("user.home");
        File saveDir = new File(userHome, "MinesweeperSaves");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        return saveDir;
    }

    /**
     * Saves the current game state in a file. Format is:
     * Line 1: height, width, numBombs, gameOver, newGame, bombX, bombY
     * Line 2-max height is the rest of the game board's states, with
     * 
     * @param fileName name of saved game
     */
    public void saveGameState(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException();
        }

        File saveDir = getSaveDirectory();
        File file = new File(saveDir, fileName + ".csv");

        try {
            if (file.createNewFile()) {
                System.out.println(file.getPath() + " File Created");
            } else {
                System.out.println("File " + file.getPath() + " already exists");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(
                        height + "," + width + "," + numBombs + "," +
                                gameOver + "," + newGame + "," + bombX + "," + bombY + "," +
                                gameWon + "," + remainingFlags + "," + remainingTiles
                );
                writer.newLine();
                for (int j = 0; j < height; j++) {
                    for (int i = 0; i < width; i++) {
                        writer.write(Integer.toString(map[j][i].getShownVal()));
                        if (i != width - 1) {
                            writer.write(",");
                        }
                    }
                    writer.newLine();
                }
                writer.flush();
            }
        } catch (RuntimeException | IOException e) {
            System.out.println("Make file error encountered: " + e.getMessage());
            throw new RuntimeException("Failed to save game", e);
        }
    }

    public void loadGame(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException();
        }
        File saveDir = getSaveDirectory();
        File file = new File(saveDir, fileName + ".csv");

        if (!file.exists()) {
            throw new RuntimeException("Save file not found: " + file.getPath());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String gameConfig = reader.readLine();
            if (gameConfig == null) {
                throw new RuntimeException("Save file is empty");
            }

            String[] gameSets = gameConfig.split(",");
            height = Integer.parseInt(gameSets[0]);
            width = Integer.parseInt(gameSets[1]);
            numBombs = Integer.parseInt(gameSets[2]);
            gameOver = Boolean.parseBoolean(gameSets[3]);
            newGame = Boolean.parseBoolean(gameSets[4]);
            bombX = Integer.parseInt(gameSets[5]);
            bombY = Integer.parseInt(gameSets[6]);
            gameWon = Boolean.parseBoolean(gameSets[7]);
            remainingFlags = Integer.parseInt(gameSets[8]);
            remainingTiles = Integer.parseInt(gameSets[9]);

            map = new Box[height][width];
            for (int j = 0; j < height; j++) {
                String readRow = reader.readLine();
                if (readRow == null) {
                    throw new RuntimeException("Incomplete save file");
                }

                String[] rowList = readRow.split(",");
                for (int i = 0; i < width; i++) {
                    int boxValue = Integer.parseInt(rowList[i]);
                    if ((boxValue + 10) % 10 == 9) {
                        map[j][i] = new BombBox(i, j);
                    } else {
                        map[j][i] = new NumberBox(i, j, (boxValue + 10) % 10);
                    }
                    if (boxValue >= 10) {
                        map[j][i].flag();
                    } else if (boxValue >= 0) {
                        map[j][i].reveal();
                    }
                }
            }
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Load error: " + e.getMessage());
            throw new RuntimeException("Failed to load game", e);
        }
    }

    public static void main(String[] args) {
        Minesweeper t = new Minesweeper(10, 10, 20);

        t.playTurn(1, 1);
        t.printGameState();

        t.playTurn(0, 0);
        t.printGameState();

        t.playTurn(0, 2);
        t.printGameState();

        t.playTurn(2, 0);
        t.printGameState();

        t.playTurn(3, 9);
        t.printGameState();

        t.playTurn(4, 4);
        t.printGameState();

        t.playTurn(8, 2);
        t.printGameState();

        t.playTurn(6, 4);
        t.printGameState();

        t.playTurn(6, 9);
        t.printGameState();
        System.out.println();
        System.out.println();
        System.out.println("Bomb hit? " + t.gameOver());
    }
}
