package com.example.miguel.sports.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.miguel.sports.Match;

/**
 * Created by miguel on 22/01/2018.
 */

public class DataBDAdapter {

    // We define fields in the class to represent the fields of the BD's table
    public static final String FIELD_ID = "_id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_HOUR = "hour";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_NAME_PARTICIPANT1 = "nameParticipant1";
    public static final String FIELD_NAME_PARTICIPANT2 = "nameParticipant2";
    public static final String FIELD_PRIZE = "prize";
    public static final String FIELD_PLACE = "place";
    private static final String TABLE_BD = "matches";
    private Context context;
    private SQLiteDatabase database;
    private DataBDHelper dbHelper;

    // Constructor
    public DataBDAdapter(Context context) {
        this.context = context;
    }

    // Method that opens the BD so we can write on it
    public DataBDAdapter openForWriting() throws SQLException {
        // We open the database in write-mode
        dbHelper = new DataBDHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    // Method that opens the BD so we can read it
    public DataBDAdapter openForReading() throws SQLException {
        // We open the database in reading-mode
        dbHelper = new DataBDHelper(context);
        database = dbHelper.getReadableDatabase();
        return this;
    }

    // Method that close the database
    public void close() {
        dbHelper.close();
    }

    // Method to create a object ContentValues with the choosen parameters
    private ContentValues createContentValues(Match match) {
        ContentValues values = new ContentValues();
        values.put(FIELD_NAME, match.getName());
        values.put(FIELD_HOUR, match.getHour());
        values.put(FIELD_DATE, match.getDate());
        values.put(FIELD_NAME_PARTICIPANT1, match.getNameParticipant1());
        values.put(FIELD_NAME_PARTICIPANT2, match.getNameParticipant2());
        values.put(FIELD_PLACE, match.getPlace());
        values.put(FIELD_PRIZE, match.getPrize());
        values.put(FIELD_ID, match.getId());
        return values;
    }

    // Method for creating a new tournament. Returns the id of the new search if it isn't
    // in the database or -1 if it is.
    public long addMatch(Match match) {
        // We use a variable argument to modify the search
        ContentValues initialValues = createContentValues(match);
        // We use the insert function of the SQLiteDatabase
        return database.insert(TABLE_BD, null, initialValues);
    }

    // Method that modifies the info of a tournament where id is the identifier of the search and
    // tournament has the new info of the tournament
    public boolean updateElement(long id, Match match) {
        ContentValues updateValues = createContentValues(match);
        // We use the update funtion of the SQLDatabase
        return database.update(TABLE_BD, updateValues, FIELD_ID + "=" + id, null) > 0;
    }

    // Method that deletes an element
    public boolean deleteElement(long id) {
        // We use the delete function of the SQLiteDatabase
        return database.delete(TABLE_BD, FIELD_ID + "=" + id, null) > 0;
    }

    // Returns a cursor to represent a query of all the searches in the database
    public Cursor obtainElement(String sportName, String field) {
        return database.query(TABLE_BD, new String[]{FIELD_ID, FIELD_NAME, FIELD_HOUR, FIELD_DATE,
                        FIELD_NAME_PARTICIPANT1, FIELD_NAME_PARTICIPANT2, FIELD_PRIZE, FIELD_PLACE},
                "name=?", new String[]{sportName}, null, null, field + " DESC", null);
    }
}

