package com.example.plantparenthood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Space_Activity extends AppCompatActivity {
    CardView addSpace;
    private SpaceDataBaseHandler spaceHandler;
    public List<Space> spaceList;

    private RecyclerView spaceGrid;

    private SpaceActivityCreatorAdapter spaceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spaces);
        spaceList = new ArrayList<>();
        spaceHandler = SpaceDataBaseHandler.getDatabase(getApplicationContext());

        spaceGrid = findViewById(R.id.spaces_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        spaceGrid.setLayoutManager(gridLayoutManager);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                spaceList = spaceHandler.getSpacesFromDB();
                System.out.println(spaceList.size());
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> createSpaceGrid(spaceGrid));
            }
        });


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
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Space_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    public void createSpaceGrid(RecyclerView spaceGrid) {
        spaceAdapter = new SpaceActivityCreatorAdapter(spaceList, this);
        spaceGrid.setAdapter(spaceAdapter);
    }
}
