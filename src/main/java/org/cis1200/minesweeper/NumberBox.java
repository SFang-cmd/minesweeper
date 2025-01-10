package org.cis1200.minesweeper;

/**
 * The number and non-bomb box class, which deals with understanding the values
 * at each box and storing their values
 */
public class NumberBox extends Box {

    public NumberBox(int x, int y) {
        super(x, y);
        this.val = 0;
    }

    public NumberBox(int x, int y, int val) {
        super(x, y);
        this.val = val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
