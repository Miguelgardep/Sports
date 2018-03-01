package com.example.miguel.sports;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import com.example.miguel.sports.database.DataBDAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by miguel on 02/01/2018.
 */

public class DataViewBinder implements SimpleCursorAdapter.ViewBinder {

    private Context context;

    public DataViewBinder(Context context) {
        super();
        this.context = context;
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        if (columnIndex == cursor.getColumnIndex(DataBDAdapter.FIELD_DATE)) {

            TextView typeControl = (TextView) view;

            SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy",
                    context.getResources().getConfiguration().locale);

            long date = cursor.getLong(cursor.getColumnIndexOrThrow(DataBDAdapter.FIELD_DATE));

            Date dateObj = new Date(date);

            typeControl.setText(curFormater.format(dateObj));

            return true;
        }
        return false;
    }
}
