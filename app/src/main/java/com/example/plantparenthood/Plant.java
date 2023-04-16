package com.example.plantparenthood;

import android.graphics.Bitmap;
import android.widget.ImageView;

import org.json.JSONObject;

public class Plant
{
    int id;
    String common_name;
    String[] scientific_name;
    String[] other_name;
    String cycle;
    String watering;
    String[] sunlight;
    Bitmap default_image;
    String plantImageURL;
    //images are large they will be refetched from api each time
}