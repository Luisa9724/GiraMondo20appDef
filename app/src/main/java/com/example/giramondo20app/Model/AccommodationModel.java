package com.example.giramondo20app.Model;


import java.util.List;

public class AccommodationModel {
    private String Name;
    private double Rating;
    private int Price;
    private String Description;
    private String Services;
    private boolean Prefered;
    private String City;
    private String State;
    private List<PhotoModel> Photos;
    private int[] Stars ={1,2,3,4,5};

    public AccommodationModel(String name,double rating, int price) {
        Name = name;
        Rating = rating;
        Price = price;
    }

    public AccommodationModel(String name,double rating, int price, String description, String services, boolean prefered, String city, String state, List<PhotoModel> photos, int[] stars) {
        Name = name;
        Rating = rating;
        Price = price;
        Description = description;
        Services = services;
        Prefered = prefered;
        City = city;
        State = state;
        Photos = photos;
        Stars = stars;
    }

    public AccommodationModel(String name, double rating, int price, String description, String services, boolean prefered, String city, String state, List<PhotoModel> photos) {
        Name = name;
        Rating = rating;
        Price = price;
        Description = description;
        Services = services;
        Prefered = prefered;
        City = city;
        State = state;
        Photos = photos;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getRating() {
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

    public List<PhotoModel> getPhotos() {
        return Photos;
    }

    public void setPhotos(List<PhotoModel> photos) {
        Photos = photos;
    }

    public int[] getStars() {
        return Stars;
    }

    public void setStars(int[] stars) {
        Stars = stars;
    }
}