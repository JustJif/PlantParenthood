package com.example.plantparenthood;

import android.content.Context;

import androidx.room.Room;

/**
 * Follows singleton pattern, only call methods in here using async task,
 * as database cannot be accesses on the main thread
 */
public class StatisticsDatabaseHandler {
    private static StatisticsDatabaseHandler activeDatabase = null;
    private static StatisticsDatabase database;
    //private PlantDatabase wateringDB;
    private StatisticsDatabaseHandler(Context context) {
        database = Room.databaseBuilder(context, StatisticsDatabase.class, "StatisticsDatabase").build();
    }
    public static StatisticsDatabaseHandler getDatabase(Context applicationContext) {
        if (activeDatabase == null) {
            activeDatabase = new StatisticsDatabaseHandler(applicationContext);
        }
        return activeDatabase;
    }
    public StatisticsAccessObject getDataAccessObject() {
        return database.statisticsAccessObject();
    }
    public Statistics getStatistics(){
        return database.statisticsAccessObject().loadStatistics();
    }
    public void pushToDatabase(Statistics statistics) {
        database.statisticsAccessObject().addStatistics(statistics);
    }

    public static StatisticsDatabaseHandler createInMemoryDatabase(Context context)
    {
        database = Room.inMemoryDatabaseBuilder(context, StatisticsDatabase.class).build();
        return getDatabase(context);
    }
}