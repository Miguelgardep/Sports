package com.example.miguel.sports;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

/**
 * Created by miguel on 02/01/2018.
 */

public class DataCursorAdapter extends SimpleCursorAdapter {

    public DataCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setViewBinder(new DataViewBinder(context));
    }
}
