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
    private Deque<List<int[]>> history;

    /**
     * @return the board
     */
    public int[][] getBoard() {
        return board.clone();
    }

    /**
     * @return the gameOver
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @return the history
     */
    public Deque<List<int[]>> getHistory() {
        Deque<List<int[]>> toReturn = new LinkedList<List<int[]>>();
        toReturn.addAll(history);
        return toReturn;
    }

    /**
     * Constructor sets up game state.
     */
    public Othello() {
        reset();
    }
    
    /**
     * Constructor sets up game state given the field variables
     */
    public Othello(boolean player1, boolean gameOver, int[][] board, 
            Deque<List<int[]>> history) {
        this.player1 = player1;
        this.gameOver = gameOver;
        this.board = board;
        this.history = history;
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
     * @return the number of pieces flipped
     */
    public Integer playTurn(int c, int r) {
        if (getCell(c, r) != 0 || gameOver) {
            return 0;
        }
        
        if (player1) {
            board[r][c] = 1;
        } else {
            board[r][c] = 2;
        }
        
        Collection<int[]> flipped = flippedPieces(c, r);  
        int size = flipped.size();
        //create integer array to add to history
        int[] coords = {c, r};

        /*if no piece was flipped then remove the piece placed and check if player has 
        any available options, if not then ask player to play again*/
        if (size == 0) {
            board[r][c] = 0;
            //since we don't play anything, we set x to be 9 to catch it later
            coords[0] = 9;
            //loop is terminated early if another option was available
            if (isAvailable().size() > 0) {
                return size;
            }
        }
        flipper(flipped);

        //add move to history for undo 
        List<int[]> move = new LinkedList<int[]>();
        move.add(coords);
        move.addAll(flipped);
        history.add(move);

        changePlayer();
        return size;
    }
    
    /**
     * Change player
     */
    private void changePlayer() {
        player1 = !player1;
    }
    
    /**
     * Flip a collection of pieces
     */
    private void flipper(Collection<int[]> pieces) {
        for (int[] coords : pieces) {
            int x = coords[0];
            int y = coords[1];
            int cell = getCell(x, y);
            if (cell == 1) {
                board[y][x] = 2;
            } else if (cell == 2) {
                board[y][x] = 1;
            }
        }
        
    }
    
    /**
     * undo the last move
     */
    public void undo() {
        //get the last move from the end of the queue (LIFO)
        List<int[]> lastMove = history.pollLast();
        if (lastMove != null) {
            //get piece played
            int[] coords = lastMove.remove(0);
            //check that the last player had a valid move
            if (coords[0] != 9) {
                board[coords[1]][coords[0]] = 0;
                //flip all other pieces moved by that piece
                flipper(lastMove);
            }
            changePlayer();
        }
    }
    
    /**
     * Returns the collection of available points to play at
     */
    private Collection<int[]> isAvailable() {
        Collection<int[]> availableCells = new HashSet<int[]>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (getCell(x, y) == 0 && flippedPieces(x, y).size() != 0) {
                    int[] coord = {x, y};
                    availableCells.add(coord);
                }
            }
        }
        return availableCells;
    }
    
    /**
     * helper that traverses given directions from a point
     */
    private Collection<int[]> flippedPiecesHelper(int c, int r, int x, int y) {
        int currentRow = r + y;
        int currentPiece;
        int currentColumn = c + x;
        boolean endOfSeries = false;
        
        //set current piece
        if (getCurrentPlayer()) {
            currentPiece = 1;
        } else {
            currentPiece = 2;
        }
        
        Collection<int[]> intermediaryPieces = new HashSet<int[]>();
        
        while ((!endOfSeries) && currentColumn < 8 && currentColumn >= 0 &&
                currentRow < 8 && currentRow >= 0) {
            int newPiece = getCell(currentColumn, currentRow);
            
            if (newPiece == 0) {
                //if cell is blank then nothing is flipped
                endOfSeries = true;
            } else if (newPiece == currentPiece) {
                //if cell is the same then our series ends
                return intermediaryPieces;
            } else {
                //if the cell is not the same then add it
                int[] coords = {currentColumn, currentRow};
                intermediaryPieces.add(coords);
            }
            currentColumn += x;
            currentRow += y;
        }
        intermediaryPieces.clear();
        return intermediaryPieces;
    }
    
    /**
     * Given coordinates of a point returns the collection of pieces to flip
     */
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
     * find the maximum number of pieces that can be flipped in any one move
     */
    private int[] findMaxDelta() {
        int delta = 0;
        int[] max = new int[2];
        Collection<int[]> available = isAvailable();
        //if no spot is available then choose a random spot to play
        if (available.size() == 0) {
            max = anyOpenPlace();
        }
        for (int[] coord : isAvailable()) {
            int x = coord[0];
            int y = coord[1];            
            int flipped = playTurn(x, y);
            //if more than current max value, then update max values and coordinates
            if (flipped > delta) {
                max = coord;
                delta = flipped;
            }
            undo();
        }
        return max;
    }
    
    /**
     * Gets any open place to play. Implemented for MaxDelta. To be used with playturn
     */
    private int[] anyOpenPlace() {
        int[] coord = {9,0};
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (getCell(i, j) == 0) {
                    coord[0] = i;
                    coord[1] = j;
                    return coord;
                }
            }
        }
        //if there are no available spaces then add move to history and change player
        List<int[]> move = new LinkedList<int[]>();
        move.add(coord);
        history.add(move);
        changePlayer();
        return coord;
    }
    
    /**
     * Plays the move with the highest number of net flips over three moves
     */
    public void hint() {
        int[] move = new int[2];
        //set as -65 in case all options net to flipping all pieces for the other player
        int maxDelta = -65;
        
        //try all combinations and find maximum gain in flips over three moves/two rounds
        Collection<int[]> available = isAvailable();
        //if no spot is available then choose a random spot to play
        if (available.size() == 0) {
            move = anyOpenPlace();
        }
        for (int[] coord : isAvailable()) {
            int x = coord[0];
            int y = coord[1];            
            int delta = playTurn(x, y);
            int[] secondMove = findMaxDelta();
            //the next player plays so we subtract this value
            delta -= playTurn(secondMove[0], secondMove[1]);
            //the current player plays so we add this value
            int[] thirdMove = findMaxDelta();
            delta += playTurn(thirdMove[0], thirdMove[1]);
            if (delta > maxDelta) {
                maxDelta = delta;
                move = coord;
            }
            //undo changes to the board three times since we played three times
            for (int i = 0; i < 3; i++) {
                undo();
            }
        }
        //play the piece
        playTurn(move[0], move[1]);
    }

    /**
     * checkWinner checks whether the game has reached a win 
     * condition. checkWinner only looks for horizontal wins.
     * 
     * @return 0 if nobody has won yet, 1 if player 1 has won, and
     *            2 if player 2 has won, 3 if the game is tied
     */
    public int checkWinner() {
        int whiteCount = 0;
        int blackCount = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int cell = getCell(i, j);
                if (cell == 0) {
                    return 0;
                } else if (cell == 1) {
                    blackCount ++;
                } else {
                    whiteCount ++;
                }
            }
        }
        if (blackCount > whiteCount) {
            return 1;
        } else if (whiteCount > blackCount) {
            return 2;
        } else {
            return 3;
        }
    
    }
    
    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[8][8];
        board[3][3] = 2;
        board[4][4] = 2;
        board[3][4] = 1;
        board[4][3] = 1;
        player1 = true;
        gameOver = false;
        history = new LinkedList<List<int[]>>();
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
     *         game board.  0 = empty, 1 = Black,
     *         2 = White; -1 = index outofbounds
     */
    public int getCell(int c, int r) {
        if (c < 8 && -1 < c && r < 8 && -1 < r) {
            return board[r][c];            
        }
        return -1;
    }
    
}
