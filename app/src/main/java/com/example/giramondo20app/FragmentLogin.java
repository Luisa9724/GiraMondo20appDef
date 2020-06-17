package com.example.giramondo20app;


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

import android.widget.Toast;

import com.example.giramondo20app.Model.UserModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class FragmentLogin extends Fragment implements OnTaskCompletedLogin {

    private TextInputEditText etEmailLogin;
    private TextInputEditText etPassLogin;

    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        etEmailLogin = view.findViewById(R.id.email_login);
        etPassLogin = view.findViewById(R.id.password_login);
        Button btnLogin = view.findViewById(R.id.login_btn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!emptyValidate(etEmailLogin,etPassLogin)){
                    AsyncLogin task;
                    if(getFragmentManager().findFragmentByTag("frag_login") != null) {
                        task = new AsyncLogin((FragmentLogin) getFragmentManager().findFragmentByTag("frag_login"));
                    }else{
                        task = new AsyncLogin(new FragmentLogin());
                    }
                    String[] dataTransfer = {etEmailLogin.getText().toString().trim(),etPassLogin.getText().toString().trim()};
                    task.execute(dataTransfer);
                }else{
                    Toast.makeText(getContext(), "i campi non possono essere vuoti", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean emptyValidate(TextInputEditText etEmail, TextInputEditText etPass){
        String email = etEmail.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        return email.isEmpty() || password.isEmpty();
    }

    @Override
    public void onTaskComplete(UserModel result) {

        if(result != null){
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


            SharedPreferences pref = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username",result.getUsername());
            editor.putString("surname",result.getSurname());
            editor.putString("nick",result.getNickname());
            editor.putString("email",result.getUserEmail());
            editor.putString("birthday",dateFormat.format(result.getBirthday()));
            editor.putBoolean("name_is_visible",result.isNameIsVisible());
            editor.putBoolean("photo_approved",result.isApproved());

            if(result.getUserImage() != null && result.isApproved()){
                editor.putString("userimage", Arrays.toString(result.getUserImage()));
            }

            editor.commit();

             FragmentManager fragmentManager = getFragmentManager();
             FragmentTransaction fragmentTransaction;
             if (fragmentManager != null) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new FragmentProfile(result.getUsername(),result.getSurname(),result.getNickname(),dateFormat.format(result.getBirthday()),result.getUserEmail(),result.isNameIsVisible(),result.getUserImage(),result.isApproved()),"frag_profile");
                fragmentTransaction.addToBackStack("frag_profile");
                fragmentTransaction.commit();
             }
           }else{
            Toast.makeText(getContext(),"combinazione di email e password errata. Riprova.",Toast.LENGTH_LONG).show();
          }
    }

    @Override
    public void onResume() {
        super.onResume();

        etEmailLogin.setText("");
        etPassLogin.setText("");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
