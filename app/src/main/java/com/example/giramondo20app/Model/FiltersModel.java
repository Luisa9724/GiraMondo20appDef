package com.example.giramondo20app.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FiltersModel {

    private HashMap<String,ArrayList<String>> StructureCategory;

    private ArrayList<String> TypeTravel;
    private List<Integer> PriceRange;
    private int[] rating={1,2,3,4,5};


    public FiltersModel(){

        TypeTravel= new ArrayList<String>();
                TypeTravel.add("city");
                TypeTravel.add("relax");
                TypeTravel.add("culture");
                TypeTravel.add("withFriends");
                TypeTravel.add("withFamily");
                TypeTravel.add("weekend");
        StructureCategory = new HashMap<String, ArrayList<String>>();
        ArrayList<String> subCategory = new ArrayList<String>();
                subCategory.add("restaurant");
                subCategory.add("barAndPub");
                subCategory.add("iceCreamShop");
                subCategory.add("teaAndCoffee");
                StructureCategory.put("TypeRestaurant",subCategory);

        subCategory= new ArrayList<String>();
                subCategory.add("hotel");
                subCategory.add("bAndB");
                subCategory.add("hostel");
                subCategory.add("flat");
                StructureCategory.put("TypeHotel",subCategory);

        subCategory= new ArrayList<String>();
                subCategory.add("museum");
                subCategory.add("shopping");
                subCategory.add("stadio");
                subCategory.add("parkAndNature");
                subCategory.add("concertsAndShows");
                subCategory.add("zooAndAquarium");
                subCategory.add("funAndEnterteinment");
                subCategory.add("spa");
                StructureCategory.put("TypeAttraction",subCategory);





    }

    public HashMap<String, ArrayList<String>> getStructureCategory() {
        return StructureCategory;
    }

    public void setStructureCategory(HashMap<String, ArrayList<String>> structureCategory) {
        StructureCategory = structureCategory;
    }

    public ArrayList<String> getTypeTravel() {
        return TypeTravel;
    }

    public void setTypeTravel(ArrayList<String> typeTravel) {
        TypeTravel = typeTravel;
    }

    public List<Integer> getPriceRange() {
        return PriceRange;
    }

    public void setPriceRange(List<Integer> priceRange) {
        PriceRange = priceRange;
    }

    public int[] getRating() {
        return rating;
    }

    public void setRating(int[] rating) {
        this.rating = rating;
    }
}
