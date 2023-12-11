package org.cis1200.minesweeper;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunMinesweeper implements Runnable {
    public void run() {

        final JList loadInput = new JList();
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Play");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Title Screen and settings page
        final JFrame homeScreen = new JFrame("Minesweeper");
        homeScreen.setLocation(410, 300);

        final JPanel title = new JPanel();
        title.setLayout(new BoxLayout(title, BoxLayout.PAGE_AXIS));
        homeScreen.add(title);
        final JLabel gameLogo = new JLabel(new ImageIcon("files/MinesweeperTitle.png"));
        gameLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.add(gameLogo);

        // Save Game Input Frame
        final JFrame saveFrame = new JFrame("Save Game");
        saveFrame.setLocation(410, 300);

        final JPanel save = new JPanel();
        save.setLayout(new FlowLayout());
        saveFrame.add(save);

        // Load Game Input Frame
        final JFrame loadFrame = new JFrame("Load Game");
        loadFrame.setLocation(410, 300);

        final JPanel load = new JPanel();
        load.setLayout(new FlowLayout());
        loadFrame.add(load);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton pause = new JButton("Pause");
        pause.addActionListener(e -> homeScreen.setVisible(true));
        pause.setAlignmentX(Component.CENTER_ALIGNMENT);
        control_panel.add(pause);

        final JButton resumeGame = new JButton("Resume");
        resumeGame.addActionListener(e -> homeScreen.setVisible(false));
        resumeGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.add(resumeGame);

        final JButton newGame = new JButton("New Game");
        newGame.addActionListener(e -> {
            board.reset();
            homeScreen.setVisible(false);
        });
        newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.add(newGame);

        final JButton saveGame = new JButton("Save Game");
        saveGame.addActionListener(e -> saveFrame.setVisible(true));
        saveGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.add(saveGame);

        final JButton loadGame = new JButton("Load Game");
        loadGame.addActionListener(e -> {
            setLoad(loadInput, board.getLoads());
            loadFrame.setVisible(true);
        });
        loadGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.add(loadGame);

        ///////////
        final JLabel saveLabel = new JLabel("Name your save: ");
        save.add(saveLabel);
        final JTextField saveInput = new JTextField("File Name");
        save.add(saveInput);
        final JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                board.saveGame(saveInput.getText());
            } catch (RuntimeException f) {
                System.out.println("Error saving, name not valid");
            }
            saveFrame.setVisible(false);
        });
        save.add(saveButton);

        ///////////

        final JLabel loadLabel = new JLabel("Select a file to load: ");
        load.add(loadLabel);
        loadInput.setListData(board.getLoads());
        load.add(loadInput);
        final JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> {
            try {
                board.loadGame(((String) loadInput.getSelectedValue()).split("\\.")[0]);
                board.repaint();
            } catch (RuntimeException f) {
                System.out.println("Error loading game");
            }
            loadFrame.setVisible(false);
            homeScreen.dispose();
        });
        load.add(loadButton);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        homeScreen.pack();
        homeScreen.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        homeScreen.setVisible(false);

        saveFrame.pack();
        saveFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        saveFrame.setVisible(false);

        loadFrame.pack();
        loadFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        loadFrame.setVisible(false);

        // Start the game
        board.reset();
    }

    private void setLoad(JList list, String[] fileList) {
        list.setListData(fileList);
    }
}