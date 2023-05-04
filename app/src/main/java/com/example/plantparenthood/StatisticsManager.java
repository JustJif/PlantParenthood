package com.example.plantparenthood;

import android.content.Context;

import androidx.room.Room;

public class StatisticsManager {
    static Statistics statistics;
    static Context context;

    public StatisticsManager(Context context)
    {
        this.context = context;
    }

    public static void waterPlant() {
        statistics = Room.databaseBuilder(context, StatisticsDatabase.class, "StatisticsDatabase").build().statisticsAccessObject().loadStatistics();
        statistics.waterPlant();
        Room.databaseBuilder(context, StatisticsDatabase.class, "StatisticsDatabase").build().statisticsAccessObject().addStatistics(statistics);
    }

    public static void addPlant(){
        statistics = Room.databaseBuilder(context, StatisticsDatabase.class, "StatisticsDatabase").build().statisticsAccessObject().loadStatistics();
        statistics.plantAdded();
        Room.databaseBuilder(context, StatisticsDatabase.class, "StatisticsDatabase").build().statisticsAccessObject().addStatistics(statistics);
    }

    public static void deletePlant(){
        statistics = Room.databaseBuilder(context, StatisticsDatabase.class, "StatisticsDatabase").build().statisticsAccessObject().loadStatistics();
        statistics.plantDied();
        Room.databaseBuilder(context, StatisticsDatabase.class, "StatisticsDatabase").build().statisticsAccessObject().addStatistics(statistics);
    }

    public int getNumOwnedPlants() {
        return statistics.getNumOwnedPlants();
    }

    public int getTotalOwnedPlants() {
        return statistics.getTotalOwnedPlants();
    }

    public int getTotalDeadPlants() {
        return statistics.getTotalDeadPlants();
    }

    public double getMeanTimeBetweenWatering() {
        return statistics.getMeanTimeBetweenWatering();
    }

    public double getMedianTimeBetweenWatering() {
        return statistics.getMedianTimeBetweenWatering();
    }

    public Long getLastTimeWatered() {
        return statistics.getLastWateringDateLong();
    }

    public Long getFirstTimeWatered() {
        return statistics.getFirstWateringDateLong();
    }
}
