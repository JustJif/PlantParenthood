package com.example.plantparenthood;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface WateringDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addWatering(Watering wateringCycle);

    @Query("SELECT * FROM Watering WHERE plantID = :id")
    Watering loadWateringByID(int id);
    @Delete
    void deleteSchedule(Watering wateringCycle);
}
