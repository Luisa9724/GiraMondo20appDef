package com.example.giramondo20app.Controller.DAO;

import com.example.giramondo20app.Model.AccommodationModel;

import java.util.List;

public interface AccommodationDAO {

    public List<AccommodationModel> getAccomodationsPerPriceRange(int priceRange);
}
