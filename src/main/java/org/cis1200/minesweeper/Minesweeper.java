package org.cis1200.minesweeper;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * This class is a model for TicTacToe.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 * 
 * Run this file to see the main method play a game of TicTacToe,
 * visualized with Strings printed to the console.
 */
public class Minesweeper {
    private int numTurns;
    private boolean gameOver;

    // Started new game boolean
    private boolean newGame;
    Box[][] map;

    // Random number generator
    Random rand = new Random();

    // Bomb generation X and Y
    int bGenX;
    int bGenY;

    int bombX = 0;
    int bombY = 0;

    int height;
    int width;
    private int numBombs;

    /**
     * Constructor sets up game state.
     */
    public Minesweeper(int height, int width, int numBombs) {
        this.height = height;
        this.width = width;
        this.numBombs = numBombs;

        reset();
    }

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     *
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */
    public boolean playTurn(int c, int r) {
//        if (map[r][c] == null || gameOver) {
//            return false;
//        }

        if (newGame) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    try{
                        map[r + j - 1][c + i - 1] = new NumberBox(c + i - 1, r + j - 1);
                    } catch (RuntimeException e) {
                        System.out.println("Out of bounds");
                    }
                }
            }
            generateGame(height,width,numBombs);
            reveal(c,r);
            newGame = false;
            System.out.println("Game Generated");
        } else {
            if (!map[r][c].isFlagged()){
                System.out.println(map[r][c].isFlagged());
                reveal(c,r);
            }
            System.out.println("Game Square revealed");
        }

        if(map[r][c].getShownVal() == 9) {
            System.out.println("Bomb hit");
            bombX = c;
            bombY = r;
            gameOver = true;
        }
        System.out.println("Turn finished");
        numTurns++;
        return true;
    }

    /**
     * reveal is a recursive method that reveals the current box,
     * then if the box is an empty one, will reveal the 3-by-3 box
     * surrounding the box. If the surrounding box is a number,
     * the reveal recursion will stop (thus being a base case).
     * @param c column in box map
     * @param r row in box map
     */
    public void reveal(int c, int r) {
        if (!map[r][c].isRevealed() && !map[r][c].isFlagged()) {
            map[r][c].reveal();
//            System.out.println(map[r][c].isFlagged());
//            System.out.println(r);
//            System.out.println(c);
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

            }
        }
    }

    /**
     * revealSurroundings calls reveal on the 3-by-3 (or less if on an edge)
     * so that the middle mouse-click functionality can work.
     * @param c
     * @param r
     */
    public void revealSurroundings(int c, int r) {
        int startX;
        int endX;
        int startY;
        int endY;
        if (map[r][c].isRevealed() && getNumFlags(c,r) == getNumBombs(c,r)) {
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
                        if(map[r + j - 1][c + i - 1].getVal() == 9) {
                            System.out.println("Bomb hit");
                            bombX = c;
                            bombY = r;
                            gameOver = true;
                        }
                    }
                }
            }

        }
    }

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

        for(int i = startX; i < endX; i++){
            for (int j = startY; j < endY; j++) {
                grid[i][j] = map[r + j - 1][c + i - 1];
            }
        }
        return grid;
    }

    public int getNumBombs(int c, int r) {
        int totalBombs = 0;
        for(Box[] row : getAroundValues(c,r)) {
            for(Box elem : row) {
                if (elem != null)
                    totalBombs += elem.getVal() == 9 ? 1 : 0;
            }
        }
        return totalBombs;
    }

    public int getNumFlags(int c, int r) {
        int totalBombs = 0;
        for(Box[] row : getAroundValues(c,r)) {
            for(Box elem : row) {
                if (elem != null)
                    totalBombs += elem.getShownVal() >= 10 ? 1 : 0;
            }
        }
        return totalBombs;
    }

    public int getBombX() {
        return bombX;
    }

    public int getBombY() {
        return bombY;
    }

    public boolean touchesOpen(Box[][] b) {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++) {
                try {
                    if (b[j][i].getVal() == 0) {
                        return true;
                    }
                } catch (RuntimeException e) {
//                    System.out.println("None");
                }
            }
        }
        return false;
    }

    public void generateGame(int height, int width, int numBombs) {
        for (int r = 0; r < numBombs; r++) {
            while ((map[bGenY][bGenX] != null) ||
                    (touchesOpen(getAroundValues(bGenX, bGenY)))) {
                bGenX = rand.nextInt(width);
                bGenY = rand.nextInt(height);
            }
            map[bGenY][bGenX] = new BombBox(bGenX, bGenY);
//            System.out.println(map[bGenY][bGenX].getVal());
        }
        // System.out.println(map[0][0]);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map[j][i] == null){
                    map[j][i] = new NumberBox(i, j);
                    map[j][i].setVal(getNumBombs(i,j));
                }
            }
        }
    }

    public void flagTile(int c, int r) {
        map[r][c].flag();
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * checkWinner only looks for horizontal wins.
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2
     *         has won, 3 if the game hits stalemate
     */
    public boolean gameOver() {
        return gameOver;
    }

    public boolean isFlagged(int c, int r) {
        return map[r][c].isFlagged();
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + numTurns + ":\n");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map[i][j] != null){
                    System.out.print(map[i][j].getVal());
                }
                System.out.print(", ");
            }
            System.out.println();
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        bGenX = rand.nextInt(width);
        bGenY = rand.nextInt(height);

        map = new Box[height][width];
        numTurns = 0;
        gameOver = false;

        newGame = true;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 = Player 1, 2 = Player 2
     */
    public int getCell(int c, int r) {
        return map[r][c].getVal();
    }

    public int getCellShown(int c, int r) {
        try {
            return map[r][c].getShownVal();
        } catch (RuntimeException e) {
            return -1;
        }
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */

    /**
     * Saves the current game state in a file. Format is:
     * Line 1: height, width, numBombs, numTurns, gameOver, newGame, bombX, bombY
     * Line 2-max height is the rest of the game board's states, with
     * @param fileName name of saved game
     */
    public void saveGameState(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException();
        }
        File file;
        String filePath = "saves/" + fileName + ".csv";
        try {
            file = new File(filePath);
            if(file.createNewFile()){
                System.out.println(filePath + " File Created");
            } else {
                System.out.println("File " + filePath + " already exists");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(height + "," + width + "," + numBombs + "," + numTurns + "," +
                    gameOver + "," + newGame + "," + bombX + "," + bombY);
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
            writer.close();

        } catch (RuntimeException | IOException e) {
            System.out.println("Make file error encountered: " + e.getMessage());
        }
    }

    public void loadGame(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException();
        }
        File file;
        String filePath = "saves/" + fileName + ".csv";
        String gameConfig;
        String[] gameSets;
        String readRow;
        String[] rowList;
        int boxValue;
        try {
            file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            if (reader.ready()) {
                gameConfig = reader.readLine();
                gameSets = gameConfig.split(",");
                height = Integer.parseInt(gameSets[0]);
                width = Integer.parseInt(gameSets[1]);
                numBombs = Integer.parseInt(gameSets[2]);
                numTurns = Integer.parseInt(gameSets[3]);
                gameOver = Boolean.parseBoolean(gameSets[4]);
                newGame = Boolean.parseBoolean(gameSets[5]);
                bombX = Integer.parseInt(gameSets[6]);
                bombY = Integer.parseInt(gameSets[7]);
            }
            map = new Box[height][width];
            for (int j = 0; j < height; j++) {
                if (!reader.ready()) {
                    throw new NoSuchElementException();
                }
                readRow = reader.readLine();
                rowList = readRow.split(",");
                for (int i = 0; i < width; i++) {
                    boxValue = Integer.parseInt(rowList[i]);
                    if ((boxValue + 10) % 10 == 9) {
                        map[j][i] = new BombBox(i,j);
                    } else {
                        map[j][i] = new NumberBox(i,j,(boxValue + 10) % 10);
                    }
                    if (boxValue >= 10) {
                        map[j][i].flag();
                    } else if (boxValue >= 0) {
                        map[j][i].reveal();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Invalid File");
        }
    }

    public static void main(String[] args) {
        Minesweeper t = new Minesweeper(10,10,20);

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
