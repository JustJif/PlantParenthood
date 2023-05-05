package com.example.plantparenthood;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Perenual extends com.example.plantparenthood.AbstractAPI
{
    private String APIUrl;
    private String APIPage;
    private String APIKey;
    private String APIParams;
    private JSONObject queryReceived;
    private String defaultImageURL;
    private PlantController plantController;
    private Context whatContext;
    public Perenual(PlantController plantController, Context context)
    {
        APIUrl = "https://perenual.com/api/species-list?";
        APIPage = "page=";
        APIKey = "&key=" + "sk-d6w863e859a77e76936";
        APIParams = "&q=";
        queryReceived = null;
        defaultImageURL = "https://perenual.com/storage/species_image/2_abies_alba_pyramidalis/og/49255769768_df55596553_b.jpg";
        this.plantController = plantController;
        whatContext = context;
    }

    @Override
    public void queryAPI(RequestQueue queue, String queryParams, Integer page)
    {
        String localParams = APIParams + queryParams;
        String localPage = APIPage + page;

        String APIQuery = APIUrl + localPage + APIKey + localParams;
        System.out.println("User query is: " + APIQuery);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIQuery,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            queryReceived = new JSONObject(response);
                            //plantSearcherRef.passDataToCreator(queryReceived);
                            ArrayList<Plant> plants = plantController.createPlant(queryReceived,whatContext);
                        } catch (JSONException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        System.out.println("Error failed connecting to API");
                    }
                });
        queue.add(stringRequest);
    }

    @Override
    public void queryImageAPI(RequestQueue queue, Plant plant, AbstractCreatorAdapter plantAdapter, int plantLocation)
    {
        //this is the default image URL
        if(plant.getPlantImageURL().equals(defaultImageURL) || plant.getPlantImageURL().equals(""))
            return;
        ImageRequest imageRequest = new ImageRequest(plant.getPlantImageURL(), new Response.Listener<Bitmap>()
        {
            @Override
            public void onResponse(Bitmap response)
            {
                plant.setDefault_image(response);
                plantAdapter.notifyItemChanged(plantLocation);
            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                System.out.println("Error finding image");
            }
        });
        imageRequest.setTag(DatabaseHandler.getDatabase());
        queue.add(imageRequest);
    }
}
