package com.example.eviat.mines.Logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by eviat on 1/11/2017.
 */

public class DataBase {

    private SQLiteDatabase myDatabase ;
    private Context context ;

    public DataBase(Context c){

        // create the database
        context=c;
        myDatabase = context.openOrCreateDatabase("Scores", MODE_PRIVATE, null);

        // create the table
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Scores(name VARCHAR, time INTEGER, lat VARCHAR, lng VARCHAR, mode VARCHAR)");
        // myDatabase.close();
    }

    public void insert(String name ,int time,double lat, double lng, String mode){
        // insert values into the table
        myDatabase = context.openOrCreateDatabase("Scores", MODE_PRIVATE ,null);

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("time", time);
        values.put("lat", ""+lat);
        values.put("lng", ""+lng);
        values.put("mode", mode);

     //   String val = "'"+name+"'"+","+"'"+time+"'"+","+"'"+lat+"'"+","+"'"+lng+"'"+","+"'"+mode+"'"+")";
      //  myDatabase.execSQL("INSERT INTO Scores(name,time,lat,lng,mode) VALUES ("+val);

        long result = myDatabase.insert("Scores", null, values);

        if (result == -1) {
            Log.d("inserted player", "player inserted = false");
                 }
        else {
            Log.d("inserted player", "player inserted = true");
        }

        //db.close();
    }

    private Cursor getTop10Cursor(String mode){

        Cursor c = myDatabase.rawQuery("SELECT * FROM Scores WHERE mode = "+"'"+mode+"'"+" ORDER BY time",null);
        return c;
    }

//    public int getModeScoreCount(String mode){
//
//        Cursor c = myDatabase.rawQuery("SELECT COUNT(name) FROM Scores WHERE mode = "+"'"+mode+"'"+" LIMIT 10",null);
//        int counter = 0;
//
//        while (c.moveToNext())
//            counter++;
//
//        return counter;
//    }

    public String[] getRow(int row, String mode){

        Cursor c = getTop10Cursor(mode);
        String str[] = new String[4];

        if (!c.moveToPosition(row))
          return null;

        str[0] = getNameFromCursor(c);
        str[1] = getTimeFromCursor(c);
        str[2] = getLatFromCursor(c);
        str[3] = getLngFromCursor(c);

        Log.d("row: "+row+" : "+str[0]+" : "+str[1], " : "+str[2]+" : "+str[3]);

        return str;
    }

    private String getNameFromCursor(Cursor c){
        if(c == null) return null;
        int nameIndex = c.getColumnIndex("name");
        String name = c.getString(nameIndex);
        return name;
    }

    private String getTimeFromCursor(Cursor c){
        if(c == null) return "-1";
        int timeIndex = c.getColumnIndex("time");
        int time = c.getInt(timeIndex);
        return ""+time;
    }

    private String getLatFromCursor(Cursor c){
        if(c == null) return null;
        int locationIndex = c.getColumnIndex("lat");
        String location = c.getString(locationIndex);
        return location;
    }

    private String getLngFromCursor(Cursor c){

        if(c == null) return null;
        int locationIndex = c.getColumnIndex("lng");
        String location = c.getString(locationIndex);
        return location;

    }

}
