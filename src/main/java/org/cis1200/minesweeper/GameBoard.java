package org.cis1200.minesweeper;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    public static final String TILE_1_FILE = "files/mswp_1.png";
    public static final String TILE_2_FILE = "files/mswp_2.png";
    public static final String TILE_3_FILE = "files/mswp_3.png";
    public static final String TILE_4_FILE = "files/mswp_4.png";
    public static final String TILE_5_FILE = "files/mswp_5.png";
    public static final String TILE_6_FILE = "files/mswp_6.png";
    public static final String TILE_7_FILE = "files/mswp_7.png";
    public static final String TILE_8_FILE = "files/mswp_8.png";
    public static final String TILE_BOMB_FILE = "files/mswp_bomb.png";
    public static final String TILE_RED_BOMB_FILE = "files/mswp_red_bomb.png";
    public static final String TILE_NO_BOMB_FILE = "files/mswp_no_bomb.png";
    public static final String TILE_HIDDEN_FILE = "files/mswp_hidden.png";
    public static final String TILE_EMPTY_FILE = "files/mswp_empty.png";
    public static final String TILE_FLAG_FILE = "files/mswp_flag.png";

    private Minesweeper mnswp; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 620;
    public static final int BOARD_HEIGHT = 744;

    // Size of the map (can be changed later)
    int width = 20;
    // int width = 10;
    int height = 24;
    // int height = 8;

    // Number of bombs to be generated
    int numBombs = 99;
    // int numBombs = 10;

    int boxWidth;
    int boxHeight;

    private static BufferedImage hidden;
    private static BufferedImage empty;
    private static BufferedImage bomb;
    private static BufferedImage redBomb;
    private static BufferedImage noBomb;
    private static BufferedImage flag;
    private static BufferedImage box1;
    private static BufferedImage box2;
    private static BufferedImage box3;
    private static BufferedImage box4;
    private static BufferedImage box5;
    private static BufferedImage box6;
    private static BufferedImage box7;
    private static BufferedImage box8;

    // Timer Stuff
    private TimeClock mGameTimer;
    private int mTimeLeft = 20;
    private final int mDelay = 1000; // Start after 1 second
    private final int mPeriod = 1000; // Ticks every 1 second

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // first location for the bomb to be placed in the grid

        boxWidth = BOARD_WIDTH / width;
        boxHeight = BOARD_HEIGHT / height;

        try {
            if (hidden == null) {
                hidden = ImageIO.read(new File(TILE_HIDDEN_FILE));
            }
            if (empty == null) {
                empty = ImageIO.read(new File(TILE_EMPTY_FILE));
            }
            if (bomb == null) {
                bomb = ImageIO.read(new File(TILE_BOMB_FILE));
            }
            if (flag == null) {
                flag = ImageIO.read(new File(TILE_FLAG_FILE));
            }
            if (box1 == null) {
                box1 = ImageIO.read(new File(TILE_1_FILE));
            }
            if (box2 == null) {
                box2 = ImageIO.read(new File(TILE_2_FILE));
            }
            if (box3 == null) {
                box3 = ImageIO.read(new File(TILE_3_FILE));
            }
            if (box4 == null) {
                box4 = ImageIO.read(new File(TILE_4_FILE));
            }
            if (box5 == null) {
                box5 = ImageIO.read(new File(TILE_5_FILE));
            }
            if (box6 == null) {
                box6 = ImageIO.read(new File(TILE_6_FILE));
            }
            if (box7 == null) {
                box7 = ImageIO.read(new File(TILE_7_FILE));
            }
            if (box8 == null) {
                box8 = ImageIO.read(new File(TILE_8_FILE));
            }
            if (redBomb == null) {
                redBomb = ImageIO.read(new File(TILE_RED_BOMB_FILE));
            }
            if (noBomb == null) {
                noBomb = ImageIO.read(new File(TILE_NO_BOMB_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        mnswp = new Minesweeper(height, width, numBombs); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        final JFrame winFrame = new JFrame("You Won!");
        final JPanel win = new JPanel();
        winFrame.setPreferredSize(new Dimension(310, 75));
        winFrame.setLocation(500, 300);
        final JLabel winLabel = new JLabel("Congrats! You Successfully Cleared the mines!");
        final JButton resetButton = new JButton("Replay");
        resetButton.addActionListener(e -> {
            reset(height, width, numBombs);
            winFrame.setVisible(false);
        });

        winFrame.add(win);
        win.add(winLabel);
        win.add(resetButton);

        winFrame.pack();
        winFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        winFrame.setVisible(false);

        final JFrame loseFrame = new JFrame("You Lost!");
        final JPanel lose = new JPanel();
        loseFrame.setPreferredSize(new Dimension(310, 75));
        loseFrame.setLocation(500, 300);
        final JLabel loseLabel = new JLabel("Womp womp, better luck next time! Play again?");
        final JButton replayButton = new JButton("Replay");
        replayButton.addActionListener(e -> {
            reset(height, width, numBombs);
            loseFrame.setVisible(false);
        });

        loseFrame.add(lose);
        lose.add(loseLabel);
        lose.add(replayButton);

        loseFrame.pack();
        loseFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        loseFrame.setVisible(false);

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            // @Override
            // public void mouseReleased(MouseEvent e) {
            // Point p = e.getPoint();
            // mnswp.hidePreview(p.x/boxWidth, p.y/boxHeight);
            // }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick

                if (!mnswp.gameOver() && !mnswp.gameWon()) {
                    if (e.getButton() == 1) {
                        mnswp.playTurn(p.x / boxWidth, p.y / boxHeight);
                    } else if (e.getButton() == 3) {
                        mnswp.flagTile(p.x / boxWidth, p.y / boxHeight);
                    } else if (e.getButton() == 2) {
                        mnswp.revealSurroundings(p.x / boxWidth, p.y / boxHeight);
                    }
                }

                if (mnswp.gameWon()) {
                    winFrame.setVisible(true);
                } else if (mnswp.gameOver()) {
                    loseFrame.setVisible(true);
                }

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset(int height, int width, int numBombs) {
        this.height = height;
        this.width = width;
        this.numBombs = numBombs;

        boxWidth = BOARD_WIDTH / this.width;
        boxHeight = BOARD_HEIGHT / this.height;

        mnswp.reset(this.height, this.width, this.numBombs);
        status.setText("~~<PLAY MINESWEEPER>~~");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (mnswp.gameOver()) {
            status.setText("You lose!");
        } else {
            status.setText("Game start");
        }
    }

    public void saveGame(String fileName) {
        mnswp.saveGameState(fileName);
    }

    public void loadGame(String fileName) {
        mnswp.loadGame(fileName);
    }

    public int getRemainingFlags() {
        return mnswp.getRemainingFlags();
    }

    public int getRemainingTiles() {
        return mnswp.getRemainingTiles();
    }

    public String[] getLoads() {
        File folder = new File("saves");
        File[] folderContents = folder.listFiles();
        String[] fileNames = new String[folderContents.length];
        if (folderContents == null) {
            return fileNames;
        }
        for (int i = 0; i < folderContents.length; i++) {
            fileNames[i] = folderContents[i].getName().split("\\.")[0];
        }
        return fileNames;
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        for (int j = 1; j <= height - 1; j++) {
            for (int i = 1; i <= width - 1; i++) {
                g.drawLine(i * boxWidth, 0, i * boxWidth, BOARD_HEIGHT);
                g.drawLine(0, j * boxHeight, BOARD_WIDTH, j * boxHeight);
            }
        }

        // Draws X's and O's
        for (int i = 0; i <= width - 1; i++) {
            for (int j = 0; j <= height - 1; j++) {
                int state = mnswp.getCellShown(i, j);
                if (!mnswp.gameOver()) {
                    if (state >= 10) {
                        g.drawImage(flag, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null);
                    } else if (state <= -1) {
                        g.drawImage(hidden, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null);
                    } else if (state == 0) {
                        g.drawImage(empty, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null);
                    } else if (state == 1) {
                        g.drawImage(box1, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null);
                    } else if (state == 2) {
                        g.drawImage(box2, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null);
                    } else if (state == 3) {
                        g.drawImage(box3, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null);
                    } else if (state == 4) {
                        g.drawImage(box4, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null);
                    } else if (state == 5) {
                        g.drawImage(box5, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null);
                    } else if (state == 6) {
                        g.drawImage(box6, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null);
                    } else if (state == 7) {
                        g.drawImage(box7, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null);
                    } else if (state == 8) {
                        g.drawImage(box8, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null);
                    } else if (state == 9) {
                        g.drawImage(
                                redBomb, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                        );
                    }

                } else {
                    try {
                        if (state <= -1) {
                            g.drawImage(
                                    hidden, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                            if (mnswp.getCell(i, j) == 9) {
                                g.drawImage(
                                        bomb, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                                );
                            }
                        } else if (state == 9) {
                            g.drawImage(
                                    redBomb, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                        } else if (mnswp.getCell(i, j) != 9 && state >= 10) {
                            g.drawImage(
                                    noBomb, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                        } else if (state >= 10) {
                            g.drawImage(
                                    flag, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                        } else if (state == 0) {
                            g.drawImage(
                                    empty, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                        } else if (state == 1) {
                            g.drawImage(
                                    box1, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                        } else if (state == 2) {
                            g.drawImage(
                                    box2, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                        } else if (state == 3) {
                            g.drawImage(
                                    box3, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                        } else if (state == 4) {
                            g.drawImage(
                                    box4, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                        } else if (state == 5) {
                            g.drawImage(
                                    box5, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                        } else if (state == 6) {
                            g.drawImage(
                                    box6, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                        } else if (state == 7) {
                            g.drawImage(
                                    box7, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                        } else if (state == 8) {
                            g.drawImage(
                                    box8, boxWidth * i, boxHeight * j, boxWidth, boxHeight, null
                            );
                        }
                    } catch (RuntimeException ignored) {

                    }
                }
                // g.drawString(Integer.toString(state), (boxWidth * i + (boxWidth/2)),
                // (boxHeight * j + (boxHeight/2)));
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
