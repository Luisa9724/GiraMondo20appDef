package com.example.giramondo20app.test;



import com.example.giramondo20app.AsyncAccommodations;
import com.example.giramondo20app.Model.AccommodationModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class AsyncAccommodationsTest {


    //Class to be tested
    private AsyncAccommodations asyncAccommodations;

    private List<AccommodationModel> listAcm;

    @Before
    public void setUp() {
        asyncAccommodations = new AsyncAccommodations();
        listAcm = new ArrayList<>();
    }

    @Test
    public void listWithAllAccommodations(){

        HashMap<String,List<AccommodationModel>> actualResults;

        listAcm.add(new AccommodationModel("B&b Margarita","Fuorigrotta","Albergo"));
        listAcm.add(new AccommodationModel("Alicella Ristorante","Fuorigrotta","Ristorante"));
        listAcm.add(new AccommodationModel("Zoo di Napoli","Napoli","Attrazione"));

        actualResults = asyncAccommodations.getMostPopularAccommodations(listAcm);

        assertEquals("B&b Margarita",actualResults.get("hotels").get(0).getName());
        assertEquals("Alicella Ristorante",actualResults.get("restaurants").get(0).getName());
        assertEquals("Zoo di Napoli",actualResults.get("attractions").get(0).getName());
        assertEquals(3,actualResults.size());
    }

    @Test
    public void listWithOnlyHotels(){

        HashMap<String,List<AccommodationModel>> actualResults;

        listAcm.add(new AccommodationModel("B&b Margarita","Fuorigrotta","Albergo"));
        listAcm.add(new AccommodationModel("Hotel Experiece","Napoli","Albergo"));
        listAcm.add(new AccommodationModel("Hostel Ginevra","Napoli","Albergo"));

        actualResults = asyncAccommodations.getMostPopularAccommodations(listAcm);

        assertEquals("B&b Margarita",actualResults.get("hotels").get(0).getName());
        assertEquals("Hotel Experiece",actualResults.get("hotels").get(1).getName());
        assertEquals("Hostel Ginevra",actualResults.get("hotels").get(2).getName());
        assertEquals(1,actualResults.size());
    }

    @Test
    public void listWithOnlyRestaurants(){

        HashMap<String,List<AccommodationModel>> actualResults;

        listAcm.add(new AccommodationModel("Alicella Ristorante","Fuorigrotta","Ristorante"));
        listAcm.add(new AccommodationModel("Yao Restaurant","Napoli","Ristorante"));

        actualResults = asyncAccommodations.getMostPopularAccommodations(listAcm);

        assertEquals(true,actualResults.keySet().contains("restaurants"));
        assertEquals(1,actualResults.size());
    }

    @Test
    public void listWithOnlyAttractions(){

        HashMap<String,List<AccommodationModel>> actualResults;

        listAcm.add(new AccommodationModel("Edenlandia","Fuorigrotta","Attrazione"));
        listAcm.add(new AccommodationModel("Zoo di Napoli","Napoli","Attrazione"));

        actualResults = asyncAccommodations.getMostPopularAccommodations(listAcm);

        assertEquals(true,actualResults.keySet().contains("attractions"));
        assertEquals(1,actualResults.size());
    }

    @Test
    public void listAccommodationsLenghtZero(){

        HashMap<String,List<AccommodationModel>> actualResults;

        actualResults = asyncAccommodations.getMostPopularAccommodations(listAcm);

        assertEquals(0,actualResults.size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void listAccommodationsNullValue(){

        listAcm = null;

        asyncAccommodations.getMostPopularAccommodations(listAcm);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBrenchCoverage_1_2(){
        listAcm = null;

        asyncAccommodations.getMostPopularAccommodations(listAcm);
    }

    @Test
    public void testBrenchCoverage_1_3_4_5_3_4_6_7_3_4_6_8_9_3_10_11_12_13_14_15_16(){
        HashMap<String,List<AccommodationModel>> actualResults;

        listAcm.add(new AccommodationModel("B&b Margarita","Fuorigrotta","Albergo"));
        listAcm.add(new AccommodationModel("Alicella Ristorante","Fuorigrotta","Ristorante"));
        listAcm.add(new AccommodationModel("Zoo di Napoli","Napoli","Attrazione"));

        actualResults = asyncAccommodations.getMostPopularAccommodations(listAcm);

        assertEquals("B&b Margarita",actualResults.get("hotels").get(0).getName());
        assertEquals("Alicella Ristorante",actualResults.get("restaurants").get(0).getName());
        assertEquals("Zoo di Napoli",actualResults.get("attractions").get(0).getName());
        assertEquals(3,actualResults.size());
    }

    @Test
    public void testBrenchCoverage_1_3_10_12_14_16(){

        HashMap<String,List<AccommodationModel>> actualResults;

        actualResults = asyncAccommodations.getMostPopularAccommodations(listAcm);

        assertEquals(0,actualResults.size());
    }

    @Test
    public void testBrenchCoverage_1_3_4_5_3_4_5_3_10_11_12_14_16(){
        HashMap<String,List<AccommodationModel>> actualResults;

        listAcm.add(new AccommodationModel("B&b Margarita","Fuorigrotta","Albergo"));
        listAcm.add(new AccommodationModel("Hotel Experiece","Napoli","Albergo"));

        actualResults = asyncAccommodations.getMostPopularAccommodations(listAcm);

        assertEquals("B&b Margarita",actualResults.get("hotels").get(0).getName());
        assertEquals("Hotel Experiece",actualResults.get("hotels").get(1).getName());
        assertEquals(1,actualResults.size());
    }
}