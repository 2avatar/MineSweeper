package com.example.eviat.mines;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eviat.mines.Logic.DataBase;
import com.example.eviat.mines.Logic.ModeEasy;
import com.example.eviat.mines.Logic.ModeHard;
import com.example.eviat.mines.Logic.ModeMedium;

public class MainActivity extends AppCompatActivity {

    public final static String M_LEVEL_KEY = "Level-Key";
    public final static String M_NAME_KEY = "Name-Key";
    private final static String M_LAST_LEVEL_CHECKED = "Last-Level-Checked";
    public final static String M_SHARED_PREFERENCES_KEY = "Game-Scores";
    private final static int M_INITIAL = 0;

    private int mLevelMode = M_INITIAL;

    private TextView mTextView2, mTextView3, mTextView4;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEditor;
    private RadioGroup mRadioGroup;
    private ImageButton mPlayButton;
    private Button mScoreButton;
    private RadioButton mRadioButton1, mRadioButton2, mRadioButton3;
    private Intent mMainToGame;
    private Intent mMainToScore;
    private EditText userName;
    public static DataBase db;


    @Override
    protected void onResume() {
        super.onResume();

  //     setBestScoreTextView();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.INTERNET}, 3);

        getMemebers();

      //  setBestScoreTextView();

        setRadioChecked();

        setListeners();

    }

    private void setListeners(){

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.radioButton6)
                    mLevelMode = 0;
                if (i == R.id.radioButton7)
                    mLevelMode = 1;
                if (i == R.id.radioButton8)
                    mLevelMode = 2;

                mPrefEditor.putInt(M_LAST_LEVEL_CHECKED , mLevelMode);
                mPrefEditor.commit();
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = userName.getText().toString();

                if (name.length() > 0) {
                    mMainToGame.putExtra(M_NAME_KEY, name);
                    mMainToGame.putExtra(M_LEVEL_KEY, mLevelMode);
                    startActivity(mMainToGame);
                }
                else
                    Toast.makeText(getApplicationContext(), getString(R.string.insert_name_toast), Toast.LENGTH_SHORT).show();

                }
        });

        mScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainToScore.putExtra(M_LEVEL_KEY, mLevelMode);
                startActivity(mMainToScore);
            }
        });

    }

    private void getMemebers(){

        db = new DataBase(this);

        mScoreButton = (Button) findViewById(R.id.scoreButton);

        userName = (EditText) findViewById(R.id.nameText);

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mPlayButton = (ImageButton) findViewById(R.id.imageButton);

        mRadioButton1 = ((RadioButton)findViewById(R.id.radioButton6));
        mRadioButton2 = ((RadioButton)findViewById(R.id.radioButton7));
        mRadioButton3 = ((RadioButton)findViewById(R.id.radioButton8));

        mRadioButton1.setTextColor(Color.BLACK);
        mRadioButton2.setTextColor(Color.BLACK);
        mRadioButton3.setTextColor(Color.BLACK);

        mPrefEditor = getSharedPreferences(M_SHARED_PREFERENCES_KEY, MODE_PRIVATE).edit();
        mPref = getSharedPreferences(M_SHARED_PREFERENCES_KEY, MODE_PRIVATE);

        mLevelMode = mPref.getInt(M_LAST_LEVEL_CHECKED, M_INITIAL);

        mMainToGame = new Intent(getApplicationContext(), GameActivity.class);
        mMainToScore = new Intent(getApplicationContext(), ScoreActivity.class);

    }

    private void setRadioChecked(){

        switch (mLevelMode){

            default:
            case 0:
                mRadioButton1.setChecked(true);
                break;
            case 1:
                mRadioButton2.setChecked(true);
                break;
            case 2:
                mRadioButton3.setChecked(true);
                break;
        }

    }


    private void setBestScoreTextView(){

//        mTextView2 = (TextView) findViewById(R.id.textView2);
//        mTextView3 = (TextView) findViewById(R.id.textView3);
//        mTextView4 = (TextView) findViewById(R.id.textView4);
//
//        mTextView2.setTextColor(Color.BLACK);
//        mTextView3.setTextColor(Color.BLACK);
//        mTextView4.setTextColor(Color.BLACK);
//
//        mTextView2.setText("Best Score: "+mPref.getInt(new ModeEasy().toString(), M_INITIAL));
//        mTextView3.setText("Best Score: "+mPref.getInt(new ModeMedium().toString(), M_INITIAL));
//        mTextView4.setText("Best Score: "+mPref.getInt(new ModeHard().toString(), M_INITIAL));
//


    }
}
