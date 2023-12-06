package org.cis1200.minesweeper;

public class NumberBox extends Box {

    public NumberBox(Box[][]map, int x, int y) {
        super(x, y);
        this.val = getNumBombs(map);
    }

    public NumberBox(Box[][]map, int x, int y, int val) {
        super(x, y);
        this.val = 0;
    }

    public int getNumBombs(Box[][] map) {
        int totalBombs = 0;
        for(Box[] row : getAroundValues(map)) {
            for(Box elem : row) {
                if (elem != null)
                totalBombs += elem.getVal() == 9 ? 1 : 0;
            }
        }
        return totalBombs;
    }
}
