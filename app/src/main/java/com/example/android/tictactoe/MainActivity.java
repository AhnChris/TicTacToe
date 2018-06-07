package com.example.android.tictactoe;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView[][] mCellImageViews;
    private char[][] mUsedCells;
    private int mCellsUsedCount = 0;

    private TextView mPlayerDisp;

    private boolean mIsXPlayer;
    private boolean mGameCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayerDisp = findViewById(R.id.playerDisplay);
        mCellImageViews = new ImageView[3][3];

        // Check if we have saved states
        if (savedInstanceState != null) {
            mUsedCells = (char[][]) savedInstanceState.getSerializable(TicTacToeUtil.USED_CELLS_TAG);
            mGameCompleted = savedInstanceState.getBoolean(TicTacToeUtil.GAME_COMPLETED_TAG);
            mIsXPlayer = savedInstanceState.getBoolean(TicTacToeUtil.PLAYER_TAG);

            String winnerDispTxt = savedInstanceState.getString(TicTacToeUtil.PLAYER_DISP_TEXT_TAG);

            // Display the winner text if game has already completed
            if (mGameCompleted)
                mPlayerDisp.setText(winnerDispTxt);

        } else {
            mUsedCells = new char[3][3];
        }

        // Initialize views
        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(TicTacToeUtil.USED_CELLS_TAG, mUsedCells);
        outState.putBoolean(TicTacToeUtil.GAME_COMPLETED_TAG, mGameCompleted);
        outState.putBoolean(TicTacToeUtil.PLAYER_TAG, mIsXPlayer);
        outState.putString(TicTacToeUtil.PLAYER_DISP_TEXT_TAG, mPlayerDisp.getText().toString());
        super.onSaveInstanceState(outState);
    }

    /**
     * Method to initialize the views.
     */
    private void init() {

        // Set-up 3x3 ImageView cells
        for (int cellRowPos = 0; cellRowPos < mCellImageViews.length; cellRowPos++) {
            for (int cellColPos = 0; cellColPos < mCellImageViews[0].length; cellColPos++) {

                // Find the specific ImageView from resource ID of the view (id)name
                String cellViewIdName = "cell" + cellRowPos + cellColPos;
                int cellViewResId = getResources().getIdentifier(cellViewIdName, "id", getPackageName());
                mCellImageViews[cellRowPos][cellColPos] = findViewById(cellViewResId);

                // Check for cells that are not yet used
                if (mUsedCells[cellRowPos][cellColPos] == Character.MIN_VALUE && !mGameCompleted) {
                    final int curRowPos = cellRowPos;
                    final int curColPos = cellColPos;

                    // Set a listener for un-used cells
                    mCellImageViews[curRowPos][curColPos].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Check for valid cell move
                            if (mUsedCells[curRowPos][curColPos] == Character.MIN_VALUE) {
                                markCell(curRowPos, curColPos);
                            } else {
                                Toast.makeText(MainActivity.this
                                        , R.string.invalid_cell_txt, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                } else {
                    // Set the corresponding player image for 'X' or 'O' or blank
                    mCellImageViews[cellRowPos][cellColPos].setImageResource(mUsedCells[cellRowPos][cellColPos] == 'X'
                            ? R.drawable.ic_x_white_24dp : mUsedCells[cellRowPos][cellColPos] == 'O' ? R.drawable.ic_circle_white_24dp
                            : R.color.colorPrimary);

                    ++mCellsUsedCount;
                }
            }
        }

        // Set the player display text if game has not completed
        if (!mGameCompleted) {
            if (mIsXPlayer)
                mPlayerDisp.setText(R.string.player_x_txt);
            else
                mPlayerDisp.setText(R.string.player_o_txt);
        }

        // Set a listener for the New Game Fab
        FloatingActionButton newGameFab = findViewById(R.id.newGameFab);
        newGameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                recreate();
                // Recreate current activity without saving state
                finish();
                startActivity(getIntent());
            }
        });
    }

    /**
     * Method is used to populate the current ImageView with the corresponding player image. The
     * player TextView will also be updated to reflect the turn of the next player.
     * <p>
     * Note: Method will also look for a winning combination. If found, the player TextView will be
     * updated to reflect the winner.
     *
     * @param curRowPos The current row position of the ImageView cell.
     * @param curColPos The current col position of the ImageView cell.
     */
    private void markCell(int curRowPos, int curColPos) {
        // Mark current cell and toggle the player display text
        if (mIsXPlayer) {
            mCellImageViews[curRowPos][curColPos].setImageResource(R.drawable.ic_x_white_24dp);
            mUsedCells[curRowPos][curColPos] = 'X';
            mPlayerDisp.setText(R.string.player_o_txt);
        } else {
            mCellImageViews[curRowPos][curColPos].setImageResource(R.drawable.ic_circle_white_24dp);
            mUsedCells[curRowPos][curColPos] = 'O';
            mPlayerDisp.setText(R.string.player_x_txt);
        }

        ++mCellsUsedCount;

        // Look for a winner if appropriate amount of cells are used
        if (mCellsUsedCount >= (mUsedCells.length * 2 - 1)
                && TicTacToeUtil.isWinner(mUsedCells, curRowPos, curColPos)) {
            mGameCompleted = true;

            // Set current player win text
            mPlayerDisp.setText(mIsXPlayer ? getString(R.string.win_txt_player_x)
                    : getString(R.string.win_txt_player_o));
        } else if (mCellsUsedCount == (mUsedCells.length * mUsedCells[0].length)) {
            mGameCompleted = true;

            // Set Draw text
            mPlayerDisp.setText(R.string.win_txt_draw);
        }

        // Disable ImageView listeners if game has completed
        if (mGameCompleted)
            disableAllCellListeners();
        else
            // Switch player turn
            mIsXPlayer = !mIsXPlayer;
    }

    /**
     * Method to disable all ImageView listeners.
     */
    private void disableAllCellListeners() {
        for (int row = 0; row < mCellImageViews.length; row++) {
            for (int col = 0; col < mCellImageViews[0].length; col++) {
                mCellImageViews[row][col].setEnabled(false);
            }
        }
    }
}
