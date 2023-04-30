package com.example.plantparenthood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

import java.util.List;

public class SpacePopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_space);
        EditText nameEditText = (EditText) findViewById(R.id.names);
        String fullName = nameEditText.getText().toString();
        Space newSpace = new Space(fullName);
    }
}