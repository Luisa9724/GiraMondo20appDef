package com.example.giramondo20app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.giramondo20app.Controller.GPSController;
import com.example.giramondo20app.Controller.NetworkController;
import com.example.giramondo20app.Model.AccommodationModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;


public class FragmentHome extends Fragment implements OnTaskCompletedAccommodationMostlyPopular {

    SearchView searchView;
    GPSController gpsController;
    NetworkController ntw;

    Context mContext;

    ListView lvHotels;
    ListView lvRestaurants;
    ListView lvAttractions;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ntw = new NetworkController(getActivity());
        gpsController = new GPSController(getActivity());

        if(ntw.isNetworkConnected() && ntw.internetIsConnected())
            gpsController.buildAlertMessageIfNoGps();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        searchView  = view.findViewById(R.id.destinazione);
        lvHotels = view.findViewById(R.id.hotelListHome);
        lvRestaurants = view.findViewById(R.id.restaurantListHome);
        lvAttractions = view.findViewById(R.id.attractionListHome);

        searchView.setFocusable(false);

        Button position = view.findViewById(R.id.position);
        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean pushedButton =true;
                Bundle bundle= new Bundle();
                bundle.putBoolean("pushedButton",pushedButton);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                FragmentDestination fragmentObj = new FragmentDestination();
                fragmentObj.setArguments(bundle);
                fr.replace(R.id.fragment_container, fragmentObj);
                fr.addToBackStack(null);
                fr.commit();
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (ntw.isNetworkConnected() && ntw.internetIsConnected()) {
                    gpsController.buildAlertMessageIfNoGps();
                    String location = searchView.getQuery().toString();

                    FragmentDestination fragmentObj = new FragmentDestination();

                    AsyncAccommodation task = new AsyncAccommodation(fragmentObj);
                    task.execute(location);

                    Bundle bundle2 = new Bundle();
                    bundle2.putString("location", location);
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fragmentObj.setArguments(bundle2);
                    fr.replace(R.id.fragment_container, fragmentObj);
                    fr.addToBackStack(null);
                    fr.commit();
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("nessuna connessione ad internet!")
                            .setCancelable(true);
                    final AlertDialog alert = builder.create();
                    alert.show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setIconified(true);

        SharedPreferences pref = mContext.getSharedPreferences("acmList",Context.MODE_PRIVATE);
        String json = pref.getString("list_acm",null);
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String,List<AccommodationModel>>>(){}.getType();
        HashMap<String,List<AccommodationModel>> results = gson.fromJson(json, type);

        if(results != null){
            onTaskComplete(results);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onTaskComplete(HashMap<String,List<AccommodationModel>> results) {
        if(!results.isEmpty()){
            List<AccommodationModel> hotels = results.get("hotels");
            List<AccommodationModel> restaurants = results.get("restaurants");
            List<AccommodationModel> attractions = results.get("attractions");

            SharedPreferences pref = mContext.getSharedPreferences("acmList",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(results); //save acm
            editor.putString("list_acm", json);
            editor.commit();

            AdapterAccommodations adapterHotels = new AdapterAccommodations(mContext,hotels);
            AdapterAccommodations adapterRestaurants = new AdapterAccommodations(mContext,restaurants);
            AdapterAccommodations adapterAttractions = new AdapterAccommodations(mContext,attractions);

            if(lvHotels != null && lvRestaurants != null && lvAttractions != null){

                lvHotels.setAdapter(adapterHotels);
                lvRestaurants.setAdapter(adapterRestaurants);
                lvAttractions.setAdapter(adapterAttractions);
            }

        }
    }
}
