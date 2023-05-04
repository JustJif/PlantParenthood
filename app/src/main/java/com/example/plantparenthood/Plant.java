package com.example.plantparenthood;

import android.graphics.Bitmap;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

public class Plant
{
    public static class PlantBuilder
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
        private Watering wateringCycle;

        public PlantBuilder() {}
        public PlantBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public PlantBuilder setCommon_name(String common_name) {
            this.common_name = common_name;
            return this;
        }

        public PlantBuilder setScientific_name(String scientific_name) {
            this.scientific_name = scientific_name;
            return this;
        }

        public PlantBuilder setOther_name(String other_name) {
            this.other_name = other_name;
            return this;
        }

        public PlantBuilder setCycle(String cycle) {
            this.cycle = cycle;
            return this;
        }

        public PlantBuilder setWatering(String watering) {
            this.watering = watering;
            return this;
        }

        public PlantBuilder setSunlight(String sunlight) {
            this.sunlight = sunlight;
            return this;
        }

        public PlantBuilder setDefault_image(Bitmap default_image) {
            this.default_image = default_image;
            return this;
        }

        public PlantBuilder setPlantImageURL(String plantImageURL) {
            this.plantImageURL = plantImageURL;
            return this;
        }

        public Plant buildPlant(){
            Plant plant = new Plant(this);
            return plant;
        }
        public void setWateringCycle(Watering wateringCycle) {
            this.wateringCycle = wateringCycle;
        }
    }

    private int id;
    private String common_name;
    private String scientific_name;
    private String other_name;
    private String cycle;
    private String watering;
    private String sunlight;
    private Bitmap default_image;
    private String plantImageURL;//images are large they will be refetched from api whenever required
    private Watering wateringCycle;

    private Plant(PlantBuilder plantBuilder){
        this.id = plantBuilder.id;
        this.common_name = plantBuilder.common_name;
        this.scientific_name = plantBuilder.scientific_name;
        this.other_name = plantBuilder.other_name;
        this.cycle = plantBuilder.cycle;
        this.watering = plantBuilder.watering;
        this.sunlight = plantBuilder.sunlight;
        this.default_image = plantBuilder.default_image;
        this.plantImageURL = plantBuilder.plantImageURL;
        this.wateringCycle = plantBuilder.wateringCycle;
    }

    public void waterPlant(int todaysDate)
    {
        getWateringCycle().setLastWateredDay(todaysDate);
        getWateringCycle().iterateTimesWater();
        DatabaseHandler.getDatabase(null).saveWateringSchedule(getWateringCycle());
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

    public Bitmap getDefault_image() {
        return default_image;
    }

    public String getPlantImageURL() {
        return plantImageURL;
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

    public void setDefault_image(Bitmap newImage) {
        default_image = newImage;
    }

    public Watering getWateringCycle() { return wateringCycle;}

    public void setWateringCycle(Watering wateringCycle) {this.wateringCycle = wateringCycle;}
}
