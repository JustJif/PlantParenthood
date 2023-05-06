package com.example.plantparenthood;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class GroupPopup extends AppCompatActivity {
    private ActivityResultLauncher<Intent> camera;

    int lightLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        EditText nameEditText = (EditText) findViewById(R.id.names);
        Button submitButton = findViewById(R.id.submitButton);


        Button editCamera = findViewById(R.id.scanLightLevel);
        editCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupPopup.this, LightScannerActivity.class);
                startActivity(intent);
            }
        });



        submitButton.setOnClickListener(v -> {
            String fullName = nameEditText.getText().toString();
            Group newGroup = new Group(fullName,lightLevel);
            AsyncTask.execute(() -> GroupDataBaseHandler.getDatabase(getApplicationContext()).addGroupToDatabase(newGroup));
            startActivity(new Intent(getApplicationContext(), Group_Activity.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Camera","Returning values");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                lightLevel = data.getIntExtra("lightLevel", 0);
            }
        }
    }

}