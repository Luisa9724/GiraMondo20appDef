package com.example.giramondo20app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.giramondo20app.Controller.GPSController;
import com.example.giramondo20app.Controller.NetworkController;
import com.example.giramondo20app.Model.AccommodationModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class FragmentDestination extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,View.OnClickListener, OnTaskCompletedSearchAccommodationByName {

    GoogleMap map; // GoogleMap is the main class of the Maps API and is responsible for handling important operations such as connecting to the Google Maps service, downloading map tiles, and responding to user interactions
    SupportMapFragment mapFragment;
    FragmentFilters fragFilters;
    Bundle bundleCity;
    String TAG="FragmentDestination";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationClient;
    LocationCallback mlocationCallback;


    Marker mLocationMarker;
    private Location locationData;

    private static final long TIME_INTERVAL_GET_LOCATION = 1000 * 5; // 1 Minute
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 5000;

    SearchView searchView;
    ImageButton buttonRestaurant;
    ImageButton buttonHotel;
    ImageButton buttonAttraction;
    boolean pushedButton = false;

    GPSController gpsController;
    NetworkController ntw;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ntw = new NetworkController(getActivity());
        gpsController = new GPSController(getActivity());
        if(ntw.isNetworkConnected() && ntw.internetIsConnected())
            gpsController.buildAlertMessageIfNoGps();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);
        searchView  = view.findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        buttonRestaurant = view.findViewById(R.id.buttonRestaurant);
        buttonHotel = view.findViewById(R.id.buttonHotel);
        buttonAttraction = view.findViewById(R.id.buttonAttraction);

        if(mapFragment == null){
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.google_map, mapFragment).commit();
        }

        //You can’t instantiate a GoogleMap object directly, but you can use getMapAsync to set a callback that’s triggered once the GoogleMap instance is ready to use
        mapFragment.getMapAsync(this);

        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (ntw.isNetworkConnected() && ntw.internetIsConnected()) {
                    gpsController.buildAlertMessageIfNoGps();
                map.clear();
                if(mFusedLocationClient != null && mlocationCallback != null)
                    mFusedLocationClient.removeLocationUpdates(mlocationCallback);

                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null||location.equals("")){
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }

                if(!location.equals("")) {
                    bundleCity = new Bundle();
                    if (fragFilters == null)
                        fragFilters = new FragmentFilters();
                    bundleCity.putString("cityName", location);
                    fragFilters.setArguments(bundleCity);
                }

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





        buttonHotel.setOnClickListener(this);
        buttonRestaurant.setOnClickListener(this);
        buttonAttraction.setOnClickListener(this);

//if click on the button "use the current position" check the permission and show the current user position
        if(getArguments()!= null && getArguments().getBoolean("pushedButton") == true)
            pushedButton = getArguments().getBoolean("pushedButton");

        if(pushedButton== true) {
            TedPermission.with(getActivity())
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("Se rifiuta l'autorizzazione,non potrà usare il servizio\n\nPer favore, abiliti i permessi in [Impostazioni] > [Autorizzazioni]")
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();

            /*Use the GoogleApiClient.Builder class to create an instance of the Google Play Services API client*/
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        return view;
    }

    //Set an instance of OnMapReadyCallback on your MapFragment
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Log.d(TAG, "onMapReady");

        map.getUiSettings().setZoomControlsEnabled(false);
        map.setBuildingsEnabled(false);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setIndoorEnabled(false);
        map.getUiSettings().setRotateGesturesEnabled(true);

        if(!pushedButton) {
            String locationHome = getArguments().getString("location");
            if(!locationHome.equals("")) {
                bundleCity = new Bundle();
                if (fragFilters == null)
                    fragFilters = new FragmentFilters();
                bundleCity.putString("cityName", locationHome);
                fragFilters.setArguments(bundleCity);
            }
            List<Address> addressList = null;
            if (locationHome != null || locationHome.equals("")) {
                Geocoder geocoder = new Geocoder(getActivity());
                try {
                    addressList = geocoder.getFromLocationName(locationHome, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                map.addMarker(new MarkerOptions().position(latLng).title(locationHome));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            }
        }
    }


    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            // Create the LocationRequest object
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(TIME_INTERVAL_GET_LOCATION)    // 3 seconds, in milliseconds
                    .setFastestInterval(TIME_INTERVAL_GET_LOCATION); // 1 second, in milliseconds

            // Connect to Google Play Services, by calling the connect() method
            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        }
    };

    /* If the connect request is completed successfully, the onConnected(Bundle) method will be invoked and any queued items will be executed*/
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // If your app does have access to COARSE_LOCATION && FINE_LOCATION, then this method will return PackageManager.PERMISSION_GRANTED
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        // Retrieve the user’s last known location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mlocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                locationData = locationResult.getLastLocation();
                Toast.makeText(getActivity(), "Latitude: " + locationData.getLatitude() + ", Longitude: " + locationData.getLongitude(), Toast.LENGTH_SHORT).show();

                if (locationData != null) {

                    LatLng point = new LatLng(locationData.getLatitude(), locationData.getLongitude());

                    try {
                        List<Address> address = new Geocoder(getActivity(), Locale.getDefault()).getFromLocation(locationData.getLatitude(),locationData.getLongitude(),1);
                        String cityName = address.get(0).getLocality();

                        fragFilters = new FragmentFilters();
                        bundleCity = new Bundle();
                        bundleCity.putString("cityName",cityName);
                        fragFilters.setArguments(bundleCity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    map.clear();

                    mLocationMarker = map.addMarker(new MarkerOptions().position(point).title("La tua posizione attuale"));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(16).build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }

            }
        };
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,mlocationCallback, null); //update location
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (connectionResult.hasResolution() && getActivity() instanceof Activity) {
            try {
                Activity activity = getActivity();
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i("", "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }


    @Override
    public void onLocationChanged(Location location) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onTaskCompleteSearchAccommodationByName(AccommodationModel result) {
        if(result != null){
            SharedPreferences pref = getContext().getSharedPreferences("fav_acm",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("favourite",true);
            editor.commit();

            FragmentTransaction fragmentTransaction;
            if (getFragmentManager() != null) {
                FragmentAccommodationOverview fragOverview = new FragmentAccommodationOverview();
                fragmentTransaction = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putSerializable("acm_fav", result);
                fragOverview.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, fragOverview);
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(fragFilters == null)
            fragFilters = new FragmentFilters();

            fragmentTransaction.replace(R.id.fragment_container, fragFilters);
            fragFilters.setButton(v.getId());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
    }
    @Override
    public void onPause() {
        super.onPause();
        if(mFusedLocationClient != null)
            mFusedLocationClient.removeLocationUpdates(mlocationCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setIconified(true);
        if(mFusedLocationClient != null)
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,mlocationCallback,null);
    }
}
