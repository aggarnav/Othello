/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
        repaint();
        updateStatus();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }
    
    /**
     * Undo the last move
     */
    public void undo() {
        int input = JOptionPane.showConfirmDialog(this, "The other player has requested an undo,"
                + " do you accept", "Undo", JOptionPane.YES_NO_OPTION);
        if (input == 0) {
            model.undo();
            repaint();
            updateStatus();
        }

    }
    
    /**
     * Saves the game state
     */
    public void save() {
        String file = JOptionPane.showInputDialog("Enter the file name you want to store to");
        try {
            FileWriter fs = new FileWriter(file);
            BufferedWriter buffer = new BufferedWriter(fs);
            buffer.write(String.valueOf(model.getCurrentPlayer()));
            buffer.newLine();
            
            buffer.write(String.valueOf(model.isGameOver()));
            buffer.newLine();
            
            for (int[] row : model.getBoard()) {
                for (int cell : row) {
                    buffer.write(String.valueOf(cell));
                }
                buffer.write(",");
            }
            buffer.newLine();
            buffer.flush();
            
            Iterator<List<int[]>> iterator = model.getHistory().iterator();
            while (iterator.hasNext()) {
                Iterator<int[]> listIterator = iterator.next().iterator();
                while (listIterator.hasNext()) {
                    int[] coord = listIterator.next();
                    buffer.write(String.valueOf(coord[0]));
                    buffer.write(String.valueOf(coord[1]));
                }
                buffer.write(",");
                buffer.flush();
            }
            
            buffer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Uncaught IO error", "Error",
                    JOptionPane.ERROR_MESSAGE);        
        }
    }    
    
    /**
     * Loads the game state from a file
     */
    public void load() {
        String file = JOptionPane.showInputDialog("Enter the file name you want to read from");
        try {
            FileReader fs = new FileReader(file);
            BufferedReader buffer = new BufferedReader(fs);
            boolean currentPlayer = Boolean.parseBoolean(buffer.readLine());
            boolean gameOver = Boolean.parseBoolean(buffer.readLine());
            
            int[][] board = new int[8][8];
            String boardString = buffer.readLine();
            String[] boardRow = boardString.split(",");
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    board[i][j] = Integer.valueOf(Integer.valueOf(
                            Character.toString(boardRow[i].charAt(j)
                            )));
                }
            }
            
            Deque<List<int[]>> history = new LinkedList<List<int[]>>();
            String historyString = buffer.readLine();
            String[] historyRow = historyString.split(",");
            for (int i = 0; i < historyRow.length; i++) {
                List<int[]> rowList = new LinkedList<int[]>();
                for (int j = 0; j < historyRow[i].length(); j++) {
                    int x = Integer.valueOf(
                            Character.toString(historyRow[i].charAt(j)
                            ));
                    j++;
                    int y = Integer.valueOf(
                            Character.toString(historyRow[i].charAt(j)
                            ));
                    int[] coord = {x, y};
                    rowList.add(coord);
                }
                history.add(rowList);
            }            
            buffer.close();
            model = new Othello(currentPlayer, gameOver, board, history);
            updateStatus();
            repaint();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Uncaught IO error", "Error",
                    JOptionPane.ERROR_MESSAGE);        
        }
    }
    
    /**
     * Uses no. of flips as a success metric. When performed, plays the move
     *  that maximizes the success metric over three moves 
     */
    public void hint() {
        model.hint();
        repaint();
        updateStatus();
    }
    
    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        String state;
        if (model.getCurrentPlayer()) {
            state = "Black plays";
        } else {
            state = "White plays";
        }
        
        int[] result = model.checkWinner();
        int blackCount = result[0];
        int whiteCount = result[1];
        state += "; Black pieces: " + String.valueOf(blackCount);
        state += "; White pieces: " + String.valueOf(whiteCount);
        status.setText(state);
       
        
        if (whiteCount + blackCount == 64) {
            if (blackCount > whiteCount) {
                JOptionPane.showMessageDialog(this, "Black wins!!!", "Game Over", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (whiteCount > blackCount) {
                JOptionPane.showMessageDialog(this, "White wins!!!", "Game Over", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "It's a tie", "Game Over", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
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
        for (int i = 1; i < 9; i++) {
            int index = i * SCALE;
            g.drawLine(index, 0, index, BOARD_HEIGHT);
            g.drawLine(0, index, BOARD_WIDTH, index);
        }
        
        // Draws X's and O's
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int state = model.getCell(j, i);
                if (state != 0) {
                    if (state == 1) {
                        g.setColor(Color.BLACK);
                    } else if (state == 2) {
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