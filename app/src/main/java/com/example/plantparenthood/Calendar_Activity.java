package com.example.plantparenthood;

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
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Calendar_Activity extends AppCompatActivity {

    private CalendarView simpleCalendarView;
    private DatabaseHandler plantDatabase;
    private RecyclerView plantGrid;
    private CalendarCreatorAdapter plantAdapter;

    private boolean showSchedule = true;

    private List<Plant> plantList;


    private TextView monthYearText;
    private GridView calendarGridView;
    private CalendarCreatorAdapter calendarAdapter;
    private Button addPlantButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        plantDatabase = DatabaseHandler.getDatabase(getApplicationContext());




        plantGrid = findViewById(R.id.plantGrid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        plantGrid.setLayoutManager(gridLayoutManager);

        if (showSchedule){
            AsyncTask.execute(new Runnable() {
            @Override
                public void run() {
                    plantList = plantDatabase.getPlantsFromDB();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> createPlantGrid(plantGrid));
                }
            });
        }
        else {
            AsyncTask.execute(new Runnable() {
                @Override
                //check for all the plants that need to be watered today
                //remove all other plants
                //TODO
                public void run() {
                    plantList = plantDatabase.getPlantsFromDB();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> createPlantGrid(plantGrid));
                }
            });
        }




        simpleCalendarView = (CalendarView) findViewById(R.id.calendarView);

        Button addWateringSchedule = findViewById(R.id.addWateringScheduleButton);
        //Button updateWateringSchedule = findViewById(R.id.updateSchedule);
        Button deleteSchedule = findViewById(R.id.deleteSchedule);
        addWateringSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showSchedule = !showSchedule;
                if (showSchedule){
                    addWateringSchedule.setText("add watering schedule");
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            plantList = plantDatabase.getPlantsFromDB();
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(() -> createPlantGrid(plantGrid));
                        }
                    });
                }
                else {
                    addWateringSchedule.setText("show watering schedules");
                    AsyncTask.execute(new Runnable() {
                        @Override
                        //check for all the plants that need to be watered today
                        //remove all other plants
                        //TODO
                        // basically make an if else statement checking if a plant needs to be wwatered
                        //maybe a for loop cheking through ever plant if it is in the thing if so it is displayed
                        //the other one needs to take the amount of water and post an event for the plant to be watered
                        //this one also needs to take the event and take it off of the event and marked as 'watered'
                        public void run() {
                            plantList = plantDatabase.getPlantsFromDB();
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(() -> createPlantGrid(plantGrid));
                        }
                    });
                }



                /*int interval = 1; // for daily watering
                Date startDate = new Date(); // current date
                Date endDate = null; // null for ongoing schedule
                Schedule schedule = new Schedule(interval, startDate, endDate);

                // Call the addPlantSchedule method to add the schedule to the plant
                //schedule.addPlantSchedule(Calendar.this, );
                Toast.makeText(Calendar_Activity.this, "Watering schedule added", Toast.LENGTH_SHORT).show();*/
            }


        });



        deleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day)
            {
                System.out.println("Year is: " + year + " Month is: " + month + " Day is: " + day);
            }
        });


        // get the start date for the watering schedule
        long startDate = new Date().getTime();

        int currentDay = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDay = LocalDateTime.now().getDayOfYear();
        }

        System.out.println(currentDay);
        // create a new schedule for the plant with interval of 7 days
        //Schedule plantSchedule = new Schedule(7, startDate, startDate);

        // add the plant watering schedule to the calendar





/*
        updateWateringSchedule.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Schedule schedule = new Schedule(1, new Date(), null);
                schedule.updatePlantSchedule(CalendarActivity.this, currentPlant);
                Toast.makeText(CalendarActivity.this, "Watering schedule updated", Toast.LENGTH_SHORT).show();
            }
        });

        deleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Schedule schedule = new Schedule(1, new Date(), null);
                schedule.deletePlantSchedule(CalendarActivity.this, currentPlant);
                Toast.makeText(CalendarActivity.this, "Watering schedule deleted", Toast.LENGTH_SHORT).show();
            }
        });

        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day)
            {
                // Get the plant that was watered on this day
                currentPlant = Plant.getPlantByWateringDate(CalendarActivity.this, year, month, day);
                if (currentPlant != null) {
                    // Display the plant name on the button
                    addWateringSchedule.setText("Mark " + currentPlant.getCommon_name() + " as watered");
                    updateWateringSchedule.setVisibility(View.VISIBLE);
                    deleteSchedule.setVisibility(View.VISIBLE);
                } else {
                    // Hide the buttons
                    addWateringSchedule.setText("Add watering schedule");
                    updateWateringSchedule.setVisibility(View.GONE);
                    deleteSchedule.setVisibility(View.GONE);
                }
            }
        });
    }

}
 */

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
                    case R.id.spaces:
                        startActivity(new Intent(getApplicationContext(), Space_Activity.class));
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
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        // highlight the start date on the calendar view
        simpleCalendarView.setDate(startDate);




    }
    public void createPlantGrid(RecyclerView plantGrid) {
        plantAdapter = new CalendarCreatorAdapter(plantList, this);
        plantGrid.setAdapter(plantAdapter);
    }

}






