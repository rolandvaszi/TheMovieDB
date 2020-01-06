package com.example.themoviedb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {
    private static final String TAG = "MovieList";

    List<Movie> movies;
    Context context;

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;

        public MovieViewHolder(@NonNull View view) {
            super(view);
            this.cardView = view.findViewById(R.id.cardView_ListItem);
        }
    }

    public MovieListAdapter(List<Movie> movies, Context context){
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "Creating view holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieListAdapter.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Log.d(TAG, "Binding view holder");
        ((TextView) holder.cardView.findViewById(R.id.textView_ListItem_Title)).
                setText(movies.get(position).getTitle());
        ((TextView) holder.cardView.findViewById(R.id.textView_ListItem_Overview)).
                setText(movies.get(position).getOverview());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.showDetails(movies.get(position));
            }
        });

        Glide.with(context).
                load("https://image.tmdb.org/t/p/w500" + movies.get(position).getPosterPath()).
                into((ImageView) holder.cardView.findViewById(R.id.imageView_ListItem_Poster));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateMovies(List<Movie> movies){
        this.movies = movies;
    }
}
