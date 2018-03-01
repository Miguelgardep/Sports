package com.example.miguel.sports.sportMenu;

import java.util.HashMap;

/**
 * Created by miguel on 20/01/2018.
 */

public class Sport extends HashMap<String, Object> {

    public Sport(String name, Integer image) {
        this.put(SportKeys.SPORT_NAME.toString(), name);
        this.put(SportKeys.SPORT_IMAGE.toString(), image);
    }

}
