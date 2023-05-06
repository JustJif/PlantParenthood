package com.example.plantparenthood;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;
import android.os.AsyncTask;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class AddGroupTest {
    GroupDataBaseHandler handler;
    List<Group> comparedGroups;
    Plant plant;

    @Before
    public void initializeDatabase() {
        handler = DatabaseHandler.createInMemoryDatabase(ApplicationProvider.getApplicationContext());
        comparedGroups = new ArrayList<>();
    }

    @Test
    public void createDeletGroupTest()
    {
            Group testGroup = new Group("Test");

            AsyncTask.execute(() ->
            {
                handler.addGroupToDatabase(testGroup);
                handler.removeGroupToDatabase(testGroup);

                assertEquals(null, comparedGroups.get(0).getGroupID());
            });

    }


    @After
    public void cleanUp()
    {
        for (int i = 0; i < comparedGroups.size(); i++) {
            handler.removeGroupToDatabase(compareGroups);
        }
    }
}