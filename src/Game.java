/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.awt.*;
import javax.swing.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework.  This
 * framework is very effective for turn-based games.  We STRONGLY 
 * recommend you review these lecture slides, starting at slide 8, 
 * for more details on Model-View-Controller:  
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard.  The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class Game implements Runnable {
    
    public static final Color DARK_GREEN = new Color(0, 102, 0);
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("TicTacToe");

        // Status panel
        final JPanel status_panel = new JPanel();
        status_panel.setBackground(DARK_GREEN);
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status.setForeground(Color.WHITE);
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        board.setBackground(DARK_GREEN);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        control_panel.setBackground(DARK_GREEN);
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> {
            board.reset();   
        });
        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> {
            board.undo();
        });
        final JButton load = new JButton("Load game");
        load.addActionListener(e -> {
            board.load();
        });
        final JButton save = new JButton("Save Game");
        save.addActionListener(e -> {
            board.save();
        });
        final JButton hint = new JButton("Hint");
        hint.addActionListener(e -> {
            board.hint();
        });
        final JButton info = new JButton("Info");
        info.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Each player has either black or white pieces"
                    + " where playing\n a new piece flips all pieces in all directions from the "
                    + "placed piece that are now between two\n pieces of the player's color. "
                    + "Each turn must flip at least one"
                    + " piece. If \nan invalid cell is chosen where no pieces are flipped then "
                    + "the game won't transition\nand it'll remain your turn. If, however, "
                    + "there is no other options where you can flip a piece "
                    + "\n then you forfeit your turn and the"
                    + " other player plays. The player with the\n maximum pieces at the end wins."
                    + " You can also load/save a game and ask the other\n player to undo their "
                    + "move. You can also use hint to let the\n computer decide your move"
                    + ".\nFor more please see "
                    + "https://www.mastersofgames.com/rules/reversi-othello-rules.htm");
        });
        control_panel.add(reset);
        control_panel.add(undo);
        control_panel.add(save);
        control_panel.add(load);
        control_panel.add(hint);
        control_panel.add(info);
        
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}