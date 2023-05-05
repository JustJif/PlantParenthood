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

public class WeeklyStatistics_Activity extends AppCompatActivity {
    public StatisticsManager statisticsManager;
    TextView meanTimeBetweenWatering;
    TextView lastTimeWatered;
    TextView firstTimeWatered;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_statistics);
        initializeViews();

        StatisticsDatabaseHandler.getDatabase(getApplicationContext());
        statisticsManager = new StatisticsManager();
        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run(){
                if(StatisticsDatabaseHandler.getDatabase(null).getStatistics() == null)
                {
                    StatisticsDatabaseHandler.getDatabase(null).pushToDatabase(new Statistics());
                }
                statisticsManager.setStatistics(StatisticsDatabaseHandler.getDatabase(getApplicationContext()).getStatistics());
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> updateTextViews());
            }
        });

        Button shareButton = (Button)findViewById(R.id.share_button);
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
        meanTimeBetweenWatering.append(""+statisticsManager.getMeanTimeBetweenWatering());
    }

    public void initializeViews(){
        meanTimeBetweenWatering = (TextView)findViewById(R.id.meanTimeBetweenWatering);
    }

    public void share(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String textToSend = "Here are my statistics from last week in PlantParenthood!\n\n" +
                meanTimeBetweenWatering.getText() + "\n" +
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT,textToSend);
        startActivity(Intent.createChooser(sendIntent,null));
    }
}
