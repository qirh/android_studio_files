package tutorials.cs371m.androidtictactoe;

/* TicTacToeConsole.java
 * By Frank McCown (Harding University)
 *
 * This is a tic-tac-toe game that runs in the console window.  The human
 * is X and the computer is O.
 */

import android.util.Log;
import java.util.Random;

public class TicTacToeGame {

    private static final String TAG = "TicTacToeGame";
    public final static int BOARD_SIZE = 9;

    // Characters used to represent the human, computer, and open spots
    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';

    // The computer's difficulty levels
    public enum DifficultyLevel {Easy, Harder, Expert}
    // Current difficulty level
    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;


    private Random mRand;
    private char mBoard[];

    public TicTacToeGame() {
        // Seed the random number generator
        mRand = new Random();
        mBoard = new char[BOARD_SIZE];
        clearBoard();
    }

    /** Clear the board of all X's and O's by setting all spots to OPEN_SPOT. */
    public void clearBoard() {
        for (int i = 0; i < mBoard.length; i++) {
            mBoard[i] = OPEN_SPOT;
        }
    }

    public DifficultyLevel getDifficultyLevel() {
        return mDifficultyLevel;
    }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        mDifficultyLevel = difficultyLevel;
    }

    /** Set the given player at the given location on the game board.
     *  The location must be available, or the board will not be changed.
     *
     * @param player - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */
    public void setMove(char player, int location) {
//        assert player == HUMAN_PLAYER || player == COMPUTER_PLAYER
//                : "illegal player character";
//        assert 0 <= location && location < BOARD_SIZE
//                : "illegal location: " + location + ". Location must be between 0 and 8 inclusive.";
        mBoard[location] = player;
    }

    // Check for a winner.  Return
    //  0 if no winner or tie yet
    //  1 if it's a tie
    //  2 if X won
    //  3 if O won
    public int checkForWinner() {

        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+1] == HUMAN_PLAYER &&
                    mBoard[i+2]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+1]== COMPUTER_PLAYER &&
                    mBoard[i+2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+3] == HUMAN_PLAYER &&
                    mBoard[i+6]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+3] == COMPUTER_PLAYER &&
                    mBoard[i+6]== COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((mBoard[0] == HUMAN_PLAYER &&
                mBoard[4] == HUMAN_PLAYER &&
                mBoard[8] == HUMAN_PLAYER) ||
                (mBoard[2] == HUMAN_PLAYER &&
                        mBoard[4] == HUMAN_PLAYER &&
                        mBoard[6] == HUMAN_PLAYER))
            return 2;
        if ((mBoard[0] == COMPUTER_PLAYER &&
                mBoard[4] == COMPUTER_PLAYER &&
                mBoard[8] == COMPUTER_PLAYER) ||
                (mBoard[2] == COMPUTER_PLAYER &&
                        mBoard[4] == COMPUTER_PLAYER &&
                        mBoard[6] == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
                return 0;
        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }

    private int getRandomMove() {
        Log.d(TAG, "getRandomMove() begin");
        int move;
        do {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);
        Log.d(TAG, "getRandomMove() returns - " + move);
        return move;
    }
    private int getWinningMove(){
        Log.d(TAG, "getWinningMove() begin");
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];
                mBoard[i] = COMPUTER_PLAYER;
                if (checkForWinner() == 3) {
                    Log.d(TAG, "getWinningMove() returns - " + i);
                    mBoard[i] = curr;
                    return i;
                }
                else
                    mBoard[i] = curr;
            }
        }
        Log.d(TAG, "getWinningMove() returns - " + (-1));
        return -1;
    }
    private int getBlockingMove() {
        Log.d(TAG, "getBlockingMove() begin");
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];   // Save the current number
                mBoard[i] = HUMAN_PLAYER;
                if (checkForWinner() == 2) {
                    mBoard[i] = curr;
                    Log.d(TAG, "getBlockingMove() returns - " + i);
                    return i;
                }
                else
                    mBoard[i] = curr;
            }
        }
        Log.d(TAG, "getBlockingMove() returns - " + (-1));
        return -1;
    }


    public int getComputerMove() {
        Log.d(TAG, "getComputerMove()");
        int move = -1;

        if (mDifficultyLevel == DifficultyLevel.Easy) {
            Log.d(TAG, "getComputerMove() - EASY");
            move = getRandomMove();
        }
        else if (mDifficultyLevel == DifficultyLevel.Harder) {
            Log.d(TAG, "getComputerMove() - Harder");
            move = getWinningMove();
            if (move == -1)
                move = getRandomMove();
        }
        else if (mDifficultyLevel == DifficultyLevel.Expert) {
            Log.d(TAG, "getComputerMove() - Expert");
            move = getWinningMove();
            if (move == -1)
                move = getBlockingMove();
            if (move == -1)
                move = getRandomMove();
        }
        mBoard[move] = COMPUTER_PLAYER;
        return move;
    }

}
