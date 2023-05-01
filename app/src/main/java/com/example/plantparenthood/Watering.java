package com.example.plantparenthood;

public class Watering
{
    private int plantID; //used to see which plant this Watering cycle exists to
    private int lastWateredDay;
    private int wateringInterval;
    private int timesWatered;

    public Watering(int plantId, int lastWateredDay, int wateringInterval, int timesWatered) {
        this.plantID = plantId;
        this.lastWateredDay = lastWateredDay;
        this.wateringInterval = wateringInterval;
        this.timesWatered = timesWatered;
    }
    public int getPlantID() {
        return plantID;
    }

    public void setPlantID(int plantID) {
        this.plantID = plantID;
    }

    public int getLastWateredDay() {
        return lastWateredDay;
    }

    public void setLastWateredDay(int lastWateredDay) {
        this.lastWateredDay = lastWateredDay;
    }

    public int getWateringInterval() {
        return wateringInterval;
    }

    public void setWateringInterval(int wateringInterval) {
        this.wateringInterval = wateringInterval;
    }

    public int getTimesWatered() {
        return timesWatered;
    }

    public void iterateTimesWater() {
        timesWatered++;
    }
}