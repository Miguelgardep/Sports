package com.example.miguel.sports.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by miguel on 22/01/2018.
 */

public class DataBDHelper extends SQLiteOpenHelper {

    private static final String BD_NAME = "dbcompany.db";
    private static final int BD_VERSION = 1;
    private static final String DB_CREATE = "CREATE TABLE matches " + "(_id long primary key, " +
            "name text not null, hour int, date long, nameParticipant1 text not null, " +
            "nameParticipant2 text not null" + ", prize int" + ", place text not null) ";


    // Constructor
    public DataBDHelper(Context context) {
        super(context, BD_NAME, null, BD_VERSION);
    }


    // Method invoked by Android if the table doesn't exist
    @Override
    public void onCreate(SQLiteDatabase database) {
        // We create the table or tables of the BD
        database.execSQL(DB_CREATE);
    }


    // Method invoked by Android if there's a change of the BD's version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // We delete the table and we create again even the correct way would be using ALTER TABLE
        // We delete the table and we create it again
        database.execSQL("DROP TABLE IF EXISTS matches ");
        onCreate(database);
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // We delete the table and we create again even the correct way would be using ALTER TABLE
        // We delete the table and we create it again
        database.execSQL("DROP TABLE IF EXISTS matches ");
        onCreate(database);
    }


}
