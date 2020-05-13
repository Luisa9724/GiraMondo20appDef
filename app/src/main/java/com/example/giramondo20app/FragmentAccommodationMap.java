package com.example.giramondo20app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.giramondo20app.Controller.GPSController;
import com.example.giramondo20app.Controller.NetworkController;
import com.example.giramondo20app.Model.AccommodationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.HashMap;
import java.util.List;


public class FragmentAccommodationMap extends Fragment implements OnMapReadyCallback, OnTaskCompletedPlacesOnMap {

    SupportMapFragment mapFragment;
    Spinner mSpinner;

    AccommodationModel accommodation;

    GoogleMap map;
    Marker mMarker;

    GPSController gpsController;
    NetworkController ntw;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ntw = new NetworkController(getActivity());
        gpsController = new GPSController(getActivity());

        Bundle bundle = getArguments();
        accommodation = (AccommodationModel) bundle.getSerializable("selectedAccommodation");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accommodation_map, container, false);

        mSpinner = rootView.findViewById(R.id.spr_place_type);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(map != null) {
                    map.clear();
                    AsyncNearbyPlaces task = new AsyncNearbyPlaces(getActivity(), map, new FragmentAccommodationMap(), (String) parent.getItemAtPosition(position), accommodation);
                    task.execute(accommodation.getCity());

                    Toast.makeText(getContext(), "stai vedendo" + " " + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ntw.isNetworkConnected() && ntw.internetIsConnected()) {
            gpsController.buildAlertMessageIfNoGps();

            map = googleMap;
        } else {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("nessuna connessione ad internet!")
                    .setCancelable(true);

            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onTaskCompletePlacesOnMap(HashMap<String, List<AccommodationModel>> results, String selectedItem, Activity activity, Object mMap, AccommodationModel currentAcm) {

        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        GoogleMap googleMap = (GoogleMap) mMap;

        ArrayList<AccommodationModel> placeList = null;
        double mLatitude = 0F;
        double mLongitude = 0F;

        if (selectedItem.equals("Hotel")) {
            placeList = (ArrayList) results.get("hotels");
        } else if (selectedItem.equals("Ristoranti")) {
            placeList = (ArrayList) results.get("restaurants");

        } else if (selectedItem.equals("Cose da fare")) {
            placeList = (ArrayList) results.get("attractions");
        }
        if (placeList != null) {
            for (int counter = 0; counter < placeList.size(); counter++) {
                AccommodationModel acm = placeList.get(counter);
              if (acm != null) {
                    String location = acm.getAddress();
                    List<Address> addressList = null;
                    if (googleMap != null || location != null || location.equals("")) {
                        Geocoder geocoder = new Geocoder(activity);
                        try {
                            addressList = geocoder.getFromLocationName(location, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Address address = null;
                        if (addressList != null) {
                            address = addressList.get(0);
                        }
                        LatLng latLng = new LatLng(address != null ? address.getLatitude() : 0, address != null ? address.getLongitude() : 0);
                        mMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title(acm.getName()+": "+acm.getAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                        if(mMarker.getTitle().equals(currentAcm.getName()+": "+currentAcm.getAddress())){
                            mMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            if (address != null) {
                                mLatitude = address.getLatitude();
                                mLongitude = address.getLongitude();
                            }

                            editor = putDouble(editor,"lat",mLatitude);
                            editor = putDouble(editor,"lng",mLongitude);
                            editor.commit();
                        }
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                    }
              }
            }
        }
        mLatitude = getDouble(preferences,"lat",0F);
        mLongitude = getDouble(preferences,"lng",0F);

        if(mLatitude != 0F && mLongitude !=0F){
            LatLng acmLatLng = new LatLng(mLatitude,mLongitude);
            googleMap.addMarker(new MarkerOptions().position(acmLatLng).title(currentAcm.getName()+": "+currentAcm.getAddress()));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(acmLatLng, 13));
        }
    }

    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value){
        return edit.putLong(key,Double.doubleToRawLongBits(value));
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue){
        return Double.longBitsToDouble(prefs.getLong(key,Double.doubleToLongBits(defaultValue)));
    }
}

