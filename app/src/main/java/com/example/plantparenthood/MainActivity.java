
package com.example.plantparenthood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PPMobileNotificationFactory.createNotificationChannel(getApplicationContext());//need to call this at app start
        //final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.lol);

        StatisticsDatabaseHandler.getDatabase(getApplicationContext());

        Button rickRoll =  findViewById(R.id.rickRoll);
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.song);
        rickRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            }
        });


            Button showStatistics = findViewById(R.id.showStatistics);
            showStatistics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), Statistics_Activity.class));
                    }
            });


            // Initialize and assign variable
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

            // Set Home selected
            bottomNavigationView.setSelectedItemId(R.id.home);

            // Perform item selected listener
            bottomNavigationView
                    .setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.Groups:
                                    startActivity(new Intent(getApplicationContext(), Group_Activity.class));
                                    overridePendingTransition(0, 0);
                                    return true;
                                case R.id.plants:
                                    startActivity(new Intent(getApplicationContext(), Plant_Activity.class));
                                    overridePendingTransition(0, 0);
                                    return true;
                                case R.id.home:
                                    return true;
                                case R.id.calendar:
                                    startActivity(new Intent(getApplicationContext(), Calendar_Activity.class));
                                    overridePendingTransition(0, 0);
                                    return true;
                                case R.id.scanner:
                                    startActivity(new Intent(getApplicationContext(), QRScannerMenuActivity.class));
                                    overridePendingTransition(0, 0);
                                    return true;
                            }
                            return false;
                        }
                    });

        }
    }
