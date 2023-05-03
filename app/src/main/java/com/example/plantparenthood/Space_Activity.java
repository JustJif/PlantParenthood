package com.example.plantparenthood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Space_Activity extends AppCompatActivity {
    CardView addSpace;
    public static SpaceDataAccessObject dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spaces);

        PlantDatabase plantDatabase = Room.databaseBuilder(getApplicationContext(), PlantDatabase.class, "PlantDatabase").build();
        dataBase = plantDatabase.spaceDataAccessObject();

        dataBase.loadAllSpaces();

        addSpace = (CardView) findViewById(R.id.addspace);
        addSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SpacePopup.class));
            }
        });
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.spaces);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.spaces:
                        return true;
                    case R.id.plants:
                        startActivity(new Intent(getApplicationContext(), Plant_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(), QRScannerMenuActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.scanner:
                        startActivity(new Intent(getApplicationContext(), Space_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}
