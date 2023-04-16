package com.example.plantparenthood;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DataAccessObject
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Completable addPlant(Plant plant);

    @Query("SELECT * FROM plant WHERE id = :id")
    public Single<Plant> loadPlantByID(int id);

    @Query("SELECT * FROM plant")
    public Single<ArrayList<Plant>> loadAllPlants();
}
