package com.example.giramondo20app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.giramondo20app.Model.AccommodationModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AdapterFavouriteAccommodations extends RecyclerView.Adapter<AdapterFavouriteAccommodations.MyViewHolder> {

    private ArrayList<AccommodationModel> acmList;

    private Context mContext;

    private  String userEmail;

    FragmentManager fragmentManager;


    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView acmImageView;
        TextView acmNameTv;
        RatingBar userValuationRb;
        Button deleteAcm;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            acmImageView = itemView.findViewById(R.id.image_acm_pref);
            acmNameTv = itemView.findViewById(R.id.acm_name_pref);
            userValuationRb = itemView.findViewById(R.id.ratingbar_acm);
            deleteAcm = itemView.findViewById(R.id.delete);

        }
    }

    public AdapterFavouriteAccommodations(Context context, List<AccommodationModel> list,String userEmail,FragmentManager fragmentManager) {
        mContext = context;
        acmList = (ArrayList) list;
        this.userEmail = userEmail;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public AdapterFavouriteAccommodations.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_acm_user_prefered, parent, false);
        return new AdapterFavouriteAccommodations.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavouriteAccommodations.MyViewHolder holder, final int position) {
        final AccommodationModel currentAcm = acmList.get(position);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_broken_img)
                .error(R.drawable.ic_broken_img)
                .fallback(R.drawable.ic_broken_img)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(mContext).load(currentAcm.getPhoto()).apply(options).into(holder.acmImageView);

        holder.acmNameTv.setText(currentAcm.getName());
        holder.userValuationRb.setRating(currentAcm.getRating());

        holder.deleteAcm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                acmList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,acmList.size());

                SharedPreferences pref = mContext.getSharedPreferences("loginData",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("fav",false);
                editor.commit();

                if(!userEmail.isEmpty()) {
                    AsyncFavouriteDestination task = new AsyncFavouriteDestination(userEmail, currentAcm.getName(), false);
                    task.execute();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = mContext.getSharedPreferences("fav_acm",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("favourite",true);
                editor.commit();

                FragmentTransaction fragmentTransaction;
                if (fragmentManager != null) {
                    FragmentAccommodationOverview fragOverview = new FragmentAccommodationOverview();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("acm_fav", currentAcm);
                    fragOverview.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_container, fragOverview);
                    fragmentTransaction.commit();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return acmList.size();
    }
}
