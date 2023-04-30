package com.example.plantparenthood;

import java.util.Date;
public class Watering
{
    private int plantID; //used to see which plant this Watering cycle exists to
    private Date lastWateredDate;
    private Date nextWateringDate;
    private int timesWatered;

    public Watering(Date lastWateredDate, Date nextWateringDate, int timesWatered) {
        this.lastWateredDate = lastWateredDate;
        this.nextWateringDate = nextWateringDate;
        this.timesWatered = timesWatered;
    }
    public Date getLastWateredDate() {
        return lastWateredDate;
    }
}