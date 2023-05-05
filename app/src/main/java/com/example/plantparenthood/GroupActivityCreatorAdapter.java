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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupActivityCreatorAdapter extends AbstractCreatorAdapter
{
    private List<Group> groupList;
    private Group_Activity group_activity;
    private Context whatContext;
    private ImageView groupImage;
    private GroupDataBaseHandler databaseHandler;
    private boolean[] changes;
    private EditText[] textBoxes;
    private Bitmap newImage;
    private RecyclerView.ViewHolder holder;
    private RecyclerView displayAllPlants;
    private List<Plant> plantList;
    public GroupActivityCreatorAdapter(List<Group> newGroupList, Group_Activity group_activity)
    {
        groupList = newGroupList;
        this.group_activity = group_activity;
        whatContext = group_activity;
        databaseHandler = GroupDataBaseHandler.getDatabase(whatContext);
        changes = new boolean[3];
        textBoxes = new EditText[2];
        Arrays.fill(changes,false);
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
        TextView groupName = (TextView) holder.itemView.findViewById(R.id.GroupName);
        //groupImage = (ImageView) holder.itemView.findViewById(R.id.groupImage);

        groupName.setText(thisGroup.getGroupName());
        //groupImage.setImageBitmap(thisgroup.getDefault_image());

        this.holder = holder;

        holder.itemView.setOnClickListener(view -> setupPopup(view, thisGroup));
    }

    @Override
    public int getItemCount()
    {
        return groupList.size();
    }

    private void setupPopup(View view, Group thisgroup) {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.activity_group_popup, null);
        displayAllPlants = newPopup.findViewById(R.id.plant_recycler_view);
        PopupWindow newPopupWindow = new PopupWindow(newPopup, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        newPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        EditText groupName = newPopup.findViewById(R.id.groupName);
        textBoxes[0] = groupName;
        groupName.setText(thisgroup.getGroupName());
        groupName.setEnabled(false);

        Button closeButton = newPopup.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPopupWindow.dismiss();
            }
        });

        ImageView editName = newPopup.findViewById(R.id.editGroupName);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click)
            {
                changes[0] = true;
                modifyText(groupName, view);
            }
        });
        Button addPlantToGroup = newPopup.findViewById(R.id.addPlant);
       addPlantToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupAddPlantPopup(newPopup, thisgroup);
            }
        });
        Button waterAll = newPopup.findViewById(R.id.waterAll);
        waterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                thisgroup.waterAll();
            }
        });

        Button updategroup = newPopup.findViewById(R.id.updateGroup);
        updategroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new AlertDialog.Builder(whatContext)
                        .setTitle("Confirm changes")
                        .setMessage("Changes will override previous information, this cannot be undone.")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id)
                            {
                                setChanges(thisgroup);
                                Toast.makeText(view.getContext(), "Applied changes", Toast.LENGTH_SHORT).show();
                                newPopupWindow.dismiss();
                            }
                        })
                        .setNegativeButton("Revert", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                Toast.makeText(view.getContext(), "Reverted changes", Toast.LENGTH_SHORT).show();
                                Arrays.fill(changes, false);
                                newPopupWindow.dismiss();
                            }
                        })
                        .show();
            }

        });
        Button removeGroup = newPopup.findViewById(R.id.deleteGroup);
        removeGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new AlertDialog.Builder(whatContext)
                        .setTitle("Delete Group?")
                        .setMessage("Changes will override previous information, this cannot be undone.")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id)
                            {
                                deleteGroup(thisgroup);
                                Toast.makeText(view.getContext(), "Applied changes", Toast.LENGTH_SHORT).show();
                                newPopupWindow.dismiss();
                            }
                        })
                        .setNegativeButton("Revert", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                Toast.makeText(view.getContext(), "Reverted changes", Toast.LENGTH_SHORT).show();
                                Arrays.fill(changes, false);
                                newPopupWindow.dismiss();
                            }
                        })
                        .show();
            }
        });
        showPlants(thisgroup);
    }

    private void setupAddPlantPopup(View view, Group thisGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.activity_add_plant_to_group_popup, null);
        displayAllPlants = newPopup.findViewById(R.id.plant_recycler_view);
        PopupWindow newPopupWindow = new PopupWindow(newPopup, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        newPopupWindow.showAtLocation(newPopup, Gravity.CENTER, 0, 0);


        Button closeButton = newPopup.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newPopupWindow.dismiss();
            }
        });

        showAllPlants(thisGroup);
    }
    public void showAllPlants(Group group) {

        GridLayoutManager newGridLayoutManager = new GridLayoutManager(whatContext, 1 );
        displayAllPlants.setLayoutManager(newGridLayoutManager);
        ArrayList<Plant> notGrabbedPlants = group.getAllPlants();
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
            plantDisplayHandler.post(() -> createPlants(group));
        });
    }

    private void createPlants(Group group) {
        InnerPlantRecyclerAdapter adapter = new InnerPlantRecyclerAdapter(this, group, plantList, whatContext,false);
        displayAllPlants.setAdapter(adapter);
    }
    public void showPlants(Group group) {
        plantList = group.getAllPlants();
        GridLayoutManager newGridLayoutManager = new GridLayoutManager(whatContext, 1 );
        displayAllPlants.setLayoutManager(newGridLayoutManager);

        InnerPlantRecyclerAdapter adapter = new InnerPlantRecyclerAdapter(this, group,plantList, whatContext,true);
        displayAllPlants.setAdapter(adapter);
    }

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
        }
    }
    private void deleteGroup(Group group)
    {

        AsyncTask.execute(() -> databaseHandler.removeGroupToDatabase(group));
        this.notifyItemChanged(holder.getAdapterPosition());
    }

    private void setChanges(Group group)
    {
        if(changes[0])
            group.setGroupName(textBoxes[0].getText().toString());

        AsyncTask.execute(() -> databaseHandler.addGroupToDatabase(group));
        this.notifyItemChanged(holder.getAdapterPosition());
    }
}