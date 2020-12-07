/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated.  Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework.  This
 * framework is very effective for turn-based games.  We STRONGLY 
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:  
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with 
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Othello model; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 560;
    public static final int BOARD_HEIGHT = 560;
    public static final int SCALE = 70;
    public static final int DIAMETER = 60;
    public static final int SPACE = (SCALE - DIAMETER) / 2;


    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.WHITE));

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);
        
        model = new Othello(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks.  Updates the model, then updates the game board
         * based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                
                // updates the model given the coordinates of the mouseclick
                model.playTurn(p.x / SCALE, p.y / SCALE);
                
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        model.reset();
        status.setText("Black's Turn");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }
    
    /**
     * Undo the last move
     */
    public void undo() {
        //TODO implement undo
    }
    
    public void save() {
        //TODO implement save
    }    
    
    public void load() {
        //TODO implement load
    }
    
    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (model.getCurrentPlayer()) {
            status.setText("Black plays");
        } else {
            status.setText("White plays");
        }
        
        int winner = model.checkWinner();
        if (winner == 1) {
            JOptionPane.showMessageDialog(this, "Black wins!!!", "Game Over", 
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (winner == 2) {
            JOptionPane.showMessageDialog(this, "White wins!!!", "Game Over", 
                    JOptionPane.INFORMATION_MESSAGE);        
        } else if (winner == 3) {
            JOptionPane.showMessageDialog(this, "It's a tie", "Game Over", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board.  This approach
     * will not be sufficient for most games, because it is not 
     * modular.  All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper 
     * methods.  Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        
        // Draws board grid
        for (int i = 1; i < 10; i++) {
            int index = i * SCALE;
            g.drawLine(index, 0, index, BOARD_HEIGHT);
            g.drawLine(0, index, BOARD_WIDTH, index);
        }
        
        // Draws X's and O's
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int state = model.getCell(j, i);
                if (state != 0) {
                    if (state == 1) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(Color.WHITE);                        
                    }
                    g.fillOval(SPACE + SCALE * j, 
                            SPACE + SCALE * i, 
                            DIAMETER, DIAMETER);                    
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}