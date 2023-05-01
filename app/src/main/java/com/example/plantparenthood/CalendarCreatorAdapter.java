package com.example.plantparenthood;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.TimeZone;
import android.net.Uri;
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

                System.out.println("New watering number is: " + wateringNumber);
                schedule.addPlantSchedule(thisPlant,wateringNumber);
                /*Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, wateringNumber);
                long startTime = calendar.getTimeInMillis();
                long endTime = startTime + 60 * 60 * 1000;

                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.TITLE, thisPlant.getCommon_name() + " needs to be watered");
                values.put(CalendarContract.Events.DESCRIPTION, "This plant needs to be watered.");
                values.put(CalendarContract.Events.EVENT_LOCATION, "Home");
                values.put(CalendarContract.Events.CALENDAR_ID, 1); // 1 is the default calendar ID
                values.put(CalendarContract.Events.DTSTART, startTime);
                values.put(CalendarContract.Events.DTEND, endTime);
                values.put(CalendarContract.Events.ALL_DAY, false);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

                ContentResolver contentResolver = view.getContext().getContentResolver();
                Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);

                // Display a toast message
                if (uri != null) {
                    Toast.makeText(view.getContext(), "Event added to calendar", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Failed to add event to calendar", Toast.LENGTH_SHORT).show();
                }*/

                newPopupWindow.dismiss();
            }
        });
    }

    private void addExistingSchedulePopup(View view, Plant thisPlant)
    {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.activity_calendar_popup_existing_plant, null);

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
    }
}