package com.example.plantparenthood;

import java.util.ArrayList;

public class Space {
    private int spaceID;
    private String spaceName;
    private int spaceLightLevel;
    private ArrayList<Plant> plantList;

    // Constructor
    public Space(int spaceID, String spaceName, int spaceLightLevel) {
        this.spaceID = spaceID;
        this.spaceName = spaceName;
        this.spaceLightLevel = spaceLightLevel;
        this.plantList = new ArrayList<Plant>();
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
