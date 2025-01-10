package org.cis1200.minesweeper;

/**
 * Abstract class for all boxes, which are implemented within Bomb Box and Number Box.
 * Is hidden until the box is revealed, in which case
 * it just displays the number or a blank box
 */
public abstract class Box {

    public int y;
    public int x;
    public int val;
    public boolean shown;
    public int[][] map;
    public boolean flagged;
    public boolean previewed;

    public Box(int x, int y) {
        this.x = x;
        this.y = y;
        this.shown = false;
        this.flagged = false;
        this.previewed = false;
    }

    public int getVal() {
        return val;
    }

    // numbers 0-9, where 9 is a bomb and 0 is an empty square
    public int getShownVal() {
        try {
            if (shown) {
                return val;
            } else if (flagged) {
                return val + 10;
            } else {
                return val - 10;
            }
        } catch (RuntimeException e) {
            return 0;
        }
    }

    public void reveal() {
        shown = true;
    }

    public boolean isRevealed() {
        return shown;
    }

    public void flag() {
        if (!shown) {
            flagged = flagged ? false : true;
        }
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setVal(int val) {
    }
}
