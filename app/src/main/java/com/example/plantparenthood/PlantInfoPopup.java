package com.example.plantparenthood;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;


public class PlantInfoPopup
{
    private boolean[] changes;
    private EditText[] textBoxes;
    private Bitmap newImage;
    private RecyclerView.ViewHolder holder;
    private AbstractCreatorAdapter adapter;
    private Plant_Activity plant_activity;
    private Context whatContext;
    private ImageView plantImage;
    private ImageView qrImage;
    private PlantController plantController;

    private Uri uri;

    public PlantInfoPopup(View view, Plant thisPlant, Context activityContext, RecyclerView.ViewHolder holder, AbstractCreatorAdapter adapter, Plant_Activity plant_activity)
    {
        this.holder = holder;
        this.adapter = adapter;
        this.plant_activity = plant_activity;
        whatContext = activityContext;
        changes = new boolean[3];
        textBoxes = new EditText[2];
        plantController = new PlantController();
        setupPopup(view, thisPlant);
    }

    public PlantInfoPopup(View view, Plant thisPlant, Context activityContext)
    {
        whatContext = activityContext;
        changes = new boolean[3];
        textBoxes = new EditText[2];
        plantController = new PlantController();
        setupPopup(view, thisPlant);
    }

    public void setupPopup(View view, Plant thisPlant) {
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
        qrImage.post(new Runnable()
        {//wait until qrImage has been drawn to execute
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

            }
        });
        ImageView image = (ImageView) newPopup.findViewById(R.id.plantImage);
        image.setImageURI(uri);

        Button deletePlant = newPopup.findViewById(R.id.deletePlant);
        deletePlant.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View view) {
               new AlertDialog.Builder(whatContext)
                   .setTitle("Remove plant")
                   .setMessage("Removing a plant is permanent this cannot be undone.")
                   .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int id) {
                           AsyncTask.execute(() -> DatabaseHandler.getDatabase().deletePlant(thisPlant));
                           plant_activity.notifyGridOfUpdate(holder.getAdapterPosition());
                           Toast.makeText(view.getContext(), "Plant deleted", Toast.LENGTH_SHORT).show();
                           newPopupWindow.dismiss();
                       }
                   })
                   .setNegativeButton("Revert", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int id) {
                           Toast.makeText(view.getContext(), "Deletion reverted", Toast.LENGTH_SHORT).show();
                       }
                   })
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


    private void setChanges(Plant plant)
    {
        plantController.updatePlant(plant,changes,textBoxes,newImage);

        AsyncTask.execute(() -> DatabaseHandler.getDatabase(whatContext).addPlantToDatabase(plant));
        if(holder != null && adapter != null)
            adapter.notifyItemChanged(holder.getAdapterPosition());
    }
}
