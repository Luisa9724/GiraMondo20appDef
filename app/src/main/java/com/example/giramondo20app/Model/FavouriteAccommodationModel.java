package com.example.giramondo20app.Model;

public class FavouriteAccommodationModel {
    private String Name;
    private float Rating;
    private int Price;
    private byte[] Photo;
    private String Description;
    private String City;
    private String State;
    private boolean Prefered;
    private String Address;
    private String Services;
    private String TravelType;
    private String AccommodationType;
    private String SubCategoryRestaurant;
    private String SubCategoryHotel;
    private String SubCategoryAttraction;
    private int[] Stars ={1,2,3,4,5};

    public FavouriteAccommodationModel(String name, float rating, int price, byte[] photo, String description, String city, String state, boolean prefered, String address, String services) {
        Name = name;
        Rating = rating;
        Price = price;
        Photo = photo;
        Description = description;
        City = city;
        State = state;
        Prefered = prefered;
        Address = address;
        Services = services;
    }




    public FavouriteAccommodationModel(String name,float rating, int price, byte[] photo) {
        Name = name;
        Rating = rating;
        Price = price;
        Photo = photo;
    }


    public FavouriteAccommodationModel(String name,String address) {
        Name = name;
        Address = address;
    }


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTravelType() {
        return TravelType;
    }

    public void setTravelType(String travelType) {
        TravelType = travelType;
    }

    public String getAccommodationType() {
        return AccommodationType;
    }

    public void setAccommodationType(String accommodationType) {
        AccommodationType = accommodationType;
    }

    public String getSubCategoryRestaurant() {
        return SubCategoryRestaurant;
    }

    public void setSubCategoryRestaurant(String subCategoryRestaurant) {
        SubCategoryRestaurant = subCategoryRestaurant;
    }

    public String getSubCategoryHotel() {
        return SubCategoryHotel;
    }

    public void setSubCategoryHotel(String subCategoryHotel) {
        SubCategoryHotel = subCategoryHotel;
    }

    public String getSubCategoryAttraction() {
        return SubCategoryAttraction;
    }

    public void setSubCategoryAttraction(String subCategoryAttraction) {
        SubCategoryAttraction = subCategoryAttraction;
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getServices() {
        return Services;
    }

    public void setServices(String services) {
        Services = services;
    }

    public boolean isPrefered() {
        return Prefered;
    }

    public void setPrefered(boolean prefered) {
        Prefered = prefered;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public byte[] getPhoto() {
        return Photo;
    }

    public void setPhoto(byte[] photo) {
        Photo = photo;
    }

    public int[] getStars() {
        return Stars;
    }

    public void setStars(int[] stars) {
        Stars = stars;
    }
}
