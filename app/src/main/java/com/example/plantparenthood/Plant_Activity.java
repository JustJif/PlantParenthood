package com.example.plantparenthood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Plant_Activity extends AppCompatActivity {
    CardView addPlant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);
        createPlantGrid();
        addPlant = (CardView) findViewById(R.id.addplant);
        addPlant.setOnClickListener(new View.OnClickListener() {
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
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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

    private ArrayList<Plant> getPlantsFromDB() {
        ArrayList<Plant> plantList = new ArrayList<Plant>();
        for(int i = 0; i < 5; i++) {
            Plant plantToAdd = new Plant();
            plantToAdd.common_name = "BlahBlahBlahBlah";
            plantToAdd.scientific_name = new String[1];
            plantToAdd.scientific_name[0] ="BlehBlehBlehBleh";
            plantList.add(plantToAdd);
        }
        return plantList;
    }

    public void createPlantGrid()
    {
        ArrayList<Plant> dataBasePlants = getPlantsFromDB();

        RecyclerView plantGrid = findViewById(R.id.plant_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        plantGrid.setLayoutManager(gridLayoutManager);
        PlantActivityCreatorAdapter plantAdapter = new PlantActivityCreatorAdapter(dataBasePlants,Plant_Activity.this);
        plantGrid.setAdapter(plantAdapter);

    }
}
