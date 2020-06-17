package com.example.giramondo20app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.giramondo20app.Controller.GPSController;
import com.example.giramondo20app.Controller.NetworkController;
import com.example.giramondo20app.Model.AccommodationModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FragmentHome extends Fragment implements OnTaskCompletedAccommodationMostlyPopular {

    private SearchView searchView;
    private GPSController gpsController;
    private NetworkController ntw;

    Context mContext;

    private ListView lvHotels;
    private ListView lvRestaurants;
    private ListView lvAttractions;


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
                FragmentTransaction fr;
                if (getFragmentManager() != null) {
                    fr = getFragmentManager().beginTransaction();

                    FragmentDestination fragmentObj = new FragmentDestination();
                    fragmentObj.setArguments(bundle);
                    fr.replace(R.id.fragment_container, fragmentObj,"frag_des");
                    fr.addToBackStack(null);
                    fr.commit();
                }
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
                    FragmentTransaction fr;
                    if (getFragmentManager() != null) {
                        fr = getFragmentManager().beginTransaction();

                        fragmentObj.setArguments(bundle2);
                        fr.replace(R.id.fragment_container, fragmentObj, "frag_des");
                        fr.addToBackStack(null);
                        fr.commit();
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
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences pref = getActivity().getSharedPreferences("acmList",Context.MODE_PRIVATE);
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
        if(!results.isEmpty()) {
            List<AccommodationModel> hotels = new ArrayList<>();
            List<AccommodationModel> restaurants = new ArrayList<>();
            List<AccommodationModel> attractions = new ArrayList<>();

            hotels = results.get("hotels");
            restaurants = results.get("restaurants");
            attractions = results.get("attractions");

            SharedPreferences pref = getActivity().getSharedPreferences("acmList", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            Gson gson = new Gson();
            String json = gson.toJson(results); //save acm
            editor.putString("list_acm", json);
            editor.commit();

            if (hotels != null && restaurants != null && attractions != null) {

                AdapterAccommodations adapterHotels = new AdapterAccommodations(getActivity(), hotels);
                AdapterAccommodations adapterRestaurants = new AdapterAccommodations(getActivity(), restaurants);
                AdapterAccommodations adapterAttractions = new AdapterAccommodations(getActivity(), attractions);

                if (lvHotels != null && lvRestaurants != null && lvAttractions != null) {

                    lvHotels.setAdapter(adapterHotels);
                    lvRestaurants.setAdapter(adapterRestaurants);
                    lvAttractions.setAdapter(adapterAttractions);

                    ListUtils.setDynamicHeight(lvHotels);
                    ListUtils.setDynamicHeight(lvRestaurants);
                    ListUtils.setDynamicHeight(lvAttractions);

                    lvHotels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            AccommodationModel acm = (AccommodationModel) parent.getItemAtPosition(position);

                            SharedPreferences prefs = mContext.getSharedPreferences("fav_acm", MODE_PRIVATE);
                            SharedPreferences.Editor editors = prefs.edit();
                            editors.putBoolean("favourite", true);
                            editors.commit();

                            FragmentTransaction fragmentTransaction;
                            if (getFragmentManager() != null) {
                                FragmentAccommodationOverview fragOverview = new FragmentAccommodationOverview();
                                fragmentTransaction = getFragmentManager().beginTransaction();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("acm_fav", acm);
                                fragOverview.setArguments(bundle);
                                fragmentTransaction.replace(R.id.fragment_container, fragOverview);
                                fragmentTransaction.commit();
                            }
                        }
                    });

                    lvRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            AccommodationModel acm = (AccommodationModel) parent.getItemAtPosition(position);

                            SharedPreferences prefs = mContext.getSharedPreferences("fav_acm", MODE_PRIVATE);
                            SharedPreferences.Editor editors = prefs.edit();
                            editors.putBoolean("favourite", true);
                            editors.commit();

                            FragmentTransaction fragmentTransaction;
                            if (getFragmentManager() != null) {
                                FragmentAccommodationOverview fragOverview = new FragmentAccommodationOverview();
                                fragmentTransaction = getFragmentManager().beginTransaction();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("acm_fav", acm);
                                fragOverview.setArguments(bundle);
                                fragmentTransaction.replace(R.id.fragment_container, fragOverview);
                                fragmentTransaction.commit();
                            }
                        }
                    });

                    lvAttractions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            AccommodationModel acm = (AccommodationModel) parent.getItemAtPosition(position);

                            SharedPreferences prefs = mContext.getSharedPreferences("fav_acm", MODE_PRIVATE);
                            SharedPreferences.Editor editors = prefs.edit();
                            editors.putBoolean("favourite", true);
                            editors.commit();

                            FragmentTransaction fragmentTransaction;
                            if (getFragmentManager() != null) {
                                FragmentAccommodationOverview fragOverview = new FragmentAccommodationOverview();
                                fragmentTransaction = getFragmentManager().beginTransaction();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("acm_fav", acm);
                                fragOverview.setArguments(bundle);
                                fragmentTransaction.replace(R.id.fragment_container, fragOverview);
                                fragmentTransaction.commit();
                            }
                        }
                    });

                }

            }
        }
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}
