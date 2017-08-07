package com.example.eviat.mines.UI;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.BaseAdapter;

import com.example.eviat.mines.Logic.Board;
import com.example.eviat.mines.Logic.Game;
import com.example.eviat.mines.Logic.Size;
import com.example.eviat.mines.Logic.Tile;
import com.example.eviat.mines.R;

import java.util.Random;

/**
 * Created by eviat on 11/30/2016.
 */

public class TileAdapter extends BaseAdapter {

    private Board mBoard;
    private Game mGame;
    private Context mContext;
    private Size mSize;
    private boolean flagBeginGame = true;
    private int numOfTiles = 0;
    private String tileStatus;

    public TileAdapter(Context context, Game game) {

        mGame = game;
        mBoard = mGame.getmBoard();
        mContext = context;
        mSize = mBoard.getmSize();

    }

    @Override
    public int getCount() {
        return mSize.getmRows() * mSize.getmCols();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        TileView tileView = (TileView) view;

        tileView = setTileView(position, tileView);

        if (flagBeginGame)
            setBeginGameAnimation(tileView);

        if (mGame.getmGameState().toString().equals(Game.gameState.LOSE.toString()))
            setLoseGameAnimation(tileView);

        if (mGame.getmGameState().toString().equals(Game.gameState.WIN.toString()))
            setWinGameAnimation(tileView);

        return tileView;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mBoard.getTileByPosition(position / mSize.getmRows(), position % mSize.getmCols());
    }

    public void setWinGameAnimation(final TileView tileView) {

        Random r = new Random();
        int direction, speed;
        ValueAnimator valueAnimator = null;

        direction = r.nextInt(1000) % 4;
        speed = r.nextInt(4000 - 2000) + 2000;

        if (direction == 0 || direction == 2)
            valueAnimator = ValueAnimator.ofFloat(0, 1600);
        if (direction == 1 || direction == 3)
            valueAnimator = ValueAnimator.ofFloat(0, -1600);

        if (direction == 0 || direction == 1)
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    float value = (float) animation.getAnimatedValue();

                    tileView.setTranslationY(value);

                    tileView.setRotation(value / 2);
                }
            });

        if (direction == 2 || direction == 3)
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    float value = (float) animation.getAnimatedValue();

                    tileView.setTranslationX(value);

                    tileView.setRotation(value / 2);
                }
            });

        valueAnimator.setInterpolator(new AccelerateInterpolator(1.5f));
        valueAnimator.setDuration(speed/4);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(1);
        valueAnimator.start();

    }

    public void setLoseGameAnimation(final TileView tileView) {

        Random r = new Random();
        int direction, speed;
        ValueAnimator valueAnimator = null;

        direction = r.nextInt(1000) % 4;
        speed = r.nextInt(4000 - 2000) + 2000;

        if (direction == 0 || direction == 2)
            valueAnimator = ValueAnimator.ofFloat(0, 1600);
        if (direction == 1 || direction == 3)
            valueAnimator = ValueAnimator.ofFloat(0, -1600);

        if (direction == 0 || direction == 1)
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    float value = (float) animation.getAnimatedValue();

                    tileView.setTranslationY(value);

                    tileView.setRotation(value / 2);
                }
            });

        if (direction == 2 || direction == 3)
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    float value = (float) animation.getAnimatedValue();

                    tileView.setTranslationX(value);

                    tileView.setRotation(value / 2);
                }
            });

        valueAnimator.setInterpolator(new AccelerateInterpolator(1.5f));
        valueAnimator.setDuration(speed);
        valueAnimator.start();


        if (tileStatus.equals(Tile.tileStatus.BOMB.toString())) {

            tileView.setBackgroundResource(R.drawable.frame);
            AnimationDrawable spriteAnimation = (AnimationDrawable) tileView.getBackground();

                spriteAnimation.start();

            }

    }

    public void setBeginGameAnimation(final TileView tileView) {

        numOfTiles++;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1400, 0);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float) animation.getAnimatedValue();

                tileView.setTranslationY(value);
                tileView.setRotation(value / 2);
            }
        });

        valueAnimator.setInterpolator(new AccelerateInterpolator(1.5f));
        valueAnimator.setDuration(3000);
        valueAnimator.start();

        if (numOfTiles == getCount() + 4)
            flagBeginGame = false;

    }

    public TileView setTileView(int position, TileView tileView) {

        boolean tileVisible;
        boolean tileFlagged;
        boolean tileBomb = false;


        if (tileView == null)
            tileView = new TileView(mContext);

        tileVisible = mBoard.getTileVisibleByPosition(position / mSize.getmRows(), position % mSize.getmCols());
        tileFlagged = mBoard.getTileFlaggedByPosition(position / mSize.getmRows(), position % mSize.getmCols());
        tileStatus = mBoard.getTileStatusByPosition(position / mSize.getmRows(), position % mSize.getmCols()).toString();

        if (tileStatus.equals(Tile.tileStatus.BOMB.toString()))
            tileBomb = true;

        if (tileVisible) {

            if (tileBomb) {
                tileView.setBackgroundColor(Color.GRAY);
                tileView.setBackgroundResource(R.drawable.bomb);
            } else {
                tileView.setBackgroundColor(Color.GRAY);
                tileView.mText.setText(tileStatus);
            }
        } else {

            if (tileFlagged)
                tileView.setBackgroundResource(R.drawable.cannabis);

            else {
                tileView.setBackgroundColor(Color.LTGRAY);
                tileView.mText.setText("");
            }
        }
        return tileView;
    }
}
