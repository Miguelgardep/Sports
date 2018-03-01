package com.example.miguel.sports;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.miguel.sports.sportMenu.Sport;
import com.example.miguel.sports.sportMenu.SportKeys;
import com.example.miguel.sports.sportMenu.SportsManager;
import com.example.miguel.sports.sportsActivities.BasketballActivity;
import com.example.miguel.sports.sportsActivities.FootballActivity;
import com.example.miguel.sports.sportsActivities.TennisActivity;

/**
 * Created by miguel on 02/01/2018.
 */

public class Query extends Activity {

    private ListView list;
    private SportsManager listaItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlist);


        list = this.findViewById(R.id.mainlist);


        listaItems = new SportsManager(this);

        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listaItems,
                R.layout.line_mainlist,
                new String[]{SportKeys.SPORT_NAME.toString(), SportKeys.SPORT_IMAGE.toString()},
                new int[]{R.id.sportText, R.id.sportImage});

        list.setAdapter(listItemAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sport sport = listaItems.get(position);
                String sportChoosen = (String) sport.get(SportKeys.SPORT_NAME.toString());

                Class choosenClass = null;
                if (sportChoosen.equals("Basketball"))
                    choosenClass = BasketballActivity.class;
                else if (sportChoosen.equals("Football"))
                    choosenClass = FootballActivity.class;
                else
                    choosenClass = TennisActivity.class;

                Intent myIntent = new Intent(Query.this, choosenClass);

                startActivity(myIntent);
            }
        });
    }
}
