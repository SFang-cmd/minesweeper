package org.cis1200.minesweeper;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

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

    private int[][] board;
    private int numTurns;
    private boolean player1;
    private boolean gameOver;

    // Started new game boolean
    private boolean newGame;

    // Random number generator
    Random rand = new Random();

    // Bomb generation X and Y
    int bGenX;
    int bGenY;
    Box[][] map;

    int bombX = 0;
    int bombY = 0;

    int height;
    int width;
    private final int numBombs;

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
                        map[r + j - 1][c + i - 1] = new NumberBox(c + i - 1, r + j - 1, 0);
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
            reveal(c,r);
            System.out.println("Game Square revealed");
        }

        if(map[r][c].getVal() == 9) {
            System.out.println("Bomb hit");
            bombX = c;
            bombY = r;
            gameOver = true;
        }
        System.out.println("Turn finished");
        return true;
    }

    public void reveal(int c, int r) {
        if (!map[r][c].isRevealed() && !map[r][c].isFlagged()) {
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

            }
        }
    }

    public void revealSurroundings(int c, int r) {
        int startX;
        int endX;
        int startY;
        int endY;
        System.out.println(getNumFlags(c,r));
        System.out.println(getNumBombs(c,r));
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
                    reveal(c + i - 1, r + j - 1);
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
                    totalBombs += elem.getShownVal() == -2 ? 1 : 0;
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
        player1 = true;
        gameOver = false;

        newGame = true;
    }

    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     * 
     * @return true if it's Player 1's turn,
     *         false if it's Player 2's turn.
     */
    public boolean getCurrentPlayer() {
        return player1;
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
