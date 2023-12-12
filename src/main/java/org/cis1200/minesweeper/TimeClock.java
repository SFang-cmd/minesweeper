package org.cis1200.minesweeper;

import java.awt.*;
import javax.swing.*;

public class TimeClock extends JPanel {

    public static final int INTERVAL = 1000;
    public int timeText = 0;
    JLabel displayText;

    public TimeClock(JLabel status) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        displayText = status;

        javax.swing.Timer timer = new javax.swing.Timer(INTERVAL, e -> tick());
        timer.start();
    }

    private void tick() {
        displayTime();
        timeText++;
    }

    public void displayTime() {
        displayText.setText("   -::|::-   Time: " + timeText);
    }

    public void reset() {
        timeText = 0;
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
