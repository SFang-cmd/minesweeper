package org.cis1200.minesweeper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing the minesweeper implementation (only logic, not including Java Swing
 * implementation)
 */

public class MinesweeperTest {

    @Test
    public void defaultMineGridGenerates() {
        Minesweeper mnswp = new Minesweeper(10, 10, 10);
        assertEquals(90, mnswp.getRemainingTiles());
        assertEquals(10, mnswp.getRemainingFlags());
        assertFalse(mnswp.gameWon());
        assertFalse(mnswp.gameOver());
    }

    @Test
    public void firstPlayGuarantees5x5BomblessGrid() {
        Minesweeper mnswp = new Minesweeper(10, 10, 10);
        assertEquals(90, mnswp.getRemainingTiles());
        assertEquals(10, mnswp.getRemainingFlags());
        assertFalse(mnswp.gameWon());
        assertFalse(mnswp.gameOver());
        mnswp.playTurn(5, 5);
        // At least 5x5 grid must be revealed since there are no bombs.
        assertTrue(mnswp.getRemainingTiles() <= 65);
    }

    @Test
    public void firstPlayEdgeClickReveals3x5BomblessGrid() {
        Minesweeper mnswp = new Minesweeper(10, 10, 10);
        assertEquals(90, mnswp.getRemainingTiles());
        assertEquals(10, mnswp.getRemainingFlags());
        assertFalse(mnswp.gameWon());
        assertFalse(mnswp.gameOver());
        mnswp.playTurn(0, 5);
        // At least 3x5 grid must be revealed since there are no bombs.
        assertTrue(mnswp.getRemainingTiles() <= 75);
    }

    @Test
    public void firstPlayCornerClickReveals3x3BomblessGrid() {
        Minesweeper mnswp = new Minesweeper(10, 10, 10);
        assertEquals(90, mnswp.getRemainingTiles());
        assertEquals(10, mnswp.getRemainingFlags());
        assertFalse(mnswp.gameWon());
        assertFalse(mnswp.gameOver());
        mnswp.playTurn(0, 0);
        // At least 3x3 grid must be revealed since there are no bombs.
        assertTrue(mnswp.getRemainingTiles() <= 81);
    }

    @Test
    public void resetAfterPlayMakesNewGameOnClick() {
        Minesweeper mnswp = new Minesweeper(10, 10, 10);
        assertEquals(90, mnswp.getRemainingTiles());
        assertEquals(10, mnswp.getRemainingFlags());
        assertFalse(mnswp.gameWon());
        assertFalse(mnswp.gameOver());
        mnswp.playTurn(5, 5);
        assertTrue(mnswp.getRemainingTiles() <= 65);
        // At least 3x3 grid must be revealed since there are no bombs.
        mnswp.reset(10, 10, 10);
        assertEquals(90, mnswp.getRemainingTiles());
        assertEquals(10, mnswp.getRemainingFlags());
    }

    @Test
    public void playTurnRevealsTile() {
        Minesweeper mnswp = new Minesweeper(10, 10, 10);
        mnswp.make3x3CentralMineTestGame();
        assertEquals(8, mnswp.getRemainingTiles());
        assertEquals(1, mnswp.getRemainingFlags());
        mnswp.playTurn(0, 0);
        assertEquals(7, mnswp.getRemainingTiles());
        assertEquals(1, mnswp.getRemainingFlags());
    }

    @Test
    public void playTurnMineLosesGame() {
        Minesweeper mnswp = new Minesweeper(10, 10, 10);
        mnswp.make3x3CentralMineTestGame();
        assertEquals(8, mnswp.getRemainingTiles());
        assertEquals(1, mnswp.getRemainingFlags());
        mnswp.playTurn(1, 1);
        assertTrue(mnswp.gameOver());
    }

    @Test
    public void playsWinsState() {
        Minesweeper mnswp = new Minesweeper(10, 10, 10);
        mnswp.make3x3CentralMineTestGame();
        assertEquals(8, mnswp.getRemainingTiles());
        assertEquals(1, mnswp.getRemainingFlags());
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                if (i != 1 || j != 1) {
                    mnswp.playTurn(i, j);
                }
            }
        }
        assertTrue(mnswp.gameWon());
    }

    @Test
    public void generateGameMakesNewGame() {
        Minesweeper mnswp = new Minesweeper(3, 3, 1);
        mnswp.make3x3CentralMineTestGame();
        assertEquals(8, mnswp.getRemainingTiles());
        assertEquals(1, mnswp.getRemainingFlags());
        mnswp.playTurn(0, 0);
        assertEquals(7, mnswp.getRemainingTiles());
        assertEquals(1, mnswp.getRemainingFlags());
        mnswp.reset(10, 10, 10);
        mnswp.generateGame(10, 10, 10);
        assertEquals(90, mnswp.getRemainingTiles());
        assertEquals(10, mnswp.getRemainingFlags());
    }

    @Test
    public void revealWinsGame() {
        Minesweeper mnswp = new Minesweeper(3, 3, 1);
        mnswp.make3x3CornerMineTestGame();
        assertEquals(8, mnswp.getRemainingTiles());
        assertEquals(1, mnswp.getRemainingFlags());
        mnswp.reveal(0, 0);
        assertEquals(0, mnswp.getRemainingTiles());
        assertEquals(1, mnswp.getRemainingFlags());
        mnswp.reveal(1, 1);
        mnswp.reveal(2, 1);
        mnswp.reveal(1, 2);
        assertTrue(mnswp.gameWon());
    }

    @Test
    public void revealAppropriateNumberRevealed() {
        Minesweeper mnswp = new Minesweeper(5, 5, 1);
        mnswp.make5x5CentralLine5MinesTestGame();
        assertEquals(20, mnswp.getRemainingTiles());
        assertEquals(5, mnswp.getRemainingFlags());
        mnswp.reveal(0, 0);
        assertEquals(10, mnswp.getRemainingTiles());
        assertEquals(5, mnswp.getRemainingFlags());
    }

    @Test
    public void revealGameWon() {
        Minesweeper mnswp = new Minesweeper(5, 5, 1);
        mnswp.make5x5CentralLine5MinesTestGame();
        assertEquals(20, mnswp.getRemainingTiles());
        assertEquals(5, mnswp.getRemainingFlags());
        mnswp.reveal(0, 0);
        assertEquals(10, mnswp.getRemainingTiles());
        assertEquals(5, mnswp.getRemainingFlags());
        mnswp.reveal(4, 0);
        assertEquals(0, mnswp.getRemainingTiles());
        assertEquals(5, mnswp.getRemainingFlags());
        assertTrue(mnswp.gameWon());
    }

    @Test
    public void flagTileFlagsTile() {
        Minesweeper mnswp = new Minesweeper(5, 5, 1);
        mnswp.make3x3CentralMineTestGame();
        assertEquals(8, mnswp.getRemainingTiles());
        assertEquals(1, mnswp.getRemainingFlags());
        mnswp.flagTile(1, 1);
        assertEquals(8, mnswp.getRemainingTiles());
        assertEquals(0, mnswp.getRemainingFlags());
        assertEquals(19, mnswp.getCellShown(1, 1));
    }

    @Test
    public void fullGameFunctionality() {
        Minesweeper mnswp = new Minesweeper(5, 5, 1);
        mnswp.make5x5CentralLine5MinesTestGame();
        assertEquals(20, mnswp.getRemainingTiles());
        assertEquals(5, mnswp.getRemainingFlags());
        mnswp.reveal(0, 0);
        assertEquals(10, mnswp.getRemainingTiles());
        assertEquals(5, mnswp.getRemainingFlags());
        for (int i = 0; i < 5; i++) {
            mnswp.flagTile(2, i);
        }
        assertEquals(0, mnswp.getRemainingFlags());
        mnswp.reveal(4, 0);
        assertEquals(0, mnswp.getRemainingTiles());
        assertTrue(mnswp.gameWon());
    }

}
