package com.example.plantparenthood;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Plant_Activity extends AppCompatActivity {
    private CardView addPlant;
    private List<Plant> plantList;
    private RecyclerView plantGrid;
    private DatabaseHandler plantDatabase;
    private ActivityResultLauncher<Intent> camera;
    private PlantActivityCreatorAdapter plantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);
        plantList = new ArrayList<>();
        plantDatabase = DatabaseHandler.getDatabase(getApplicationContext());

        plantGrid = findViewById(R.id.plant_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        plantGrid.setLayoutManager(gridLayoutManager);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                plantList = plantDatabase.getPlantsFromDB();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> createPlantGrid(plantGrid));
            }
        });
        addPlant = (CardView) findViewById(R.id.addplant);
        addPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PlantSearcher.class));
            }
        });

        camera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>()
                {
                    @Override
                    public void onActivityResult(ActivityResult result)
                    {
                        if (result.getResultCode() == RESULT_OK)
                        {
                            System.out.println("Image found");
                            Bitmap image = (Bitmap) result.getData().getExtras().get("data");
                            //plantAdapter.setCameraPreview(image);
                        }
                    }
                }
        );

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.plants);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.spaces:
                        startActivity(new Intent(getApplicationContext(), Space_Activity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.plants:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(), Calendar_Activity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                plantList = plantDatabase.getPlantsFromDB();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> createPlantGrid(plantGrid));
            }
        });
    }

    public void createPlantGrid(RecyclerView plantGrid) {
        plantAdapter = new PlantActivityCreatorAdapter(plantList, this);
        plantGrid.setAdapter(plantAdapter);
    }

    public void openCamera()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
        else
        {
            Intent cameraInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camera.launch(cameraInt);
        }
    }

    public void notifyGridOfUpdate(int position)
    {
        plantAdapter.notifyItemChanged(position);
    }
}
