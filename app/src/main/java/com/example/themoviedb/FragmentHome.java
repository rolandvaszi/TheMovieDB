package com.example.themoviedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentHome extends Fragment {
    private static final String TAG = "MovieHome";

    private SearchView searchView;
    private RecyclerView recyclerViewResults;

    private RecyclerView.LayoutManager layoutManager;
    private TextView textViewTitle;
    private MovieListAdapter adapter;

    private Call<Page> call;
    private String searchQuery;

    public FragmentHome() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ((MainActivity) getActivity()).setBottomNavigationMenu(MainActivity.MENU_MAIN);

        searchView = view.findViewById(R.id.searchView_Home_Search);
        recyclerViewResults = view.findViewById(R.id.recyclerView_Home_SearchResults);
        textViewTitle = view.findViewById(R.id.textView_Home_Title);

        if (searchQuery == null || searchQuery.isEmpty()){
            loadPopularMovies();
        }
        else{
            searchMovie(searchQuery);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textViewTitle.setText(R.string.search_results);
                searchQuery = query;
                searchMovie(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = null;
        layoutManager = null;
    }

    private void loadPopularMovies(){
        Log.d(TAG, "loadPopularMovies");
        GetQueries getQuery = RetrofitObject.getRetrofit().create(GetQueries.class);

        call = getQuery.getPopularMovies(1, RetrofitObject.KEY);

        call.enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Log.d(TAG, "Creating new adapter");
                if (adapter == null){
                    layoutManager = new LinearLayoutManager(getContext());
                    adapter = new MovieListAdapter(response.body().getResults(), getContext());
                    recyclerViewResults.setLayoutManager(layoutManager);
                    recyclerViewResults.setAdapter(adapter);
                }
                else{
                    Log.d(TAG, "notifyDataSetChanged");
                    adapter.updateMovies(response.body().getResults());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                Toast.makeText(getContext(), "An error occurred.", Toast.LENGTH_LONG).show();
                Log.d(TAG, t.toString());
            }
        });
    }

    private void searchMovie(String query){
        GetQueries getQuery = RetrofitObject.getRetrofit().create(GetQueries.class);

        call = getQuery.searchMovie(1, RetrofitObject.KEY, query);

        call.enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                if (adapter == null){
                    layoutManager = new LinearLayoutManager(getContext());
                    adapter = new MovieListAdapter(response.body().getResults(), getContext());
                    recyclerViewResults.setLayoutManager(layoutManager);
                    recyclerViewResults.setAdapter(adapter);

                }
                else{
                    adapter.updateMovies(response.body().getResults());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                Toast.makeText(getContext(), "An error occurred.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
