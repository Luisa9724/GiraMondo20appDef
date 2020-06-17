package com.example.giramondo20app;

import android.os.AsyncTask;
import android.util.Log;

import com.example.giramondo20app.Controller.DAO.AccommodationDAO;
import com.example.giramondo20app.Controller.DAO.MySQLAccommodationDAO;
import com.example.giramondo20app.Model.AccommodationModel;

import java.util.ArrayList;
import java.util.List;

public class AsyncHotels extends AsyncTask<Void,Void, List<AccommodationModel>> {

    OnTaskCompletedResearchResults listener;
    Integer priceRange;
    Float rating;
    String travelType;
    String subCategory;
    String city;

    AsyncHotels(OnTaskCompletedResearchResults listener, Integer price, Float rating, String travelType, String subCategory, String cityNameByPosition) {
        this.listener=listener;
        this.priceRange = price;
        this.rating = rating;
        this.travelType = travelType;
        this.subCategory = subCategory;
        city = cityNameByPosition;
        Log.d("luisa",city);
    }

    AsyncHotels(OnTaskCompletedResearchResults listener, String cityName){
        this.listener = listener;
        city = cityName;
    }

    @Override
    protected List<AccommodationModel> doInBackground(Void... params) {
        ArrayList<AccommodationModel> results = new ArrayList<>();
        AccommodationDAO acmSQL = new MySQLAccommodationDAO();
        if(priceRange > 0 && rating == 0F && travelType == "" && subCategory == "" && city != null) {
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerPriceRange(priceRange,city);
        }else if(priceRange == 0 && rating != 0F  && travelType == "" && subCategory == "" && city != null) {
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerRating(rating,city);
        }else if(priceRange == 0 && rating == 0F  && travelType != "" && subCategory == "" && city != null) {
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerTravelType(travelType,city);
        }else if(priceRange == 0 && rating == 0F  && travelType == "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerSubCategory(subCategory,city);
        }else if(priceRange > 0 && rating != 0F  && travelType == "" && subCategory == "" && city != null) {
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerPriceAndRating(priceRange, rating,city);
        }else if(priceRange > 0 && rating == 0F  && travelType != "" && subCategory == "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerTravelAndPrice(travelType, priceRange,city);
        }else if(priceRange > 0 && rating == 0F  && travelType == "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerPriceAndSubCategory(priceRange, subCategory,city);
        }else if(priceRange == 0 && rating != 0F  && travelType != "" && subCategory == "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerTravelAndRating(travelType, rating,city);
        }else if(priceRange == 0 && rating != 0F  && travelType == "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerRatingAndSubCategory(rating, subCategory,city);
        }else if(priceRange == 0 && rating == 0F  && travelType != "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerTravelAndSubCategory(travelType, subCategory,city);
        }else if(priceRange > 0 && rating != 0F  && travelType != "" && subCategory == "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerTravelPriceRating(travelType, priceRange,rating,city);
        }else if(priceRange == 0 && rating != 0F  && travelType != "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerRatingSubCategoryTravel(rating, subCategory,travelType,city);
        }else if(priceRange > 0 && rating != 0F  && travelType == "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerPriceRatingSubCategory(priceRange, rating,subCategory,city);
        }else if(priceRange > 0 && rating == 0F  && travelType != "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerTravelPriceSubCategory(travelType,priceRange,subCategory,city);
        }else if(priceRange > 0 && rating != 0F  && travelType != "" && subCategory != "" && city != null) {
            results = (ArrayList<AccommodationModel>) acmSQL.getHotelsPerTravelPriceRatingSubCategory(travelType, priceRange, rating, subCategory,city);
        }else if(priceRange == 0 && rating == 0F  && travelType == "" && subCategory == "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getFirstHotelsByPosition(city);
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
