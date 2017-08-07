package com.example.eviat.mines.Logic;

import android.util.Log;

/**
 * Created by eviat on 11/26/2016.
 */

public class Game {

    public enum gameState{GO,WIN,LOSE;

        @Override
        public String toString() {

            switch (this) {
                case GO:
                default:
                    return "GO";
                case WIN:
                    return "WIN";
                case LOSE:
                    return "LOSE";

            }
        }
    }

    private Board mBoard;
    private Mode mMode;
    private gameState mGameState=gameState.GO;
    public int numberOfTilesRevealed = 0;
    public int numberOfMines;

    public Game(Mode mode){

        mMode = mode;
        numberOfMines = mMode.getmNumberOfMines();
        mBoard = new Board(mode.getmSizeOfBoard(), mode.getmNumberOfMines());

    }

    public Board getmBoard() {
        return mBoard;
    }

    public void toggleTileFlagByPosition (int row, int col){
        mBoard.toggleTileFlagByPosition(row, col);
    }

    public String getmGameState() {
        return mGameState.toString();
    }

    public int getNumberOfMines(){
        return numberOfMines;
    }

    public void revealAll(){

        for (int i=0; i<mMode.getmSizeOfBoard().getmRows(); i++)
            for (int j=0; j<mMode.getmSizeOfBoard().getmCols(); j++)
               mBoard.setTileVisibleByPosition(i, j);
    }


    public boolean getTileFlaggedByPosition(int row, int col){
        return mBoard.getTileFlaggedByPosition(row, col);
    }

    public void decreaseNumberOfTilesRevealed(int countNumberOfTilesExposed){
        numberOfTilesRevealed = numberOfTilesRevealed - countNumberOfTilesExposed;

    }

    public void increaseNumberOfMines(){
        numberOfMines++;
    }

    public void addBombs(){

      if (numberOfMines == mMode.getmSizeOfBoard().getmCols()*mMode.getmSizeOfBoard().getmRows())
        return;

     int countNumberOfTilesExposed = mBoard.setMines(1);

        decreaseNumberOfTilesRevealed(countNumberOfTilesExposed);
        increaseNumberOfMines();

        Log.d("Number of mines:"+numberOfMines, "Number of tiles revealed: "+numberOfTilesRevealed);

    }

    public void revealTile(int row, int col){

        if (mBoard.getTileVisibleByPosition(row, col))
            return;

        if (mBoard.getTileStatusByPosition(row, col).toString().equals(Tile.tileStatus.BOMB.toString())){
            mBoard.setTileVisibleByPosition(row, col);
            mGameState = gameState.LOSE;
            return;
        }

        if (!(mBoard.getTileStatusByPosition(row, col).toString().equals(Tile.tileStatus.ZERO.toString()))){

            mBoard.setTileVisibleByPosition(row, col);
            numberOfTilesRevealed++;

            if (numberOfTilesRevealed ==
                    mMode.getmSizeOfBoard().getmCols()*mMode.getmSizeOfBoard().getmRows() - numberOfMines)
                mGameState = gameState.WIN;

            return;
        }

        if (mBoard.getTileStatusByPosition(row, col).toString().equals(Tile.tileStatus.ZERO.toString())){

            mBoard.setTileVisibleByPosition(row, col);
            numberOfTilesRevealed++;

            for (int i=row-1; i<=row+1; i++){
                for (int j=col-1; j<=col+1; j++){

                    if (((j>=0 && j<mMode.getmSizeOfBoard().getmCols()) && (i>=0 && i<mMode.getmSizeOfBoard().getmRows()))){

                        revealTile(i, j);

                    }
                }
            }
        }
    }
}



