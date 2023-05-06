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

    private static int numGroups = 0;

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
        if(plantList.isEmpty()) {
            plantList.add(plant);
            plantIDs += plant.getId();
        } else {
            plantList.add(plant);
            plantIDs += ("," + plant.getId());
        }
    }

    public void removePlant(Plant plant) {
        String plantID = Integer.toString(plant.getId());
        int index = this.plantIDs.indexOf(plantID);
        if(index == 0) {
            setPlantIDs(this.plantIDs.substring(index + (String.valueOf(plant.getId())).length()));
            plantList.remove(plant);
        } else {
            this.plantIDs = this.plantIDs.substring(0,index-1) + this.plantIDs.substring((String.valueOf(plant.getId())).length());
            plantList.remove(plant);
        }

    }

    public void waterAll() {
        for (Plant plant : plantList) {
            if(plant != null && plant.getWateringCycle() != null) {
                plant.waterPlant(ComputeDate.getDayOfTheYear());
            }

        }
    }
}
