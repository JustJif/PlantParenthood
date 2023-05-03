package com.example.plantparenthood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Group_Activity extends AppCompatActivity {
    CardView addGroup;
    private GroupDataBaseHandler GroupHandler;
    public List<Group> groupList;

    private RecyclerView GroupGrid;

    private GroupActivityCreatorAdapter GroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        groupList = new ArrayList<>();
        GroupHandler = GroupDataBaseHandler.getDatabase(getApplicationContext());

        GroupGrid = findViewById(R.id.Groups_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        GroupGrid.setLayoutManager(gridLayoutManager);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                groupList = GroupHandler.getGroupsFromDB();
                System.out.println(groupList.size());
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> createGroupGrid(GroupGrid));
            }
        });


        addGroup = (CardView) findViewById(R.id.addGroup);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GroupPopup.class));
            }
        });
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.Groups);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.Groups:
                        return true;
                    case R.id.plants:
                        startActivity(new Intent(getApplicationContext(), Plant_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Group_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    public void createGroupGrid(RecyclerView GroupGrid) {
        GroupAdapter = new GroupActivityCreatorAdapter(groupList, this);
        GroupGrid.setAdapter(GroupAdapter);
    }
}
