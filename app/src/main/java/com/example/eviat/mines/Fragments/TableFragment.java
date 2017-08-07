package com.example.eviat.mines.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.eviat.mines.R;
import com.example.eviat.mines.ScoreActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TableFragment extends Fragment {

    private final static int NUM_OF_COLS = 3;
    private static TableLayout tableLayout;
    private static View myView;

    public TableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myView = inflater.inflate(R.layout.fragment_table, container, false);

        tableLayout = (TableLayout)myView.findViewById(R.id.tableLayout);
        tableLayout.setStretchAllColumns(true);

        addFirstRow();

        return myView;
    }

    private static void addFirstRow(){

        String str[] = new String[3];
        str[0] = "Name";
        str[1] = "Score";
        str[2] = "Street";
        addTableRow(0, str);
    }

    public static void addTableRow(int row, String str[]){

        TextView tv[] = new TextView[NUM_OF_COLS];
        TableRow rows = new TableRow(tableLayout.getContext());

        for (int col=0; col<NUM_OF_COLS; col++){
            tv[col] = new TextView(tableLayout.getContext());
            tv[col].setText(str[col]);
            tv[col].setTextSize(15);
            rows.addView(tv[col], col);
        }

        tableLayout.addView(rows, row, tableLayout.getLayoutParams());
    }
    public static void removeMode() {

        tableLayout.removeAllViews();

        addFirstRow();

    }
}
