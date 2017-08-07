package com.example.eviat.mines.Logic;

/**
 * Created by eviat on 11/24/2016.
 */

public abstract class Mode {

    private int mNumberOfMines;
    private Size mSizeOfBoard;

    public Mode (int numberOfMines, Size sizeOfBoard){

        mNumberOfMines = numberOfMines;
        mSizeOfBoard = sizeOfBoard;

    }

    public int getmNumberOfMines() {
        return mNumberOfMines;
    }

    public Size getmSizeOfBoard() {
        return mSizeOfBoard;
    }

}
