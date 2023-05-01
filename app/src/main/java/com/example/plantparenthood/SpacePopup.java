package com.example.plantparenthood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class SpacePopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_space);
        EditText nameEditText = (EditText) findViewById(R.id.names);
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> {
            String fullName = nameEditText.getText().toString();
            Space newSpace = new Space(fullName);
            AsyncTask.execute(() -> SpaceDataBaseHandler.getDatabase(getApplicationContext()).addSpaceToDatabase(newSpace));
            startActivity(new Intent(getApplicationContext(), Space_Activity.class));
        });
    }
}