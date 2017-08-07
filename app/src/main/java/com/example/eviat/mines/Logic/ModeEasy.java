package com.example.eviat.mines.Logic;

/**
 * Created by eviat on 11/26/2016.
 */

public class ModeEasy extends Mode {

    public ModeEasy (){
        super(6, new Size(6,6));
    }

    @Override
    public String toString() {
        return "Easy";
    }
}
