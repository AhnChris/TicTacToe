package com.example.android.tictactoe;

public class TicTacToeUtil {

    // Tic-Tac-Toe tags
    public static final String USED_CELLS_TAG = "USED_CELLS_TAG";
    public static final String GAME_COMPLETED_TAG = "GAME_COMPLETED_TAG";
    public static final String PLAYER_TAG = "PLAYER_TAG";
    public static final String PLAYER_DISP_TEXT_TAG = "PLAYER_DISP_TEXT_TAG";

    private TicTacToeUtil() {

    }

    /**
     * Method to check if a winner can be found. A winner is defined by the entire row or column or
     * diagonal are the same value as the value found from the curRowPos and curColPos of the 2D array
     * usedCells.
     *
     * @param usedCells     2D array consisting of played marked cells.
     * @param curRowPos     Row position in usedCells 2D array.
     * @param curColPos     Col position in the usedCells 2D array.
     * @return
     */
    public static boolean isWinner(char[][] usedCells, int curRowPos, int curColPos) {
        char curPlayerChar = usedCells[curRowPos][curColPos];
        boolean foundWinner = true;

        // Check row win condition
        for (int col = 0; col < usedCells[0].length; col++) {
            char cellChar = usedCells[curRowPos][col];

            // Break if not matching
            if (cellChar != curPlayerChar) {
                foundWinner = false;
                break;
            }
        }

        if (!foundWinner) {

            // Check col win condition
            for (int row = 0; row < usedCells.length; row++) {
                char cellChar = usedCells[row][curColPos];

                // Break if not matching
                if (cellChar != curPlayerChar) {
                    foundWinner = false;
                    break;
                }

                foundWinner = true;
            }
        }

        // Check diagonal win condition
        if (!foundWinner) {

            boolean validDiagCell = false;
            boolean completeDiagMatch = true;

            // Check the Top-Left Bot-Right diagonal
            for (int pos = 0; pos < usedCells.length; pos++) {
                char cellChar = usedCells[pos][pos];

                if (curRowPos == pos && curColPos == pos)
                    validDiagCell = true;

                if (cellChar != curPlayerChar)
                    completeDiagMatch = false;
            }

            foundWinner = validDiagCell && completeDiagMatch;

            // If winner is still not found, check if current cell is within the Top-Right Bot-Left Diagonal
            if (!foundWinner) {
                validDiagCell = false;
                completeDiagMatch = true;

                // Check the Top-Right Bot-Left diagonal
                for (int row = usedCells.length - 1, col = 0; row >= 0; row--, col++) {
                    char cellChar = usedCells[row][col];

                    if (curRowPos == row && curColPos == col)
                        validDiagCell = true;

                    if (cellChar != curPlayerChar)
                        completeDiagMatch = false;
                }

                foundWinner = validDiagCell && completeDiagMatch;
            }
        }

        return foundWinner;
    }
}
