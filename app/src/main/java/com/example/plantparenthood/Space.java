package com.example.plantparenthood;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Space")
public class Space {
    @PrimaryKey
    private int spaceID;
    private String spaceName;
    private int spaceLightLevel;
    @Ignore
    private ArrayList<Plant> plantList;

    private static int numSpaces;

    private String plantIDs;

    // Constructor
    public Space(String spaceName) {
        this.spaceID = numSpaces;
        this.spaceName = spaceName;
        this.spaceLightLevel = 0;
        this.plantList = new ArrayList<Plant>();
        numSpaces++;
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

    public int getSpaceID() {
        return spaceID;
    }

    public void setSpaceID(int spaceID) {
        this.spaceID = spaceID;
    }

    public int getSpaceLightLevel() {
        return spaceLightLevel;
    }

    public void setSpaceLightLevel(int spaceLightLevel) {
        this.spaceLightLevel=spaceLightLevel;
    }


    public void parseStrings() {
        //later
    }

    // Getters and Setters
    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    // Other Methods
    public void addPlant(Plant plant) {

        plantList.add(plant);
        if(plantList.isEmpty()) {
            plantIDs += plant.getId();
        } else {
            plantIDs += ("," + plant.getId());
        }
    }

    public void removePlant(Plant plant) {
        plantList.remove(plant);
    }

    public void waterAll() {
        for (Plant plant : plantList) {
            //plant.water ();
        }
    }
}
