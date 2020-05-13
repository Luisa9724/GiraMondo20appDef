package com.example.giramondo20app.Controller.DAO;

import com.example.giramondo20app.Model.AccommodationModel;

import java.util.List;

public interface AccommodationDAO {

    AccommodationModel getAccommodationByName(String acmName);
    List<AccommodationModel> getAcmsMostlyPopular();
    List<AccommodationModel> getAcmsByUserEmail(String userEmail);
    void insertFavourite(String userEmail, String acmName);
    void deleteFavourite(String userEmail,String acmName);
    List<AccommodationModel> getNearbyHotels(String city);
    List<AccommodationModel> getNearbyRestaurants(String city);
    List<AccommodationModel> getNearbyAttractions(String city);
    List<AccommodationModel> getAttractionsPerPriceRange(Integer priceRange,String city);
    List<AccommodationModel> getAttractionsPerRating(Float rating,String city);
    List<AccommodationModel> getAttractionsPerSubCategory(String subCategory,String city);
    List<AccommodationModel> getAttractionsPerTravelType(String travelType,String city);
    List<AccommodationModel> getAttractionsPerPriceAndRating(Integer priceRange,Float rating,String city);
    List<AccommodationModel> getAttractionsPerTravelAndPrice(String travelType, Integer priceRange,String city);
    List<AccommodationModel> getAttractionsPerRatingAndSubCategory(Float rating, String subCategory,String city);
    List<AccommodationModel> getAttractionsPerTravelAndRating(String travelType, Float rating,String city);
    List<AccommodationModel> getAttractionsPerTravelAndSubCategory(String travelType,String subCategory,String city);
    List<AccommodationModel> getAttractionsPerPriceAndSubCategory(Integer priceRange, String subCategory,String city);
    List<AccommodationModel> getAttractionsPerTravelPriceRating(String travelType, Integer priceRange, Float rating,String city);
    List<AccommodationModel> getAttractionsPerTravelPriceSubCategory(String travelType, Integer priceRange, String subCategory,String city);
    List<AccommodationModel> getAttractionsPerPriceRatingSubCategory(Integer priceRange, Float rating, String subCategory,String city);
    List<AccommodationModel> getAttractionsPerRatingSubCategoryTravel(Float rating, String subCategory,String travelType,String city);
    List<AccommodationModel> getAttractionsPerTravelPriceRatingSubCategory(String travelType, Integer priceRange, Float rating, String subCategory,String city);
    List<AccommodationModel> getFirstAttractionsByPosition(String city);
    List<AccommodationModel> getRestaurantsPerPriceRange(Integer priceRange,String city);
    List<AccommodationModel> getRestaurantsPerRating(Float rating,String city);
    List<AccommodationModel> getRestaurantsPerSubCategory(String subCategory,String city);
    List<AccommodationModel> getRestaurantsPerTravelType(String travelType,String city);
    List<AccommodationModel> getRestaurantsPerPriceAndRating(Integer priceRange,Float rating,String city);
    List<AccommodationModel> getRestaurantsPerTravelAndPrice(String travelType, Integer priceRange,String city);
    List<AccommodationModel> getRestaurantsPerRatingAndSubCategory(Float rating, String subCategory,String city);
    List<AccommodationModel> getRestaurantsPerTravelAndRating(String travelType, Float rating,String city);
    List<AccommodationModel> getRestaurantsPerTravelAndSubCategory(String travelType,String subCategory,String city);
    List<AccommodationModel> getRestaurantsPerPriceAndSubCategory(Integer priceRange, String subCategory,String city);
    List<AccommodationModel> getRestaurantsPerTravelPriceRating(String travelType, Integer priceRange, Float rating,String city);
    List<AccommodationModel> getRestaurantsPerTravelPriceSubCategory(String travelType, Integer priceRange, String subCategory,String city);
    List<AccommodationModel> getRestaurantsPerPriceRatingSubCategory(Integer priceRange, Float rating, String subCategory,String city);
    List<AccommodationModel> getRestaurantsPerRatingSubCategoryTravel(Float rating, String subCategory,String travelType,String city);
    List<AccommodationModel> getRestaurantsPerTravelPriceRatingSubCategory(String travelType, Integer priceRange, Float rating, String subCategory,String city);
    List<AccommodationModel> getFirstRestaurantsByPosition(String city);
    List<AccommodationModel> getHotelsPerPriceRange(Integer priceRange,String city);
    List<AccommodationModel> getHotelsPerRating(Float rating,String city);
    List<AccommodationModel> getHotelsPerSubCategory(String subCategory,String city);
    List<AccommodationModel> getHotelsPerTravelType(String travelType,String city);
    List<AccommodationModel> getHotelsPerPriceAndRating(Integer priceRange,Float rating,String city);
    List<AccommodationModel> getHotelsPerTravelAndPrice(String travelType, Integer priceRange,String city);
    List<AccommodationModel> getHotelsPerRatingAndSubCategory(Float rating, String subCategory,String city);
    List<AccommodationModel> getHotelsPerTravelAndRating(String travelType, Float rating,String city);
    List<AccommodationModel> getHotelsPerTravelAndSubCategory(String travelType,String subCategory,String city);
    List<AccommodationModel> getHotelsPerPriceAndSubCategory(Integer priceRange, String subCategory,String city);
    List<AccommodationModel> getHotelsPerTravelPriceRating(String travelType, Integer priceRange, Float rating,String city);
    List<AccommodationModel> getHotelsPerTravelPriceSubCategory(String travelType, Integer priceRange, String subCategory,String city);
    List<AccommodationModel> getHotelsPerPriceRatingSubCategory(Integer priceRange, Float rating, String subCategory,String city);
    List<AccommodationModel> getHotelsPerRatingSubCategoryTravel(Float rating, String subCategory,String travelType,String city);
    List<AccommodationModel> getHotelsPerTravelPriceRatingSubCategory(String travelType, Integer priceRange, Float rating, String subCategory,String city);
    List<AccommodationModel> getFirstHotelsByPosition(String city);
}
