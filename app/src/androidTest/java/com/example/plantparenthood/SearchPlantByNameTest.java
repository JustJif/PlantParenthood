package com.example.plantparenthood;

import static org.junit.Assert.fail;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.prefs.NodeChangeListener;

@RunWith(AndroidJUnit4.class)
public class SearchPlantByNameTest
{
    DatabaseHandler handler;

    /**
     * Pre-initialize a database
     */
    @Before
    public void initializeDatabase()
    {
        handler = DatabaseHandler.createInMemoryDatabase(ApplicationProvider.getApplicationContext());
    }
    @Test
    public void searchPlantByName()
    {
        PlantSearcher searcher = new PlantSearcher(null, ApplicationProvider.getApplicationContext());

        String output = searcher.searchByNameForPlant("Desert Rose", 1);
        Assert.assertEquals(output,"Loading...");
    }
    @Test
    public void searchPlantByNameNoName()
    {
        PlantSearcher searcher = new PlantSearcher(null, ApplicationProvider.getApplicationContext());
        String output = searcher.searchByNameForPlant("", 1);
        Assert.assertEquals(output,"Error no name");
    }
}
