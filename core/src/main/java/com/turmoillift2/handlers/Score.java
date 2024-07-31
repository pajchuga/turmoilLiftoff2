package com.turmoillift2.handlers;

public class Score {
    private long points = 0;


    public void addPoints(long points) {
        this.points += points;
    }
    public long getPoints() {
        return points;
    }
}
