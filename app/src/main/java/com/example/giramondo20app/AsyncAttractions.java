package com.example.giramondo20app;

import android.os.AsyncTask;

import com.example.giramondo20app.Controller.DAO.AccommodationDAO;
import com.example.giramondo20app.Controller.DAO.MySQLAccommodationDAO;
import com.example.giramondo20app.Model.AccommodationModel;

import java.util.ArrayList;
import java.util.List;

public class AsyncAttractions extends AsyncTask<Void,Void,List<AccommodationModel>> {

    OnTaskCompletedResearchResults listener;
    Integer priceRange;
    Float rating;
    String travelType;
    String subCategory;
    String city;

    AsyncAttractions(OnTaskCompletedResearchResults listener, Integer price, Float rating, String travelType, String subCategory, String cityName) {
        this.listener=listener;
        this.priceRange = price;
        this.rating = rating;
        this.travelType = travelType;
        this.subCategory = subCategory;
        city = cityName;
    }


    @Override
    protected List<AccommodationModel> doInBackground(Void... params) {
        ArrayList<AccommodationModel> results = new ArrayList<>();
        AccommodationDAO acmSQL = new MySQLAccommodationDAO();
        if(priceRange > 0 && rating == 0F && travelType == "" && subCategory == "" && city != null) {
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerPriceRange(priceRange, city);
        }else if(priceRange == 0 && rating != 0F  && travelType == "" && subCategory == "" && city != null) {
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerRating(rating,city);
        }else if(priceRange == 0 && rating == 0F  && travelType != "" && subCategory == "" && city != null) {
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerTravelType(travelType,city);
        }else if(priceRange == 0 && rating == 0F  && travelType == "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerSubCategory(subCategory,city);
        }else if(priceRange > 0 && rating != 0F  && travelType == "" && subCategory == "" && city != null) {
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerPriceAndRating(priceRange, rating,city);
        }else if(priceRange > 0 && rating == 0F  && travelType != "" && subCategory == "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerTravelAndPrice(travelType, priceRange,city);
        }else if(priceRange > 0 && rating == 0F  && travelType == "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerPriceAndSubCategory(priceRange, subCategory,city);
        }else if(priceRange == 0 && rating != 0F  && travelType != "" && subCategory == "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerTravelAndRating(travelType, rating,city);
        }else if(priceRange == 0 && rating != 0F  && travelType == "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerRatingAndSubCategory(rating, subCategory,city);
        }else if(priceRange == 0 && rating == 0F  && travelType != "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerTravelAndSubCategory(travelType, subCategory,city);
        }else if(priceRange > 0 && rating != 0F  && travelType != "" && subCategory == "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerTravelPriceRating(travelType, priceRange,rating,city);
        }else if(priceRange == 0 && rating != 0F  && travelType != "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerRatingSubCategoryTravel(rating, subCategory,travelType,city);
        }else if(priceRange > 0 && rating != 0F  && travelType == "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerPriceRatingSubCategory(priceRange, rating,subCategory,city);
        }else if(priceRange > 0 && rating == 0F  && travelType != "" && subCategory != "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerTravelPriceSubCategory(travelType,priceRange,subCategory,city);
        }else if(priceRange > 0 && rating != 0F  && travelType != "" && subCategory != "" && city != null) {
            results = (ArrayList<AccommodationModel>) acmSQL.getAttractionsPerTravelPriceRatingSubCategory(travelType, priceRange, rating, subCategory,city);
        }else if(priceRange == 0 && rating == 0F  && travelType == "" && subCategory == "" && city != null){
            results = (ArrayList<AccommodationModel>) acmSQL.getFirstAttractionsByPosition(city);
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
