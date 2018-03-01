package com.example.miguel.sports.sportMenu;

import android.content.Context;

import com.example.miguel.sports.R;

import java.util.ArrayList;

/**
 * Created by miguel on 20/01/2018.
 */

public class SportsManager extends ArrayList<Sport> {

    public SportsManager(Context context) {
        String[] sportNames = {context.getString(R.string.basketball),
                context.getString(R.string.football),
                context.getString(R.string.tennis)};
        int[] sportImages = {R.drawable.basketball_ball,
                R.drawable.football_ball, R.drawable.tennis_ball};

        for (int x = 0; x < sportNames.length; x++) {
            this.add(new Sport(sportNames[x], sportImages[x]));
        }
    }
}
