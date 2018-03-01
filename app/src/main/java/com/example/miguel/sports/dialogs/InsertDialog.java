package com.example.miguel.sports.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
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

import com.example.miguel.sports.Match;
import com.example.miguel.sports.R;
import com.example.miguel.sports.database.DataBDAdapter;
import com.example.miguel.sports.sportsActivities.BasketballActivity;
import com.example.miguel.sports.sportsActivities.FootballActivity;
import com.example.miguel.sports.sportsActivities.TennisActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by miguel on 03/01/2018.
 */

public class InsertDialog extends DialogFragment {
    private EditText etId;
    private Spinner sHour;
    private EditText etNameParticipant1;
    private EditText etNameParticipant2;
    private EditText etPrize;
    private EditText etPlace;
    private DatePicker dpDate;
    private Button btMainAccept;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // First we set up the indoor of the window of the XML's dialog
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(infService);
        // We inflate the compound component defined in the XML
        final View inflatedLayout = li.inflate(R.layout.activity_insert, null);

        // We search the components inside the dialog
        etId = inflatedLayout.findViewById(R.id.etInsId);
        sHour = inflatedLayout.findViewById(R.id.sInsHour);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.hoursTournaments));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sHour.setAdapter(adapter);
        etNameParticipant1 = inflatedLayout.findViewById(R.id.etInsNameParticipant1);
        etNameParticipant2 = inflatedLayout.findViewById(R.id.etInsNameParticipant2);
        etPrize = inflatedLayout.findViewById(R.id.etInsPrize);
        etPlace = inflatedLayout.findViewById(R.id.etInsPlace);
        dpDate = inflatedLayout.findViewById(R.id.dpInsDate);
        GregorianCalendar now = new GregorianCalendar();
        dpDate.init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), null);
        btMainAccept = inflatedLayout.findViewById(R.id.btInsAccept);

        AlertDialog.Builder window = new AlertDialog.Builder(getActivity());

        // We assign the content inside de dialog(the one we have just inflated)
        window.setView(inflatedLayout);

        btMainAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> mistakes = new ArrayList<String>();

                // ID
                if (etId.getText().toString().compareTo("") == 0) {
                    etId.setBackgroundColor(Color.RED);
                    mistakes.add(getActivity().getString(R.string.tournament_id));
                } else {
                    etId.setBackgroundColor(Color.WHITE);
                }

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
                        int id = Integer.parseInt(etId.getText().toString());
                        String sportName = sportChoosed();
                        int hour = Integer.parseInt(sHour.getSelectedItem().toString());
                        long date = c.getTimeInMillis();
                        String nameParticipant1 = etNameParticipant1.getText().toString();
                        String nameParticipant2 = etNameParticipant2.getText().toString();
                        int prize = Integer.parseInt(etPrize.getText().toString());
                        String place = etPlace.getText().toString();

                        Match match = new Match(id, sportName, hour, date, nameParticipant1,
                                nameParticipant2, prize, place);
                        saveMatch(match);

                        if (sportName.equals("basketball")) {
                            BasketballActivity activity1 = (BasketballActivity) getActivity();
                            activity1.update();
                        } else if (sportName.equals("football")) {
                            FootballActivity activity2 = (FootballActivity) getActivity();
                            activity2.update();
                        } else {
                            TennisActivity activity3 = (TennisActivity) getActivity();
                            activity3.update();
                        }
                        cancel();
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

    private void saveMatch(Match match) {
        // open the database to write
        DataBDAdapter gestora = new DataBDAdapter(getActivity());
        gestora.openForWriting();
        // we save
        long id = gestora.addMatch(match);
        if (id != -1) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.message_newId) + Long.toString(id), Toast.LENGTH_SHORT).show();
            // we close the database
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.message_incorrectDeleted) + Long.toString(id), Toast.LENGTH_SHORT).show();
        }
        gestora.close();
        //Log.d("saveMatch", "Se ha guardado el partido");
    }

    private String sportChoosed() {

        if (getActivity().getClass().getSimpleName().equals("BasketballActivity"))
            return "basketball";
        else if (getActivity().getClass().getSimpleName().equals("FootballActivity"))
            return "football";
        else
            return "tennis";
    }

    public void cancel() {
        etId.setText("");
        sHour.setSelected(false);
        etNameParticipant1.setText("");
        etNameParticipant2.setText("");
        etPrize.setText("");
        etPlace.setText("");
        dpDate.setSelected(false);
    }

}
