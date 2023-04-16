package com.example.plantparenthood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class AddSpace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_space);
        EditText nameEditText = (EditText) findViewById(R.id.names);
        String fullName = nameEditText.getText().toString();

    }
}