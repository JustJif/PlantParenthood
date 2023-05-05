package com.example.plantparenthood;
import android.content.Context;
import android.os.Bundle;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.plantparenthood.databinding.ActivityPlantSearcherBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlantSearcher extends AppCompatActivity
{
    private ActivityPlantSearcherBinding binding;
    private AbstractAPI api;
    private RequestQueue queue;
    private TextView errorText;
    private ArrayList<Plant> currentDisplayedPlants;
    private Integer pageNumber, maxPageNumber;
    private String plantName, previousPlantName;
    private PlantCreator plantCreator;
    private PlantDatabase database;

    private String tagFilter = "";
    private StatisticsManager statisticsManager = new StatisticsManager();
    private Button customPlantButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        customPlantButton = findViewById(R.id.addCustomPlant);



        binding = ActivityPlantSearcherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());
        errorText = findViewById(R.id.errorText);

        api = new Perenual(this);
        queue = Volley.newRequestQueue(getApplicationContext());

        pageNumber = 1;
        maxPageNumber = 1;
        plantName = "";
        previousPlantName = "";

        database = Room.databaseBuilder(getApplicationContext(),PlantDatabase.class, "PlantDatabase").build();
        plantCreator = new PlantCreator(database.dataAccessObject());
        TextView searchPage = findViewById(R.id.pageNum);
        Button leftButton = findViewById(R.id.leftButton);
        Button rightButton = findViewById(R.id.rightButton);




        leftButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                previousArrow(searchPage);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                nextArrow(searchPage);
            }
        });


        Button button = (Button) findViewById(R.id.filterPlants);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupFilter popup = new PopupFilter();
                popup.show(getSupportFragmentManager(), "popup");
            }
        });



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
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                plantName = String.valueOf(text.getText());
                searchByNameForPlant(plantName);
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

    public void createPlantGrid(ArrayList<Plant> plantsList, Integer currentPage, Integer numberOfPages)
    {
        customPlantButton = findViewById(R.id.addCustomPlant);
        currentDisplayedPlants = plantsList;
        TextView text = findViewById(R.id.errorText);
        RecyclerView plantGrid = findViewById(R.id.plantGridView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        plantGrid.setLayoutManager(gridLayoutManager);
        pageNumber = currentPage;
        maxPageNumber = numberOfPages;
        TextView searchText = (TextView) findViewById(R.id.pageNum);
        searchText.setText(pageNumber + "/" + maxPageNumber);

        if(plantsList.size() > 0)
        {

            customPlantButton.setVisibility(View.INVISIBLE);
            PlantCreatorAdapter plantAdapter = new PlantCreatorAdapter(plantsList, PlantSearcher.this);

            for (int i = 0; i < plantsList.size(); i++)
            {
                Plant thisPlant = plantsList.get(i);
                api.queryImageAPI(queue, thisPlant, plantAdapter, i);
            }
            text.setText("");
            plantGrid.setAdapter(plantAdapter);
        }
        else//don't bother setting up grid as no valid plants
        {
            text.setText("Plant(s) not found");

            customPlantButton.setVisibility(View.VISIBLE);
        }
    }

    private void searchByNameForPlant(String nameOfPlant)
    {
        //apply filters later
        if (!nameOfPlant.equals(""))
        {
            errorText.setText("Loading...");//this is just some UI stuff
            api.queryAPI(queue, nameOfPlant, pageNumber);
        }
        else
        {
            errorText.setText("Error no name");//UI message to user saying empty query, do not processes
        }
    }

    private void previousArrow(TextView searchPage)
    {
        if(pageNumber > 1)
        {
            pageNumber--;
            queue.cancelAll(this); //stops spamming the api with queries, really helps with performance
            RecyclerView plantGrid = findViewById(R.id.plantGridView);
            plantGrid.setAdapter(null);
            searchByNameForPlant(plantName);
        }
    }

    private void nextArrow(TextView searchPage)
    {
        if(pageNumber < maxPageNumber)
        {
            pageNumber++;
            queue.cancelAll(this);
            RecyclerView plantGrid = findViewById(R.id.plantGridView);
            plantGrid.setAdapter(null);
            searchByNameForPlant(plantName);
        }
    }

    public void passDataToCreator(JSONObject unparsedFile) throws JSONException
    {
        plantCreator.createPlant(unparsedFile,getApplicationContext(), this);
    }

    public void filterSearchResult(String query, String watering, String cycle, String sunlight) {


    }





    }