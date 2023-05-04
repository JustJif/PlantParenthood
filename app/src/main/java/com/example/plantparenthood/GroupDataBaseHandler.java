package com.example.plantparenthood;

import android.content.Context;
import android.text.TextUtils;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import kotlin.text.Regex;

/**
 * Follows singleton pattern, only call methods in here using async task,
 * as database cannot be accesses on the main thread
 */
public class GroupDataBaseHandler {
    private static GroupDataBaseHandler activeDatabase = null;
    private PlantDatabase GroupDB;

    private GroupDataBaseHandler(Context context)
    {
        GroupDB = Room.databaseBuilder(context, PlantDatabase.class, "PlantDatabase").build();

    }

    public static GroupDataBaseHandler getDatabase(Context applicationContext)
    {
        if(activeDatabase == null)
        {
            activeDatabase = new GroupDataBaseHandler(applicationContext);
        }

        return activeDatabase;
    }

    public GroupDataAccessObject getDataAccessObject()
    {
        return GroupDB.GroupDataAccessObject();
    }

    public Group getGroupFromDBbyID(int GroupID)
    {
        Group loadedGroup = GroupDB.GroupDataAccessObject().loadGroupByID(GroupID);
        ArrayList<Plant> plantArrayList = new ArrayList<Plant>();
        loadPlantsIntoGroup(loadedGroup.getPlantIDs(),plantArrayList);
        loadedGroup.setAllPlants(plantArrayList);
        return loadedGroup;
    }

    private void loadPlantsIntoGroup(String unparsed, ArrayList<Plant> plantArrayList) {
        if(!TextUtils.isEmpty(unparsed)) {
            String[] parsed = unparsed.split(",");
            System.out.println("LENGTH " + parsed.length);
            System.out.println("Plant: " + parsed[0]);
            for(int i = 0; i < parsed.length; i++) {
                System.out.println("Plants" + parsed[i]);
                if(TextUtils.isDigitsOnly(parsed[i]) && !TextUtils.isEmpty(parsed[i])) {
                    plantArrayList.add(DatabaseHandler.getDatabase(null).getPlantFromDBbyID(Integer.parseInt(parsed[i])));
                }
            }
        }

    }

    public List<Group> getGroupsFromDB()
    {
        List<Group> loadedGroups = GroupDB.GroupDataAccessObject().loadAllGroups();
        ArrayList<Plant> plantArrayList = new ArrayList<Plant>();
        for(int i = 0; i < loadedGroups.size(); i++) {
            loadPlantsIntoGroup(loadedGroups.get(i).getPlantIDs(),plantArrayList);
            loadedGroups.get(i).setAllPlants(plantArrayList);
        }

        return loadedGroups;
    }

    public void addGroupToDatabase(Group group)
    {
        GroupDB.GroupDataAccessObject().addGroup(group);
    }
    public void removeGroupToDatabase(Group group)
    {
        GroupDB.GroupDataAccessObject().deleteGroup(group);
    }

    public void deletePlantFromGroup(Group group, Plant plant) {
        group.removePlant(plant);
    }
}
