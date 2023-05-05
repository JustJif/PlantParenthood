package com.example.plantparenthood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class AddGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        EditText nameEditText = (EditText) findViewById(R.id.names);
        String fullName = nameEditText.getText().toString();
        Group newGroup = new Group(fullName);
    }
}