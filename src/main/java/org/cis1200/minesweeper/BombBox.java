package org.cis1200.minesweeper;

/**
 * Bomb Box class for specifically the boxes marked as bombs
 */
public class BombBox extends Box {

    public BombBox(int x, int y) {
        super(x, y);
        this.val = 9;
    }
}
