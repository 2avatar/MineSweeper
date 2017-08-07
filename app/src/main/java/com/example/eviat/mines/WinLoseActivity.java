package com.example.eviat.mines;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eviat.mines.Logic.Game;

public class WinLoseActivity extends AppCompatActivity {

    private int mLevelMode;
    private String userName;
    private String mCheckWinLose;
    private ImageView mImageView;
    private ImageButton mCreateNewGame;
    private  Intent mWinLoseToGame;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_lose);

        getMemebers();

        setWinLoseBackground();

        setListeners();

    }

    private void setWinLoseBackground(){

        if (mCheckWinLose.equals(Game.gameState.WIN.toString())) {
            mPlayer = MediaPlayer.create(this, R.raw.usa);
            mImageView.setBackgroundResource(R.drawable.flagsanimation);
         //   mImageView.setMaxWidth(mImageView.getMaxWidth());
            AnimationDrawable frameAnimation = (AnimationDrawable) mImageView.getBackground();
            frameAnimation.start();
            Toast.makeText(getApplicationContext(), "Please stand up..", Toast.LENGTH_LONG).show();
            mPlayer.start();
        }
        else {
            mImageView.setBackgroundResource(R.drawable.lose);
        }

    }

    private void setListeners(){

        mCreateNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPlayer != null)
                mPlayer.stop();
                mWinLoseToGame.putExtra(MainActivity.M_NAME_KEY, userName);
                mWinLoseToGame.putExtra(MainActivity.M_LEVEL_KEY, mLevelMode);
                startActivity(mWinLoseToGame);
                finish();
            }
        });

    }

    private void getMemebers(){


        userName = getIntent().getExtras().getString(MainActivity.M_NAME_KEY);
        mLevelMode = getIntent().getExtras().getInt(MainActivity.M_LEVEL_KEY);
        mCheckWinLose = getIntent().getExtras().getString(GameActivity.M_WIN_LOSE_KEY);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mCreateNewGame = (ImageButton) findViewById(R.id.imageButton2);
        mWinLoseToGame = new Intent(getApplicationContext(), GameActivity.class);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null)
        mPlayer.stop();
    }
}
