package com.example.eviat.mines;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.eviat.mines.Logic.Game;
import com.example.eviat.mines.Logic.Mode;
import com.example.eviat.mines.Logic.ModeEasy;
import com.example.eviat.mines.Logic.ModeHard;
import com.example.eviat.mines.Logic.ModeMedium;
import com.example.eviat.mines.Service.GPSService;
import com.example.eviat.mines.Service.MotionService;
import com.example.eviat.mines.UI.TileAdapter;

import static com.example.eviat.mines.MainActivity.db;

public class GameActivity extends AppCompatActivity implements MotionService.MotionListener, GPSService.GPSListener{

    public final static String M_WIN_LOSE_KEY = "Win-Lose";
    private final static int M_INITIATE = 0;

    private int mTime = M_INITIATE;
    private int mLevelMode;
    private Mode mMode;
    private Game mGame;
    private GridView mGridView;
    private Intent mGameToWinLose;
    private TextView mTimerTextView;
    private String userName;
   // private SharedPreferences.Editor mPrefEditor;
  //  private SharedPreferences mPref;
    private double lat;
    private double lng;
    private Thread mTimerThread;
    private TextView mNumOfBombTextView;
    private LinearLayout layout;
    private MediaPlayer mPlayer;

    private MotionService.LocalBinder mMotionBinder;
    private boolean isMotionBound = false;

    private GPSService.LocalBinder mGPSBinder;
    private boolean isGPSBound = false;

    private ServiceConnection mGPSConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("Service Connection", "bound to service");
            mGPSBinder = (GPSService.LocalBinder)service;
            mGPSBinder.registerListener(GameActivity.this);
            Log.d("Service Connection", "registered as listener");
            isGPSBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            isGPSBound = false;
        }
    };

    private ServiceConnection mMotionConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("Service Connection", "bound to service");
            mMotionBinder = (MotionService.LocalBinder)service;
            mMotionBinder.registerListener(GameActivity.this);
            Log.d("Service Connection", "registered as listener");
            isMotionBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            isMotionBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getMemebers();

        setTimerThread();

        setListeners();

        Toast.makeText(getApplicationContext(), getString(R.string.good_luck), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        startMotionService();
        startGPSService();
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopMotionService();
        stopGPSService();

    }


    public void startMotionService(){

        Intent intent = new Intent(this, MotionService.class);
        Log.d("On start", "binding to service...");
        bindService(intent, mMotionConnection, Context.BIND_AUTO_CREATE);

    }

    public void startGPSService(){

        Intent intent = new Intent(this, GPSService.class);
        Log.d("On start", "binding to service...");
        bindService(intent, mGPSConnection, Context.BIND_AUTO_CREATE);

    }

    public void stopMotionService(){

        if (isMotionBound){
            unbindService(mMotionConnection);
            isMotionBound = false;
        }
    }

    public void stopGPSService(){

        if (isGPSBound){
            unbindService(mGPSConnection);
            isGPSBound = false;
        }
    }


    @Override
    public void getLatLng(double lat, double lng) {

       this.lat = lat;
        this.lng = lng;

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//                Toast.makeText(getApplicationContext(), "Lat Lng", Toast.LENGTH_LONG).show();
//
//            }
//        });


    }

    @Override
    public void didMotion() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPlayer = MediaPlayer.create(getApplicationContext(),R.raw.alarm);
                mPlayer.start();
                Toast.makeText(getApplicationContext(), getString(R.string.warning_bombs), Toast.LENGTH_SHORT).show();

            }
        });

        mGame.addBombs();
        ((TileAdapter)mGridView.getAdapter()).notifyDataSetChanged();
        mNumOfBombTextView.setText("X "+mGame.getNumberOfMines());

        layout.setBackgroundResource(R.drawable.motion);
        AnimationDrawable spriteAnimation = (AnimationDrawable) layout.getBackground();

        if (spriteAnimation.isRunning())
            spriteAnimation.stop();
        else {
            spriteAnimation.stop();
            spriteAnimation.start();
        }


    }

    private void setTimerThread(){

        mTimerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (mGame.getmGameState().toString().equals(Game.gameState.GO.toString())){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTimerTextView.setText(getString(R.string.time_for_activity)+Integer.toString(mTime));
                        }
                    });

                    mTime++;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (mGame.getmGameState().toString().equals(Game.gameState.WIN.toString())) {

                    db.insert(userName, mTime, lat, lng, mMode.toString());

//                    mPref = getSharedPreferences(MainActivity.M_SHARED_PREFERENCES_KEY, MODE_PRIVATE);
//
//                    if ((mPref.getInt(mMode.toString(), 0) == 0) || (mPref.getInt(mMode.toString(), 0) > mTime)) {
//
//                        mPrefEditor = getSharedPreferences(MainActivity.M_SHARED_PREFERENCES_KEY, MODE_PRIVATE).edit();
//                        mPrefEditor.putInt(mMode.toString(), mTime);
//                        mPrefEditor.commit();
//
//                    }
                }
            }
        });

        mTimerThread.start();
    }

    private void setListeners(){

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (!mGame.getTileFlaggedByPosition(position/mGame.getmBoard().getmSize().getmRows(), position%mGame.getmBoard().getmSize().getmCols()))
                    mGame.revealTile(position/mGame.getmBoard().getmSize().getmRows(), position%mGame.getmBoard().getmSize().getmCols());

                Log.d("Number of mines:"+mGame.numberOfMines, "Number of tiles revealed: "+mGame.numberOfTilesRevealed);

                if (mGame.getmGameState().toString().equals(Game.gameState.LOSE.toString()) ||
                        mGame.getmGameState().toString().equals(Game.gameState.WIN.toString())){

                    stopMotionService();
                    mGame.revealAll();
                    mGameToWinLose.putExtra(MainActivity.M_NAME_KEY, userName);
                    mGameToWinLose.putExtra(MainActivity.M_LEVEL_KEY, mLevelMode);
                    mGameToWinLose.putExtra(M_WIN_LOSE_KEY, mGame.getmGameState().toString());

                    ((TileAdapter)mGridView.getAdapter()).notifyDataSetChanged();

                    if (mGame.getmGameState().toString().equals(Game.gameState.LOSE.toString())) {
                        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bomb);
                        mPlayer.start();
                    }
                    else{
                        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.win);
                        mPlayer.start();
                    }


                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(4500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(mGameToWinLose);
                                    finish();
                                }
                            });


                        }
                    });
                    t.start();

                }
                ((TileAdapter)mGridView.getAdapter()).notifyDataSetChanged();
            }
        });

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                mGame.toggleTileFlagByPosition(position/mGame.getmBoard().getmSize().getmRows(), position%mGame.getmBoard().getmSize().getmCols());

                ((TileAdapter)mGridView.getAdapter()).notifyDataSetChanged();

                return true;
            }
        });

    }

    private void getMemebers(){

        mLevelMode = getIntent().getExtras().getInt(MainActivity.M_LEVEL_KEY);
        userName = getIntent().getExtras().getString(MainActivity.M_NAME_KEY);

        switch (mLevelMode) {

            default:
            case 0:
                mMode = new ModeEasy();
                break;
            case 1:
                mMode = new ModeMedium();
                break;
            case 2:
                mMode = new ModeHard();
                break;
        }

        mNumOfBombTextView = (TextView)findViewById(R.id.textView);

        mGameToWinLose = new Intent(getApplicationContext(), WinLoseActivity.class);

        mGame = new Game(mMode);

        mNumOfBombTextView.setText("X "+mGame.getNumberOfMines());

        mGridView = (GridView) findViewById(R.id.gridView);

        mGridView.setNumColumns(mMode.getmSizeOfBoard().getmCols());

        mGridView.setAdapter(new TileAdapter(getApplicationContext(), mGame));

        mTimerTextView = (TextView)findViewById(R.id.textView6);

        layout = (LinearLayout)findViewById(R.id.activity_game);



    }
}
