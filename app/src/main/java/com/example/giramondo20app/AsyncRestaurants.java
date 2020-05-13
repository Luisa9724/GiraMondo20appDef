package com.example.giramondo20app;

import android.os.AsyncTask;

import com.example.giramondo20app.Controller.DAO.AccommodationDAO;
import com.example.giramondo20app.Controller.DAO.MySQLAccommodationDAO;
import com.example.giramondo20app.Model.AccommodationModel;

import java.util.ArrayList;
import java.util.List;

public class AsyncRestaurants extends AsyncTask<Void,Void,List<AccommodationModel>> {
    OnTaskCompletedResearchResults listener;
    Integer priceRange;
    Float rating;
    String travelType;
    String subCategory;
    String city;

    AsyncRestaurants(OnTaskCompletedResearchResults listener, Integer price, Float rating, String travelType, String subCategory, String cityName) {
        this.listener=listener;
        priceRange = price;
        this.rating = rating;
        this.travelType = travelType;
        this.subCategory = subCategory;
        city = cityName;
    }


    @Override
    protected List<AccommodationModel> doInBackground(Void... params) {
        ArrayList<AccommodationModel> results = new ArrayList<>();
        AccommodationDAO acmSQL = new MySQLAccommodationDAO();
        if(priceRange > 0 && rating == 0F && travelType == "" && subCategory == "") {
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerPriceRange(priceRange,city);
        }else if(priceRange == 0 && rating != 0F  && travelType == "" && subCategory == "") {
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerRating(rating,city);
        }else if(priceRange == 0 && rating == 0F  && travelType != "" && subCategory == "") {
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerTravelType(travelType,city);
        }else if(priceRange == 0 && rating == 0F  && travelType == "" && subCategory != ""){
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerSubCategory(subCategory,city);
        }else if(priceRange > 0 && rating != 0F  && travelType == "" && subCategory == "") {
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerPriceAndRating(priceRange, rating,city);
        }else if(priceRange > 0 && rating == 0F  && travelType != "" && subCategory == ""){
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerTravelAndPrice(travelType, priceRange,city);
        }else if(priceRange > 0 && rating == 0F  && travelType == "" && subCategory != ""){
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerPriceAndSubCategory(priceRange, subCategory,city);
        }else if(priceRange == 0 && rating != 0F  && travelType != "" && subCategory == ""){
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerTravelAndRating(travelType, rating,city);
        }else if(priceRange == 0 && rating != 0F  && travelType == "" && subCategory != ""){
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerRatingAndSubCategory(rating, subCategory,city);
        }else if(priceRange == 0 && rating == 0F  && travelType != "" && subCategory != ""){
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerTravelAndSubCategory(travelType, subCategory,city);
        }else if(priceRange > 0 && rating != 0F  && travelType != "" && subCategory == ""){
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerTravelPriceRating(travelType, priceRange,rating,city);
        }else if(priceRange == 0 && rating != 0F  && travelType != "" && subCategory != ""){
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerRatingSubCategoryTravel(rating, subCategory,travelType,city);
        }else if(priceRange > 0 && rating != 0F  && travelType == "" && subCategory != ""){
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerPriceRatingSubCategory(priceRange, rating,subCategory,city);
        }else if(priceRange > 0 && rating == 0F  && travelType != "" && subCategory != ""){
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerTravelPriceSubCategory(travelType,priceRange,subCategory,city);
        }else if(priceRange > 0 && rating != 0F  && travelType != "" && subCategory != "") {
            results = (ArrayList<AccommodationModel>) acmSQL.getRestaurantsPerTravelPriceRatingSubCategory(travelType, priceRange, rating, subCategory,city);
        }else if(priceRange == 0 && rating == 0F  && travelType == "" && subCategory == ""){
            results = (ArrayList<AccommodationModel>) acmSQL.getFirstRestaurantsByPosition(city);
        }
        listener.showProgressBar();
        return results;
    }


    @Override
    protected void onPostExecute(List<AccommodationModel> results) {
        if(results.isEmpty()){
            listener.sendErrorResponse();
        }
        listener.hideProgressBar();
        listener.onTaskComplete(results);
    }
}
