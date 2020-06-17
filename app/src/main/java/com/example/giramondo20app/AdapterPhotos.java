package com.example.giramondo20app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.giramondo20app.Model.AccommodationModel;
import com.example.giramondo20app.Model.AccommodationPhotoModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterPhotos extends RecyclerView.Adapter<AdapterPhotos.MyViewHolder> {
    private ArrayList<AccommodationPhotoModel> photosList;
    private Context mContext;
    private AccommodationModel accommodation;

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.accommodation_photos);
            title = itemView.findViewById(R.id.accomm_name);
        }
    }

    public AdapterPhotos(Context context, List<AccommodationPhotoModel> list, AccommodationModel selectedAccommodation) {
        mContext = context;
        photosList = (ArrayList) list;
        accommodation = selectedAccommodation;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_full_image_view, parent, false);
    return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AccommodationPhotoModel currentPhoto = photosList.get(position);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_broken_img)
                .error(R.drawable.ic_broken_img)
                .fallback(R.drawable.ic_broken_img)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(mContext).load(currentPhoto.getFile()).apply(options).into(holder.imageView);
        if(accommodation != null)
        holder.title.setText(accommodation.getName());
    }

    @Override
    public int getItemCount() {
       return photosList.size();
    }


}
