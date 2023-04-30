package com.example.plantparenthood;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SpaceAdapter extends AbstractCreatorAdapter
{
    private ArrayList<Space> spaceList;
    private Context openActivity;

    private DatabaseHandler databaseHandler;

    public SpaceAdapter(ArrayList<Space> newSpaceList, Context newContext)
    {
        spaceList = newSpaceList;
        openActivity = newContext;
        databaseHandler = DatabaseHandler.getDatabase(newContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_square, parent, false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(currentView) {};
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Space thisSpace = spaceList.get(position);

        holder.itemView.setOnClickListener(view -> setupPopup(view, thisSpace));
    }

    @Override
    public int getItemCount() {
        return spaceList.size();
    }

    private void setupPopup(View view, Space thisSpace) {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.activity_space_popup, null);

        PopupWindow newPopupWindow = new PopupWindow(newPopup, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        newPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView spaceName = newPopup.findViewById(R.id.spaceName);
        spaceName.setText(thisSpace.getSpaceName());

        Button closeButton = newPopup.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPopupWindow.dismiss();
            }
        });

        Button addPlant = newPopup.findViewById(R.id.addPlant);
        addPlant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AsyncTask.execute(() -> databaseHandler.addPlantToDatabase(thisSpace));
                Toast.makeText(view.getContext(), "Plant successfully added", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
