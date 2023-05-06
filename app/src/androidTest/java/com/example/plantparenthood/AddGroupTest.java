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
    public void createSingularGroupTest()
    {
        try {
            Group testGroup = new Group("Test");
            Context context = ApplicationProvider.getApplicationContext();
            ArrayList<Group> createdGroups = new ArrayList<Group>();
            createdGroups.add(testGroup);

            AsyncTask.execute(() ->
            {
                Group createdGroup = createdGroups.get(0);
                handler.addGroupToDatabase(createdGroup);

                comparedGroups.add(handler.getGroupFromDBbyID(0));//load plant from DB and compare

                assertEquals(createdGroup.getGroupID(), comparedGroups.get(0).getGroupID());
                assertEquals(createdGroup.getGroupName(), comparedGroups.get(0).getGroupName());
                assertEquals(createdGroup.getPlantIDs(), comparedGroups.get(0).getPlantIDs());
                assertEquals(createdGroup.getGroupLightLevel(), comparedGroups.get(0).getGroupLightLevel());
                assertEquals(createdGroup.getAllPlants(), comparedGroups.get(0).getAllPlants());
            });
        }
        catch (JSONException e)
        {
            fail();
        }
    }

    @Test
    public void createMultipleGroupsTest() {
        try {
            Group testGroup1 = new Group("Test1");
            Group testGroup2= new Group("Test2");
            Group testGroup3 = new Group("Test3");
            Group testGroup4 = new Group("Test4");
            Context context = ApplicationProvider.getApplicationContext();
            ArrayList<Group> createdGroups = new ArrayList<Group>();
            createdGroups.add(testGroup1);
            createdGroups.add(testGroup2);
            createdGroups.add(testGroup3);
            createdGroups.add(testGroup4);
            handler.addGroupToDatabase(testGroup1);
            handler.addGroupToDatabase(testGroup2);
            handler.addGroupToDatabase(testGroup3);
            handler.addGroupToDatabase(testGroup4);

            context = ApplicationProvider.getApplicationContext();
            AsyncTask.execute(() ->
            {
                comparedGroups = handler.getGroupsFromDB();

                for (int i = 0; i < createdGroups.size(); i++) {
                    assertEquals(createdGroups.get(i).getGroupID(), comparedGroups.get(i).getGroupID());
                    assertEquals(createdGroups.get(i).getGroupName(), comparedGroups.get(i).getGroupName());
                    assertEquals(createdGroups.get(i).getPlantIDs(), comparedGroups.get(i).getPlantIDs());
                    assertEquals(createdGroups.get(i).getGroupLightLevel(), comparedGroups.get(i).getGroupLightLevel());
                    assertEquals(createdGroups.get(i).getAllPlants(), comparedGroups.get(i).getAllPlants());
                }
            });
        }
        catch (JSONException e)
        {
            fail();
        }
    }


    @Test
    public void createNullGroupTest() {
        try {
            Group nullGroup = new Group(null);
            Context context = ApplicationProvider.getApplicationContext();

            AsyncTask.execute(() ->
            {
                comparedGroups.add(handler.getGroupFromDBbyID(0));
                assertEquals(comparedGroups.size(), 0);
            });
        }
        catch (JSONException e)
        {
            fail(); //forcefully fails if the exception is thrown
        }
    }


    @After
    public void cleanUp()
    {
        for (int i = 0; i < comparedGroups.size(); i++) {
            handler.removeGroupToDatabase(comparedGroups.get(i));
        }
    }
}