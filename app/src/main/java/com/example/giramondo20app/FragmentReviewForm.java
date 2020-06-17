package com.example.giramondo20app;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.giramondo20app.Model.AccommodationModel;

import static android.content.Context.MODE_PRIVATE;


public class FragmentReviewForm extends Fragment {


    private AccommodationModel accommodation;
    private String accommodationName;
    private Integer idReview;

    private RatingBar ratingBarQuality;
    private RatingBar ratingBarPosition;
    RatingBar ratingBarCleaning;
    private RatingBar ratingBarService;
    private RadioGroup radioGroup;
    private EditText etComment;

    private String travelType;
    private String comment;

    private float ratingQuality = 0F;
    private float ratingPosition = 0F;
    private float ratingCleaning = 0F;
    private float ratingService = 0F;

    private String acmName;

    FragmentReviewForm(int idReview,String acmName){this.idReview = idReview; this.acmName = acmName;}

    FragmentReviewForm(AccommodationModel accommodation) {
        this.accommodation = accommodation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ratingBarQuality = view.findViewById(R.id.ratingbar_quality);
        ratingBarPosition = view.findViewById(R.id.ratingbar_position);
        ratingBarCleaning = view.findViewById(R.id.ratingbar_cleaning);
        ratingBarService = view.findViewById(R.id.ratingbar_service);
        radioGroup = view.findViewById(R.id.radioGroup_reviews);
        etComment = view.findViewById(R.id.write_review);
        Button btnConfirm = view.findViewById(R.id.confirm_btn);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStored;
                SharedPreferences pref = getActivity().getSharedPreferences("loginData", MODE_PRIVATE);
                emailStored = pref.getString("email", "");

                if (emailStored.isEmpty()) {
                    Toast.makeText(getContext(),"Effettua prima il login.",Toast.LENGTH_LONG).show();
                } else {
                    ratingQuality = ratingBarQuality.getRating();
                    ratingPosition = ratingBarPosition.getRating();
                    ratingCleaning = ratingBarCleaning.getRating();
                    ratingService = ratingBarService.getRating();

                    setRadioButton();

                    if (emptyValidate(etComment, ratingQuality, ratingPosition, ratingCleaning, ratingService, travelType)) {

                        Toast.makeText(getContext(), "uno o più campi sono vuoti.Prego riempirli tutti.", Toast.LENGTH_LONG).show();

                    } else {
                        comment = etComment.getText().toString().trim();
                        if (comment.length() < 100) {
                            Toast.makeText(getContext(), "il commento deve essere almeno di 100 caratteri", Toast.LENGTH_LONG).show();
                        } else {
                            if(accommodation != null) {
                                accommodationName = accommodation.getName();
                            }else{
                                accommodationName = acmName;
                            }
                            if (accommodationName != null && idReview ==null) { //insert new review
                                AsyncReviewForm task = new AsyncReviewForm((FragmentAccommodationOverview) getFragmentManager().findFragmentByTag("frag_overview"),accommodationName, emailStored, comment, travelType, ratingQuality, ratingPosition, ratingCleaning, ratingService);
                                task.execute();
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                if(getFragmentManager().findFragmentByTag("frag_overview") != null) {
                                    fr.replace(R.id.fragment_container, getFragmentManager().findFragmentByTag("frag_overview"));
                                }else {
                                    fr.replace(R.id.fragment_container, new FragmentAccommodationOverview());
                                }
                                fr.commit();
                            }else if(accommodationName != null && idReview !=null){ //update review already written
                                if(getFragmentManager().findFragmentByTag("frag_user_rev") != null) {
                                    AsyncReviewForm task = new AsyncReviewForm((FragmentProfile) getFragmentManager().findFragmentByTag("frag_profile"), idReview, accommodationName, emailStored, comment, travelType, ratingQuality, ratingPosition, ratingCleaning, ratingService);
                                    task.execute();
                                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                                    fr.replace(R.id.fragment_container, getFragmentManager().findFragmentByTag("frag_profile"));
                                    fr.commit();
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    private void setRadioButton() {
        travelType = "";
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != 0) {
            RadioButton radioButton = getView().findViewById(selectedId);
            if (radioButton != null)
                travelType = radioButton.getText().toString();
        }
    }

    private boolean emptyValidate(EditText etComment, float quality, float position, float cleaning, float service, String travelType) {
        String comment = etComment.getText().toString().trim();
        return (comment.isEmpty() || quality == 0F || position == 0F || cleaning == 0F || service == 0F || travelType.isEmpty());
    }

    public void onBackPressed() {
        SharedPreferences pref = getActivity().getSharedPreferences("acm", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("firstCall",true); //when press back button turn back to accommodation info where three tabs have to be shown again so the first call is already done
        editor.commit();
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        fr.replace(R.id.fragment_container, getFragmentManager().findFragmentByTag("frag_overview"));
        fr.commit();
    }
}
