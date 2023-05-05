package com.example.plantparenthood;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;

public class Statistics_Activity extends AppCompatActivity {
    public StatisticsManager statisticsManager;

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

        StatisticsDatabaseHandler.getDatabase(getApplicationContext());
        statisticsManager = new StatisticsManager();
        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run(){
//                    if(StatisticsDatabaseHandler.getDatabase(null).getStatistics() == null)
//                    {
                        StatisticsDatabaseHandler.getDatabase(null).pushToDatabase(new Statistics());
//                    }
                    Log.e("ASYNC","Grabbing Database");
                    statisticsManager.setStatistics(StatisticsDatabaseHandler.getDatabase(getApplicationContext()).getStatistics());
                    Log.e("Database Test", statisticsManager.getTotalDeadPlants()+"");
                    Log.e("ASYNC","Grabbed Database");
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> updateTextViews());
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
                    case R.id.scanner:
                        startActivity(new Intent(getApplicationContext(),QRScannerMenuActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void updateTextViews() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        curOwnedPlants.append(""+statisticsManager.getNumOwnedPlants());
        totalOwnedPlants.append(""+statisticsManager.getTotalOwnedPlants());
        totalDeadPlants.append(""+statisticsManager.getTotalDeadPlants());
        meanTimeBetweenWatering.append(""+statisticsManager.getMeanTimeBetweenWatering());
        medianTimeBetweenWatering.append(""+statisticsManager.getMedianTimeBetweenWatering());
        if(statisticsManager.getLastTimeWatered() == 0)
        {
            lastTimeWatered.append("Never Watered");
        }else{
            lastTimeWatered.append(""+sdf.format(statisticsManager.getLastTimeWatered()));
        }

        if(statisticsManager.getFirstTimeWatered() == 0)
        {
            firstTimeWatered.append("Never Watered");
        }else{
            firstTimeWatered.append(""+sdf.format(statisticsManager.getFirstTimeWatered()));
        }
    }

    public void shareStatistics() {

    }

    public void initializeViews(){
        Log.e("TextView","Initializing Text Views");
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
