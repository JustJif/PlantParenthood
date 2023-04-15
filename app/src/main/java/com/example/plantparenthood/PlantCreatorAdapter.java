package com.example.plantparenthood;

import android.content.Context;
import android.content.Intent;
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
    private Context whatContext;
    public PlantCreatorAdapter(ArrayList<Plant> newPlantsList, Context newContext)
    {
        plantsList = newPlantsList;
        whatContext = newContext;
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

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent currentActivity = new Intent(whatContext, PlantPopup.class);
                currentActivity.putExtra("image", thisPlant.default_image);
                whatContext.startActivity(currentActivity);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return plantsList.size();
    }

}
