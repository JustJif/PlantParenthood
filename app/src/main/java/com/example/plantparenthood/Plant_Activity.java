package com.example.plantparenthood;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Plant_Activity extends AppCompatActivity {
    private Button addPlant;
    private List<Plant> plantList;
    private RecyclerView plantGrid;
    private DatabaseHandler plantDatabase;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    private ActivityResultLauncher<Intent> camera;
    private PlantActivityCreatorAdapter plantAdapter;
    private static final int PICK_IMAGE_REQUEST = 100;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);

        plantList = new ArrayList<>();
        plantDatabase = DatabaseHandler.getDatabase(getApplicationContext());

        plantGrid = findViewById(R.id.plant_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        plantGrid.setLayoutManager(gridLayoutManager);

        StatisticsDatabaseHandler.getDatabase(getApplicationContext());

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
                startActivity(new Intent(getApplicationContext(), PlantSearcherActivity.class));
                newPopupWindow.dismiss();
            }
        });
        Button addCustom = newPopup.findViewById(R.id.addCustomPlant);
        addCustom.setOnClickListener(new View.OnClickListener() {
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

        ImageView editCamera = newPopup.findViewById(R.id.editCamera);
        editCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();

            }
        });
        image = (ImageView) newPopup.findViewById(R.id.plantImage);


        Button submit = newPopup.findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlantCreator newPlantCreator = new PlantCreator();
                newPlantCreator.addCustomPlant(getApplicationContext(),plantName.getText().toString(),imageToStore,plantSciName.getText().toString());
                newPopupWindow.dismiss();
            }
        });
    }

    public void chooseImage() {

        Intent objectIntent = new Intent();
        objectIntent.setType("image/*");
        objectIntent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(objectIntent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data != null && data.getData() != null) {
                {
                    imageFilePath = data.getData();
                    imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);

                    image.setImageBitmap(imageToStore);
                }
            }
        }
        catch(Exception e) {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }

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



    public void notifyGridOfUpdate(int position)
    {
        plantList.remove(position);
        plantAdapter.notifyItemChanged(position);
    }
}
