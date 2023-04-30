package com.example.plantparenthood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

import android.provider.CalendarContract.Events;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CalendarCreatorAdapter extends AbstractCreatorAdapter
{
    private List<Plant> plantsList;
    private Calendar_Activity calendar_activity;
    private Context whatContext;
    private DatabaseHandler databaseHandler;
    private boolean[] changes;
    public CalendarCreatorAdapter(List<Plant> newPlantsList, Calendar_Activity calendar_activity)
    {
        plantsList = newPlantsList;
        this.calendar_activity = calendar_activity;
        whatContext = calendar_activity;
        databaseHandler = DatabaseHandler.getDatabase(whatContext);
        changes = new boolean[9];
        Arrays.fill(changes,false);
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

    private void setupPopup(View view, Plant thisPlant) {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.activity_calendar_popup, null);
        //valid user input using user plant


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

                // use the wateringNumber variable here




        new AlertDialog.Builder(whatContext)
                        .setTitle("Confirm changes")
                        .setMessage("Changes will override previous information, this cannot be undone.")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                // TODO: Use the wateringNumber to set the watering frequency for the plant

                                Calendar calendar = Calendar.getInstance();
                                int wateringNumber = Integer.parseInt(userInput.getText().toString());
                                calendar.add(Calendar.DATE, wateringNumber);
                                Intent intent = new Intent(Intent.ACTION_INSERT);
                                intent.setType("vnd.android.cursor.item/event");
                                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis());
                                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar.getTimeInMillis() + 60 * 60 * 1000);
                                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                                intent.putExtra(Events.TITLE, thisPlant.getCommon_name() + " needs to be watered");
                                intent.putExtra(Events.DESCRIPTION, "This plant needs to be watered.");
                                whatContext.startActivity(intent);
                                Toast.makeText(view.getContext(), "Applied changes", Toast.LENGTH_SHORT).show();
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



/*
        private void modifyText(TextView editableText, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) whatContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(!editableText.isEnabled())
        {
            editableText.setEnabled(true);
            editableText.requestFocus();
            inputMethodManager.showSoftInput(editableText, 1);
        }
        else
        {
            editableText.setEnabled(false);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }*/
    }


}