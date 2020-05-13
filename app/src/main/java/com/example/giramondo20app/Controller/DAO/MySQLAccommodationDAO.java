package com.example.giramondo20app.Controller.DAO;



import com.example.giramondo20app.Controller.DatabaseController;
import com.example.giramondo20app.Model.AccommodationModel;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLAccommodationDAO implements AccommodationDAO {

    private byte[] imageBytes;

    @Override
    public AccommodationModel getAccommodationByName(String acmName) {
        AccommodationModel result = null;
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT  ACCOMMODATIONTYPE,ADDRESS,CITY,DESCRIPTION,NAME,PHOTO,PRICE,RATING,SERVICES, STATE FROM ACCOMMODATION WHERE TRIM(NAME) = ?");
            ps.setString(1,acmName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob("Photo");
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                result = new AccommodationModel(rs.getString("Name"),rs.getFloat("Rating"),rs.getInt("Price"),imageBytes,rs.getString("Description"),rs.getString("City"),rs.getString("State"),rs.getString("Address"),rs.getString("Services"),rs.getString("AccommodationType"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return result;
    }

    @Override
    public List<AccommodationModel> getAcmsMostlyPopular() {
        List<AccommodationModel> results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT DISTINCT ACCOMMODATIONTYPE,ADDRESS,CITY,DESCRIPTION,NAME,PHOTO,PRICE,RATING,SERVICES, STATE FROM ACCOMMODATION WHERE RATING >=4 GROUP BY STATE ORDER BY RATING");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob("Photo");
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                results.add(new AccommodationModel(rs.getString("Name"),rs.getFloat("Rating"),rs.getInt("Price"),imageBytes,rs.getString("Description"),rs.getString("City"),rs.getString("State"),rs.getString("Address"),rs.getString("Services"),rs.getString("AccommodationType")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return results;
    }

    @Override
    public List<AccommodationModel> getAcmsByUserEmail(String userEmail) {
        List<AccommodationModel> results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT DISTINCT A.ACCOMMODATIONTYPE,A.ADDRESS,A.CITY,A.DESCRIPTION,A.NAME,A.PHOTO,A.PRICE,A.RATING,A.SERVICES,A.STATE FROM (FAVOURITEACCOMMODATION AS FA JOIN USER ON USEREMAIL = ?) JOIN ACCOMMODATION AS A ON FA.ACCOMMODATIONNAME = A.NAME");
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob("Photo");
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                results.add(new AccommodationModel(rs.getString("Name"),rs.getFloat("Rating"),rs.getInt("Price"),imageBytes,rs.getString("Description"),rs.getString("City"),rs.getString("State"),rs.getString("Address"),rs.getString("Services"),rs.getString("AccommodationType")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return results;
    }

    public void insertFavourite(String userEmail, String acmName){
    DatabaseController.connect();
    try {
        PreparedStatement ps = DatabaseController.getConnection().prepareStatement("INSERT INTO FAVOURITEACCOMMODATION VALUES(?,?)");
        ps.setString(1, userEmail);
        ps.setString(2, acmName);
        ps.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    DatabaseController.disconnect();
}

    @Override
    public void deleteFavourite(String userEmail, String acmName) {
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("DELETE FROM FAVOURITEACCOMMODATION WHERE ACCOMMODATIONNAME = ? AND USEREMAIL = ?");
            ps.setString(1, acmName);
            ps.setString(2, userEmail);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
    }

    public List<AccommodationModel> getNearbyHotels(String city){
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME, ADDRESS FROM ACCOMMODATION WHERE (CITY = ?) AND (ACCOMMODATIONTYPE = 'ALBERGO')");
            ps.setString(1, city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Results.add(new AccommodationModel(rs.getString("Name"),rs.getString("Address")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    public List<AccommodationModel> getNearbyRestaurants(String city){
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME, ADDRESS FROM ACCOMMODATION WHERE (CITY = ?) AND (ACCOMMODATIONTYPE = 'RISTORANTE')");
            ps.setString(1, city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Results.add(new AccommodationModel(rs.getString("Name"),rs.getString("Address")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    public List<AccommodationModel> getNearbyAttractions(String city){
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME, ADDRESS FROM ACCOMMODATION WHERE (CITY = ?) AND (ACCOMMODATIONTYPE = 'ATTRAZIONE')");
            ps.setString(1, city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Results.add(new AccommodationModel(rs.getString("Name"),rs.getString("Address")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerPriceRange(Integer priceRange,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (PRICE <= ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(priceRange==500){
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (PRICE >= ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerRating(Float rating,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setFloat(1, rating);
            ps.setString(2,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerSubCategory(String subCategory, String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE,ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setString(1, subCategory);
            ps.setString(2,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerTravelType(String travelType,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE,ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (TRAVELTYPE = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setString(1, travelType);
            ps.setString(2,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerPriceAndRating(Integer priceRange, Float rating,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE,ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (PRICE <= ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setFloat(2, rating);
                ps.setString(3,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (PRICE >= ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setFloat(2,rating);
                    ps.setString(3,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerTravelAndPrice(String travelType, Integer priceRange, String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (PRICE <= ?) AND (TRAVELTYPE = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2, travelType);
                ps.setString(3,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE,ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (PRICE >= ?) AND (TRAVELTYPE = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setString(2,travelType);
                    ps.setString(3,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerRatingAndSubCategory(Float rating, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setFloat(1, rating);
            ps.setString(2, subCategory);
            ps.setString(3,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerTravelAndRating(String travelType, Float rating,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (TRAVELTYPE = ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setString(1, travelType);
            ps.setFloat(2,rating);
            ps.setString(3,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerTravelAndSubCategory(String travelType, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (TRAVELTYPE = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setString(1, travelType);
            ps.setString(2,subCategory);
            ps.setString(3,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerPriceAndSubCategory(Integer priceRange, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (PRICE <= ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2, subCategory);
                ps.setString(3,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE,ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (PRICE >= ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setString(2,subCategory);
                    ps.setString(3,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerTravelPriceRating(String travelType, Integer priceRange, Float rating,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (PRICE <= ?) AND (TRAVELTYPE = ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2, travelType);
                ps.setFloat(3,rating);
                ps.setString(4,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (PRICE >= ?) AND (TRAVELTYPE = ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setString(2,travelType);
                    ps.setFloat(3,rating);
                    ps.setString(4,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerTravelPriceSubCategory(String travelType, Integer priceRange, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (TRAVELTYPE = ?)  AND  (PRICE <= ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setString(1,travelType);
                ps.setInt(2, priceRange);
                ps.setString(3, subCategory);
                ps.setString(4,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (TRAVELTYPE = ?) AND (PRICE >= ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setString(1,travelType);
                    ps.setInt(2, priceRange);
                    ps.setString(3, subCategory);
                    ps.setString(4,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerPriceRatingSubCategory(Integer priceRange, Float rating, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (PRICE <= ?) AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setFloat(2,rating);
                ps.setString(3, subCategory);
                ps.setString(4,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (PRICE >= ?) AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setFloat(2,rating);
                    ps.setString(3, subCategory);
                    ps.setString(4,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerRatingSubCategoryTravel(Float rating, String subCategory, String travelType,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (RATING= ?) AND (SUBCATEGORYHOTEL = ?) AND (TRAVELTYPE = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setFloat(1, rating);
            ps.setString(2,subCategory);
            ps.setString(3,travelType);
            ps.setString(4,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getAttractionsPerTravelPriceRatingSubCategory(String travelType, Integer priceRange, Float rating, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (TRAVELTYPE = ?)  AND  (PRICE <= ?) AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setString(1,travelType);
                ps.setInt(2, priceRange);
                ps.setFloat(3,rating);
                ps.setString(4, subCategory);
                ps.setString(5,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (TRAVELTYPE = ?) AND (PRICE >= ?) AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setString(1,travelType);
                    ps.setInt(2, priceRange);
                    ps.setFloat(3,rating);
                    ps.setString(4, subCategory);
                    ps.setString(5,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getFirstAttractionsByPosition(String city) {
        int i;
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ATTRAZIONE') AND (CITY = ?)");
            ps.setString(1,city);
            ResultSet rs = ps.executeQuery();
            for(i=0;i<10;i++) {
                if(rs.next()){
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerPriceRange(Integer priceRange,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (PRICE <= ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(priceRange==500){
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (PRICE >= ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerRating(Float rating,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setFloat(1, rating);
            ps.setString(2,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerSubCategory(String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setString(1, subCategory);
            ps.setString(2,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerTravelType(String travelType,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (TRAVELTYPE = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setString(1, travelType);
            ps.setString(2,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerPriceAndRating(Integer priceRange, Float rating,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (PRICE <= ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setFloat(2, rating);
                ps.setString(3,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (PRICE >= ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setFloat(2,rating);
                    ps.setString(3,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerTravelAndPrice(String travelType, Integer priceRange,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (PRICE <= ?) AND (TRAVELTYPE = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2, travelType);
                ps.setString(3,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (PRICE >= ?) AND (TRAVELTYPE = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setString(2,travelType);
                    ps.setString(3,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerRatingAndSubCategory(Float rating, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setFloat(1, rating);
            ps.setString(2, subCategory);
            ps.setString(3,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerTravelAndRating(String travelType, Float rating,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (TRAVELTYPE = ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setString(1, travelType);
            ps.setFloat(2,rating);
            ps.setString(3,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerTravelAndSubCategory(String travelType, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (TRAVELTYPE = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setString(1, travelType);
            ps.setString(2,subCategory);
            ps.setString(3,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerPriceAndSubCategory(Integer priceRange, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (PRICE <= ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2, subCategory);
                ps.setString(3,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (PRICE >= ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setString(2,subCategory);
                    ps.setString(3,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerTravelPriceRating(String travelType, Integer priceRange, Float rating,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (PRICE <= ?) AND (TRAVELTYPE = ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2, travelType);
                ps.setFloat(3,rating);
                ps.setString(4,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (PRICE >= ?) AND (TRAVELTYPE = ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setString(2,travelType);
                    ps.setFloat(3,rating);
                    ps.setString(4,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerTravelPriceSubCategory(String travelType, Integer priceRange, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (TRAVELTYPE = ?)  AND  (PRICE <= ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setString(1,travelType);
                ps.setInt(2, priceRange);
                ps.setString(3, subCategory);
                ps.setString(4,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (TRAVELTYPE = ?) AND (PRICE >= ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setString(1,travelType);
                    ps.setInt(2, priceRange);
                    ps.setString(3, subCategory);
                    ps.setString(4,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerPriceRatingSubCategory(Integer priceRange, Float rating, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (PRICE <= ?) AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setFloat(2,rating);
                ps.setString(3, subCategory);
                ps.setString(4,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (PRICE >= ?) AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setFloat(2,rating);
                    ps.setString(3, subCategory);
                    ps.setString(4,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerRatingSubCategoryTravel(Float rating, String subCategory, String travelType,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (RATING= ?) AND (SUBCATEGORYHOTEL = ?) AND (TRAVELTYPE = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setFloat(1, rating);
            ps.setString(2,subCategory);
            ps.setString(3,travelType);
            ps.setString(4,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getRestaurantsPerTravelPriceRatingSubCategory(String travelType, Integer priceRange, Float rating, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (TRAVELTYPE = ?)  AND  (PRICE <= ?) AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setString(1,travelType);
                ps.setInt(2, priceRange);
                ps.setFloat(3,rating);
                ps.setString(4, subCategory);
                ps.setString(5,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (TRAVELTYPE = ?) AND (PRICE >= ?) AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setString(1,travelType);
                    ps.setInt(2, priceRange);
                    ps.setFloat(3,rating);
                    ps.setString(4, subCategory);
                    ps.setString(5,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getFirstRestaurantsByPosition(String city) {
        int i;
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'RISTORANTE') AND (CITY = ?)");
            ps.setString(1,city);
            ResultSet rs = ps.executeQuery();
            for(i=0;i<10;i++) {
                if(rs.next()){
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    public List<AccommodationModel> getHotelsPerPriceRange(Integer priceRange,String city){
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (PRICE <= ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                   Blob imageBlob = rs.getBlob(4);
                   imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                   Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(priceRange==500){
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (PRICE >= ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        DatabaseController.disconnect();
        return Results;
    }

    public List<AccommodationModel> getHotelsPerRating(Float rating,String city){
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setFloat(1, rating);
            ps.setString(2,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }


    public List<AccommodationModel> getHotelsPerSubCategory(String subCategory,String city){
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setString(1, subCategory);
            ps.setString(2,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }



    public List<AccommodationModel> getHotelsPerTravelType(String travelType,String city){
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (TRAVELTYPE = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setString(1, travelType);
            ps.setString(2,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getHotelsPerPriceAndRating(Integer priceRange, Float rating,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (PRICE <= ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setFloat(2, rating);
                ps.setString(3,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes, rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (PRICE >= ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setFloat(2,rating);
                    ps.setString(3,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes, rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    public List<AccommodationModel> getHotelsPerTravelAndPrice(String travelType, Integer priceRange,String city){
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (PRICE <= ?) AND (TRAVELTYPE = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2, travelType);
                ps.setString(3,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (PRICE >= ?) AND (TRAVELTYPE = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setString(2,travelType);
                    ps.setString(3,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getHotelsPerRatingAndSubCategory(Float rating, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setFloat(1, rating);
            ps.setString(2, subCategory);
            ps.setString(3,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getHotelsPerTravelAndRating(String travelType, Float rating,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (TRAVELTYPE = ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setString(1, travelType);
            ps.setFloat(2,rating);
            ps.setString(3,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getHotelsPerTravelAndSubCategory(String travelType, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (TRAVELTYPE = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setString(1, travelType);
            ps.setString(2,subCategory);
            ps.setString(3,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getHotelsPerPriceAndSubCategory(Integer priceRange, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (PRICE <= ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2, subCategory);
                ps.setString(3,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (PRICE >= ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setString(2,subCategory);
                    ps.setString(3,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getHotelsPerTravelPriceRating(String travelType, Integer priceRange, Float rating,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (PRICE <= ?) AND (TRAVELTYPE = ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setString(2, travelType);
                ps.setFloat(3,rating);
                ps.setString(4,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (PRICE >= ?) AND (TRAVELTYPE = ?) AND (RATING = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setString(2,travelType);
                    ps.setFloat(3,rating);
                    ps.setString(4,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getHotelsPerTravelPriceSubCategory(String travelType, Integer priceRange, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (TRAVELTYPE = ?)  AND  (PRICE <= ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setString(1,travelType);
                ps.setInt(2, priceRange);
                ps.setString(3, subCategory);
                ps.setString(4,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (TRAVELTYPE = ?) AND (PRICE >= ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setString(1,travelType);
                    ps.setInt(2, priceRange);
                    ps.setString(3, subCategory);
                    ps.setString(4,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getHotelsPerPriceRatingSubCategory(Integer priceRange, Float rating, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (PRICE <= ?) AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setInt(1, priceRange);
                ps.setFloat(2,rating);
                ps.setString(3, subCategory);
                ps.setString(4,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (PRICE >= ?) AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setInt(1, priceRange);
                    ps.setFloat(2,rating);
                    ps.setString(3, subCategory);
                    ps.setString(4,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getHotelsPerRatingSubCategoryTravel(Float rating, String subCategory, String travelType,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (RATING= ?) AND (SUBCATEGORYHOTEL = ?) AND (TRAVELTYPE = ?) AND (CITY = ?) ORDER BY RATING");
            ps.setFloat(1, rating);
            ps.setString(2,subCategory);
            ps.setString(3,travelType);
            ps.setString(4,city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(4);
                imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3),imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getHotelsPerTravelPriceRatingSubCategory(String travelType, Integer priceRange, Float rating, String subCategory,String city) {
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (TRAVELTYPE = ?)  AND  (PRICE <= ?) AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                ps.setString(1,travelType);
                ps.setInt(2, priceRange);
                ps.setFloat(3,rating);
                ps.setString(4, subCategory);
                ps.setString(5,city);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Blob imageBlob = rs.getBlob(4);
                    imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes, rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(priceRange==500){
                try {
                    PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (TRAVELTYPE = ?) AND (PRICE >= ?) AND (RATING = ?) AND (SUBCATEGORYHOTEL = ?) AND (CITY = ?) ORDER BY RATING");
                    ps.setString(1,travelType);
                    ps.setInt(2, priceRange);
                    ps.setFloat(3,rating);
                    ps.setString(4, subCategory);
                    ps.setString(5,city);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        DatabaseController.disconnect();
        return Results;
    }

    @Override
    public List<AccommodationModel> getFirstHotelsByPosition(String city) {
        int i;
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE,PHOTO,DESCRIPTION, CITY, STATE, ADDRESS, SERVICES FROM ACCOMMODATION WHERE (ACCOMMODATIONTYPE = 'ALBERGO') AND (CITY = ?)");
                ps.setString(1,city);
                ResultSet rs = ps.executeQuery();
                for(i=0;i<10;i++) {
                    if(rs.next()){
                        Blob imageBlob = rs.getBlob(4);
                        imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                        Results.add(new AccommodationModel(rs.getString(1), rs.getFloat(2), rs.getInt(3), imageBytes,rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
                    }
                    }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        DatabaseController.disconnect();
        return Results;
    }
}