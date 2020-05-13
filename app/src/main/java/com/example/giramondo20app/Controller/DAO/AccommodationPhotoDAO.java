package com.example.giramondo20app.Controller.DAO;

import com.example.giramondo20app.Model.AccommodationPhotoModel;

import java.util.List;

public interface AccommodationPhotoDAO {

    List<AccommodationPhotoModel> getAccommodationPhotos(String name);
}
