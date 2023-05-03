package com.example.plantparenthood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantparenthood.AbstractCreatorAdapter;
import com.example.plantparenthood.Plant;
import com.example.plantparenthood.R;
import com.example.plantparenthood.Space;
import com.example.plantparenthood.SpaceDataBaseHandler;
import com.example.plantparenthood.Space_Activity;

import java.util.Arrays;
import java.util.List;

public class InnerPlantRecyclerAdapter extends AbstractCreatorAdapter
{

    private Context whatContext;
    private List<Plant> plantList;

    private Space thisSpace;

    private SpaceActivityCreatorAdapter spaceActivity;
    public InnerPlantRecyclerAdapter(SpaceActivityCreatorAdapter activity, Space newSpace, List<Plant> newPlantList, Context context)
    {
        spaceActivity = activity;
        thisSpace = newSpace;
        plantList = newPlantList;
        whatContext = context;
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

        holder.itemView.setOnClickListener(view -> confirmPopup(view, newPlant));

    }
    private void confirmPopup(View view, Plant thisPlant) {
        new AlertDialog.Builder(whatContext)
                .setTitle("Add to this group?")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id)
                    {
                        thisSpace.addPlant(thisPlant);
                        spaceActivity.showAllPlants();
                        AsyncTask.execute(() -> SpaceDataBaseHandler.getDatabase(whatContext).addSpaceToDatabase(thisSpace));


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
