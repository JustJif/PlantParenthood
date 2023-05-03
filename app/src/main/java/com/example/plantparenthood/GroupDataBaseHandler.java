package com.example.plantparenthood;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

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
        String[] parsed = unparsed.split(",");
        for(int i = 0; i < parsed.length; i++) {
            plantArrayList.add(DatabaseHandler.getDatabase(null).getPlantFromDBbyID(Integer.parseInt(parsed[i])));
        }
    }

    public List<Group> getGroupsFromDB()
    {
        List<Group> loadedGroups = GroupDB.GroupDataAccessObject().loadAllGroups();
        return loadedGroups;
    }

    public void addGroupToDatabase(Group group)
    {
        GroupDB.GroupDataAccessObject().addGroup(group);
        System.out.println(getGroupsFromDB().size());
    }
}
