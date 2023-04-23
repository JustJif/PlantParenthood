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
public interface SpaceDataAccessObject
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addSpace(Space space);

    @Query("SELECT * FROM Space WHERE spaceID = :id")
    public Space loadSpaceByID(int id);

    @Query("SELECT * FROM Space")
    public List<Space> loadAllSpaces();
}
