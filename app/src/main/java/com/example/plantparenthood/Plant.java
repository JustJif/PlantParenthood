package com.example.plantparenthood;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

@Entity
public class Plant {
    @PrimaryKey
    int id;
    String common_name;
    String scientific_name;
    String other_name;
    String cycle;
    String watering;
    String sunlight;
    @Ignore
    Bitmap default_image;
    String plantImageURL;// images are large they will be refetched from api each time
}
