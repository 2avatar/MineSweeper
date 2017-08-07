package com.example.eviat.mines;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.example.eviat.mines.Fragments.MapFragment;
import com.example.eviat.mines.Fragments.TableFragment;

import static com.example.eviat.mines.Fragments.MapFragment.makeMarker;
import static com.example.eviat.mines.MainActivity.db;

public class ScoreActivity extends AppCompatActivity {

    public final static int BEST_SCORES = 10;
    private Switch mSwitch;
    private Fragment mapFragment;
    private Fragment tableFragment;
    private FragmentManager manager;
    private RadioGroup mRadioGroup;
    private int mLevelMode = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        getMembers();

        setListeners();

        setModeScores();

    }

    private void setModeScores() {

        switch (mLevelMode) {
            default:
            case 0:
                printScore("Easy");
                break;
            case 1:
                printScore("Medium");
                break;
            case 2:
                printScore("Hard");
                break;
        }

    }

    private void setListeners() {

        mSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean mapVisible) {

                if (mapVisible){

                    mapFragment.getView().setVisibility(View.VISIBLE);
                    tableFragment.getView().setVisibility(View.GONE);

                }
                else{

                    mapFragment.getView().setVisibility(View.GONE);
                    tableFragment.getView().setVisibility(View.VISIBLE);

                }

            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                TableFragment.removeMode();
                MapFragment.clearMap();

                if (i == R.id.ScoreEasy)
                    printScore("Easy");
                if (i == R.id.ScoreMedium)
                    printScore("Medium");
                if (i == R.id.ScoreHard)
                    printScore("Hard");

            }
        });
    }

    private void getMembers(){

        mLevelMode = getIntent().getExtras().getInt(MainActivity.M_LEVEL_KEY);
        RadioButton rb;

        switch (mLevelMode) {
            default:
            case 0:
                rb = (RadioButton)findViewById(R.id.ScoreEasy);
                rb.setChecked(true);
                break;
            case 1:
                rb = (RadioButton)findViewById(R.id.ScoreMedium);
                rb.setChecked(true);
                break;
            case 2:
                rb = (RadioButton)findViewById(R.id.ScoreHard);
                rb.setChecked(true);
                break;
        }


        mSwitch = (Switch)findViewById(R.id.switch1);
        manager = getSupportFragmentManager();
        mapFragment = manager.findFragmentById(R.id.fragmentMap);
        mapFragment.getView().setVisibility(View.GONE);
        tableFragment = manager.findFragmentById(R.id.fragmentTable);
        mRadioGroup = (RadioGroup) findViewById(R.id.ScoreGroup);

    }

    private void printScore(String mode){

        String mapStr[] = {""};

        String tableStr[] = new String[3];

        for (int i=0; (i<BEST_SCORES) && (mapStr != null) ; i++) {

            mapStr = db.getRow(i, mode);

            if (mapStr != null) {

                MapFragment.makeMarker(Double.parseDouble(mapStr[2]), Double.parseDouble(mapStr[3]), mapStr[0], mapStr[1]);

                if (i==0)
                    MapFragment.zoomIn(Double.parseDouble(mapStr[2]), Double.parseDouble(mapStr[3]));

                // mapStr[0] => name mapStr[1] = score mapStr[2 , 3] = lat , lng
                tableStr[0] = mapStr[0];
                tableStr[1] = mapStr[1];
                tableStr[2] = MapFragment.getLocationAddress(Double.parseDouble(mapStr[2]), Double.parseDouble(mapStr[3]));

                TableFragment.addTableRow(i + 1, tableStr);
            }
        }
    }
}
