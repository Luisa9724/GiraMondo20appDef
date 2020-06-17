package com.example.giramondo20app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    Dialog dialog;
    RelativeLayout logoLayout;
    boolean firstStart;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    Toolbar mToolbar;

    View welcomeView;

    FragmentHome fragmentHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            prefs = getSharedPreferences("mypref", MODE_PRIVATE);

            fragmentHome = new FragmentHome();

            firstStart = prefs.getBoolean("show", true);

            if(firstStart) {

                welcomeView = getLayoutInflater().inflate(R.layout.welcome_dialog, null);

                showWelcomeScreen();

                getRecommendedAccommodations();

            }

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragmentHome,"frag_home");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.getMenu().getItem(0).setCheckable(false);


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_action_logo);
        getSupportActionBar().setTitle("GiraMondo");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Both navigation bar back press and title bar back press will trigger this method
    @Override
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (getSupportFragmentManager().getBackStackEntryCount() == 2) {

                setWelcomeScreenVisible(false);

                restartTheApp();
                finish();

            }else if (f instanceof FragmentAccommodationOverview){

                ((FragmentAccommodationOverview) f).onBackPressed();

            }else if(f instanceof FragmentAccount) {

                ((FragmentAccount) f).onBackPressed();

            }else if(f instanceof FragmentProfile ) {

                ((FragmentProfile) f).onBackPressed();

            }else if(f instanceof FragmentReviewForm ) {

                ((FragmentReviewForm) f).onBackPressed();

            }else if(f instanceof FragmentFilters){

                ((FragmentFilters) f).onBackPressed();

            }else if(f instanceof FragmentFilteredResearchResults){

                ((FragmentFilteredResearchResults) f).onBackPressed();

            }else if(f instanceof FragmentDestination){

                ((FragmentDestination) f).onBackPressed();

            }else if(getSupportFragmentManager().getBackStackEntryCount() == 1) {

                SharedPreferences pref = getSharedPreferences("loginData", Context.MODE_PRIVATE); //close user session
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                finish();

            }else if(f instanceof FragmentHome || f instanceof FragmentFavorites){

                setWelcomeScreenVisible(false);

                restartTheApp();
                finish();
            }else{

                super.onBackPressed();
            }
        }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;
                    boolean home = false;

                    switch (item.getItemId()) {

                        case R.id.nav_preferiti:

                            bottomNav.getMenu().getItem(0).setCheckable(true);

                            selectedFragment = new FragmentFavorites();

                            getUserListFavorites(selectedFragment);

                            break;
                        case R.id.nav_home:

                            selectedFragment = new FragmentHome();
                            home = true;

                            break;
                        case R.id.nav_account:

                            selectedFragment = restoreUserSessionIfLoggedIn(selectedFragment);

                            break;
                    }
                    if (selectedFragment != null && !home) {

                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                        if (selectedFragment instanceof FragmentProfile) {

                            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                            fragmentTransaction.replace(R.id.fragment_container, selectedFragment, "frag_profile");
                            fragmentTransaction.addToBackStack("frag_profile");

                        } else if (selectedFragment instanceof FragmentFavorites) {

                            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                            fragmentTransaction.replace(R.id.fragment_container, selectedFragment, "frag_fav");
                            fragmentTransaction.addToBackStack("frag_profile");

                        } else {
                            fragmentTransaction.replace(R.id.fragment_container, selectedFragment);
                        }

                        fragmentTransaction.commit();

                    } else if (selectedFragment != null && home) {

                        setWelcomeScreenVisible(false);

                        restartTheApp();
                        finish();
                    }
                    return true;
                }
            };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        setWelcomeScreenVisible(true);
    }


    public void hideToolbar(){
            getSupportActionBar().hide();
    }

    public void showToolbar(){
            setSupportActionBar(mToolbar);
            getSupportActionBar().show();

    }

    public void restartTheApp(){
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showWelcomeScreen(){

        dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(welcomeView);
        dialog.show();
    }

    public void setWelcomeScreenVisible(boolean visible){

        prefs = getSharedPreferences("mypref", MODE_PRIVATE);
        editor = prefs.edit();
        editor.putBoolean("show", visible);
        editor.commit();
    }



    private void getRecommendedAccommodations(){

        logoLayout = welcomeView.findViewById(R.id.logoLayout);
        logoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncAccommodations task = new AsyncAccommodations(fragmentHome);
                task.execute();

                dialog.dismiss();
            }
        });
    }

    private void getUserListFavorites(Fragment selectedFragment){
        SharedPreferences prefs = getSharedPreferences("loginData",MODE_PRIVATE);
        String userEmailLogged = prefs.getString("email","");
        AsyncFavouriteAccommodations task = new AsyncFavouriteAccommodations((FragmentFavorites)selectedFragment);
        task.execute(userEmailLogged);
    }

    private Fragment restoreUserSessionIfLoggedIn(Fragment selectedFragment){
        String emailStored, usernameStored, surnameStored,nicknameStored,birthdayStored,userImageStored;
        boolean nameIsVisibleStored,photoApprovedStored;
        SharedPreferences pref = getSharedPreferences("loginData",MODE_PRIVATE);

        emailStored = pref.getString("email",null);
        usernameStored = pref.getString("username",null);
        surnameStored = pref.getString("surname",null);
        nicknameStored = pref.getString("nick",null);
        birthdayStored = pref.getString("birthday",null);
        nameIsVisibleStored = pref.getBoolean("name_is_visible",false);
        userImageStored = pref.getString("userimage",null);
        photoApprovedStored = pref.getBoolean("photo_approved",false);

        if(emailStored == null) {
            selectedFragment = new FragmentAccount();
        }else{
            selectedFragment = new FragmentProfile(usernameStored,surnameStored,nicknameStored,birthdayStored,emailStored,nameIsVisibleStored,retrieveUserImage(userImageStored),photoApprovedStored);
        }
        return selectedFragment;
    }

    public byte[] retrieveUserImage(String stringArray){

        byte[] array = null;
        if(stringArray != null){
            String[] split = stringArray.substring(1,stringArray.length()-1).split(", ");
             array = new byte[split.length];
            for(int i=0;i<split.length;i++){
                array[i] = Byte.parseByte(split[i]);
            }
        }
        return array;
    }

}




