package com.example.plantparenthood;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
@Entity(tableName = "Space")
public class Space {
    @PrimaryKey
    private int spaceID;
    private String spaceName;
    private int spaceLightLevel;
    @Ignore
    private ArrayList<Plant> plantList;

    private String plantID;

    // Constructor
    public Space(int spaceID, String spaceName, int spaceLightLevel) {
        this.spaceID = spaceID;
        this.spaceName = spaceName;
        this.spaceLightLevel = spaceLightLevel;
        this.plantList = new ArrayList<Plant>();
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

    public String getPlantID() {
        return plantID;
    }

    public void setPlantID(String plantID) {
        this.plantID = plantID;
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
