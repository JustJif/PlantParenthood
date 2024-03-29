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
            for(int i = 0; i < parsed.length; i++) {
                if(TextUtils.isDigitsOnly(parsed[i]) && !TextUtils.isEmpty(parsed[i])) {
                    plantArrayList.add(DatabaseHandler.getDatabase(null).getPlantFromDBbyID(Integer.parseInt(parsed[i])));
                }
            }
        }

    }

    public List<Group> getGroupsFromDB()
    {
        List<Group> loadedGroups = GroupDB.GroupDataAccessObject().loadAllGroups();
        for(int i = 0; i < loadedGroups.size(); i++) {
            ArrayList<Plant> plantArrayList = new ArrayList<Plant>();
            loadPlantsIntoGroup(loadedGroups.get(i).getPlantIDs(),plantArrayList);
            loadedGroups.get(i).setAllPlants(plantArrayList);
        }

        return loadedGroups;
    }

    public void addGroupToDatabase(Group newGroup)
    {
        GroupDB.GroupDataAccessObject().addGroup(newGroup);
    }
    public void removeGroupToDatabase(Group group)
    {
        GroupDB.GroupDataAccessObject().deleteGroup(group);
    }

    public void deletePlantFromGroup(Group group, Plant plant) {
        group.removePlant(plant);
        GroupDB.GroupDataAccessObject().addGroup(group);
    }
    public void deletePlantFromGroups(Plant plant) {
        List<Group> loadedGroups = GroupDB.GroupDataAccessObject().loadAllGroups();
        for(int i = 0; i < loadedGroups.size(); i++) {
            if(loadedGroups.get(i).getPlantIDs().contains(String.valueOf(plant.getId()))) {
                loadedGroups.get(i).removePlant(plant);
                GroupDB.GroupDataAccessObject().addGroup(loadedGroups.get(i));
            }
        }
    }
}
