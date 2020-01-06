package com.example.themoviedb;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentFavourites extends Fragment {
    private static final String TAG = "MovieFavourites";

    private RecyclerView recyclerViewMovies;

    private RecyclerView.LayoutManager layoutManager;
    private TextView textViewTitle;
    private MovieListAdapter adapter;

    DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        recyclerViewMovies = view.findViewById(R.id.recyclerView_Favourite_Movies);
        textViewTitle = view.findViewById(R.id.textView_Favourite_Title);

        databaseHelper = new DatabaseHelper(getContext());

        List<Movie> movies = databaseHelper.getMovies();

        if (adapter == null){
            layoutManager = new LinearLayoutManager(getContext());
            adapter = new MovieListAdapter(movies, getContext());
            recyclerViewMovies.setLayoutManager(layoutManager);
            recyclerViewMovies.setAdapter(adapter);
        }
        else{
            adapter.updateMovies(movies);
            adapter.notifyDataSetChanged();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = null;
        layoutManager = null;
    }
}
