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
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class PlantActivityCreatorAdapter extends AbstractCreatorAdapter
{
    private List<Plant> plantsList;
    private Plant_Activity plant_activity;
    private Context whatContext;
    private ImageView plantImage;
    private ImageView qrImage;
    private DatabaseHandler databaseHandler;
    private boolean[] changes;
    private EditText[] textBoxes;
    private Bitmap newImage;
    private RecyclerView.ViewHolder holder;
    public PlantActivityCreatorAdapter(List<Plant> newPlantsList, Plant_Activity plant_activity)
    {
        plantsList = newPlantsList;
        this.plant_activity = plant_activity;
        whatContext = plant_activity;
        databaseHandler = DatabaseHandler.getDatabase(whatContext);
        changes = new boolean[3];
        textBoxes = new EditText[2];
        Arrays.fill(changes,false);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_display_square,parent,false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(currentView) {};
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Plant thisPlant = plantsList.get(position);
        TextView plantCommonName = (TextView) holder.itemView.findViewById(R.id.plantCommonName);
        plantImage = (ImageView) holder.itemView.findViewById(R.id.plantImage);

        plantCommonName.setText(thisPlant.getCommon_name());
        plantImage.setImageBitmap(thisPlant.getDefault_image());

        this.holder = holder;

        holder.itemView.setOnClickListener(view -> setupPopup(view, thisPlant));
    }

    @Override
    public int getItemCount()
    {
        return plantsList.size();
    }

    private void setupPopup(View view, Plant thisPlant) {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.activity_plant_display_popup, null);

        PopupWindow newPopupWindow = new PopupWindow(newPopup, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        newPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        EditText plantCommonName = newPopup.findViewById(R.id.plantCommonName);
        textBoxes[0] = plantCommonName;
        plantCommonName.setText(thisPlant.getCommon_name());
        plantCommonName.setEnabled(false);

        EditText plantScientificName = newPopup.findViewById(R.id.plantScientificName);
        textBoxes[1] = plantScientificName;
        plantScientificName.setText(thisPlant.getScientific_name());
        plantScientificName.setEnabled(false);

        plantImage = newPopup.findViewById(R.id.plantImage);
        plantImage.setImageBitmap(thisPlant.getDefault_image());

        qrImage = newPopup.findViewById(R.id.qr_image);
        qrImage.post(new Runnable(){//wait until qrImage has been drawn to execute
            @Override
            public void run(){
                qrImage.setImageBitmap(QRCodeManager.generateQRCodeBitmap(Integer.toString(thisPlant.getId()), qrImage.getWidth()));
                qrImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        QRCodeManager.printQRCode(qrImage, Integer.toString(thisPlant.getId()), qrImage.getContext());
                    }
                });
            }
        });



        Button closeButton = newPopup.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPopupWindow.dismiss();
            }
        });

        ImageView editCommonName = newPopup.findViewById(R.id.editCommon);
        editCommonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click)
            {
                changes[0] = true;
                modifyText(plantCommonName, view);
            }
        });

        ImageView editScientificName = newPopup.findViewById(R.id.editScientific);
        editScientificName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click) {
                changes[1] = true;
                modifyText(plantScientificName, view);
            }
        });

        ImageView editCamera = newPopup.findViewById(R.id.editCamera);
        editCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changes[2] = true;
                plant_activity.openCamera();
            }
        });

        Button deletePlant = newPopup.findViewById(R.id.deletePlant);
        deletePlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(whatContext)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this plant?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deletePlant(thisPlant);
                                Toast.makeText(view.getContext(), "Plant deleted", Toast.LENGTH_SHORT).show();
                                newPopupWindow.dismiss();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        Button updatePlant = newPopup.findViewById(R.id.updatePlant);
        updatePlant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new AlertDialog.Builder(whatContext)
                .setTitle("Confirm changes")
                .setMessage("Changes will override previous information, this cannot be undone.")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id)
                    {
                        setChanges(thisPlant);
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

    public void setCameraPreview(Bitmap image)
    {
        plantImage.setImageBitmap(image);
        newImage = image;
    }

    private void setChanges(Plant plant)
    {
        if(changes[0])
            plant.setCommon_name(textBoxes[0].getText().toString());
        if(changes[1])
            plant.setScientific_name(textBoxes[1].getText().toString());
        if(changes[2])
            plant.setDefault_image(newImage);

        AsyncTask.execute(() -> databaseHandler.addPlantToDatabase(plant));
        this.notifyItemChanged(holder.getAdapterPosition());
    }



    private void deletePlant(Plant plant) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                databaseHandler.deletePlant(plant.getId());
                plantsList.remove(plant);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> notifyItemRemoved(holder.getAdapterPosition()));
            }
        });
    }







}