=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: Sefang
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

  1. 2D Arrays - This concept is appropriate since in the Minesweeper Game, the playing
  grid must have a way to be stored. Because it is a 2D grid of bombs and blocks, it is
  appropriate to use a 2D array to store objects containing information about the value
  and behavior of each block.

  2. JUnit testable component - Because the structure I created had the Minesweeper game
  in a separate file, this meant that the game itself could be played completely without
  its respective Java Swing GUI components. This is because all of the data was self-
  contained within Minesweeper.java, while the GameBoard.java and RunMinesweeper.java
  classes were responsible for setting up the GUI that would allow user interaction without
  code/terminal needs.

  3. Recursion - Because Minesweeper game is played to be fun, some of the pointless revealed
  squares that have no bombs touching should automatically reveal their surroundings, since
  there is no risk of users hitting bombs. Thus, recursion is used to ensure that empty squares
  are able to reveal their surroundings recursively without users needing to manually click on
  these tiles.

  4. File I/O - This version of Minesweeper allows users to save the game state in a csv file,
  which allows them to play them at a later date. All game saves are placed in the "saves" directory,
  which allows the program to access, create, and write to the files within this folder, in order to
  store different games of Minesweeper.

  5. (Small Extra concept) Inheritance and subtyping - Since each tile can either be a number or a bomb,
  this means that the 2D array must store types of both, which is where a Box object can serve as the superclass
  to the bomb or number boxes, allowing both to be stored in the 2D array while having different functionality.

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  Box - Implementation of each tile and adds the getter and setter methods to
  creat flags, reveal, and set certain behaviors
  BombBox - Implementation of the bomb box stored in the 2D array
  NumberBox - Implementation of the number boxes, includes an extra constructor and setter
  method to change the value stored
  Minesweeper - The game of minesweeper, completely self contained and playable
  GameBoard - The UI design and painting/access to the playable board
  TimeClock - JPanel that creates a timer
  TurnClock - JPanel that gets the number of remaining flags and turns and displays on the board.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  Not too much, I felt that previous coding exercises had prepared me fairly well for this assignment

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  I struggled a bit with the separation of UI between GameBoard and RunMinesweeper, but other than that, most of the
  rest of it is encapsulated.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
