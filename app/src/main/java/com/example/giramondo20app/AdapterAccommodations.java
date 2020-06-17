package com.example.giramondo20app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.giramondo20app.Model.AccommodationModel;


import java.util.ArrayList;
import java.util.List;

public class AdapterAccommodations extends ArrayAdapter<AccommodationModel> {
    private Context mContext;
    private ArrayList<AccommodationModel> accommodationsList;



    public AdapterAccommodations(@NonNull Context context, List<AccommodationModel> list) {
        super(context, 0 , list);
        mContext = context;
        accommodationsList = (ArrayList)list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

        AccommodationModel currentAccommodation = accommodationsList.get(position);


        TextView name = listItem.findViewById(R.id.name);
        name.setText(currentAccommodation.getName());


        RatingBar ratingBar =listItem.findViewById(R.id.ratingBar);
        ratingBar.setRating(currentAccommodation.getRating());

        TextView price = listItem.findViewById(R.id.price);
        price.setText("€ " + currentAccommodation.getPrice());

        ImageView imageView = listItem.findViewById(R.id.image);
        RequestOptions options = new RequestOptions()
                            .fitCenter()
                            .error(R.drawable.ic_broken_img)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true);

        Glide.with(mContext).load(currentAccommodation.getPhoto()).apply(options).into(imageView);


        return listItem;
    }

    @Override
    public int getCount() {
        return accommodationsList.size();
    }
}

