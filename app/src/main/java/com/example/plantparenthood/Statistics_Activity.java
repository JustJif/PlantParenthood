package com.example.plantparenthood;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Statistics_Activity extends AppCompatActivity {
    public StatisticsManager statisticsManager = new StatisticsManager(this);

    public void retrieveWeeklyAnalysis(){
        //TODO
    }
    TextView curOwnedPlants;
    TextView totalOwnedPlants;
    TextView totalDeadPlants;
    TextView meanTimeBetweenWatering;
    TextView medianTimeBetweenWatering;
    TextView lastTimeWatered;
    TextView firstTimeWatered;
    Button shareButton;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initializeViews();

        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run(){
                StatisticsDatabase statisticsDatabase = Room.databaseBuilder(getApplicationContext(), StatisticsDatabase.class, "StatisticsDatabase").build();
                try {
                    Statistics statistics = statisticsDatabase.statisticsAccessObject().loadStatistics();

                    curOwnedPlants.append(""+statisticsManager.getNumOwnedPlants());
                    totalOwnedPlants.append(""+statisticsManager.getTotalOwnedPlants());
                    totalDeadPlants.append(""+statisticsManager.getTotalDeadPlants());
                    meanTimeBetweenWatering.append(""+statisticsManager.getMeanTimeBetweenWatering());
                    medianTimeBetweenWatering.append(""+statisticsManager.getMedianTimeBetweenWatering());
                    lastTimeWatered.append(""+statisticsManager.getLastTimeWatered());
                    firstTimeWatered.append(""+statisticsManager.getFirstTimeWatered());
                }catch(NullPointerException e)
                {
                    curOwnedPlants.append("No plants currently owned");
                    totalOwnedPlants.append("No plants ever owned");
                    totalDeadPlants.append("No dead plants :D");
                    meanTimeBetweenWatering.append("Never Watered");
                    medianTimeBetweenWatering.append("Never Watered");
                    lastTimeWatered.append("Never Watered");
                    firstTimeWatered.append("Never Watered");
                }

            }
        });


        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                share();
            }
        });

        //Bottom Navigation View
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.spaces);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.spaces:
                        startActivity(new Intent(getApplicationContext(),LightScannerActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.plants:
                        startActivity(new Intent(getApplicationContext(),Plant_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),Calendar_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    public void shareStatistics() {

    }

    public void initializeViews(){
        curOwnedPlants = (TextView)findViewById(R.id.curOwnedPlants);
        totalOwnedPlants = (TextView)findViewById(R.id.totalOwnedPlants);
        totalDeadPlants = (TextView)findViewById(R.id.totalDeadPlants);
        meanTimeBetweenWatering = (TextView)findViewById(R.id.meanTimeBetweenWatering);
        medianTimeBetweenWatering = (TextView)findViewById(R.id.medianTimeBetweenWatering);
        lastTimeWatered = (TextView)findViewById(R.id.lastTimeWatered);
        firstTimeWatered = (TextView)findViewById(R.id.firstTimeWatered);
        shareButton = (Button)findViewById(R.id.share_button);
    }

    public void share(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String textToSend = "Here are my statistics from PlantParenthood!\n\n" +
                curOwnedPlants.getText() + "\n" +
                totalOwnedPlants.getText() + "\n" +
                totalDeadPlants.getText() + "\n" +
                meanTimeBetweenWatering.getText() + "\n" +
                medianTimeBetweenWatering.getText() + "\n" +
                lastTimeWatered.getText() + "\n" +
                firstTimeWatered.getText();
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT,textToSend);
        startActivity(Intent.createChooser(sendIntent,null));
    }
}
