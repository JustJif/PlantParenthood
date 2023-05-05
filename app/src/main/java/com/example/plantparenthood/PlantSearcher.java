package com.example.plantparenthood;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class PlantSearcher
{
    private AbstractAPI api;
    private RequestQueue queue;
    private PlantController plantController;
    public PlantSearcher(PlantController plantController, Context context)
    {
        this.plantController = plantController;
        api = new Perenual(plantController,context);
        queue = Volley.newRequestQueue(context);
    }

    public String searchByNameForPlant(String nameOfPlant, Integer pageNumber)
    {
        if (!nameOfPlant.equals(""))
        {
            api.queryAPI(queue, nameOfPlant, pageNumber);
            return "Loading...";
        }
        else
        {
            return "Error no name";
        }
    }

    public void cancelQueueRequests()
    {
        queue.cancelAll(DatabaseHandler.getDatabase()); //stops spamming the api with queries, really helps with performance
    }

    public void queryImageAPI(Plant plant, int plantLocation)
    {
        api.queryImageAPI(queue,plant,plantLocation);
    }

    public void filterSearchResult(String query, String watering, String cycle, String sunlight)
    {
        
    }
}
