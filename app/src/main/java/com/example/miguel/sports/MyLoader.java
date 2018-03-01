package com.example.miguel.sports;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import com.example.miguel.sports.database.DataBDAdapter;

/**
 * Created by miguel on 02/01/2018.
 */

public class MyLoader extends CursorLoader {

    private DataBDAdapter manager;
    private String sportName;
    private String field;

    public MyLoader(Context context, DataBDAdapter manager, String sportName, String field) {
        super(context);
        this.manager = manager;
        this.sportName = sportName;
        this.field = field;
    }

    @Override
    public Cursor loadInBackground() {

        return manager.obtainElement(sportName, field);
    }

}

