package com.example.plantparenthood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Layout;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpaceActivityCreatorAdapter extends AbstractCreatorAdapter
{
    private List<Space> spaceList;
    private Space_Activity space_activity;
    private Context whatContext;
    private SpaceDataBaseHandler spaceDatabaseHandler;
    private boolean[] changes;
    private EditText[] textBoxes;
    private Bitmap newImage;
    private RecyclerView.ViewHolder holder;
    private List<Plant> plantList;
    private boolean showPlantList = true;
    private RecyclerView displayAllPlants;

    private LayoutInflater layoutInflater;


    public SpaceActivityCreatorAdapter(List<Space> newSpaceList, Space_Activity space_activity)
    {

        RecyclerView displayAllPlants = null;
        spaceList = newSpaceList;
        this.space_activity = space_activity;
        whatContext = space_activity;
        spaceDatabaseHandler = SpaceDataBaseHandler.getDatabase(whatContext);

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
        Space thisSpace = spaceList.get(position);
        TextView spaceName = (TextView) holder.itemView.findViewById(R.id.spaceName);
        spaceName.setText(thisSpace.getSpaceName());
        this.holder = holder;

        holder.itemView.setOnClickListener(view -> setupPopup(view, thisSpace));
    }

    @Override
    public int getItemCount()
    {
        return spaceList.size();
    }

    private void setupPopup(View view, Space thisSpace) {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.activity_space_popup, null);
        displayAllPlants = newPopup.findViewById(R.id.plant_recycler_view);
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
        addPlantRecyclerView(newPopup);



    }

    private void createPlants() {
        InnerPlantRecyclerAdapter adapter = new InnerPlantRecyclerAdapter(this, spaceList.get(holder.getAdapterPosition()),plantList, whatContext);
        displayAllPlants.setAdapter(adapter);
    }

    private void addPlantRecyclerView(View newPopup) {

        showPlants();
        Button addPlant = newPopup.findViewById(R.id.addPlant);
        addPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlantList = !showPlantList;
                if(showPlantList) {
                    showPlants();
                } else {
                    showAllPlants();
                }
            }
        });
    }

    public void showAllPlants() {

        GridLayoutManager newGridLayoutManager = new GridLayoutManager(whatContext, 1 );
        displayAllPlants.setLayoutManager(newGridLayoutManager);
        ArrayList<Plant> notGrabbedPlants = spaceList.get(holder.getAdapterPosition()).getAllPlants();
        AsyncTask.execute(() -> {
            plantList = DatabaseHandler.getDatabase(whatContext).getPlantsFromDB();
            for(int i = 0; i < plantList.size(); i++) {
                for(int j = 0; j < notGrabbedPlants.size(); j++) {
                    if(plantList.get(i).getId() == notGrabbedPlants.get(j).getId()) {
                        plantList.remove(i);
                        break;
                    }
                }
            }
            Handler plantDisplayHandler = new Handler(Looper.getMainLooper());
            plantDisplayHandler.post(() -> createPlants());
        });
    }
    public void showPlants() {

        plantList = spaceList.get(holder.getAdapterPosition()).getAllPlants();
        GridLayoutManager newGridLayoutManager = new GridLayoutManager(whatContext, 1 );
        displayAllPlants.setLayoutManager(newGridLayoutManager);
        InnerPlantRecyclerAdapter adapter = new InnerPlantRecyclerAdapter(this,spaceList.get(holder.getAdapterPosition()),plantList, whatContext);
        displayAllPlants.setAdapter(adapter);
    }


}