package com.example.plantparenthood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlantCreatorAdapter extends RecyclerView.Adapter
{
    private ArrayList<Plant> plantsList;

    public PlantCreatorAdapter(ArrayList<Plant> newPlantsList)
    {
        plantsList = newPlantsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.plantsquare,parent,false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(currentView) {};
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Plant thisPlant = plantsList.get(position);
        TextView plantCommonName = (TextView) holder.itemView.findViewById(R.id.plantCommonName);
        ImageView plantImage = (ImageView) holder.itemView.findViewById(R.id.plantImage);
        TextView plantOtherNames = (TextView) holder.itemView.findViewById(R.id.plantScientificNames);

        plantCommonName.setText(thisPlant.common_name);
        plantImage.setImageBitmap(thisPlant.default_image);
        plantOtherNames.setText(thisPlant.scientific_name[0]);
    }

    @Override
    public int getItemCount()
    {
        return plantsList.size();
    }
}
