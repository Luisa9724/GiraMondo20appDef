package com.example.giramondo20app.Controller.DAO;

import com.example.giramondo20app.Controller.DatabaseController;
import com.example.giramondo20app.Model.AccommodationModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLAccommodationDAO implements AccommodationDAO {

    public List<AccommodationModel> getAccomodationsPerPriceRange(Integer priceRange){
        List<AccommodationModel> Results = new ArrayList<>();
        DatabaseController.connect();
        if(priceRange<500) {
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE FROM ACCOMMODATION WHERE PRICE <= ? ORDER BY RATING");
                ps.setInt(1, priceRange);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Results.add(new AccommodationModel(rs.getString(1), rs.getDouble(2), rs.getInt(3)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(priceRange==500){
            try {
                PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT NAME,RATING,PRICE FROM ACCOMMODATION WHERE PRICE >= ? ORDER BY RATING");
                ps.setInt(1, priceRange);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Results.add(new AccommodationModel(rs.getString(1), rs.getDouble(2), rs.getInt(3)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
            DatabaseController.disconnect();
        return Results;
    }
}
