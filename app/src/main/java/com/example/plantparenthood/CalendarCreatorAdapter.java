package com.example.plantparenthood;

import static com.example.plantparenthood.ComputeDate.computeDayMonth;
import static com.example.plantparenthood.ComputeDate.getDayOfTheYear;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CalendarCreatorAdapter extends AbstractCreatorAdapter
{
    private List<Plant> plantsList;
    private Calendar_Activity calendar_activity;
    private Context whatContext;
    private RecyclerView.ViewHolder newHolder;
    private PlantController plantController;

    public CalendarCreatorAdapter(List<Plant> newPlantsList, Calendar_Activity calendar_activity, PlantController plantController)
    {
        plantsList = newPlantsList;
        this.calendar_activity = calendar_activity;
        whatContext = calendar_activity;
        this.plantController = plantController;
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

        newHolder = holder;

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

                plantController.addPlantSchedule(thisPlant,wateringNumber);
                Toast.makeText(view.getContext(), "Added new watering Schedule", Toast.LENGTH_SHORT).show();
                calendar_activity.checkListOfValidPlants();
                scheduleWateringNotification(thisPlant.getId(), getDayOfTheYear() + wateringNumber, view.getContext());
                newPopupWindow.dismiss();
            }
        });
    }
    private void scheduleWateringNotification(int plantID, int notifDate, Context context){

        AsyncTask.execute(() ->
        {
            //find plant from DB
            DatabaseHandler db = DatabaseHandler.getDatabase(context);
            Plant plant = db.getPlantFromDBbyID(plantID);

            //create the water notification
            PPMobileNotificationFactory ppFact = new PPMobileNotificationFactory();
            Notification waterNoti = ppFact.createWaterNotification(plantID, plant.getCommon_name(), context);

            //create new intent for when the notification is clicked
            Intent intent = new Intent(context, PPMobileWaterNotificationReceiver.class);
            intent.putExtra("plantID", plantID);
            intent.putExtra("plantName", plant.getCommon_name());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, plantID, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            //get alarm time
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, notifDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            long alarmTime = calendar.getTimeInMillis();
            int currDay = getDayOfTheYear();
            int delay = notifDate - currDay;

            if(delay > 0) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
                waterNoti.contentIntent = pendingIntent;
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
                handler.post(() ->
                {
                    calculateWateringCycle(newPopup,water, -1);
                    calendar_activity.refreshPlantGrid();
                });
            });
        });

        Button updateSchedule = newPopup.findViewById(R.id.updateSchedule);
        updateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(view.getContext(), "Applied changes", Toast.LENGTH_SHORT).show();
                EditText text = newPopup.findViewById(R.id.newIntervalValue);
                int value = Integer.parseInt(text.getText().toString());
                //thisPlant.getWateringCycle().setWateringInterval(value);

                AsyncTask.execute(() ->
                {
                    plantController.updatePlantSchedule(thisPlant,value);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> calendar_activity.refreshPlantGrid());
                });
            }
        });

        Button deleteSchedule = newPopup.findViewById(R.id.removeSchedule);
        deleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new AlertDialog.Builder(whatContext)
                        .setTitle("Confirm deletion")
                        .setMessage("This will remove the watering schedule, this cannot be undone.")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id)
                            {
                                AsyncTask.execute(() -> {
                                    plantController.removePlantSchedule(water);
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(() ->
                                    {
                                        Toast.makeText(view.getContext(), "Applied changes", Toast.LENGTH_SHORT).show();
                                        calendar_activity.checkListOfValidPlants();
                                    });
                                });

                                newPopupWindow.dismiss();
                            }
                        })
                        .setNegativeButton("Revert", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                Toast.makeText(view.getContext(), "Reverted changes", Toast.LENGTH_SHORT).show();
                                newPopupWindow.dismiss();
                            }
                        })
                        .show();
            }
        });

        ImageView edit = newPopup.findViewById(R.id.editWater);
        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                InputMethodManager inputMethodManager = (InputMethodManager) whatContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                TextView wateringInterval = newPopup.findViewById(R.id.wateringIntervalNumber);
                EditText newInterval = newPopup.findViewById(R.id.newIntervalValue);

                if(wateringInterval.getVisibility() == View.VISIBLE)
                {
                    wateringInterval.setVisibility(View.INVISIBLE);
                    newInterval.setVisibility(View.VISIBLE);
                    newInterval.requestFocus();
                    inputMethodManager.showSoftInput(newInterval, 1);
                }
                else
                {
                    newInterval.setVisibility(View.INVISIBLE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    wateringInterval.setVisibility(View.VISIBLE);
                    calculateWateringCycle(newPopup,water, Integer.parseInt(newInterval.getText().toString()));
                }
            }
        });


        calculateWateringCycle(newPopup, water, -1);
    }

    private int computeNextWatering(Watering water)
    {
        return (water.getLastWateredDay() - getDayOfTheYear()) + water.getWateringInterval();
    }

    private void calculateWateringCycle(View newPopup, Watering water, int newInterval)
    {
        if(water != null)
        {
            int interval = newInterval == -1 ? water.getWateringInterval() : newInterval;
            TextView lastWatered = newPopup.findViewById(R.id.lastWateringNum);
            lastWatered.setText(computeDayMonth(false, water.getLastWateredDay()));

            TextView nextDayToWater = newPopup.findViewById(R.id.nextWateringNumber);
            int nextWateringDay = water.getLastWateredDay() + interval;
            nextDayToWater.setText(computeDayMonth(false, nextWateringDay));
            String wateringInt = "";

            wateringInt = "Every " + interval + (interval > 1 ? " days" : " day");

            TextView wateringInterval = newPopup.findViewById(R.id.wateringIntervalNumber);

            wateringInterval.setText(wateringInt);
        }
    }
}