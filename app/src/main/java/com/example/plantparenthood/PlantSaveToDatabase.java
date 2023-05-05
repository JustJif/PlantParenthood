package com.example.plantparenthood;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;

@Entity(tableName = "Plant")
public class PlantSaveToDatabase
{
    @PrimaryKey
    private int id;
    private String common_name;
    private String scientific_name;
    private String other_name;
    private String cycle;
    private String watering;
    private String sunlight;
    private byte[] default_image;
    private String plantImageURL;

    /**
     * Save a Plant object to the database
     * @param plantToSave the plant object to save
     */
    public PlantSaveToDatabase(Plant plantToSave)
    {
        id = plantToSave.getId();
        common_name= plantToSave.getCommon_name();
        scientific_name = plantToSave.getScientific_name();
        other_name = plantToSave.getOther_name();
        cycle = plantToSave.getCycle();
        watering = plantToSave.getWatering();
        sunlight = plantToSave.getSunlight();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        plantToSave.getDefault_image().compress(Bitmap.CompressFormat.JPEG,10,byteArrayOutputStream);
        default_image = byteArrayOutputStream.toByteArray();
        plantImageURL = plantToSave.getPlantImageURL();
    }

    /**
     * Required by the database to create an object for loading from the database
     */
    public PlantSaveToDatabase(){}

    public void setId(int id) {
        this.id = id;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public void setWatering(String watering) {
        this.watering = watering;
    }

    public void setSunlight(String sunlight) {
        this.sunlight = sunlight;
    }

    public void setDefault_image(byte[] default_image) {
        this.default_image = default_image;
    }

    public void setPlantImageURL(String plantImageURL) {
        this.plantImageURL = plantImageURL;
    }

    public int getId() {
        return id;
    }

    public String getCommon_name() {
        return common_name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public String getOther_name() {
        return other_name;
    }

    public String getCycle() {
        return cycle;
    }

    public String getWatering() {
        return watering;
    }

    public String getSunlight() {
        return sunlight;
    }

    public byte[] getDefault_image() {
        return default_image;
    }

    public String getPlantImageURL() {
        return plantImageURL;
    }
}