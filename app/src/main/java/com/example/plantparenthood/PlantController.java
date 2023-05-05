package com.example.plantparenthood;

import android.content.Context;
import java.util.List;

public class PlantController
{
    //Logic classes
    private PlantCreator plantCreator;
    private Perenual perenual;
    private PlantSearcher plantSearcher;
    private Schedule schedule;

    //UI Classes
    private Calendar_Activity calendar_activity;
    private CalendarCreatorAdapter calendarCreatorAdapter;

    public PlantController() {}

    public void addPlantSchedule(Plant plant, int wateringInterval)
    {
        schedule.addPlantSchedule(plant,wateringInterval);
    }

    public void removePlantSchedule(Watering water)
    {
        schedule.deletePlantSchedule(water);
    }

    public void updatePlantSchedule(Plant thisPlant, int newValue)
    {
        schedule.updatePlantSchedule(thisPlant,newValue);
    }

    public List<Plant> findScheduledPlantsForToday(List<Plant> listOfPlants, int dateOnCalender)
    {
        return schedule.findScheduledPlantsForToday(listOfPlants,dateOnCalender);
    }

    public void setPlantCreator(PlantCreator plantCreator) {
        this.plantCreator = plantCreator;
    }

    public void setPerenual(Perenual perenual) {
        this.perenual = perenual;
    }

    public void setPlantSearcher(PlantSearcher plantSearcher) {
        this.plantSearcher = plantSearcher;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setCalendar_activity(Calendar_Activity calendar_activity) {
        this.calendar_activity = calendar_activity;
    }

    public void setCalendarCreatorAdapter(CalendarCreatorAdapter calendarCreatorAdapter) {
        this.calendarCreatorAdapter = calendarCreatorAdapter;
    }
}
