package org.cis1200.minesweeper;

public abstract class Box {
    
    public int y;
    public int x;
    public int val;
    public boolean shown;
    public int[][] map;
    
    public Box(int x, int y) {
        this.map = map;
        this.x = x;
        this.y = y;
        this.shown = false;
    }

    public Box[][] getAroundValues(Box[][] map) {
        int startX;
        int endX;
        int startY;
        int endY;

        Box[][] grid = new Box[3][3];

        if (x == 0) {
            startX = 1;
            endX = 3;
        } else if (x == map[0].length - 1) {
            startX = 0;
            endX = 2;
        } else {
            startX = 0;
            endX = 3;
        }
        if (y == 0) {
            startY = 1;
            endY = 3;
        } else if (y == map.length - 1) {
            startY = 0;
            endY = 2;
        } else {
            startY = 0;
            endY = 3;
        }

        for(int i = startX; i < endX; i++){
            for (int j = startY; j < endY; j++) {
                grid[i][j] = map[y + j - 1][x + i - 1];
            }
        }
        return grid;
    }

    public int getVal() {
        return val;
    }

    // numbers 0-9, where 9 is a bomb and 0 is an empty square
    public int getShownVal() {
        try {
            if (shown) {
                return val;
            } else {
                return -1;
            }
        } catch (RuntimeException e) {
            return 0;
        }
    }

    public void reveal() {
        shown = true;
    }
    
    public void hide() {
        shown = false;
    }
}
