package com.example.plantparenthood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
    public SpaceActivityCreatorAdapter(List<Space> newSpaceList, Space_Activity space_activity)
    {
        spaceList = newSpaceList;
        this.space_activity = space_activity;
        whatContext = space_activity;
        spaceDatabaseHandler = SpaceDataBaseHandler.getDatabase(whatContext);
        changes = new boolean[3];
        textBoxes = new EditText[2];
        Arrays.fill(changes,false);
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
    }
}