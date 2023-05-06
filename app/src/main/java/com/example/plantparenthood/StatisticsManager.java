package com.example.plantparenthood;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

public class StatisticsManager {
    Statistics statistics;

    public StatisticsManager() {
        statistics = new Statistics();
    }
    public void setStatistics(Statistics statistics) {
        if(statistics != null){
            this.statistics = statistics;
        }
    }
    public void waterPlant(){
        AsyncTask.execute(new Runnable() {
            public void run() {
                statistics = StatisticsDatabaseHandler.getDatabase(null).getStatistics();
                statistics.waterPlant();
                StatisticsDatabaseHandler.getDatabase(null).pushToDatabase(statistics);
            }
        });
    }
    public void addPlant(){
        AsyncTask.execute(new Runnable(){
           public void run(){
               Log.e("Adding Plant","Retreiving Database");
               statistics = StatisticsDatabaseHandler.getDatabase(null).getStatistics();
               Log.e("Adding Plant","Calling statistics.plantAdded()");
               statistics.plantAdded();
               Log.e("Adding Plant","Pushing to database");
               StatisticsDatabaseHandler.getDatabase(null).pushToDatabase(statistics);
           }
        });
    }
    public void deletePlant(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                statistics = StatisticsDatabaseHandler.getDatabase(null).getStatistics();
                statistics.plantDied();
                StatisticsDatabaseHandler.getDatabase(null).pushToDatabase(statistics);
            }
        });
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

    public int getTimesWatered(){
        return statistics.getTotalTimesWatered();
    }
    public Long getLastTimeWatered() {
        return statistics.getLastWateringDateLong();
    }
    public Long getFirstTimeWatered() {
        return statistics.getFirstWateringDateLong();
    }
}
