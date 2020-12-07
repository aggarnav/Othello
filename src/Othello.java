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
     * @return whether the turn was successful
     */
    public Integer playTurn(int c, int r) {
        if (board[r][c] != 0 || gameOver) {
            return 0;
        }
        
        if (player1) {
            board[r][c] = 1;
        } else {
            board[r][c] = 2;
        }
        
        Collection<int[]> flipped = flippedPieces(c, r);  
        flipper(flipped);
        int size = flipped.size();
        /*if no piece was flipped then remove the piece placed and check if player has 
        any available options, if not then ask player to play again*/
        if (size == 0) {
            board[r][c] = 0;
            if (isAvailable().size() > 0) {
                return size;
            }
        }
        
        //add move to history for undo 
        int[] coords = {c, r};
        List<int[]> move = new LinkedList<int[]>();
        move.add(coords);
        move.addAll(flipped);
        history.add(move);

        if (checkWinner() == 0) {
            changePlayer();
        }
        return size;
    }
    
    private void changePlayer() {
        player1 = !player1;
    }
    
    private void flipper(Collection<int[]> pieces) {
        for (int[] coords : pieces) {
            int x = coords[0];
            int y = coords[1];
            if (getCell(x, y) == 1) {
                board[y][x] = 2;
            } else {
                board[y][x] = 1;
            }
        }
        
    }
    
    //implements undo
    public void undo() {
        List<int[]> lastMove = history.pollLast();
        if (lastMove != null) {
            //get piece played
            int[] coords = lastMove.remove(0);
            board[coords[1]][coords[0]] = 0;
            
            //flip all other pieces moved by that piece
            flipper(lastMove);
            changePlayer();
            
        }
    }
    
    //check if player has available options to play
    private Collection<int[]> isAvailable() {
        Collection<int[]> availableCells = new HashSet<int[]>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                System.out.print("(" + getCell(x, y) + ", " + flippedPieces(x, y).size() +")");
                if (getCell(x, y) == 0 && flippedPieces(x, y).size() != 0) {
                    int[] coord = {x, y};
                    availableCells.add(coord);
                }
            }
        }
        return availableCells;
    }
    
    //helper that traverses a given direction
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
        
        while ((!endOfSeries) && currentColumn < 9 && currentColumn >= 0 &&
                currentRow < 9 && currentRow >= 0) {
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
    
    //set of pieces to flip
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
    
    //find the one round maximum gain move
    private int[] findMaxDelta() {
        int delta = 0;
        int[] max = new int[2];
        for (int[] coord : isAvailable()) {
            int x = coord[0];
            int y = coord[1];            
            delta += playTurn(x, y);
            if (delta > max[0]) {
                max = coord;
            }
            undo();
        }
        return max;
    }
    
    public void hint() {
        int[] move = new int[2];
        int maxDelta = -1;
        System.out.print(isAvailable().size());
        
        //try all combinations and find maximum gain in flips over two rounds
        for (int[] coord : isAvailable()) {
            int x = coord[0];
            int y = coord[1];            
            int delta = playTurn(x, y);
            int[] secondMove = findMaxDelta();
            delta -= playTurn(secondMove[0], secondMove[1]);
            int[] thirdMove = findMaxDelta();
            delta += playTurn(thirdMove[0], thirdMove[1]);
            if (delta > maxDelta) {
                maxDelta = delta;
                move = coord;
            }
            //undo changes to the board
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
     *            2 if player 2 has won, 3 if the game hits stalemate
     */
    public int checkWinner() {
        int whiteCount = 0;
        int blackCount = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
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
        board = new int[9][9];
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
     *         game board.  0 = empty, 1 = Player 1,
     *         2 = Player 2
     */
    public int getCell(int c, int r) {
        return board[r][c];
    }
    
}
