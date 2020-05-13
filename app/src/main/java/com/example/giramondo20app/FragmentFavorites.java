package com.example.giramondo20app;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giramondo20app.Model.AccommodationModel;

import java.util.List;

public class FragmentFavorites extends Fragment implements OnTaskCompletedFavouriteAccommodations {

    Context mContext;

    RecyclerView mRecyclerView;

    RelativeLayout relativeLayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prefered, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.acmUserPreferedList);
        relativeLayout = view.findViewById(R.id.relativeLayout);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onTaskComplete(List<AccommodationModel> results,String userEmail) {
        if(!results.isEmpty()){
            AdapterFavouriteAccommodations adapter = new AdapterFavouriteAccommodations(mContext,results,userEmail,getFragmentManager());
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        }else{
            RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(150,500,130,450);
            TextView tv= new TextView(mContext);
            tv.setLayoutParams(lparams);
            tv.setGravity(Gravity.CENTER);
            tv.setText("La lista dei preferiti è vuota");
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            if(relativeLayout != null)
            relativeLayout.addView(tv);
        }
    }
}
