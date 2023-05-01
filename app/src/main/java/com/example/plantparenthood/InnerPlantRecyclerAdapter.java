package com.example.plantparenthood;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    public InnerPlantRecyclerAdapter(List<Plant> newPlantList, Context context)
    {
        plantList = newPlantList;
        whatContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.space_square,parent,false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(currentView) {};
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return plantList.size();
    }
}
