package com.example.giramondo20app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giramondo20app.Model.AccommodationModel;
import com.example.giramondo20app.Model.AccommodationPhotoModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FragmentAccommodationOverview extends Fragment implements OnTaskCompletedOverview, OnTaskCompletedReviewForm {

    RecyclerView mRecyclerView; //list of accommodation photos

    TabLayout mTabs;
    ViewPager mPage;

    AppBarLayout mAppBarLayout;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    androidx.appcompat.widget.Toolbar mToolbar;

    AccommodationModel accommodation; //the one selected by user

    boolean firstCall; // flag to indicate when the tabs of viewpager are visualized

    SharedPreferences pref; //to save accommodation and photo when press back button from another fragment

    FragmentAccommodationInfo fragInfo;
    FragmentAccommodationMap fragMap;
    FragmentReviews fragReviews;

    Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accommodation_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mRecyclerView = view.findViewById(R.id.imagesList);
        mAppBarLayout = view.findViewById(R.id.appBarLayout);
        mCollapsingToolbarLayout = view.findViewById(R.id.collapsToolbar);
        mToolbar = view.findViewById(R.id.toolBar);
        mTabs = view.findViewById(R.id.myTabs);
        mPage = view.findViewById(R.id.myPage);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(mRecyclerView);

        ((MainActivity)getActivity()).hideToolbar();
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setIcon(R.drawable.ic_action_logo);
        mToolbar.setTitle("GiraMondo");
        hideShowMenuItems();

        pref = getActivity().getSharedPreferences("acm", MODE_PRIVATE);
        boolean firstCallStored = pref.getBoolean("firstCall",false); //at first the tabs are not visualized yet so the flag is false

        if(firstCall || firstCallStored){ // at first call these two flags are false, when slide again on one tab firstCall is true and show that tab again
            setupViewPager(mPage,accommodation); // when press back button on this fragment, the firstCallStored is true and restore accommodation info previously saved
        }

        //to show favourite accommodation or searched by name
        SharedPreferences pref = getActivity().getSharedPreferences("fav_acm",MODE_PRIVATE);
        boolean acmFavourite = pref.getBoolean("favourite",false);
        if(acmFavourite){
            Bundle bundle = getArguments();
            accommodation =(AccommodationModel)bundle.getSerializable("acm_fav");
            AsyncAccommodationPhotos task = new AsyncAccommodationPhotos(this,accommodation);
            task.execute(accommodation.getName());

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("favourite",false);
            editor.commit();
        }

    }

    //at first after fragment transition, it is called to show the info for the accommodation selected in query results
    @Override
    public void onTaskComplete(List<AccommodationPhotoModel> results, AccommodationModel selectedAccommodation) {
        if(pref != null) {
            SharedPreferences.Editor editor = pref.edit();

            Gson gson = new Gson();
            String json = gson.toJson(selectedAccommodation); //save accommodation for further retrieves when press back button tu return to this fragment
            editor.putString("acm", json);

            Gson gson2 = new Gson();
            String json2 = gson2.toJson(results); //save photos
            editor.putString("photos_acm", json2);
            editor.commit();
        }

        AdapterPhotos mAdapter = new AdapterPhotos(getActivity(),results,selectedAccommodation);

        if(!(results.isEmpty()) && mRecyclerView != null){
            mRecyclerView.setAdapter(mAdapter); //show photos
            setupViewPager(mPage,selectedAccommodation); //call this method for the first time
        }
        firstCall = true;
    }

    public void setupViewPager(ViewPager viewpage,AccommodationModel selectedAccommodation){

        SharedPreferences pref = getActivity().getSharedPreferences("acm", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        boolean firstCallStored = pref.getBoolean("firstCall",false);

        if(firstCallStored) { // true from second call of this method on
            Gson gson = new Gson();
            String json = pref.getString("acm",""); //when press back button to this fragment restore accommodation and photos
            accommodation = gson.fromJson(json,AccommodationModel.class);

            Gson gson2 = new Gson();
            String json2 = pref.getString("photos_acm","");
            Type type = new TypeToken<List<AccommodationPhotoModel>>(){}.getType();
            List<AccommodationPhotoModel> results = gson2.fromJson(json2, type);

            AdapterPhotos mAdapter = new AdapterPhotos(getActivity(),results,selectedAccommodation);

            if(!(results.isEmpty())) {
                mRecyclerView.setAdapter(mAdapter);
            }

            editor.putBoolean("firstCall",false);
            editor.commit();
        }else { //only to first call of this method

            fragInfo = new FragmentAccommodationInfo();
            fragMap = new FragmentAccommodationMap();
            fragReviews = new FragmentReviews();
        }

        bundle = new Bundle();
        bundle.putSerializable("selectedAccommodation", selectedAccommodation);

        fragInfo.setArguments(bundle);
        fragMap.setArguments(bundle);
        fragReviews.setArguments(bundle);

        mTabs.setupWithViewPager(mPage);

        final ViewPagerAdapterAccommodationOverview adapter = new ViewPagerAdapterAccommodationOverview(getChildFragmentManager());

        adapter.addFragment(fragInfo, "info");
        adapter.addFragment(fragReviews, "recensioni");
        adapter.addFragment(fragMap, "mappa");
        viewpage.setAdapter(adapter);



    }

    @Override
    public void onTaskCompleteReviewForm(String response) {
        if(response.equals("something went wrong")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Sembra che tu abbia già scritto una recensione su questa struttura,prova a modificarla nel tuo profilo o torna indietro")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Indietro", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FragmentTransaction fr = getFragmentManager().beginTransaction();
                            fr.replace(R.id.fragment_container, getFragmentManager().findFragmentByTag("frag_rev_form"));
                            fr.commit();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }else if(response.equals("operation ends successfully")){

            new AlertDialog.Builder(getContext())
                    .setTitle("Avviso")
                    .setMessage("Operazione conclusa con successo. " +
                            "A breve verrà verificata del nostro staff," +
                            "e potrai vederla pubblicata insieme alle altre!")
                    .setIcon(R.drawable.ic_sentiment_very_satisfied_black_24dp)
                    .setCancelable(true)
                    .show();
        }
    }

    public class SnapHelperOneByOne extends LinearSnapHelper {
        @Override
        //standard method without jump counter which gets calculated by scroll speed. If swipe fast and long, the next (previous) view will be centered. If swipe slow and short the current centered view stays centered after release
        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
            if(!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)){
                return RecyclerView.NO_POSITION;
            }
            final View currentView = findSnapView(layoutManager);
            if(currentView == null){
                return RecyclerView.NO_POSITION;
            }
            final int currentPosition = layoutManager.getPosition(currentView);

            if(currentPosition == RecyclerView.NO_POSITION){
                return RecyclerView.NO_POSITION;
            }
            return currentPosition;
        }
    }


    public void hideShowMenuItems() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    mCollapsingToolbarLayout.setTitle("GiraMondo");
                } else if (verticalOffset == 0) {
                    mCollapsingToolbarLayout.setTitle(" ");

                }
            }
        });
    }

    public void onBackPressed(){
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fr = fragmentManager.beginTransaction();
        fr.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        if(getFragmentManager().findFragmentByTag("frag_filters") != null) {
            fr.replace(R.id.fragment_container, getFragmentManager().findFragmentByTag("frag_filters"));
            fr.commit();
        }else{
            fr.replace(R.id.fragment_container,getFragmentManager().findFragmentByTag("frag_home"));
            fr.commit();
        }
        ((MainActivity)getActivity()).showToolbar();
    }


}
