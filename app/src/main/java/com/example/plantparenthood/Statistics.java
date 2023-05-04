package com.example.plantparenthood;

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
    private double meanTimeBetweenWatering;
    private double medianTimeBetweenWatering;
    private static int curOwnedPlants;
    private static int totalOwnedPlants;
    private int totalDeadPlants;
    @Ignore
    private ArrayList<Double> daysSinceLastWateringArr;
    private int totalTimesWatered;
    @Ignore
    private Date firstWateringDate;
    @Ignore
    private Date lastWateringDate;
    private long firstWateringDateLong;
    private long lastWateringDateLong;

    public Statistics(){
        meanTimeBetweenWatering = 0;
        medianTimeBetweenWatering = 0;
        curOwnedPlants = 0;
        totalOwnedPlants = 0;
        totalDeadPlants = 0;
        daysSinceLastWateringArr = new ArrayList<Double>();
        totalTimesWatered = 0;
        firstWateringDate = new Date();
        lastWateringDate = new Date();
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
        Statistics.totalOwnedPlants = totalOwnedPlants;
    }
    public int getTotalTimesWatered(){
        return totalTimesWatered;
    }
    public void setTotalTimesWatered(int totalTimesWatered){
        this.totalTimesWatered = totalTimesWatered;
    }
    public static void plantAdded(){
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
    public void setMedianTimeBetweenWatering(double meanTimeBetweenWatering){
        this.medianTimeBetweenWatering = meanTimeBetweenWatering;
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
    public void waterPlant(Date date){
        long lastWater = lastWateringDate.getTime();
        this.lastWateringDate=date;
        totalTimesWatered++;
        long currentWateringDate = lastWateringDate.getTime();
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
    private void computeMedianTimeBetweenWatering(){
        Collections.sort(daysSinceLastWateringArr);
        if(daysSinceLastWateringArr.size()>0){
            medianTimeBetweenWatering = daysSinceLastWateringArr.get(daysSinceLastWateringArr.size()/2);
        }
    }
    public double getMeanTimeBetweenWatering(){
        computeMeanTimeBetweenWatering();
        return meanTimeBetweenWatering;
    }
    public double getMedianTimeBetweenWatering(){
        computeMedianTimeBetweenWatering();
        return medianTimeBetweenWatering;
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