package com.example.plantparenthood;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GroupDataAccessObject
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addGroup(Group group);

    @Query("SELECT * FROM `Group` WHERE GroupID = :id")
    public Group loadGroupByID(int id);

    @Query("SELECT * FROM `Group`")
    public List<Group> loadAllGroups();

    @Delete
    void deleteGroup(Group group);
}
