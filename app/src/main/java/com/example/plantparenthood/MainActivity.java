package com.example.plantparenthood;

import android.os.Bundle;

import om.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import n droidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.plantparenthood.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

        proected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    break;
                case R.id.plants:
                    replaceFragment(new PlantsFragment());
                    break;
                case R.id.spaces:
                    replaceFragment(new SpacesFragment());
                    break;
                case R.id.calendar:
                    replaceFragment(new CalendarFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

}