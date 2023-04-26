package com.example.plantparenthood;

import android.graphics.Bitmap;

import androidx.room.Update;

public class UpdatePlant
{
    private int id;
    private String common_name;
    private String scientific_name;
    private String other_name;
    private String cycle;
    private String watering;
    private String sunlight;
    private Bitmap default_image;
    private String plantImageURL;

    public UpdatePlant() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getOther_name() {
        return other_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getWatering() {
        return watering;
    }

    public void setWatering(String watering) {
        this.watering = watering;
    }

    public String getSunlight() {
        return sunlight;
    }

    public void setSunlight(String sunlight) {
        this.sunlight = sunlight;
    }

    public Bitmap getDefault_image() {
        return default_image;
    }

    public void setDefault_image(Bitmap default_image) {
        this.default_image = default_image;
    }

    public String getPlantImageURL() {
        return plantImageURL;
    }

    public void setPlantImageURL(String plantImageURL) {
        this.plantImageURL = plantImageURL;
    }
}
