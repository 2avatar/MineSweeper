package com.example.eviat.mines.Logic;

/**
 * Created by eviat on 11/24/2016.
 */

public class Board {

    private Tile mTiles[][];
    private Size mSize;

    public Board (Size size, int numberOfMines){

        mSize = size;
        initiateTiles();
        setMines(numberOfMines);

    }

    private void initiateTiles() {

        mTiles = new Tile[mSize.getmRows()][mSize.getmCols()];

        for (int i=0; i<mSize.getmRows(); i++)
            for (int j=0; j<mSize.getmCols(); j++)
                mTiles[i][j] = new Tile();

    }

    public Size getmSize() {
        return mSize;
    }

    public String getTileStatusByPosition (int row, int col){
        return mTiles[row][col].getmStatus();
    }



    public Tile getTileByPosition(int row, int col){
        return mTiles[row][col];
    }

    public boolean getTileVisibleByPosition (int row, int col){
        return mTiles[row][col].isTileVisible();
    }

    public boolean getTileFlaggedByPosition (int row, int col){
        return mTiles[row][col].isTileFlagged();
    }

    public void toggleTileFlagByPosition(int row, int col){

        if (mTiles[row][col].isTileFlagged())
          mTiles[row][col].setTileFlagged(false);
        else
            mTiles[row][col].setTileFlagged(true);
    }

    public void setTileVisibleByPosition (int row, int col){
        mTiles[row][col].setTileVisible(true);
    }

    public int setMines(int numberOfMines){

        int countNumberOfTilesExposed = 0;

        for (int i=0; i<numberOfMines; i++){

            int randomRowPosition = ((int)(Math.random()*100)) % mSize.getmRows();
            int randomColPosition = ((int)(Math.random()*100)) % mSize.getmCols();

            if (!mTiles[randomRowPosition][randomColPosition].getmStatus().toString().equals(Tile.tileStatus.BOMB.toString())) {
                mTiles[randomRowPosition][randomColPosition].setmStatus(Tile.tileStatus.BOMB);
                countNumberOfTilesExposed =  increaseCircleMineNumber(randomRowPosition, randomColPosition);
            }
            else
                i--;
        }
        return countNumberOfTilesExposed;
    }

    private int increaseCircleMineNumber(int randomRowPosition, int randomColPosition) {

        int countNumberOfTilesExposed=0;

        for (int i=randomRowPosition-1; i<=randomRowPosition+1; i++)
            for (int j=randomColPosition-1; j<=randomColPosition+1; j++){

                if (((j>=0 && j<mTiles[0].length) && (i>=0 && i<mTiles.length))){

                    if (mTiles[i][j].isTileVisible()) {
                        countNumberOfTilesExposed++;
                        mTiles[i][j].setTileVisible(false);
                    }

                    if (!(mTiles[i][j].getmStatus().toString().equals(Tile.tileStatus.BOMB.toString()))){

                   mTiles[i][j].increaseStatus();

                   }
                }
            }
        return countNumberOfTilesExposed;
    }
}
