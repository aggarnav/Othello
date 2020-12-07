import java.util.*;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

/**
 * This class is a model for TicTacToe.  
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games.  We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:  
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 * 
 * Run this file to see the main method play a game of TicTacToe,
 * visualized with Strings printed to the console.
 */
public class Othello {

    private int[][] board;
    private boolean player1;
    private boolean gameOver;
    private Deque<Set<int[]>> history;

    /**
     * Constructor sets up game state.
     */
    public Othello() {
        reset();
    }

    /**
     * playTurn allows players to play a turn. Returns true 
     * if the move is successful and false if a player tries
     * to play in a location that is taken or after the game
     * has ended. If the turn is successful and the game has 
     * not ended, the player is changed. If the turn is 
     * unsuccessful or the game has ended, the player is not 
     * changed.
     * 
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */
    public boolean playTurn(int c, int r) {
        if (board[r][c] != 0 || gameOver) {
            return false;
        }
        
        if (player1) {
            board[r][c] = 1;
        } else {
            board[r][c] = 2;
        }
        
        Collection<int[]> flipped = flippedPieces(c, r);  
        for (int[] coord : flipped) {
            int x = coord[0];
            int y = coord[1];
            if (getCell(x, y) == 1) {
                board[y][x] = 2;
            } else {
                board[y][x] = 1;
            }
        }
        
        if (flipped.size() == 0) {
            board[r][c] = 0;
        }


        if (checkWinner() == 0) {
            player1 = !player1;
        }
        return true;
    }
    
    private Collection<int[]> flippedPiecesHelper(int c, int r, int x, int y) {
        int currentRow = r + y;
        int currentPiece = getCell(c, r);
        int currentColumn = c + x;
        boolean endOfSeries = false;
        Collection<int[]> intermediaryPieces = new HashSet<int[]>();
        
        while ((!endOfSeries) && currentColumn < 9 && currentColumn >= 0 &&
                currentRow < 9 && currentRow >= 0) {
            int newPiece = getCell(currentColumn, currentRow);
            
            if (newPiece == 0) {
                //if cell is blank then nothing is flipped
                endOfSeries = true;
                System.out.print("empty");
            } else if (newPiece == currentPiece) {
                //if cell is the same then our series ends
                System.out.print("same");
                System.out.print(intermediaryPieces.size());
                return intermediaryPieces;
            } else {
                //if the cell is not the same then add it
                int[] coords = {currentColumn, currentRow};
                intermediaryPieces.add(coords);
                System.out.print("diff");
            }
            currentColumn += x;
            currentRow += y;
        }
        intermediaryPieces.clear();
        return intermediaryPieces;
    }
    
    private Collection<int[]> flippedPieces(int c, int r) {
        Collection<int[]> pieces = new HashSet<int[]>();
        pieces.addAll(flippedPiecesHelper(c, r, 0, 1));
        pieces.addAll(flippedPiecesHelper(c, r, 1, 0));
        pieces.addAll(flippedPiecesHelper(c, r, 1, 1));
        pieces.addAll(flippedPiecesHelper(c, r, 0, -1));
        pieces.addAll(flippedPiecesHelper(c, r, -1, 0));
        pieces.addAll(flippedPiecesHelper(c, r, -1, -1));
        pieces.addAll(flippedPiecesHelper(c, r, -1, 1));
        pieces.addAll(flippedPiecesHelper(c, r, 1, -1));
        return pieces;
    }

    /**
     * checkWinner checks whether the game has reached a win 
     * condition. checkWinner only looks for horizontal wins.
     * 
     * @return 0 if nobody has won yet, 1 if player 1 has won, and
     *            2 if player 2 has won, 3 if the game hits stalemate
     */
    public int checkWinner() {
        // Check horizontal win
        for (int i = 0; i < board.length; i++) {
            if (board[i][0] == board[i][1] &&
                board[i][1] == board[i][2] &&
                board[i][1] != 0
                ) {
                gameOver = true;
                if (player1) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }
        
        return 0;
    }


    
    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[9][9];
        board[3][3] = 2;
        board[4][4] = 2;
        board[3][4] = 1;
        board[4][3] = 1;
        player1 = true;
        gameOver = false;
        history = new LinkedList<Set<int[]>>();
    }
    
    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     * 
     * @return true if it's Player 1's turn,
     * false if it's Player 2's turn.
     */
    public boolean getCurrentPlayer() {
        return player1;
    }
    
    /**
     * getCell is a getter for the contents of the
     * cell specified by the method arguments.
     * 
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents
     *         of the corresponding cell on the 
     *         game board.  0 = empty, 1 = Player 1,
     *         2 = Player 2
     */
    public int getCell(int c, int r) {
        return board[r][c];
    }
    
}
