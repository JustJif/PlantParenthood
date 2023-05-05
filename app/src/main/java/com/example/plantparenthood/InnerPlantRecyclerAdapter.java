package com.example.plantparenthood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InnerPlantRecyclerAdapter extends AbstractCreatorAdapter
{

    private Context whatContext;
    private List<Plant> plantList;

    private Group thisGroup;

    private boolean delete;

    private GroupActivityCreatorAdapter GroupActivity;
    public InnerPlantRecyclerAdapter(GroupActivityCreatorAdapter activity, Group newGroup, List<Plant> newPlantList, Context context, boolean delete)
    {
        GroupActivity = activity;
        thisGroup = newGroup;
        plantList = newPlantList;
        whatContext = context;
        this.delete = delete;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_square,parent,false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(currentView) {};
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Plant newPlant = plantList.get(position);
        System.out.println(newPlant.getCommon_name());
        TextView name = holder.itemView.findViewById(R.id.plantCommonName);
        name.setText(newPlant.getCommon_name());

        ImageView plantImage = (ImageView) holder.itemView.findViewById(R.id.plantImage);
        plantImage.setImageBitmap(newPlant.getDefault_image());
        if(delete) {
            holder.itemView.setOnClickListener(view -> confirmDeletePopup(view, newPlant));
        } else {
            holder.itemView.setOnClickListener(view -> confirmPopup(view, newPlant));
        }


    }

    private void confirmDeletePopup(View view, Plant thisPlant) {
        new AlertDialog.Builder(whatContext)
                .setTitle("Delete this from group?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id)
                    {
                        thisGroup.removePlant(thisPlant);
                        GroupActivity.showPlants();

                        AsyncTask.execute(() -> GroupDataBaseHandler.getDatabase(whatContext).addGroupToDatabase(thisGroup));


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                    }
                })
                .show();
    }
    private void confirmPopup(View view, Plant thisPlant) {
        new AlertDialog.Builder(whatContext)
                .setTitle("Add to this group?")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id)
                    {
                        thisGroup.addPlant(thisPlant);
                            GroupActivity.showPlants();

                        AsyncTask.execute(() -> GroupDataBaseHandler.getDatabase(whatContext).addGroupToDatabase(thisGroup));


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                    }
                })
                .show();
    }

    @Override
    public int getItemCount()
    {
        return plantList.size();
    }
}
