package com.example.plantparenthood;

import android.content.Context;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

@Entity(tableName = "Statistics")
public class Statistics {
    @PrimaryKey
    private int id = 0;
    private double meanTimeBetweenWatering = 0;
    private int curOwnedPlants = 0;
    private int totalOwnedPlants = 0;
    private int totalDeadPlants = 0;
    @Ignore
    private ArrayList<Double> daysSinceLastWateringArr;
    private int totalTimesWatered = 0;
    @Ignore
    private Date firstWateringDate;
    @Ignore
    private Date lastWateringDate;
    private long firstWateringDateLong = 0;
    private long lastWateringDateLong = 0;

    public Statistics(){
        meanTimeBetweenWatering = 0;
        curOwnedPlants = 0;
        totalOwnedPlants = 0;
        totalDeadPlants = 0;
        daysSinceLastWateringArr = new ArrayList<Double>();
        totalTimesWatered = 0;
        firstWateringDate = new Date(0);
        lastWateringDate = new Date(0);
        firstWateringDateLong = firstWateringDate.getTime();
        lastWateringDateLong = lastWateringDate.getTime();
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getCurOwnedPlants(){
        return curOwnedPlants;
    }
    public void setCurOwnedPlants(int curOwnedPlants){
        this.curOwnedPlants = curOwnedPlants;
    }
    public int getTotalOwnedPlants(){
        return totalOwnedPlants;
    }
    public void setTotalOwnedPlants(int totalOwnedPlants){
        this.totalOwnedPlants = totalOwnedPlants;
    }
    public int getTotalTimesWatered(){
        return totalTimesWatered;
    }
    public void setTotalTimesWatered(int totalTimesWatered){
        this.totalTimesWatered = totalTimesWatered;
    }
    public void plantAdded(){
        Log.d("Adding Plant","Incrementing curOwnedPlants");
        curOwnedPlants++;
        totalOwnedPlants++;
    }
    public long getFirstWateringDateLong(){
        return firstWateringDateLong;
    }
    public void setFirstWateringDateLong(long firstWateringDateLong){
        this.firstWateringDateLong = firstWateringDateLong;
    }
    public long getLastWateringDateLong(){
        return lastWateringDateLong;
    }
    public void setLastWateringDateLong(long lastWateringDateLong){
        this.lastWateringDateLong = lastWateringDateLong;
    }
    public void setMeanTimeBetweenWatering(double meanTimeBetweenWatering){
        this.meanTimeBetweenWatering = meanTimeBetweenWatering;
    }
    public void plantDied(){
        if(curOwnedPlants>0){
            totalDeadPlants++;
            curOwnedPlants--;
        }
    }
    public void setFirstWateringDate(Date date){
        this.firstWateringDate=date;
        totalTimesWatered++;
    }
    public void waterPlant(){
        Date date = new Date();
        long lastWater = lastWateringDate.getTime();
        this.lastWateringDate=date;
        totalTimesWatered++;
        long currentWateringDate = date.getTime();
        long timeBetween = currentWateringDate-lastWater;
        double days = (double)timeBetween/86400000;
        daysSinceLastWateringArr.add(days);
    }
    private void computeMeanTimeBetweenWatering(){
        long first = firstWateringDate.getTime();
        long last = lastWateringDate.getTime();
        long timeBetween = last-first;
        double days = (double)timeBetween/86400000;
        if(days>0)
        {
            meanTimeBetweenWatering = totalTimesWatered/days;
        }
    }
    public double getMeanTimeBetweenWatering(){
        computeMeanTimeBetweenWatering();
        return meanTimeBetweenWatering;
    }
    public Date getLastTimeWatered(){
        return new Date(firstWateringDateLong);
    }
    public Date getFirstTimeWatered(){
        return new Date(firstWateringDateLong);
    }
    public int getNumOwnedPlants(){
        return curOwnedPlants;
    }
    public int getTotalDeadPlants(){
        return totalDeadPlants;
    }
    public void setTotalDeadPlants(int totalDeadPlants)
    {
        this.totalDeadPlants = totalDeadPlants;
    }
}