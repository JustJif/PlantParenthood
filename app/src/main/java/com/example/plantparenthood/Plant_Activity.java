package com.example.plantparenthood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class Plant_Activity extends AppCompatActivity {
    CardView addPlant;
    List<Plant> plantList;
    RecyclerView plantGrid;
    PlantDatabase plantDB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);
        plantList = new ArrayList<>();
        plantDB = Room.databaseBuilder(getApplicationContext(),PlantDatabase.class,"PlantDatabase").build();

        plantGrid = findViewById(R.id.plant_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        plantGrid.setLayoutManager(gridLayoutManager);

        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run()
            {
                getPlantsFromDB(plantDB.dataAccessObject());
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> createPlantGrid(plantGrid));
            }
        });
        addPlant = (CardView) findViewById(R.id.addplant);
        addPlant.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PlantSearcher.class));
            }
        });

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.plants);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.spaces:
                        startActivity(new Intent(getApplicationContext(), Space_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.plants:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),Calendar.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run()
            {
                getPlantsFromDB(plantDB.dataAccessObject());
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> createPlantGrid(plantGrid));
            }
        });
    }

    private void getPlantsFromDB(DataAccessObject dataAccessObject)
    {
        plantList = dataAccessObject.loadAllPlants();
    }

    public void createPlantGrid(RecyclerView plantGrid)
    {
        //temp to set default image
        for (int i = 0; i < plantList.size(); i++) {
            plantList.get(i).setDefault_image(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.defaultimage));
        }

        PlantActivityCreatorAdapter plantAdapter = new PlantActivityCreatorAdapter(plantList,Plant_Activity.this);
        plantGrid.setAdapter(plantAdapter);
    }
}
