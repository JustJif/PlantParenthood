package com.example.plantparenthood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class PlantActivityCreatorAdapter extends AbstractCreatorAdapter
{
    private List<Plant> plantsList;
    private Plant_Activity plant_activity;
    private Context whatContext;
    private ImageView plantImage;
    private ImageView qrImage;
    private DatabaseHandler databaseHandler;
    private boolean[] changes;
    private EditText[] textBoxes;
    private Bitmap newImage;
    private RecyclerView.ViewHolder holder;
    public PlantActivityCreatorAdapter(List<Plant> newPlantsList, Plant_Activity plant_activity)
    {
        plantsList = newPlantsList;
        this.plant_activity = plant_activity;
        whatContext = plant_activity;
        databaseHandler = DatabaseHandler.getDatabase(whatContext);
        changes = new boolean[3];
        textBoxes = new EditText[2];
        Arrays.fill(changes,false);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_display_square,parent,false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(currentView) {};
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Plant thisPlant = plantsList.get(position);
        TextView plantCommonName = (TextView) holder.itemView.findViewById(R.id.plantCommonName);
        plantImage = (ImageView) holder.itemView.findViewById(R.id.plantImage);

        plantCommonName.setText(thisPlant.getCommon_name());
        plantImage.setImageBitmap(thisPlant.getDefault_image());

        this.holder = holder;
        holder.itemView.setOnClickListener(view ->
        {
            new PlantInfoPopup(view,thisPlant,whatContext,holder,this,plant_activity);
        });

    }

    @Override
    public int getItemCount()
    {
        return plantsList.size();
    }
}