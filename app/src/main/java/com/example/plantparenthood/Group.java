package com.example.plantparenthood;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "Group")
public class Group {
    @PrimaryKey
    private int GroupID;
    private String GroupName;
    private int GroupLightLevel;
    @Ignore
    private ArrayList<Plant> plantList;

    private static int numGroups;

    private String plantIDs;

    // Constructor
    public Group(String GroupName) {
        this.GroupID = numGroups;
        this.GroupName = GroupName;
        this.GroupLightLevel = 0;
        this.plantList = new ArrayList<Plant>();
        numGroups++;
        plantIDs = "";
    }
    public void setPlantIDs(String plantIDs) {
        this.plantIDs = plantIDs;
    }

    public String getPlantIDs() {
        return plantIDs;
    }
    public ArrayList<Plant> getAllPlants() {
        return plantList;
    }

    public void setAllPlants(ArrayList<Plant> plantList) {
        this.plantList = plantList;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int GroupID) {
        this.GroupID = GroupID;
    }

    public int getGroupLightLevel() {
        return GroupLightLevel;
    }

    public void setGroupLightLevel(int GroupLightLevel) {
        this.GroupLightLevel=GroupLightLevel;
    }

    // Getters and Setters
    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    // Other Methods
    public void addPlant(Plant plant) {
        System.out.println("Plant ID: " + plant.getId());
        plantList.add(plant);
        if(plantList.isEmpty()) {

            plantIDs += plant.getId();
        } else {
            plantIDs += ("," + plant.getId());
        }
    }

    public void removePlant(Plant plant) {
        String plantID = "," + Integer.toString(plant.getId());
        int index = plantIDs.indexOf(plantID);
        int lastIndex = plantIDs.lastIndexOf(plantID);
        plantIDs = plantIDs.substring(0,index) + plantIDs.substring(lastIndex);
        plantList.remove(plant);
    }

    public void waterAll() {
        for (Plant plant : plantList) {
            //plant.water();
        }
    }
}
