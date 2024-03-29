package com.example.plantparenthood;

import static com.example.plantparenthood.ComputeDate.Month;
import static com.example.plantparenthood.ComputeDate.computeDayOfYear;
import static com.example.plantparenthood.ComputeDate.getDayOfTheYear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Calendar_Activity extends AppCompatActivity
{
    private CalendarView simpleCalendarView;
    private DatabaseHandler plantDatabase;
    private RecyclerView plantGrid;
    private CalendarCreatorAdapter creatorAdapter;
    private boolean showSchedule;
    private List<Plant> plantList;
    private int selectedDate;
    private PlantController plantController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        plantDatabase = DatabaseHandler.getDatabase(getApplicationContext());

        plantGrid = findViewById(R.id.plantGrid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        plantGrid.setLayoutManager(gridLayoutManager);
        showSchedule = true;

        plantController = new PlantController();
        plantController.setSchedule(new Schedule());
        selectedDate = getDayOfTheYear();
        if (showSchedule)
        {
            checkListOfValidPlants();
        }
        else {
            AsyncTask.execute(() -> {
                plantList = plantDatabase.getPlantsFromDB();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> createPlantGrid(plantGrid, plantList));
            });
        }

        simpleCalendarView = findViewById(R.id.calendarView);
        TextView scheduleTask = findViewById(R.id.scheduleTask);
        Button addWateringSchedule = findViewById(R.id.addWateringScheduleButton);

        addWateringSchedule.setOnClickListener(view ->
        {
            showSchedule = !showSchedule;
            if (showSchedule){
                addWateringSchedule.setText("add watering schedule");
                scheduleTask.setText("Scheduled plants to be watered on this day");
                checkListOfValidPlants();
            }
            else {
                addWateringSchedule.setText("show watering schedules");
                scheduleTask.setText("Select a plant to schedule a watering cycle");
                AsyncTask.execute(() ->
                {
                    plantList = plantDatabase.getPlantsFromDB();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> createPlantGrid(plantGrid, plantList));
                });
            }

        });

        simpleCalendarView.setOnDateChangeListener((calendarView, year, month, day) ->
        {
            boolean isLeapYear = false;
            if((year % 4) == 0)
                isLeapYear = true;

            selectedDate = computeDayOfYear(isLeapYear,Month.getValue(month-1),day);
            showSchedule = true;
            addWateringSchedule.setText("add watering schedule");
            scheduleTask.setText("Scheduled plants to be watered on this day");
            checkListOfValidPlants();
        });

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.calendar);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.Groups:
                        startActivity(new Intent(getApplicationContext(), Group_Activity.class));
                        overridePendingTransition(0,0);
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
                        return true;
                    case R.id.scanner:
                        startActivity(new Intent(getApplicationContext(), QRScannerMenuActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        // highlight the start date on the calendar view
        simpleCalendarView.setDate(new Date().getTime());
    }

    public void refreshPlantGrid()
    {
        createPlantGrid(plantGrid,findScheduledPlants());
    }
    private void createPlantGrid(RecyclerView plantGrid, List<Plant> whatPlantsToDisplay) {
        creatorAdapter = new CalendarCreatorAdapter(whatPlantsToDisplay, this, plantController);
        plantGrid.setAdapter(creatorAdapter);
    }

    private List<Plant> findScheduledPlants()
    {
        List<Plant> currentPlants = plantController.findScheduledPlantsForToday(plantList,selectedDate);
        return currentPlants;
    }

    public boolean getShowSchedule()
    {
        return showSchedule;
    }

    public void checkListOfValidPlants()
    {
        AsyncTask.execute(() ->
        {
            plantList = plantDatabase.getPlantsFromDB();
            Handler handler = new Handler(Looper.getMainLooper());
            List<Plant> todaysPlants = findScheduledPlants();
            handler.post(() -> createPlantGrid(plantGrid,todaysPlants));
        });
    }
}






