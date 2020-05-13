package com.example.giramondo20app;



import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.example.giramondo20app.Model.AccommodationModel;

import static android.content.Context.MODE_PRIVATE;


public class FragmentAccommodationInfo extends Fragment {

    TextView locality;
    TextView description;
    TextView services;
    TextView price;
    RatingBar mRatingBar;
    ToggleButton mFavouriteButton;
    AccommodationModel accommodation;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        accommodation = (AccommodationModel) bundle.getSerializable("selectedAccommodation");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accommodation_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        locality = view.findViewById(R.id.locality);
        price = view.findViewById(R.id.accommodation_price);
        description = view.findViewById(R.id.description);
        services = view.findViewById(R.id.services);
        mRatingBar = view.findViewById(R.id.ratingbar);
        mFavouriteButton = view.findViewById(R.id.loveButton);


        SharedPreferences pref = getActivity().getSharedPreferences("loginData",MODE_PRIVATE);
        boolean favourite = pref.getBoolean(accommodation.getName(),false);

        if(favourite){
            mFavouriteButton.setChecked(true);
        }else{
            mFavouriteButton.setChecked(false);
        }

        if(accommodation != null) {
            if (accommodation.getPrice() != 0)
                price.setText("€" + accommodation.getPrice());
            if (accommodation.getDescription() != null)
                description.setText(accommodation.getDescription());
            if (accommodation.getServices() != null)
                services.setText(accommodation.getServices());
            if (accommodation.getCity() != null && accommodation.getState() != null && accommodation.getAddress() != null)
                locality.setText(accommodation.getCity() + ", " + accommodation.getState() + "  " + accommodation.getAddress());
            if (accommodation.getRating() != 0F)
                mRatingBar.setRating(accommodation.getRating());
        }

        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f,1.0f,0.7f,1.0f, Animation.RELATIVE_TO_SELF,0.7f,Animation.RELATIVE_TO_SELF,0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        mFavouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.startAnimation(scaleAnimation);
            }
        });


        mFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getActivity().getSharedPreferences("loginData",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                boolean favourite = mFavouriteButton.isChecked();
                editor.putBoolean(accommodation.getName(),favourite);
                editor.commit();

                String emailStored = pref.getString("email","");
                if(!emailStored.isEmpty()) {
                    AsyncFavouriteDestination task = new AsyncFavouriteDestination(emailStored, accommodation.getName(),favourite);
                    task.execute();
                }else{
                    Toast.makeText(getContext(),"Effettua prima il login",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
