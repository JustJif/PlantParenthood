
package com.example.plantparenthood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PPMobileNotificationFactory.createNotificationChannel(getApplicationContext());//need to call this at app start


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
                            case R.id.spaces:
                                startActivity(new Intent(getApplicationContext(), Space_Activity.class));
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

        // Check if the app has permission to show notifications
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            // Request permission to show notifications
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        }
    }

}
