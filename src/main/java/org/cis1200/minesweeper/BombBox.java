package org.cis1200.minesweeper;

public class BombBox extends Box {
    
    public BombBox(int x, int y) {
        super(x, y);
        this.val = 9;
    }
}
