package com.example.eviat.mines.Logic;

/**
 * Created by eviat on 11/26/2016.
 */

public class ModeHard extends Mode {

    public ModeHard (){

        super(10, new Size(10,10));
    }

    @Override
    public String toString() {
        return "Hard";
    }
}
