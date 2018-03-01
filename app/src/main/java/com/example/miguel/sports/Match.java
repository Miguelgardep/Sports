package com.example.miguel.sports;

/**
 * Created by miguel on 01/01/2018.
 */

public class Match {
    private int id;
    private String name;
    private int hour;
    private long date;
    private String nameParticipant1;
    private String nameParticipant2;
    private int prize;
    private String place;

    public Match(int id, String name, int hour, long date, String nameParticipant1, String nameParticipant2, int prize, String place) {
        this.id = id;
        this.name = name;
        this.hour = hour;
        this.date = date;
        this.nameParticipant1 = nameParticipant1;
        this.nameParticipant2 = nameParticipant2;
        this.prize = prize;
        this.place = place;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHour() {
        return hour;
    }

    public long getDate() {
        return date;
    }

    public String getNameParticipant1() {
        return nameParticipant1;
    }

    public String getNameParticipant2() {
        return nameParticipant2;
    }

    public int getPrize() {
        return prize;
    }

    public String getPlace() {
        return place;
    }
}


