package com.example.plantparenthood;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class PlantActivityCreatorAdapter extends AbstractCreatorAdapter
{
    private List<Plant> plantsList;
    private final Plant_Activity plant_activity;
    private Context whatContext;
    private ImageView plantImage;
    private DatabaseHandler databaseHandler;
    private boolean[] changes;
    private RecyclerView.ViewHolder holder;
    public PlantActivityCreatorAdapter(List<Plant> newPlantsList, Plant_Activity plant_activity)
    {
        plantsList = newPlantsList;
        this.plant_activity = plant_activity;
        whatContext = plant_activity;
        databaseHandler = DatabaseHandler.getDatabase(whatContext);
        changes = new boolean[3];
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
        //plant_activity.chooseImage();

        this.holder = holder;
        holder.itemView.setOnClickListener(view ->
        {
            new PlantInfoPopup(view,thisPlant,whatContext,holder,this,plant_activity);
        });
    }
    public void setPreview(Bitmap image)
    {
        if(image != null) {
            plantImage.setImageBitmap(image);
        }
    }
    @Override
    public int getItemCount()
    {
        return plantsList.size();
    }
}