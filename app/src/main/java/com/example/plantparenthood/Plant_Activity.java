package com.example.plantparenthood;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;
import static androidx.core.content.ContextCompat.startActivity;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Plant_Activity extends AppCompatActivity {
    private Button addPlant;
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
        addPlant = (Button) findViewById(R.id.addplant);
        addPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupPopup(v);
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
                    case R.id.Groups:
                        startActivity(new Intent(getApplicationContext(), Group_Activity.class));
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
                    case R.id.scanner:
                        startActivity(new Intent(getApplicationContext(), QRScannerMenuActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void setupPopup(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.custom_plant_popup, null);
        PopupWindow newPopupWindow = new PopupWindow(newPopup, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        newPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button closeButton = newPopup.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPopupWindow.dismiss();
            }
        });

        Button addPlantFromDB = newPopup.findViewById(R.id.addPlantFromDB);
        addPlantFromDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PlantSearcher.class));
                newPopupWindow.dismiss();
            }
        });
        Button addCustomPlant = newPopup.findViewById(R.id.addCustomPlant);
        addCustomPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupInnerPopup(view);
                newPopupWindow.dismiss();
            }
        });
    }
    private void setupInnerPopup(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.custom_plant_create_popup, null);

        PopupWindow newPopupWindow = new PopupWindow(newPopup, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        newPopupWindow.showAtLocation(newPopup, Gravity.CENTER, 0, 0);

        EditText plantName = newPopup.findViewById(R.id.plantCommonName);
        EditText plantSciName = newPopup.findViewById(R.id.plantScientificName);

        Button closeButton = newPopup.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPopupWindow.dismiss();
            }
        });

        Button submit = newPopup.findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlantCreator newPlantCreator = new PlantCreator();
                newPlantCreator.addCustomPlant(getApplicationContext(),plantName.getText().toString(),plantSciName.getText().toString());
                newPopupWindow.dismiss();
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
