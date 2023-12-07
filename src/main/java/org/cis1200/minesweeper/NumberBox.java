package org.cis1200.minesweeper;

public class NumberBox extends Box {

    public NumberBox(int x, int y) {
        super(x, y);
        this.val = 0;
    }

    public NumberBox(int x, int y, int val) {
        super(x, y);
        this.val = 0;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
