package org.cis1200.minesweeper;

import javax.swing.*;
import java.awt.*;

/**
 * Class that tracks game stats such as number of tiles left and
 * allows turns to occur
 */
public class TurnClock extends JPanel {

    public static final int INTERVAL = 35;
    public int flagText = 0;
    public int tileText = 0;
    GameBoard board;
    JLabel displayText;

    public TurnClock(JLabel status, GameBoard board) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.board = board;

        flagText = board.getRemainingFlags();
        tileText = board.getRemainingTiles();
        displayText = status;

        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start();
    }

    private void tick() {
        displayTime();
        flagText = board.getRemainingFlags();
        tileText = board.getRemainingTiles();
    }

    public void displayTime() {
        displayText.setText("Tiles Left: " + tileText + "   -::|::-   Flags Left: " + flagText);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(15, 15);
    }
}
