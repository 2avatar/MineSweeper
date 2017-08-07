package com.example.eviat.mines.Logic;

/**
 * Created by eviat on 11/24/2016.
 */

public class Tile {

    public enum tileStatus {ZERO,ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,BOMB;

        @Override
        public String toString() {
            switch (this) {
                default:
                case ZERO:
                    return "";
                case ONE:
                    return "1";
                case TWO:
                    return "2";
                case THREE:
                    return "3";
                case FOUR:
                    return "4";
                case FIVE:
                    return "5";
                case SIX:
                    return "6";
                case SEVEN:
                    return "7";
                case EIGHT:
                    return "8";
                case BOMB:
                    return "BOMB";
            }
        }
    }

    private boolean mIsTileFlagged = false;
    private tileStatus mStatus = tileStatus.ZERO;
    private boolean mTileVisible = false;

    public String getmStatus() {
        return mStatus.toString();
    }

    public void increaseStatus(){

        tileStatus[] temp = tileStatus.values();
        mStatus = temp[mStatus.ordinal()+1];

    }

    public void setmStatus(tileStatus mStatus) {
        this.mStatus = mStatus;
    }

    public boolean isTileVisible() {
        return mTileVisible;
    }

    public void setTileVisible(boolean tileVisible) {
        this.mTileVisible = tileVisible;
    }

    public boolean isTileFlagged() {
        return mIsTileFlagged;
    }

    public void setTileFlagged(boolean tileFlagged) {
        mIsTileFlagged = tileFlagged;
    }
}
