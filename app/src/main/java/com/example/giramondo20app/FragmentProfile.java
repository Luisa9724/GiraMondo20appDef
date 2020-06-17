package com.example.giramondo20app;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import static android.app.Activity.RESULT_OK;

public class FragmentProfile extends Fragment implements OnTaskCompletedUserProfile,OnTaskCompletedReviewForm {

    private String usernameStored;
    private String surnameStored;
    private String nicknameStored;
    private String birthdayStored;
    private String emailStored;
    private boolean nameIsVisibleStored;
    private byte[] userImageStored;
    private boolean photoApprovedStored;

    private Uri imageUri;

    private ImageView userImageView;
    private TextView tvShowPhoto;
    private TextView tvWrittenReviews;
    private TextView tvAVGUserRating;

    Context mContext;

    private Animation animRotate;
    private ImageView imgRefresh;


    FragmentProfile( String usernameStored,String surnameStored,String nicknameStored,String birthdayStored,String emailStored,boolean nameIsVisibleStored,byte[] userImageStored,boolean photoApprovedStored){
        this.usernameStored = usernameStored;
        this.surnameStored = surnameStored;
        this.nicknameStored = nicknameStored;
        this.birthdayStored = birthdayStored;
        this.emailStored = emailStored;
        this.nameIsVisibleStored = nameIsVisibleStored;
        this.userImageStored = userImageStored;
        this.photoApprovedStored = photoApprovedStored;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        userImageView = view.findViewById(R.id.user_photo);
        TextView tvUserName = view.findViewById(R.id.username_prof);
        TextView tvUserSurname = view.findViewById(R.id.surname_prof);
        TextView tvUserNickname = view.findViewById(R.id.nickname_prof);
        TextView tvUserBirthday = view.findViewById(R.id.birthday_prof);
        tvWrittenReviews = view.findViewById(R.id.reviews_written);
        tvAVGUserRating = view.findViewById(R.id.avg_of_ratings);
        Switch mSwitch = view.findViewById(R.id.switch1);
        Button changeRvw = view.findViewById(R.id.change_reviews);
        Button btnLogout = view.findViewById(R.id.logout);
        tvShowPhoto = view.findViewById(R.id.show_userphoto);
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressViewOffset(false,10,100);

        boolean photoUpdated = showUserImageIfItIsPossible(userImageStored);


        animRotate = AnimationUtils.loadAnimation(mContext,R.anim.rotate);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                AsyncUserProfile task = new AsyncUserProfile(FragmentProfile.this);
                task.execute(emailStored);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });


        changeRvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUserReviews fragmentUserReviews = new FragmentUserReviews();
                AsyncUserReviews task = new AsyncUserReviews(fragmentUserReviews);
                task.execute(emailStored);
                FragmentTransaction fragmentTransaction;
                if (getFragmentManager() != null) {
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragmentUserReviews,"frag_user_rev");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        if(usernameStored != null && surnameStored != null && nicknameStored != null && birthdayStored != null) {
            tvUserName.setText(usernameStored);
            tvUserSurname.setText(surnameStored);
            tvUserNickname.setText("@" + nicknameStored);
            tvUserBirthday.setText(birthdayStored);
        }

        if(nameIsVisibleStored) {
            mSwitch.setChecked(true);
        }else{
            mSwitch.setChecked(false);
        }

        if(photoUpdated){
            userImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                        startGallery();
                    }else{
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2000);
                    }
                }
            });
        }

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if(isChecked) {
                    editor.putBoolean("name_is_visible",true);
                    editor.commit();
                    nameIsVisibleStored = true;
                    AsyncUserNameVisible task = new AsyncUserNameVisible(FragmentProfile.this,nameIsVisibleStored,emailStored);
                    task.execute();
                }else{
                    editor.putBoolean("name_is_visible",false);
                    editor.commit();
                    nameIsVisibleStored = false;
                    AsyncUserNameVisible task = new AsyncUserNameVisible(FragmentProfile.this,nameIsVisibleStored,emailStored);
                    task.execute();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getSharedPreferences("loginData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction;
                if (fragmentManager != null) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    if(fragmentManager.findFragmentByTag("frag_login") != null) {
                        fragmentTransaction.replace(R.id.fragment_container, getFragmentManager().findFragmentByTag("frag_login"));

                    }else{
                        fragmentTransaction.replace(R.id.fragment_container, new FragmentLogin());

                    }
                    fragmentTransaction.remove(FragmentProfile.this);
                    fragmentManager.popBackStack("frag_profile",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.commit();
                }
            }
        });

    }

    public boolean showUserImageIfItIsPossible(byte[] userImage){

        if(userImage != null && photoApprovedStored) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .circleCrop()
                    .placeholder(R.drawable.ic_broken_img)
                    .error(R.drawable.ic_broken_img)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);

            Glide.with(getContext()).load(userImage).apply(options).into(userImageView);
        }else if(userImage == null){
            userImageView.setImageResource(R.drawable.ic_insert_photo_blue_24dp);

        }else if(!photoApprovedStored){

            tvShowPhoto.setText("in attesa che l'immagine venga verificata");
        }
        return true;
    }

    private void updateUserPhoto(Uri imageUri,String email){

        if(imageUri != null) {
            AsyncUserPhoto task = new AsyncUserPhoto(getActivity(), imageUri, email);
            task.execute();
        }
    }

    private void startGallery(){
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(cameraIntent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivityForResult(cameraIntent,1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1000){
            imageUri = data.getData();
            updateUserPhoto(imageUri,emailStored);
                tvShowPhoto.setText("");
                tvShowPhoto.setText("in attesa che l'immagine venga verificata");
                userImageView.setImageResource(R.drawable.ic_insert_photo_blue_24dp);
        }
    }

    public void onTaskCompletedNameIsVisible(String response){
        if(response.equals("something went wrong")){
            new AlertDialog.Builder(getContext())
                    .setTitle("Avviso")
                    .setMessage("Qualcosa è andato storto " +
                                 "Riprovare")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(true)
                    .show();
        }else if(response.equals("operation ends successfully")){
            new AlertDialog.Builder(getContext())
                    .setTitle("Avviso")
                    .setMessage("Operazione conclusa con successo. ")
                    .setIcon(R.drawable.ic_sentiment_very_satisfied_black_24dp)
                    .setCancelable(true)
                    .show();
        }
    }

    public void onTaskCompletedRefreshUserInfo(Object[] results){
        if(tvWrittenReviews != null && tvAVGUserRating != null) {
            tvWrittenReviews.setText(results[0].toString());
            tvAVGUserRating.setText(results[1].toString());
        }
    }

    @Override
    public void onTaskCompleteReviewForm(String response) {
        if (response.equals("something went wrong")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Qualcosa è andato storto.Riprova.")
                    .setCancelable(true)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Avviso");
            AlertDialog alert = builder.create();
            alert.show();
        } else if (response.equals("operation ends successfully")) {

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

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu,menu);
        imgRefresh = new ImageView(mContext);
        imgRefresh.setPadding(0,0,15,0);
        if(imgRefresh != null){
            imgRefresh.setImageResource(R.drawable.ic_refresh_26dp);
        }
        menu.findItem(R.id.option_refresh).setActionView(imgRefresh);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animRotate);

                AsyncUserProfile task = new AsyncUserProfile(FragmentProfile.this);
                task.execute(emailStored);
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.option_refresh){
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed(){
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, getFragmentManager().findFragmentByTag("frag_home"));
        fr.commit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
