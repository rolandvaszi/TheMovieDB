package com.example.themoviedb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentDetails extends Fragment {
    public static final String TAG = "MovieDetails";

    private Movie movie;
    private Call<Page> pageCall;
    private Call<Images> imageCall;

    private RecyclerView recyclerViewGallery;
    private RecyclerView recyclerViewRelatedMovies;
    private TextView textViewTitle;
    private TextView textViewOverview;
    private Button buttonSave;

    private RecyclerView.LayoutManager galleryLayoutManager;
    private RecyclerView.LayoutManager relatedLayoutManager;
    private RelatedListAdapter relatedListAdapter;
    private GalleryAdapter galleryAdapter;

    private DatabaseHelper databaseHelper;

    public FragmentDetails() {
    }

    public FragmentDetails(Movie movie){
        this.movie = movie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_details, container, false);

        recyclerViewGallery = view.findViewById(R.id.recyclerView_Details_Gallery);
        recyclerViewRelatedMovies = view.findViewById(R.id.recyclerView_Details_RelatedMovies);
        textViewTitle = view.findViewById(R.id.textView_Details_Title);
        textViewOverview = view.findViewById(R.id.textView_Details_Overview);
        buttonSave = view.findViewById(R.id.button_Details_Save);

        databaseHelper = new DatabaseHelper(getContext());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.addMovie(movie);
            }
        });

        textViewTitle.setText(movie.getTitle());
        textViewOverview.setText(movie.getOverview());

        getImages();
        getSimilarMovies();

        return view;
    }

    private void getImages(){
        GetQueries getQuery = RetrofitObject.getRetrofit().create(GetQueries.class);

        imageCall = getQuery.getImages(movie.getId(), RetrofitObject.KEY);

        imageCall.enqueue(new Callback<Images>() {
            @Override
            public void onResponse(Call<Images> call, Response<Images> response) {
                if (galleryAdapter == null) {
                    galleryLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    galleryAdapter = new GalleryAdapter(response.body(), getContext());
                    recyclerViewGallery.setLayoutManager(galleryLayoutManager);
                    recyclerViewGallery.setAdapter(galleryAdapter);
                } else {
                    galleryAdapter.updateImages(response.body());
                    galleryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Images> call, Throwable t) {

            }
        });
    }

    private void getSimilarMovies(){
        GetQueries getQuery = RetrofitObject.getRetrofit().create(GetQueries.class);

        pageCall = getQuery.getSimilar(movie.getId(), 1, RetrofitObject.KEY);

        pageCall.enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                if (relatedListAdapter == null) {
                    relatedLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    relatedListAdapter = new RelatedListAdapter(response.body().getResults(), getContext());
                    recyclerViewRelatedMovies.setLayoutManager(relatedLayoutManager);
                    recyclerViewRelatedMovies.setAdapter(relatedListAdapter);
                } else {
                    relatedListAdapter.updateMovies(response.body().getResults());
                    relatedListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                Toast.makeText(getContext(), "An error occurred.", Toast.LENGTH_LONG).show();
            }
        });
    }


}
