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
public class ChangeGroupNameTest {
    GroupDataBaseHandler handler;
    Group comparedGroup;

    @Before
    public void initializeDatabase() {
        handler = DatabaseHandler.createInMemoryDatabase(ApplicationProvider.getApplicationContext());
        comparedGroup = new Group("Test");
    }

    /**
     * Updating only one parameter from the plant
     */
    @Test
    public void changeGroupName()
    {
        AsyncTask.execute(() ->
        {
            comparedGroup.setGroupName("Updated");

            Group testGroup = handler.getGroupFromDBbyID(0);

            assertEquals(testGroup.getGroupName(),comparedGroup.getGroupName());

        });
    }


    /**
     * Cleanup database afterward to ensure next run of tests don't have interfering objects
     */
    @After
    public void cleanUp()
    {
        handler.removeGroupToDatabase(comparedGroup);
    }
}