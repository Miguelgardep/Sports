package com.example.miguel.sports;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguel.sports.database.DataBDAdapter;
import com.example.miguel.sports.dialogs.InsertDialog;
import com.example.miguel.sports.dialogs.ModifyDialog;

/**
 * Created by miguel on 10/01/2018.
 */

public class BaseActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_LOADER = 0;
    private Cursor cursor;
    private ListView list;
    private DataBDAdapter gestora;
    private int numSearches;
    private TextView number;
    private DataCursorAdapter data;
    private String sportName;
    private String fieldOrderBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_list);

        // Cursor obtain data
        sportName = this.getClass().getSimpleName().replaceAll("Activity", "").toLowerCase();
        fieldOrderBy = "hour";

        //this.deleteDatabase("dbcompany.db");
        list = (ListView) this.findViewById(R.id.tournamentsList);
        number = (TextView) this.findViewById(R.id.numberOfMatches);

        loadData();

        this.registerForContextMenu(list);
    }

    public void loadData() {
        gestora = new DataBDAdapter(this);
        gestora.openForReading();
        // create the CursorLoader
        this.getLoaderManager().initLoader(ID_LOADER, null, this);
        // we say how the info has to be passed from each match to the views (from) (to)
        String[] from = new String[]{DataBDAdapter.FIELD_DATE, DataBDAdapter.FIELD_HOUR,
                DataBDAdapter.FIELD_NAME_PARTICIPANT1, DataBDAdapter.FIELD_NAME_PARTICIPANT2,
                DataBDAdapter.FIELD_PRIZE, DataBDAdapter.FIELD_PLACE};
        int[] to = new int[]{R.id.LineDate2, R.id.LineHour2, R.id.LineParticipant12,
                R.id.LineParticipant22, R.id.LinePrize2, R.id.LinePlace2};
        // Create the adapter asociated to the cursor
        data = new DataCursorAdapter(this, R.layout.line, null, from, to);
        numSearches = data.getCount();
        list.setAdapter(data);
        update();
        number.setText(getString(R.string.num_matches) +
                Integer.toString(numSearches));
    }

    public void update() {
        this.getLoaderManager().restartLoader(ID_LOADER, null, this);
        number.setText(getString(R.string.num_matches) +
                Integer.toString(numSearches));
    }

    // activity's menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Options of the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1: {
                // insert search
                FragmentManager fragmentManager = getFragmentManager();
                InsertDialog dialogo = new InsertDialog();
                dialogo.show(fragmentManager, getString(R.string.insertion));
                return true;
            }

            case R.id.item2:
                // comparator prize
                fieldOrderBy = "prize";
                this.update();
                return true;

            case R.id.item3:
                // comparator place
                fieldOrderBy = "place";
                this.update();
                return true;

            case R.id.item4: {
                // about
                AlertDialog.Builder ventana = new AlertDialog.Builder(this);
                ventana.setTitle(R.string.author_message);
                ventana.setMessage(R.string.message_co);
                ventana.setIcon(android.R.drawable.ic_menu_info_details);
                ventana.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                ventana.show();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, view, menuInfo);
        final MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.contextual_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // pos is the position of the view when the user does a long click
        int pos = info.position;
        int id = item.getItemId();
        cursor.moveToPosition(pos);
        switch (id) {
            case R.id.item1: {
                gestora.openForWriting();
                if (gestora.deleteElement(cursor.getLong(0))) {
                    Toast.makeText(this, R.string.message_correctDeleted, Toast.LENGTH_SHORT).show();
                    this.update();
                    generateNotification();
                }
                return true;
            }
            case R.id.item2: {
                FragmentManager fragmentManager = getFragmentManager();
                ModifyDialog dialogo = new ModifyDialog();
                dialogo.cursorAct(cursor); // we pass the cursor to obtain the actual data and saw the user them
                dialogo.show(fragmentManager, getString(R.string.modification));
                return true;
            }
        }
        this.update();
        return super.onContextItemSelected(item);
    }

    // Methods that able the cursor to open in second space
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        // Instance and returns a new Loader for a particular ID
        return new MyLoader(this, gestora, sportName, fieldOrderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor _cursor) {
        // It's called when a loader finish loading info
        // Change for the new cursor
        data.swapCursor(_cursor);
        cursor = _cursor;
        numSearches = cursor.getCount();
        number.setText(getString(R.string.num_matches) +
                Integer.toString(numSearches));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        data.swapCursor(null);

    }


    public void generateNotification() {
        int NOTIF_ALERTA_ID = 99;
        String ns = this.NOTIFICATION_SERVICE;
        NotificationManager notManager = (NotificationManager) getSystemService(ns);

        int icon = android.R.drawable.stat_notify_chat;
        CharSequence titleText = getString(R.string.atention);
        CharSequence principalText = getString(R.string.match_deleted);
        long hour = System.currentTimeMillis();

        Notification.Builder feature = new Notification.Builder(this)
                .setSmallIcon(icon)
                .setWhen(hour)
                .setContentTitle(titleText)
                .setContentText(principalText);

        Notification notification = feature.build();

        notManager.notify(NOTIF_ALERTA_ID, notification);
    }
}
