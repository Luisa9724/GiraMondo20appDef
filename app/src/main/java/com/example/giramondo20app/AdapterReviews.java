package com.example.giramondo20app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.giramondo20app.Model.ReviewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.MyViewHolder> {

    private ArrayList<ReviewModel> reviewsList;
    private Context mContext;

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView userImageView;
        TextView userIdTv;
        TextView commentTv;
        TextView commentDateTv;
        RatingBar userValuationRb;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.user_photo_rev);
            userIdTv = itemView.findViewById(R.id.user_id_rev);
            commentTv = itemView.findViewById(R.id.user_comment);
            commentDateTv = itemView.findViewById(R.id.comment_date);
            userValuationRb = itemView.findViewById(R.id.ratingbar_rev);
        }
    }

    public AdapterReviews(Context context, List<ReviewModel> list) {
        mContext = context;
        reviewsList = (ArrayList) list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ReviewModel currentReview = reviewsList.get(position);

        if(currentReview.isUserPhotoApproved()) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_broken_img)
                    .error(R.drawable.ic_broken_img)
                    .fallback(R.drawable.ic_broken_img)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);
            Glide.with(mContext).load(currentReview.getUserImage()).apply(options).into(holder.userImageView);

        }else if(!currentReview.isUserPhotoApproved() || currentReview.getUserImage() == null){
            holder.userImageView.setImageResource(R.drawable.ic_person_orange_24dp);
        }
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateRev = dateFormat.format(currentReview.getDateReview());

        holder.userIdTv.setText(currentReview.getUserId());
        holder.commentTv.setText(currentReview.getComment());
        holder.commentDateTv.setText(dateRev);
        holder.userValuationRb.setRating(currentReview.getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

}
