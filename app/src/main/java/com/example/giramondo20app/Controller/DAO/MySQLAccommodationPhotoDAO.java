package com.example.giramondo20app.Controller.DAO;

import com.example.giramondo20app.Controller.DatabaseController;
import com.example.giramondo20app.Model.AccommodationPhotoModel;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLAccommodationPhotoDAO implements AccommodationPhotoDAO {
    @Override
    public List<AccommodationPhotoModel> getAccommodationPhotos(String name) {
        List<AccommodationPhotoModel> Results = new ArrayList<>();
        DatabaseController.connect();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT FILE FROM PHOTO WHERE ACCOMMODATIONNAME = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob imageBlob = rs.getBlob(1);
                byte[] imageBytes = imageBlob.getBytes(1, (int)imageBlob.length());
                Results.add(new AccommodationPhotoModel(imageBytes));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return Results;
    }
}
