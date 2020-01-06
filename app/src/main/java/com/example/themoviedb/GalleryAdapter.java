package com.example.themoviedb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageViewHolder> {
    private static final String TAG = "MovieGallery";

    Images images;
    Context context;

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;

        public ImageViewHolder(@NonNull View view) {
            super(view);
            this.cardView = view.findViewById(R.id.cardView_GalleryItem);
        }
    }

    public GalleryAdapter(Images images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "Creating view holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new GalleryAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Log.d(TAG, "Binding view holder");
        Log.d(TAG, images.getBackdrops().get(position).getFilePath());
        Glide.with(context).
                load("https://image.tmdb.org/t/p/w500" + images.getBackdrops().get(position).getFilePath()).
                into((ImageView) holder.cardView.findViewById(R.id.imageView_GalleryItem_Image));
    }

    @Override
    public int getItemCount() {
        return images.getBackdrops().size();
    }

    public void updateImages(Images images){
        this.images = images;
    }
}
