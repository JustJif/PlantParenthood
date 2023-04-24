package com.example.plantparenthood;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DataAccessObject
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addPlant(PlantSaveToDatabase plant);

    @Query("SELECT * FROM Plant WHERE id = :id")
    public PlantSaveToDatabase loadPlantByID(int id);

    @Query("SELECT * FROM Plant")
    public List<PlantSaveToDatabase> loadAllPlants();
}
