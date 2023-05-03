package com.example.plantparenthood;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GroupActivityCreatorAdapter extends AbstractCreatorAdapter
{
    private List<Group> groupList;
    private Group_Activity group_activity;
    private Context whatContext;
    private GroupDataBaseHandler groupDatabaseHandler;
    private boolean[] changes;
    private EditText[] textBoxes;
    private Bitmap newImage;
    private RecyclerView.ViewHolder holder;
    private List<Plant> plantList;
    private boolean showPlantList = true;
    private RecyclerView displayAllPlants;

    private LayoutInflater layoutInflater;


    public GroupActivityCreatorAdapter(List<Group> newGroupList, Group_Activity group_activity)
    {

        RecyclerView displayAllPlants = null;
        groupList = newGroupList;
        this.group_activity = group_activity;
        whatContext = group_activity;
        groupDatabaseHandler = GroupDataBaseHandler.getDatabase(whatContext);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_square,parent,false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(currentView) {};
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Group thisGroup = groupList.get(position);
        TextView GroupName = (TextView) holder.itemView.findViewById(R.id.GroupName);
        GroupName.setText(thisGroup.getGroupName());
        this.holder = holder;

        holder.itemView.setOnClickListener(view -> setupPopup(view, thisGroup));
    }

    @Override
    public int getItemCount()
    {
        return groupList.size();
    }

    private void setupPopup(View view, Group thisGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.activity_group_popup, null);
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
        InnerPlantRecyclerAdapter adapter = new InnerPlantRecyclerAdapter(this, groupList.get(holder.getAdapterPosition()),plantList, whatContext);
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
        ArrayList<Plant> notGrabbedPlants = groupList.get(holder.getAdapterPosition()).getAllPlants();
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

        plantList = groupList.get(holder.getAdapterPosition()).getAllPlants();
        GridLayoutManager newGridLayoutManager = new GridLayoutManager(whatContext, 1 );
        displayAllPlants.setLayoutManager(newGridLayoutManager);
        InnerPlantRecyclerAdapter adapter = new InnerPlantRecyclerAdapter(this, groupList.get(holder.getAdapterPosition()),plantList, whatContext);
        displayAllPlants.setAdapter(adapter);
    }


}