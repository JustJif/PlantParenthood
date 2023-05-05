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
public interface StatisticsAccessObject
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addStatistics(Statistics statistics);

    @Query("SELECT * FROM Statistics WHERE id=0")
    public Statistics loadStatistics();
}
