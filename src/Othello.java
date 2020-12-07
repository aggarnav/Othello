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
    private int numTurns;
    private boolean player1;
    private boolean gameOver;

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

        numTurns++;
        if (checkWinner() == 0) {
            player1 = !player1;
        }
        return true;
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
        
        if (numTurns >= 9) {
            gameOver = true;
            return 3;
        } else {
            return 0;
        }
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + numTurns + ":\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 2) { 
                    System.out.print(" | "); 
                }
            }
            if (i < 2) {
                System.out.println("\n---------"); 
            }
        }
    }
    
    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[3][3];
        numTurns = 0;
        player1 = true;
        gameOver = false;
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
