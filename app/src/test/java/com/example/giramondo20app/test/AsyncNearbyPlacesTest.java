package com.example.giramondo20app.test;

import com.example.giramondo20app.AsyncNearbyPlaces;
import com.example.giramondo20app.Controller.DAO.AccommodationDAO;
import com.example.giramondo20app.Controller.DAO.MySQLAccommodationDAO;
import com.example.giramondo20app.Model.AccommodationModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AsyncNearbyPlacesTest {

    //Class to be tested
    AsyncNearbyPlaces asyncNearbyPlaces;

    //Dependencies
    AccommodationDAO acmSQL;

    List<AccommodationModel> acmListHotels;
    List<AccommodationModel> acmListRestaurants;
    List<AccommodationModel> acmListAttractions;


    @Before
    public void setUp(){
        asyncNearbyPlaces = new AsyncNearbyPlaces();
        acmListHotels = new ArrayList<>();
        acmListRestaurants = new ArrayList<>();
        acmListAttractions = new ArrayList<>();

        acmSQL = mock(MySQLAccommodationDAO.class);
        asyncNearbyPlaces.setDAO(acmSQL);
    }

    @Test
    public void testLocationNotEmpty(){

        HashMap<String,List<AccommodationModel>> actualResults;

        acmListHotels.add(new AccommodationModel("B&b Margarita","Napoli","Albergo"));
        acmListHotels.add(new AccommodationModel("Hostel Experiece","Napoli","Albergo"));
        acmListAttractions.add(new AccommodationModel("Zoo di Napoli","Napoli","Attrazione"));
        acmListRestaurants.add(new AccommodationModel("Yao Restaurant","Napoli","Ristorante"));

        when(acmSQL.getNearbyHotels("Napoli")).thenReturn(acmListHotels);
        when(acmSQL.getNearbyRestaurants("Napoli")).thenReturn(acmListRestaurants);
        when(acmSQL.getNearbyAttractions("Napoli")).thenReturn(acmListAttractions);

        actualResults = asyncNearbyPlaces.getNearbyAccommodations("Napoli");

        assertEquals("B&b Margarita",actualResults.get("hotels").get(0).getName());
        assertEquals("Hostel Experiece",actualResults.get("hotels").get(1).getName());
        assertEquals("Zoo di Napoli",actualResults.get("attractions").get(0).getName());
        assertEquals("Yao Restaurant",actualResults.get("restaurants").get(0).getName());
        assertEquals(3,actualResults.size());
    }


    @Test
    public void testLocationEmpty(){

        HashMap<String,List<AccommodationModel>> actualResults;

        when(acmSQL.getNearbyHotels("")).thenReturn(acmListHotels);
        when(acmSQL.getNearbyRestaurants("")).thenReturn(acmListRestaurants);
        when(acmSQL.getNearbyAttractions("")).thenReturn(acmListAttractions);

        actualResults = asyncNearbyPlaces.getNearbyAccommodations("");


        assertEquals(3,actualResults.size());
        assertTrue(actualResults.get("hotels").isEmpty());
        assertTrue(actualResults.get("attractions").isEmpty());
        assertTrue(actualResults.get("restaurants").isEmpty());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLocationNull(){

        when(acmSQL.getNearbyHotels(null)).thenReturn(null);
        when(acmSQL.getNearbyRestaurants(null)).thenReturn(null);
        when(acmSQL.getNearbyAttractions(null)).thenReturn(null);

        asyncNearbyPlaces.getNearbyAccommodations(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBrenchCoverage_1_2(){

        when(acmSQL.getNearbyHotels(null)).thenReturn(null);
        when(acmSQL.getNearbyRestaurants(null)).thenReturn(null);
        when(acmSQL.getNearbyAttractions(null)).thenReturn(null);

        asyncNearbyPlaces.getNearbyAccommodations(null);
    }

    @Test
    public void testBrenchCoverage_1_3_4_5_6(){

        HashMap<String,List<AccommodationModel>> actualResults;

        acmListHotels.add(new AccommodationModel("B&b Margarita","Napoli","Albergo"));
        acmListAttractions.add(new AccommodationModel("Zoo di Napoli","Napoli","Attrazione"));

        when(acmSQL.getNearbyHotels("Napoli")).thenReturn(acmListHotels);
        when(acmSQL.getNearbyRestaurants("Napoli")).thenReturn(acmListRestaurants);
        when(acmSQL.getNearbyAttractions("Napoli")).thenReturn(acmListAttractions);

        actualResults = asyncNearbyPlaces.getNearbyAccommodations("Napoli");

        assertEquals("B&b Margarita",actualResults.get("hotels").get(0).getName());
        assertEquals("Zoo di Napoli",actualResults.get("attractions").get(0).getName());
        assertTrue(actualResults.get("restaurants").isEmpty());
        assertEquals(3,actualResults.size());

    }
}