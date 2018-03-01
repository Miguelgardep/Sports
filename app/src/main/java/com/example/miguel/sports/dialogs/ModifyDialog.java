package com.example.miguel.sports.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.miguel.sports.BaseActivity;
import com.example.miguel.sports.Match;
import com.example.miguel.sports.R;
import com.example.miguel.sports.database.DataBDAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by miguel on 07/01/2018.
 */

public class ModifyDialog extends DialogFragment {

    private Spinner sHour;
    private EditText etNameParticipant1;
    private EditText etNameParticipant2;
    private EditText etPrize;
    private EditText etPlace;
    private DatePicker dpDate;
    private Button btMainAccept;
    private Cursor cursor;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // First we set up the indoor of the window of the XML's dialog
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(infService);
        // We inflate the compound component defined in the XML
        final View inflatedLayout = li.inflate(R.layout.activity_modify, null);

        // We search the components inside the dialog
        sHour = inflatedLayout.findViewById(R.id.sInsHourm);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.hoursTournaments));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sHour.setAdapter(adapter);
        etNameParticipant1 = inflatedLayout.findViewById(R.id.etInsNameParticipant1m);
        etNameParticipant2 = inflatedLayout.findViewById(R.id.etInsNameParticipant2m);
        etPrize = inflatedLayout.findViewById(R.id.etInsPrizem);
        etPlace = inflatedLayout.findViewById(R.id.etInsPlacem);
        dpDate = inflatedLayout.findViewById(R.id.dpInsDatem);
        GregorianCalendar now = new GregorianCalendar();
        dpDate.init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), null);
        btMainAccept = inflatedLayout.findViewById(R.id.btInsAcceptm);

        AlertDialog.Builder window = new AlertDialog.Builder(getActivity());

        etNameParticipant1.setText(cursor.getString(4));
        etNameParticipant2.setText(cursor.getString(5));
        etPrize.setText(String.valueOf(cursor.getInt(6)));
        etPlace.setText(cursor.getString(7));

        // We assign the content inside de dialog(the one we have just inflated)
        window.setView(inflatedLayout);

        btMainAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> mistakes = new ArrayList<String>();

                // HOUR
                if (sHour.getSelectedItem().toString().compareTo("") == 0) {
                    sHour.setBackgroundColor(Color.RED);
                    mistakes.add(getActivity().getString(R.string.tournament_hour));
                } else {
                    sHour.setBackgroundColor(Color.WHITE);
                }

                // NAME PARTICIPANT 1
                if (etNameParticipant1.getText().toString().compareTo("") == 0) {
                    etNameParticipant1.setBackgroundColor(Color.RED);
                    mistakes.add(getActivity().getString(R.string.tournament_nameParticipant1));
                } else {
                    etNameParticipant1.setBackgroundColor(Color.WHITE);
                }

                // NAME PARTICIPANT 2
                if (etNameParticipant2.getText().toString().compareTo("") == 0) {
                    etNameParticipant2.setBackgroundColor(Color.RED);
                    mistakes.add(getActivity().getString(R.string.tournament_nameParticipant2));
                } else {
                    etPrize.setBackgroundColor(Color.WHITE);
                }

                // PRIZE
                if (etPrize.getText().toString().compareTo("") == 0) {
                    etPrize.setBackgroundColor(Color.RED);
                    mistakes.add(getActivity().getString(R.string.prize));
                } else {
                    etPrize.setBackgroundColor(Color.WHITE);
                }

                // PLACE
                if (etPlace.getText().toString().compareTo("") == 0) {
                    etPlace.setBackgroundColor(Color.RED);
                    mistakes.add(getActivity().getString(R.string.tournament_place));
                } else {
                    etPlace.setBackgroundColor(Color.WHITE);
                }

                if (mistakes.size() != 0) {
                    int x = 0;
                    StringBuilder error = new StringBuilder(getActivity().getString(R.string.have_to_indicate) + mistakes.get(x));
                    if (mistakes.size() > 1) {
                        for (x = 1; x < mistakes.size() - 2; x++) {
                            error.append("," + mistakes.get(x));
                        }
                        error.append(" y " + mistakes.get(x));
                    }
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

                } else {
                    GregorianCalendar c = new GregorianCalendar(dpDate.getYear(),
                            dpDate.getMonth(), dpDate.getDayOfMonth());

                    if (correctDate(c)) {
                        int id = Integer.parseInt(cursor.getString(0));
                        String sportName = cursor.getString(1);
                        int hour = Integer.parseInt(sHour.getSelectedItem().toString());
                        long date = c.getTimeInMillis();
                        String nameParticipant1 = etNameParticipant1.getText().toString();
                        String nameParticipant2 = etNameParticipant2.getText().toString();
                        int prize = Integer.parseInt(etPrize.getText().toString());
                        String place = etPlace.getText().toString();

                        Match match = new Match(id, sportName, hour, date, nameParticipant1,
                                nameParticipant2, prize, place);
                        updateMatch(match);
                        ((BaseActivity) getActivity()).update();
                        dismiss();
                    } else {
                        Toast.makeText(getActivity(),
                                R.string.invalid_date,
                                Toast.LENGTH_SHORT).show();
                    }

                }

            }

        });
        return window.create();
    }

    public boolean correctDate(GregorianCalendar date) {
        boolean correct = true;
        GregorianCalendar now = new GregorianCalendar();

        if (date.before(now)) {
            correct = false;
            Log.d(getActivity().getString(R.string.id_date), getActivity().getString(R.string.invalid_date) + date.get(Calendar.YEAR));
            // date before now
        } else {
            GregorianCalendar newGreg = (GregorianCalendar) date.clone();
            Log.d(getActivity().getString(R.string.id_date), getActivity().getString(R.string.valid_date) + date.get(Calendar.YEAR));
            if (now.after(newGreg)) {
                correct = false;
            }
        }
        return correct;
    }

    private void updateMatch(Match match) {
        // open the database for writing
        DataBDAdapter gestora = new DataBDAdapter(getActivity());
        gestora.openForWriting();
        // we save it
        gestora.updateElement(cursor.getLong(0), match);
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.message_modify), Toast.LENGTH_SHORT).show();
        // we close the database
        gestora.close();
    }

    public void cursorAct(Cursor _cursor) {
        this.cursor = _cursor;
    }
}
