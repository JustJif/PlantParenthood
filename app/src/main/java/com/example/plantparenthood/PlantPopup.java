package com.example.plantparenthood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class PlantPopup extends AppCompatActivity
{
    ImageView plantImage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_popup);
        plantImage = (ImageView) findViewById(R.id.plantImage);
        Intent previousActivity = getIntent();
        plantImage.setImageResource(previousActivity.getIntExtra("image",0));
    }
}