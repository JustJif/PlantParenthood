package com.example.plantparenthood;

import static com.example.plantparenthood.ComputeDate.computeDayMonth;
import static com.example.plantparenthood.ComputeDate.getDayOfTheYear;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class CalendarCreatorAdapter extends AbstractCreatorAdapter
{
    private List<Plant> plantsList;
    private Calendar_Activity calendar_activity;
    private Context whatContext;
    private Schedule schedule;
    public CalendarCreatorAdapter(List<Plant> newPlantsList, Calendar_Activity calendar_activity)
    {
        plantsList = newPlantsList;
        this.calendar_activity = calendar_activity;
        whatContext = calendar_activity;
        schedule = new Schedule();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_square_noimage,parent,false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(currentView) {};
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Plant thisPlant = plantsList.get(position);
        TextView plantCommonName = (TextView) holder.itemView.findViewById(R.id.plantCommonName);
        plantCommonName.setText(thisPlant.getCommon_name());
        holder.itemView.setOnClickListener(view -> setupPopup(view, thisPlant));
        TextView scheduled = (TextView) holder.itemView.findViewById(R.id.isScheduled);
        Watering water = thisPlant.getWateringCycle();
        if(water != null)
        {
            int nextDayToWater = computeNextWatering(water);
            String text = "Next watering in " + nextDayToWater;

            if(nextDayToWater != 1)
                text+= " days";
            else
                text+= " day";

            scheduled.setText(text);
            scheduled.setTextColor(0x995C5CFF);
        }
        else
        {
            scheduled.setText("No watering scheduled");
            scheduled.setTextColor(0x99AA4A44);
        }
    }

    @Override
    public int getItemCount()
    {
        return plantsList.size();
    }

    private void requestCalendarPermission() {
        if (ContextCompat.checkSelfPermission(whatContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // Request WRITE_CALENDAR permission
            ActivityCompat.requestPermissions((Calendar_Activity) whatContext, new String[]{Manifest.permission.WRITE_CALENDAR}, 100);
        } else {
            // Grant the app the grantUriPermission for CalendarProvider2
            whatContext.grantUriPermission("com.android.providers.calendar.CalendarProvider2",
                    Uri.parse("content://com.android.calendar/events"),
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Permission already granted, no need to request again
            //Toast.makeText(whatContext, "Calendar permission already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupPopup(View view, Plant thisPlant)
    {
        if(calendar_activity.getShowSchedule())
        {
            addExistingSchedulePopup(view,thisPlant);
        }
        else
        {
            addNewSchedulePopup(view,thisPlant);
        }
    }

    private void addNewSchedulePopup(View view, Plant thisPlant)
    {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.activity_calendar_popup_new_plant, null);

        requestCalendarPermission();
        PopupWindow newPopupWindow = new PopupWindow(newPopup, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        newPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        EditText userInput = newPopup.findViewById(R.id.wateringNumber);

        Button closeButton = newPopup.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPopupWindow.dismiss();
            }
        });

        Button addSchedule = newPopup.findViewById(R.id.addSchedule);
        addSchedule.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int wateringNumber = 0;
                try {
                    wateringNumber = Integer.parseInt(userInput.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(view.getContext(), "Please enter a valid watering number", Toast.LENGTH_SHORT).show();
                    return;
                }

                schedule.addPlantSchedule(thisPlant,wateringNumber);
                newPopupWindow.dismiss();
            }
        });
    }

    private void addExistingSchedulePopup(View view, Plant thisPlant)
    {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.activity_calendar_popup_existing_plant, null);

        Watering water = thisPlant.getWateringCycle();

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

        Button waterPlant = newPopup.findViewById(R.id.waterPlant);
        waterPlant.setOnClickListener(view1 ->
        {
            AsyncTask.execute(() ->
            {
                thisPlant.waterPlant(getDayOfTheYear());
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> calculateWateringCycle(newPopup,water));
            });
        });

        calculateWateringCycle(newPopup, water);
    }

    private int computeNextWatering(Watering water)
    {
        return (water.getLastWateredDay() - getDayOfTheYear()) + water.getWateringInterval();
    }

    private void calculateWateringCycle(View newPopup, Watering water)
    {
        if(water != null) {
            TextView lastWatered = newPopup.findViewById(R.id.lastWateringNum);
            lastWatered.setText(computeDayMonth(false, water.getLastWateredDay()));

            TextView nextDayToWater = newPopup.findViewById(R.id.nextWateringNumber);
            int nextWateringDay = water.getLastWateredDay() - water.getWateringInterval();
            nextDayToWater.setText(computeDayMonth(false, nextWateringDay));

            String wateringInt = "Every " + water.getWateringInterval() + (water.getWateringInterval() > 1 ? " days" : " day");
            TextView wateringInterval = newPopup.findViewById(R.id.wateringIntervalNumber);
            wateringInterval.setText(wateringInt);
        }
    }
}