package com.example.plantparenthood;
import android.os.Bundle;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.plantparenthood.R;
import com.example.plantparenthood.databinding.ActivityPlantSearcherBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class PlantSearcher extends AppCompatActivity
{
    private ActivityPlantSearcherBinding binding;
    private AbstractAPI api;
    private RequestQueue queue;
    private TextView errorText;
    private ArrayList<Plant> currentDisplayedPlants;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityPlantSearcherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());
        errorText = findViewById(R.id.errorText);

        api = new Perenual(); //new obj of Perenual API class
        api.plantsearchref = this; //debug remove this later... passes this class as a reference for testing/debug
        queue = Volley.newRequestQueue(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        TextInputEditText text = findViewById(R.id.textBoxText);
        Button searchButton = findViewById(R.id.sendQuery);
        searchButton.setOnClickListener(new View.OnClickListener()
        {
                @Override
                public void onClick(View view)
                {
                    searchByNameForPlant(String.valueOf(text.getText()));
                }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createPlantGrid(ArrayList<Plant> plantsList)
    {
        currentDisplayedPlants = plantsList;
        TextView text = findViewById(R.id.errorText);
        RecyclerView plantGrid = findViewById(R.id.plantGridView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        plantGrid.setLayoutManager(gridLayoutManager);

        if(plantsList.size() > 0)
        {
            for (int i = 0; i < plantsList.size(); i++)
            {
                Plant thisPlant = plantsList.get(i);
                api.queryImageAPI(queue, thisPlant);
            }
            text.setText("");

            //uh wait 3 seconds first, temporary work around
            new Handler().postDelayed(new Runnable() {
                public void run()
                {
                    PlantCreatorAdapter plantAdapter = new PlantCreatorAdapter(plantsList, PlantSearcher.this);
                    plantGrid.setAdapter(plantAdapter);
                }
            }, 3000); // 3 seconds

            //PlantCreatorAdapter plantAdapter = new PlantCreatorAdapter(plantsList);
            //plantGrid.setAdapter(plantAdapter);
        }
        else//don't bother setting up grid as no valid plants
        {
            text.setText("Plant(s) not found");
        }
    }

    private void searchByNameForPlant(String plantName)
    {
        //apply filters later
        if (!plantName.equals(""))
        {
            errorText.setText("Loading...");//this is just some UI stuff
            api.queryAPI(queue, plantName);
        }
        else
        {
            errorText.setText("Error no name");//UI message to user saying empty query, do not processes
        }
    }

    private void filterSearchResult()
    {
        //this will filter data
    }
}