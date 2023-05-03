package com.example.plantparenthood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class GroupPopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        EditText nameEditText = (EditText) findViewById(R.id.names);
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> {
            String fullName = nameEditText.getText().toString();
            Group newGroup = new Group(fullName);
            AsyncTask.execute(() -> GroupDataBaseHandler.getDatabase(getApplicationContext()).addGroupToDatabase(newGroup));
            startActivity(new Intent(getApplicationContext(), Group_Activity.class));
        });
    }
}