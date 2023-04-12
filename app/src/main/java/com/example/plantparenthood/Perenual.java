package com.example.plantparenthood;

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
    private Integer pageNumber;
    private JSONObject queryReceived;

    public Perenual()
    {
        pageNumber = 1; //unused for now but add more pages and modify URL later
        APIUrl = "https://perenual.com/api/species-list?";
        APIPage = "page=" + pageNumber;
        APIKey = "&key=" + "sk-d6w863e859a77e76936";
        APIParams = "&q=";
        queryReceived = null;
    }

    @Override
    public void queryAPI(RequestQueue queue, String queryParams)
    {
        APIParams = "&q=" + queryParams;
        String APIQuery = APIUrl + APIPage + APIKey + APIParams;
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
                            com.example.plantparenthood.PlantCreator.addPlant(queryReceived, plantsearchref);
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
                        System.out.println("Error");
                    }
                });
        queue.add(stringRequest);
    }

    @Override
    public void queryImageAPI(RequestQueue queue, Plant plant)
    {
        ImageRequest imageRequest = new ImageRequest(plant.plantImageURL, new Response.Listener<Bitmap>()
        {
            @Override
            public void onResponse(Bitmap response)
            {
                System.out.println("Found image for: " + plant.common_name);
                plant.default_image = response;
            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                System.out.println("Error finding image");
            }
        });
        queue.add(imageRequest);
    }
}
