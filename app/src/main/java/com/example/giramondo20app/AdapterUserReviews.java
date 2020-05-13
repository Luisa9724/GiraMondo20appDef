package com.example.giramondo20app;

import android.content.Context;
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
import com.example.giramondo20app.Model.ReviewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterUserReviews extends RecyclerView.Adapter<AdapterUserReviews.MyViewHolder> {

    private ArrayList<ReviewModel> reviewsList;
    private Context mContext;
    private FragmentManager fragmentManager;

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView acmImageView;
        TextView acmNameTv;
        TextView commentTv;
        TextView commentDateTv;
        RatingBar userValuationRb;
        Button changeRvw;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            acmImageView = itemView.findViewById(R.id.image_acm);
            acmNameTv = itemView.findViewById(R.id.acm_name_user_rev);
            commentTv = itemView.findViewById(R.id.user_rev);
            commentDateTv = itemView.findViewById(R.id.user_rev_date);
            userValuationRb = itemView.findViewById(R.id.ratingbar_user_rev);
            changeRvw = itemView.findViewById(R.id.change);
        }
    }

    public AdapterUserReviews(Context context, List<ReviewModel> list,FragmentManager fragmentManager) {
        mContext = context;
        reviewsList = (ArrayList) list;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public AdapterUserReviews.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_review, parent, false);
        return new AdapterUserReviews.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUserReviews.MyViewHolder holder, int position) {
        final ReviewModel currentReview = reviewsList.get(position);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_broken_img)
                .error(R.drawable.ic_broken_img)
                .fallback(R.drawable.ic_broken_img)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(mContext).load(currentReview.getAcmImage()).apply(options).into(holder.acmImageView);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateRev = dateFormat.format(currentReview.getDateReview());

        holder.acmNameTv.setText(currentReview.getAcmName());
        holder.commentTv.setText(currentReview.getComment());
        holder.commentDateTv.setText(dateRev);
        holder.userValuationRb.setRating(currentReview.getTotalAmount());
        holder.changeRvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction;
                if (fragmentManager != null) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    if(fragmentManager.findFragmentByTag("frag_rev_form") != null) {
                        fragmentTransaction.replace(R.id.fragment_container, fragmentManager.findFragmentByTag("frag_rev_form"));

                    }else{
                        fragmentTransaction.replace(R.id.fragment_container, new FragmentReviewForm(currentReview.getIdReview(),currentReview.getAcmName()));

                    }
                    fragmentTransaction.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
}
