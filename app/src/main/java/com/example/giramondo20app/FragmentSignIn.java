package com.example.giramondo20app;

import android.Manifest;

import android.app.DatePickerDialog;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class FragmentSignIn extends Fragment implements OnTaskCompletedSignIn {

    private Context mContext;
    private TextView mDisplayDate;
    private TextInputEditText etUsername,etSurname,etNickname,etEmail,etPassword;
    private TextInputLayout textInputEmail, textInputPassword;
    private RadioGroup mRadioGroup;
    private ImageView userPhoto;
    private Uri imageUri;

    private int year, month, day;

    private String nameVisible;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        etUsername = view.findViewById(R.id.username);
        etSurname = view.findViewById(R.id.surname);
        etNickname = view.findViewById(R.id.nickname);
        etEmail = view.findViewById(R.id.email);
        etPassword = view.findViewById(R.id.password);
        textInputEmail = view.findViewById(R.id.text_input_email);
        textInputPassword = view.findViewById(R.id.text_input_pass);

        Button btnSignIn = view.findViewById(R.id.signIn);
        ImageButton mDate = view.findViewById(R.id.today);
        mDisplayDate = view.findViewById(R.id.tvDate);
        mRadioGroup = view.findViewById(R.id.rad_gr);
        userPhoto = view.findViewById(R.id.user_photo_sign_in);

        Calendar acurentDate = Calendar.getInstance();

        day = acurentDate.get(Calendar.DAY_OF_MONTH);
        month = acurentDate.get(Calendar.MONTH);
        year = acurentDate.get(Calendar.YEAR);

        month = month + 1;
        mDisplayDate.setText(day + "/" + month + "/" + year);



        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        mDisplayDate.setText(dayOfMonth + "/" + month + "/" + year);


                    }
                }, year, month, day);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            }
        });

        mDisplayDate.setText(day + "/" + month + "/" + year);

        if(imageUri == null){
            Glide.with(mContext).load(R.drawable.ic_insert_photo_blue_24dp).into(userPhoto);
        }

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
                if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                    startGallery();
                }else{
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2000);
                }
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadioButton();


                if(!emptyValidate(etUsername,etSurname,etNickname,etEmail,etPassword,mDisplayDate,nameVisible) && imageUri != null){

                    String username = etUsername.getText().toString().trim();
                    String surname = etSurname.getText().toString().trim();
                    String nick = etNickname.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    String date = mDisplayDate.getText().toString().trim();

                    if(emailValidate() && passwordValidate()) {
                        String[] mDataTransfer = {username, surname, nick, email, password, date, nameVisible};
                        AsyncSignIn task = new AsyncSignIn(getActivity(),imageUri,(FragmentSignIn) getFragmentManager().findFragmentByTag("frag_sign_in"));
                        task.execute(mDataTransfer);
                    }
                }else{
                    Toast.makeText(getContext(),"Uno o più campi sono vuoti. Riprova.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean emptyValidate(EditText etUsername,EditText etSurname,EditText etNickname,EditText etEmail,EditText etPassword,TextView etDate, String nameVisible){
        String username = etUsername.getText().toString().trim();
        String surname = etSurname.getText().toString().trim();
        String nick = etNickname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        return (username.isEmpty() || surname.isEmpty() || nick.isEmpty() || email.isEmpty() || password.isEmpty() || date.isEmpty() || nameVisible.isEmpty());
    }

    private void setRadioButton(){
        nameVisible = "";
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        if(selectedId != 0) {
            RadioButton mRadioButton = getView().findViewById(selectedId);
            if(mRadioButton !=null)
                nameVisible = mRadioButton.getText().toString();
        }
    }

    @Override
    public void onTaskComplete(String status,String username,String surname,String nick,String email,String password,String date,boolean nameIsVisible) {

        switch (status) {
            case "Successful sign in":
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction;
                if (fragmentManager != null) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new FragmentLogin(), "frag_login");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;
            case "Something went wrong":
                Toast.makeText(getContext(), "Qualcosa è andato storto.Esiste già un account con questa email.", Toast.LENGTH_LONG).show();
                break;
            case "Nick already exists":
                Toast.makeText(getContext(), "Il nickname inserito esiste già.", Toast.LENGTH_LONG).show();
                break;
        }

    }

    private boolean emailValidate(){
        String emailInput = etEmail.getText().toString().trim();
        if(emailInput.isEmpty()){
            textInputEmail.setError("Il campo non può essere vuoto");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            textInputEmail.setError("Prego, inserire un indirizzo email valido");
            return false;
        }else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean passwordValidate() {
        String passwordInput = etPassword.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Il campo non può essere vuoto");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Password troppo debole");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
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
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .circleCrop()
                    .placeholder(R.drawable.ic_broken_img)
                    .error(R.drawable.ic_broken_img)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);

            Glide.with(mContext).load(imageUri).apply(options).into(userPhoto);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}

