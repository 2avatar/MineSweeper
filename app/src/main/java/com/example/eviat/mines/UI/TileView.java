package com.example.eviat.mines.UI;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by eviat on 11/30/2016.
 */

public class TileView extends LinearLayout{

    public TextView mText;

    public TileView(Context context){
        super(context);

        this.setOrientation(VERTICAL);

        mText = new TextView(context);

        this.addView(mText);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
        mText.setLayoutParams(layoutParams);

        mText.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        mText.setGravity(Gravity.CENTER_VERTICAL);
        mText.setTextSize(10);
        mText.setTextColor(Color.BLACK);

        setBackgroundColor(Color.CYAN);

    }
}
