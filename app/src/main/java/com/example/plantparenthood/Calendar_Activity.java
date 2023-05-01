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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Calendar_Activity extends AppCompatActivity
{
    private CalendarView simpleCalendarView;
    private DatabaseHandler plantDatabase;
    private RecyclerView plantGrid;
    private CalendarCreatorAdapter creatorAdapter;
    private boolean showSchedule = true;
    private List<Plant> plantList;
    private Schedule schedule;
    private int selectedDate;
    public enum Month
    {
        January,
        February,
        March,
        April,
        May,
        June,
        July,
        August,
        September,
        October,
        November,
        December;

        public static Month getValue(int month)
        {
            Month[] months = Month.values();
            Month currentMonth = null;
            for (int i = 0; i < months.length; i++)
            {
                if(months[i].ordinal() == month)
                {
                    currentMonth = months[i];
                    break;
                }
            }
            return currentMonth;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        plantDatabase = DatabaseHandler.getDatabase(getApplicationContext());

        plantGrid = findViewById(R.id.plantGrid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        plantGrid.setLayoutManager(gridLayoutManager);

        schedule = new Schedule();
        selectedDate = getDayOfTheYear();
        if (showSchedule)
        {
            checkListOfValidPlants();
        }
        else {
            //check for all the plants that need to be watered today
            //remove all other plants
            //TODO
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
        simpleCalendarView.setDate(new Date().getTime());
    }
    private void createPlantGrid(RecyclerView plantGrid, List<Plant> whatPlantsToDisply) {
        creatorAdapter = new CalendarCreatorAdapter(whatPlantsToDisply, this);
        plantGrid.setAdapter(creatorAdapter);
    }

    private List<Plant> findScheduledPlants()
    {
        List<Plant> currentPlants = schedule.findScheduledPlantsForToday(plantList,selectedDate);
        return currentPlants;
    }

    public boolean getShowSchedule()
    {
        return showSchedule;
    }

    private int getDayOfTheYear()
    {
        int currentDay = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            currentDay = LocalDateTime.now().getDayOfYear();
        }

        return  currentDay;
    }

    private int computeDayOfYear(boolean isLeapYear, Month month, int day)
    {
        int dayOfYear = day;

        //december not included as its one month less
        switch (month) {
            case January:
                dayOfYear+=31;
                break;
            case February:
                dayOfYear+=59;
                break;
            case March:
                dayOfYear+=90;
                break;
            case April:
                dayOfYear+=120;
                break;
            case May:
                dayOfYear+=151;
                break;
            case June:
                dayOfYear+=181;
                break;
            case July:
                dayOfYear+=212;
                break;
            case August:
                dayOfYear+=243;
                break;
            case September:
                dayOfYear+=273;
                break;
            case October:
                dayOfYear+=304;
                break;
            case November:
                dayOfYear+=334;
                break;
            default:
                break;
        }

        if(isLeapYear && month.ordinal()-1 > Month.February.ordinal())
            dayOfYear++;

        return dayOfYear;
    }

    private void checkListOfValidPlants()
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






